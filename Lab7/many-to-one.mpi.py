from mpi4py import MPI
import numpy as np
import sys

default_args = [62, 15, 7, True]
args = sys.argv[1:]
args.extend(default_args[len(args):])

NRA = int(args[0])
NCA = int(args[1])
NCB = int(args[2])
VERBOSE = False if args[3] == 'False' else True
MASTER = 0

comm = MPI.COMM_WORLD
numtasks = comm.Get_size()
taskid = comm.Get_rank()

if numtasks < 2:
    print("Need at least two MPI tasks. Quitting...")
    comm.Abort()

rows = None
offsets = None
c = np.empty((NRA, NCB), dtype=np.float64)

if taskid == MASTER:
    print("mpi_mm has started with %d tasks." % numtasks)

    a = np.full((NRA, NCA), 1.0)
    b = np.full((NCA, NCB), 1.0)

    averow = NRA // numtasks
    extra = NRA % numtasks
    rows = np.array([averow + 1 if i < NRA % numtasks else averow for i in range(numtasks)])
    offset = np.array([sum(rows[:i]) for i in range(numtasks)])

    a_part = a[0:rows[0]]
    for dest in range(1, numtasks):
        print("Sending %d rows to task %d offset=%d" % (rows[dest], dest, offset[dest]))
        req_rows = comm.isend(rows[dest], dest=dest)
        req_a = comm.Isend([a[offset[dest]:offset[dest] + rows[dest]], MPI.DOUBLE], dest=dest)
        req_b = comm.Isend([b, MPI.DOUBLE], dest=dest)

    comm.bcast(rows, root=MASTER)
    comm.bcast(offset, root=MASTER)
else:
    row = comm.recv(source=MASTER)
    a_part = np.empty((row, NCA), dtype=np.float64)
    b = np.empty((NCA, NCB), dtype=np.float64)
    comm.Recv([a_part, MPI.DOUBLE], source=MASTER)
    comm.Recv([b, MPI.DOUBLE], source=MASTER)
    rows = comm.bcast(rows, root=MASTER)
    offset = comm.bcast(offsets, root=MASTER)

c_part = np.dot(a_part, b)

comm.Barrier()

comm.Gatherv(c_part, [c, rows * NCB, offset * NCB, MPI.DOUBLE], root=MASTER)

if taskid == MASTER:
    if(VERBOSE):
        print("****")
        print("Result Matrix:")
        for i in range(NRA):
            print(" ".join("%6.2f" % c[i][j] for j in range(NCB)))
        print("********")
        print("Done.")

MPI.Finalize()
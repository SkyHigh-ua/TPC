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

if taskid == MASTER:
    print("mpi_mm has started with %d tasks." % numtasks)

    a = np.full((NRA, NCA), 1.0)
    b = np.full((NCA, NCB), 1.0)
    rows_per_process = NRA // numtasks
    rows = np.array([rows_per_process + 1 if i < NRA % numtasks else rows_per_process for i in range(numtasks)])
    offset = np.array([sum(rows[:i]) for i in range(numtasks)])

    comm.Bcast(b, root=MASTER)
else: 
    a = None
    b = np.empty((NCA, NCB), dtype=np.float64)
    rows = None
    offset = None
    comm.Bcast(b, root=MASTER)

rows = comm.bcast(rows, root=MASTER)
offset = comm.bcast(offset, root=MASTER)

a_part = np.empty((rows[taskid], NCA), dtype=np.float64)
comm.Scatterv([a, rows * NCA, offset * NCA, MPI.DOUBLE], a_part, root=MASTER)
print("Received %d rows to task %d" % (len(a_part), taskid))

c_part = np.dot(a_part, b)

c = np.empty((NRA, NCB), dtype=np.float64)
comm.Allgatherv(c_part, [c, rows * NCB, offset * NCB, MPI.DOUBLE])

if taskid == MASTER:
    if VERBOSE:
        print("****")
        print("Result Matrix:")
        for i in range(NRA):
            print(" ".join("%6.2f" % c[i][j] for j in range(NCB)))
        print("********")
        print("Done.")
else:
    print(f"Task {taskid} received result matrix {len(c)}x{len(c[0])}")

MPI.Finalize()
from mpi4py import MPI
import numpy as np
import sys

default_args = [62, 15, 7]
args = sys.argv[1:]
args.extend(default_args[len(args):])
args = [int(arg) for arg in args]

NRA = args[0] 
NCA = args[1] 
NCB = args[2] 
MASTER = 0
FROM_MASTER = 1
FROM_WORKER = 2

comm = MPI.COMM_WORLD
numtasks = comm.Get_size()
taskid = comm.Get_rank()

if numtasks < 2:
    print("Need at least two MPI tasks. Quitting...")
    comm.Abort()

numworkers = numtasks - 1

if taskid == MASTER:
    print("mpi_mm has started with %d tasks." % numtasks)

    a = np.full((NRA, NCA), 1.0)
    b = np.full((NCA, NCB), 1.0)
    c = np.empty((NRA, NCB), dtype=np.float64)

    averow = NRA // numworkers
    extra = NRA % numworkers
    offset = 0

    requests = []
    status = MPI.Status()

    for dest in range(1, numworkers+1):
        rows = averow + 1 if dest <= extra else averow
        print("Sending %d rows to task %d offset=%d" % (rows, dest, offset))
        req_offset = comm.isend(offset, dest=dest, tag=FROM_MASTER)
        req_rows = comm.isend(rows, dest=dest, tag=FROM_MASTER)
        req_a = comm.Isend([a[offset:offset + rows], MPI.DOUBLE], dest=dest, tag=FROM_MASTER)
        req_b = comm.Isend([b, MPI.DOUBLE], dest=dest, tag=FROM_MASTER)
        requests.extend([req_offset, req_rows, req_a, req_b])
        offset += rows

    for source in range(numworkers):
        status = MPI.Status()
        offset = comm.recv(source=MPI.ANY_SOURCE, tag=FROM_WORKER, status=status)
        rows = comm.recv(source=status.Get_source(), tag=FROM_WORKER)
        c_part = np.empty((rows, NCB), dtype=np.float64)
        comm.Recv([c_part, MPI.DOUBLE], source=status.Get_source(), tag=FROM_WORKER)
        print("Received results from task %d" % status.Get_source())
        c[offset:offset + rows] = c_part

    print("****")
    print("Result Matrix:")
    for i in range(NRA):
        print(" ".join("%6.2f" % c[i][j] for j in range(NCB)))
    print("********")
    print("Done.")

else:
    offset = comm.recv(source=MASTER, tag=FROM_MASTER)
    rows = comm.recv(source=MASTER, tag=FROM_MASTER)
    a_part = np.empty((rows, NCA), dtype=np.float64)
    b = np.empty((NCA, NCB), dtype=np.float64)
    comm.Recv([a_part, MPI.DOUBLE], source=MASTER, tag=FROM_MASTER)
    comm.Recv([b, MPI.DOUBLE], source=MASTER, tag=FROM_MASTER)

    c_part = np.dot(a_part, b)

    comm.isend(offset, dest=MASTER, tag=FROM_WORKER)
    comm.isend(rows, dest=MASTER, tag=FROM_WORKER)
    comm.Isend([c_part, MPI.DOUBLE], dest=MASTER, tag=FROM_WORKER)

MPI.Finalize()

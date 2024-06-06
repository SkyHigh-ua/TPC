from flask import Flask, request, jsonify
from mpi4py import MPI
import numpy as np

app = Flask(__name__)

comm = MPI.COMM_WORLD
taskid = comm.Get_rank()

MASTER = 0
FROM_MASTER = 1
FROM_WORKER = 2

a = None
b = None

def mpi_matrix_multiplication(a, b, numtasks):
    NRA, NCA = a.shape
    NCB = b.shape[1]
    if numtasks > comm.Get_size(): 
        numtasks = comm.Get_size()
    c = np.empty((NRA, NCB), dtype=np.float64)

    averow = NRA // (numtasks - 1)
    extra = NRA % (numtasks - 1)
    offset = 0

    for dest in range(1, numtasks):
        rows = averow + 1 if dest <= extra else averow
        comm.send(offset, dest=dest, tag=FROM_MASTER)
        comm.send(rows, dest=dest, tag=FROM_MASTER)
        comm.send(a.shape[1], dest=dest, tag=FROM_MASTER)
        comm.send(b.shape[1], dest=dest, tag=FROM_MASTER)
        comm.Send([a[offset:offset + rows, :], MPI.DOUBLE], dest=dest, tag=FROM_MASTER)
        comm.Send([b, MPI.DOUBLE], dest=dest, tag=FROM_MASTER)
        offset += rows

    for source in range(1, numtasks):
        offset = comm.recv(source=source, tag=FROM_WORKER)
        rows = comm.recv(source=source, tag=FROM_WORKER)
        c_part = np.empty((rows, NCB), dtype=np.float64)
        comm.Recv([c_part, MPI.DOUBLE], source=source, tag=FROM_WORKER)
        c[offset:offset + rows, :] = c_part

    return c

@app.route('/multiply_stored', methods=['POST'])
def multiply_stored_matrices():
    global a, b
    if taskid == MASTER:
        data = request.get_json()
        a = np.ones((data['size'],data['size']))
        b = np.ones((data['size'],data['size']))
        numtasks = data['numtasks']
        c = mpi_matrix_multiplication(a, b, numtasks)
        return jsonify(c.tolist())

@app.route('/multiply', methods=['POST'])
def multiply_matrices():
    global a, b
    if taskid == MASTER:
        data = request.get_json()
        a = np.array(data['matrix_a'])
        b = np.array(data['matrix_b'])
        numtasks = data['numtasks']

        c = mpi_matrix_multiplication(a, b, numtasks)
        return jsonify(c.tolist())

if __name__ == '__main__':
    if taskid == MASTER:
        app.run(host='0.0.0.0', port=5000)
    else:
        while True:
            offset = comm.recv(source=MASTER, tag=FROM_MASTER)
            rows = comm.recv(source=MASTER, tag=FROM_MASTER)
            NCA = comm.recv(source=MASTER, tag=FROM_MASTER)
            NCB = comm.recv(source=MASTER, tag=FROM_MASTER)
            a_part = np.empty((rows, NCA), dtype=np.float64)
            b = np.empty((NCA, NCB), dtype=np.float64)
            comm.Recv([a_part, MPI.DOUBLE], source=MASTER, tag=FROM_MASTER)
            comm.Recv([b, MPI.DOUBLE], source=MASTER, tag=FROM_MASTER)

            c_part = np.dot(a_part, b)

            comm.send(offset, dest=MASTER, tag=FROM_WORKER)
            comm.send(rows, dest=MASTER, tag=FROM_WORKER)
            comm.Send([c_part, MPI.DOUBLE], dest=MASTER, tag=FROM_WORKER)

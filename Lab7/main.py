import subprocess
import time
import numpy as np

def measure_time(file, numtasks, params):
    cmd = f"mpiexec -n {numtasks} python {file}.py {params}"
    start_time = time.time()
    subprocess.run(cmd, shell=True)
    end_time = time.time()
    return end_time - start_time

input_data = [
    (2, "2000 2000 2000 False"),
    (4, "3000 3000 3000 False"),
    (8, "4000 4000 4000 False")
]

measures = []
for i in range(len(input_data)):
    sequential_time = measure_time("Lab6/sequential", input_data[i][0], input_data[i][1])
    print("One to one:")
    one_to_one = measure_time("Lab6/nonBlocking.mpi", input_data[i][0], input_data[i][1])
    print("One to many:")
    one_to_many = measure_time("Lab7/one-to-many.mpi", input_data[i][0], input_data[i][1])
    print("Many to one:")
    many_to_one = measure_time("Lab7/many-to-one.mpi", input_data[i][0], input_data[i][1])
    print("Many to many:")
    many_to_many = measure_time("Lab7/many-to-many.mpi", input_data[i][0], input_data[i][1])
    measures.append((sequential_time, one_to_one, one_to_many, many_to_one, many_to_many))

for i, (sequential_time, one_to_one, one_to_many, many_to_one, many_to_many) in enumerate(measures):
    print(f"{i} attempt ({input_data[i][0]} tasks | input {input_data[i][1]})")
    print(f"One to one took {one_to_one:.6f} seconds | Speedup - {(sequential_time/one_to_one):.6f}")
    print(f"One to many took {one_to_many:.6f} seconds | Speedup - {(sequential_time/one_to_many):.6f}")
    print(f"Many to one took {many_to_one:.6f} seconds | Speedup - {(sequential_time/many_to_one):.6f}")
    print(f"Many to many took {many_to_many:.6f} seconds | Speedup - {(sequential_time/many_to_many):.6f}")
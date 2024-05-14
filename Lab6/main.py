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
    (2, "1000 1000 1000 False"),
    (4, "2000 2000 2000 False"),
    (8, "3000 3000 3000 False")
]

measures = []
for i in range(len(input_data)):
    sequential_time = measure_time("Lab6/sequential", input_data[i][0], input_data[i][1])
    print("Blocking:")
    blocking_time = measure_time("Lab6/blocking.mpi", input_data[i][0], input_data[i][1])
    print("Non Blocking:")
    non_blocking_time = measure_time("Lab6/nonBlocking.mpi", input_data[i][0], input_data[i][1])
    measures.append((sequential_time, blocking_time, non_blocking_time))

for i, (sequential_time, blocking_time, non_blocking_time) in enumerate(measures):
    print(f"{i} attempt ({input_data[i][0]} tasks | input {input_data[i][1]})")
    print(f"Blocking method took {blocking_time:.6f} seconds | Speedup - {(sequential_time/blocking_time):.6f}")
    print(f"Non-blocking method took {non_blocking_time:.6f} seconds | Speedup - {(sequential_time/non_blocking_time):.6f}")
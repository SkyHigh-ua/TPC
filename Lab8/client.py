import requests
import numpy as np
import time

def send_request(route, matrix_a=None, matrix_b=None, size=None, numtasks=8):
    if matrix_a is not None and matrix_b is not None:
        data = {
            'matrix_a': matrix_a.tolist(),
            'matrix_b': matrix_b.tolist()
        }
    else:
        data = {
            'size': size
        }
    data['numtasks'] = numtasks
    start_time = time.time()
    response = requests.post(f'http://localhost:5000/{route}', json=data)
    end_time = time.time()
    elapsed_time = end_time - start_time

    if response.status_code == 200:
        result = np.array(response.json())
        print(f"Result matrix: (calculation took {elapsed_time:.2f} seconds)")
        print(result)
        print(f"Result shape: {result.shape}")
        print(f"Result value: {result[0][0]}")
    else:
        print("Failed to get result from server:", response.status_code)
    return elapsed_time

sizes = [256, 512, 1024]
results = []
for size in sizes:
    print(f"Testing with matrix size {size}x{size}")
    matrix_a = np.ones((size, size))
    matrix_b = np.ones((size, size))
    print("Client side matrix")
    csm_time = send_request('multiply', matrix_a, matrix_b)
    print("Server side matrix")
    ssm_time = send_request('multiply_stored', size=size)
    results.append((size, csm_time, ssm_time))

for (size, csm_time, ssm_time) in results:
    print(f"For size {size}x{size}\n Client side matrix: {csm_time:.2f}s \n Server side matrix: {ssm_time:.2f}s")
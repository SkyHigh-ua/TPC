package Lab2.Task2;

import Lab2.Matrix;
import Lab2.Result;

public class FoxMatrixMultiplication {
    private static class Worker implements Runnable {
        private final int id;
        private final int blockSize;
        private final Matrix A;
        private final Matrix B;
        private final Result result;

        public Worker(int id, int blockSize, Matrix A, Matrix B, Result result) {
            this.id = id;
            this.blockSize = blockSize;
            this.A = A;
            this.B = B;
            this.result = result;
        }

        @Override
        public void run() {
            int blockCount = A.getRows() / blockSize;
            for (int k = 0; k < blockCount; k++) {
                for (int i = 0; i < blockCount; i++) {
                    for (int j = id; j < blockCount; j += blockCount) {
                        for (int ii = 0; ii < blockSize; ii++) {
                            for (int jj = 0; jj < blockSize; jj++) {
                                int row = i * blockSize + ii;
                                int col = j * blockSize + jj;
                                int sum = 0;
                                for (int kk = 0; kk < blockCount; kk++) {
                                    sum += A.getValue(row, (i * blockSize + kk + k * blockSize) % (blockCount * blockSize)) *
                                    B.getValue(((i + k) * blockSize + kk) % (blockCount * blockSize), col);
                                }
                                synchronized (result) {
                                    result.setValue(row, col, result.getValue(row, col) + sum);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static Result multiply(Matrix A, Matrix B, int blockSize, int numThreads) {
        if (A.getCols() != B.getRows()) {
            throw new IllegalArgumentException("Number of columns in A must be equal to number of rows in B");
        }

        int rowsA = A.getRows();
        int colsA = A.getCols();
        int colsB = B.getCols();


        if (rowsA % blockSize != 0 || colsA % blockSize != 0 || colsB % blockSize != 0) {
            throw new IllegalArgumentException("Block size must divide evenly into matrix dimensions");
        }

        Result result = new Result(rowsA, colsB);

        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new Worker(i, blockSize, A, B, result));
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                if (thread != null) {
                    thread.join();
                }
            } catch (InterruptedException e) { }
        }

        return result;
    }

    public static void main(String[] args) {
        int[][] dataA = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
        int[][] dataB = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};

        Matrix A = new Matrix(dataA);
        Matrix B = new Matrix(dataB);

        int blockSize = 2;
        int numThreads = 4;

        Result result = multiply(A, B, blockSize, numThreads);

        System.out.println("Result:");
        for (int i = 0; i < result.getRows(); i++) {
            for (int j = 0; j < result.getCols(); j++) {
                System.out.print(result.getValue(i, j) + " ");
            }
            System.out.println();
        }
    }
}


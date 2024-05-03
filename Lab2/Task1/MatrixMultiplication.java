package Lab2.Task1;

import Lab2.Matrix;
import Lab2.Result;

public class MatrixMultiplication {
    public static Result multiply(Matrix A, Matrix B, int numThreads) {
        if (A.getCols() != B.getRows()) {
            throw new IllegalArgumentException("Number of columns in A must be equal to number of rows in B");
        }

        int rowsA = A.getRows();
        int colsA = A.getCols();
        int colsB = B.getCols();

        Result result = new Result(rowsA, colsB);

        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                final int row = i;
                final int col = j;
                threads[j % numThreads] = new Thread(() -> {
                    int sum = 0;
                    for (int k = 0; k < colsA; k++) {
                        sum += A.getValue(row, k) * B.getValue(k, col);
                    }
                    result.setValue(row, col, sum);
                });
                threads[j % numThreads].start();
                for (Thread thread : threads) {
                    try {
                        if (thread != null) {
                            thread.join();
                        }
                    } catch (InterruptedException e) { }
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int[][] dataA = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
        int[][] dataB = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};

        Matrix A = new Matrix(dataA);
        Matrix B = new Matrix(dataB);

        Result result = multiply(A, B, 3);

        System.out.println("Result:");
        for (int i = 0; i < result.getRows(); i++) {
            for (int j = 0; j < result.getCols(); j++) {
                System.out.print(result.getValue(i, j) + " ");
            }
            System.out.println();
        }
    }
}
package Lab2.Task4;

import Lab2.Matrix;
import Lab2.Task1.MatrixMultiplication;
import Lab2.Task2.FoxMatrixMultiplication;

public class Comparison {
    public static void main(String[] args) {
        int[] dimensions = {1024};
        int[] threadCounts = {2, 4, 8};

        System.out.println("String Algorithm:");
        for (int threadCount : threadCounts) {
            for (int n : dimensions) {
                int[][] dataA = generateMatrix(n, n);
                int[][] dataB = generateMatrix(n, n);

                Matrix A = new Matrix(dataA);
                Matrix B = new Matrix(dataB);

                long startTime = System.currentTimeMillis();
                MatrixMultiplication.multiply(A, B, 1);
                long endTime = System.currentTimeMillis();

                long sequentialDuration = endTime - startTime;

                startTime = System.currentTimeMillis();
                MatrixMultiplication.multiply(A, B, threadCount);
                endTime = System.currentTimeMillis();

                long parallelDuration = endTime - startTime;

                double speedup = (double) sequentialDuration / parallelDuration;

                System.out.println("Thread count: " + threadCount);
                System.out.println("Matrix dimension: " + n + "x" + n);
                System.out.println("Time: " + parallelDuration + " milliseconds");
                System.out.println("Speedup: " + speedup);
            }
        }

        System.out.println("\nFox's Algorithm:");
        int blockSize = 128;
        for (int threadCount : threadCounts) {
            for (int n : dimensions) {
                int[][] dataA = generateMatrix(n, n);
                int[][] dataB = generateMatrix(n, n);

                Matrix A = new Matrix(dataA);
                Matrix B = new Matrix(dataB);

                long startTime = System.currentTimeMillis();
                MatrixMultiplication.multiply(A, B, 1);
                long endTime = System.currentTimeMillis();

                long sequentialDuration = endTime - startTime;

                startTime = System.currentTimeMillis();
                FoxMatrixMultiplication.multiply(A, B, blockSize, threadCount);
                endTime = System.currentTimeMillis();

                long parallelDuration = endTime - startTime;

                double speedup = (double) sequentialDuration / parallelDuration;

                System.out.println("Thread count: " + threadCount);
                System.out.println("Matrix dimension: " + n + "x" + n);
                System.out.println("Time: " + parallelDuration + " milliseconds");
                System.out.println("Speedup: " + speedup);
            }
        }
    }

    public static int[][] generateMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = 1;
            }
        }
        return matrix;
    }  
}

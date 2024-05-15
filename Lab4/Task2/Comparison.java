package Lab4.Task2;

import Lab2.Matrix;

public class Comparison {
    public static void main(String[] args) {
        int[] dimensions = {512, 1024, 2048};

        System.out.println("Fox's algorithm using Threads:");
        for (int n : dimensions) {
            int[][] dataA = generateMatrix(n, n);
            int[][] dataB = generateMatrix(n, n);

            Matrix A = new Matrix(dataA);
            Matrix B = new Matrix(dataB);

            long startTime = System.currentTimeMillis();
            Lab2.Task1.MatrixMultiplication.multiply(A, B, 1);
            long endTime = System.currentTimeMillis();

            long sequentialDuration = endTime - startTime;

            startTime = System.currentTimeMillis();
            Lab2.Task2.FoxMatrixMultiplication.multiply(A, B, 64, 10);
            endTime = System.currentTimeMillis();

            long parallelDuration = endTime - startTime;

            double speedup = (double) sequentialDuration / parallelDuration;

            System.out.println("Matrix dimension: " + n + "x" + n);
            System.out.println("Time: " + parallelDuration + " milliseconds");
            System.out.println("Speedup: " + speedup);
        }

        System.out.println("Fox's algorithm using ForkJoinFramework:");
        for (int n : dimensions) {
            int[][] dataA = generateMatrix(n, n);
            int[][] dataB = generateMatrix(n, n);

            Matrix A = new Matrix(dataA);
            Matrix B = new Matrix(dataB);

            long startTime = System.currentTimeMillis();
            Lab2.Task1.MatrixMultiplication.multiply(A, B, 1);
            long endTime = System.currentTimeMillis();

            long sequentialDuration = endTime - startTime;

            startTime = System.currentTimeMillis();
            FoxMatrixMultiplication.multiply(A, B, 64, 10);
            endTime = System.currentTimeMillis();

            long parallelDuration = endTime - startTime;

            double speedup = (double) sequentialDuration / parallelDuration;

            System.out.println("Matrix dimension: " + n + "x" + n);
            System.out.println("Time: " + parallelDuration + " milliseconds");
            System.out.println("Speedup: " + speedup);
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

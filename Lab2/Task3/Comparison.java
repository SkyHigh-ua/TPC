package Lab2.Task3;

import Lab2.Matrix;
import Lab2.Task1.MatrixMultiplication;
import Lab2.Task2.FoxMatrixMultiplication;

public class Comparison {
    public static void main(String[] args) {
        int[] dimensions = {128, 256, 512};
        int numThreads = 10;

        System.out.println("String Algorithm:");
        for (int n : dimensions) {
            int[][] dataA = generateRandomMatrix(n, n);
            int[][] dataB = generateRandomMatrix(n, n);

            Matrix A = new Matrix(dataA);
            Matrix B = new Matrix(dataB);

            long startTime = System.currentTimeMillis();
            MatrixMultiplication.multiply(A, B, numThreads);
            long endTime = System.currentTimeMillis();

            long duration = endTime - startTime;
            System.out.println("Matrix dimension: " + n + "x" + n + ", Time: " + duration + " milliseconds");
        }

        System.out.println("\nFox's Algorithm:");
        int blockSize = 16;
        for (int n : dimensions) {
            int[][] dataA = generateRandomMatrix(n, n);
            int[][] dataB = generateRandomMatrix(n, n);

            Matrix A = new Matrix(dataA);
            Matrix B = new Matrix(dataB);

            long startTime = System.currentTimeMillis();
            FoxMatrixMultiplication.multiply(A, B, blockSize, numThreads);
            long endTime = System.currentTimeMillis();

            long duration = endTime - startTime;
            System.out.println("Matrix dimension: " + n + "x" + n + ", Time: " + duration + " milliseconds");
        }
    }

    public static int[][] generateRandomMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = (int) (Math.random() * 10);
            }
        }
        return matrix;
    }    
}

package Lab2.Task4;

import Lab2.Matrix;
import Lab2.Task1.MatrixMultiplication;
import Lab2.Task2.FoxMatrixMultiplication;

public class Comparison {
    public static void main(String[] args) {
        int[] dimensions = {128};
        int[] threadCounts = {2, 4, 8};

        System.out.println("String Algorithm:");
        for (int threadCount : threadCounts) {
            for (int n : dimensions) {
                int[][] dataA = generateRandomMatrix(n, n);
                int[][] dataB = generateRandomMatrix(n, n);

                Matrix A = new Matrix(dataA);
                Matrix B = new Matrix(dataB);

                long startTime = System.currentTimeMillis();
                MatrixMultiplication.multiply(A, B, threadCount);
                long endTime = System.currentTimeMillis();

                long duration = endTime - startTime;
                System.out.println("Thread count: " + threadCount + ", matrix dimension " + n + "x" + n + ", Time: " + duration + " milliseconds");
            }
        }

        System.out.println("\nFox's Algorithm:");
        for (int threadCount : threadCounts) {
            for (int n : dimensions) {
                int[][] dataA = generateRandomMatrix(n, n);
                int[][] dataB = generateRandomMatrix(n, n);

                Matrix A = new Matrix(dataA);
                Matrix B = new Matrix(dataB);

                int blockSize = 2;

                long startTime = System.currentTimeMillis();
                FoxMatrixMultiplication.multiply(A, B, blockSize, threadCount);
                long endTime = System.currentTimeMillis();

                long duration = endTime - startTime;
                System.out.println("Thread count: " + threadCount + ", matrix dimension " + n + "x" + n + ", Time: " + duration + " milliseconds");
            }
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

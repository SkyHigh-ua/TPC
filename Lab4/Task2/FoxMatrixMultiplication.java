package Lab4.Task2;

import Lab2.Matrix;
import Lab2.Result;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class FoxMatrixMultiplication {
    private static class Worker extends RecursiveAction {
        private final int id;
        private final int blockSize;
        private final Matrix A;
        private final Matrix B;
        private final Result result;
        private final int startRow;
        private final int startCol;
        private final int endRow;
        private final int endCol;

        public Worker(int id, int blockSize, Matrix A, Matrix B, Result result, int startRow, int startCol, int endRow, int endCol) {
            this.id = id;
            this.blockSize = blockSize;
            this.A = A;
            this.B = B;
            this.result = result;
            this.startRow = startRow;
            this.startCol = startCol;
            this.endRow = endRow;
            this.endCol = endCol;
        }

        @Override
        protected void compute() {
            if (endRow - startRow <= blockSize && endCol - startCol <= blockSize) {
                for (int i = startRow; i < endRow; i++) {
                    for (int j = startCol; j < endCol; j++) {
                        int sum = 0;
                        for (int k = 0; k < A.getCols(); k++) {
                            sum += A.getValue(i, k) * B.getValue(k, j);
                        }
                        synchronized (result) {
                            result.setValue(i, j, result.getValue(i, j) + sum);
                        }
                    }
                }
            } else {
                int midRow = (startRow + endRow) / 2;
                int midCol = (startCol + endCol) / 2;

                invokeAll(
                    new Worker(id, blockSize, A, B, result, startRow, startCol, midRow, midCol),
                    new Worker(id, blockSize, A, B, result, startRow, midCol, midRow, endCol),
                    new Worker(id, blockSize, A, B, result, midRow, startCol, endRow, midCol),
                    new Worker(id, blockSize, A, B, result, midRow, midCol, endRow, endCol)
                );
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
        ForkJoinPool forkJoinPool = new ForkJoinPool(numThreads);
        Worker task = new Worker(0, blockSize, A, B, result, 0, 0, rowsA, colsB);
        forkJoinPool.invoke(task);
        forkJoinPool.close();
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

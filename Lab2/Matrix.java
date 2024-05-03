package Lab2;

public class Matrix {
    private int[][] data;
    private int rows;
    private int cols;

    public Matrix(int[][] data) {
        this.data = data;
        this.rows = data.length;
        this.cols = data[0].length;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getValue(int i, int j) {
        return data[i][j];
    }
}

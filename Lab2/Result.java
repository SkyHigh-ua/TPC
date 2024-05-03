package Lab2;

public class Result {
    private int[][] data;
    private int rows;
    private int cols;

    public Result(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new int[rows][cols];
    }

    public void setValue(int i, int j, int value) {
        data[i][j] = value;
    }

    public int getValue(int i, int j) {
        return data[i][j];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}

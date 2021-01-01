package hn;

import java.util.stream.IntStream;

public class Matrix {
    private final double[][] data;

    enum ScalarOperation {ADD, SUBTRACT, MULTIPLY, DIVIDE}

    public Matrix(int rows, int cols) {
        data = new double[rows][cols];
    }

    public Matrix(double[][] data) {
        this.data = new double[data.length][data[0].length];
        IntStream.range(0, this.data.length).forEach(row ->
                IntStream.range(0, this.data[0].length).forEach(column ->
                        this.data[row][column] = data[row][column]));
    }

    public Matrix add(Matrix matrix) {
        if ((data.length != matrix.data.length) || (data[0].length != matrix.data[0].length)) {
            throw new RuntimeException("Matrices must have matching sizes");
        }

        double[][] returnData = new double[data.length][data[0].length];
        IntStream.range(0, data.length).forEach(row ->
                IntStream.range(0, data[0].length).forEach(column ->
                        returnData[row][column] = data[row][column] + matrix.data[row][column]));

        return new Matrix(returnData);
    }

    public Matrix subtract(Matrix matrix) {
        return add(matrix.scalarOperation(-1, ScalarOperation.MULTIPLY));
    }

    public Matrix multiply(Matrix matrix) {
        if (data[0].length != matrix.data.length) {
            throw new RuntimeException("Matrices must have matching inner dimension");
        }

        double[][] returnData = new double[data.length][matrix.data[0].length];
        IntStream.range(0, data.length).forEach(row ->
                IntStream.range(0, matrix.data[0].length).forEach(column -> {
                    double result = 0;
                    for (int i = 0; i < data[0].length; i++) {
                        result += data[row][i] * matrix.data[i][column];
                    }
                    returnData[row][column] = result;
                })
        );

        return new Matrix(returnData);
    }

    public Matrix scalarOperation(int x, ScalarOperation scalarOperation) {
        double[][] returnData = new double[data.length][data[0].length];
        IntStream.range(0, data.length).forEach(row ->
                IntStream.range(0, data[0].length).forEach(column -> {
                    switch (scalarOperation) {
                        case ADD:
                            returnData[row][column] = data[row][column] + x;
                            break;
                        case SUBTRACT:
                            returnData[row][column] = data[row][column] - x;
                            break;
                        case MULTIPLY:
                            returnData[row][column] = data[row][column] * x;
                            break;
                        case DIVIDE:
                            returnData[row][column] = data[row][column] / x;
                            break;
                    }
                }));
        return new Matrix(returnData);
    }

    public static Matrix identity(int size) {
        Matrix matrix = new Matrix(size, size);
        IntStream.range(0, size).forEach(i -> matrix.data[i][i] = 1);
        return matrix;
    }

    public Matrix transpose() {
        double[][] returnData = new double[data[0].length][data.length];
        IntStream.range(0, data.length).forEach(row ->
                IntStream.range(0, data[0].length).forEach(column ->
                        returnData[column][row] = data[row][column]));
        return new Matrix(returnData);
    }

    public double dotProduct(Matrix matrix) {
        if (!this.isVector() || !matrix.isVector()) {
            throw new RuntimeException("Can only dot product 2 vectors");
        }
        else if ((this.flatten().length != matrix.flatten().length)) {
            throw new RuntimeException("Both vectors must have same size");
        }

        double returnValue = 0;
        for (int i = 0; i < this.flatten().length; i++) {
            returnValue += this.flatten()[i] * matrix.flatten()[i];
        }

        return returnValue;
    }

    public void clear() {
        IntStream.range(0, data.length).forEach(row ->
                IntStream.range(0, data[0].length).forEach(column ->
                        data[row][column] = 0));
    }

    public static Matrix toRowMatrix(double[] array) {
        double[][] data = new double[1][array.length];
        System.arraycopy(array, 0, data[0], 0, array.length);
        return new Matrix(data);
    }

    public Matrix getColumnMatrix(int column) {
        double[][] data = new double[this.data.length][1];
        IntStream.range(0, this.data.length).forEach(row -> data[row][0] = this.data[row][column]);
        return new Matrix(data);
    }

    public boolean isVector() {
        boolean flag = false;
        if (this.data.length == 1) {
            flag = true;
        }
        else if (this.data[0].length == 1) {
            flag = true;
        }
        return flag;
    }

    public double[] flatten() {
        double[] returnValue = new double[data.length * data[0].length];
        int i = 0;
        for (int row = 0; row < data.length; row++) {
            for (int column = 0; column < data[0].length; column++) {
                returnValue[i++] = data[row][column];
            }
        }
        return returnValue;
    }

    public double[][] getData() {return data;}

    public static Matrix getMatrix(double[] data, int numbOfRows) {
        if (data.length % numbOfRows != 0) {
            throw new RuntimeException("Size of data not divisible by number of rows");
        }

        Matrix drawingMatrix = new Matrix(numbOfRows, data.length / numbOfRows);
        int i = 0;
        for (int row = 0; row < drawingMatrix.data.length; row++) {
            for (int column = 0; column < drawingMatrix.data[0].length; column++) {
                drawingMatrix.data[row][column] = data[i++];
            }
        }

        return drawingMatrix;
    }

    public String toPackedString() {
        StringBuffer bodySb = new StringBuffer();
        IntStream.range(0, data.length).forEach(row -> {
            IntStream.range(0, data[0].length).forEach(column ->
                    bodySb.append((int)data[row][column]));
            bodySb.append("\n");
        });
        return bodySb.toString();
    }

    public String toString(String columnLabel, String rowLabel) {
        StringBuilder headingSb = new StringBuilder();

        headingSb.append(String.format("   %s |", columnLabel));
        IntStream.range(0, data[0].length).forEach(x -> headingSb.append(String.format("%3d", x)));
        headingSb.append("\n");

        StringBuilder bodySb = new StringBuilder();
        IntStream.range(0, headingSb.length()).forEach(x -> bodySb.append("-"));
        bodySb.append("\n");

        IntStream.range(0, data.length).forEach(row -> {
                bodySb.append(String.format("%s %02d |", rowLabel, row));
                IntStream.range(0, data[0].length).forEach(column -> {
                    double value = data[row][column];
                    bodySb.append(String.format("%s%d", value >= 0 ? "  " : " ", (int)value));
                });
                bodySb.append("\n");
        });

        return  headingSb.append(bodySb.toString()).toString();
    }

    public String toString() {
        return toString("C", "R");
    }
}

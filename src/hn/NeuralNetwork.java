package hn;

import java.util.stream.IntStream;

public class NeuralNetwork {
    private Matrix weightMatrix;

    public NeuralNetwork(int size) {
        weightMatrix = new Matrix(size, size);
    }

    public Matrix getWeightMatrix() { return weightMatrix; }

    public void train(double[] input) {
        double[] bipolarInput = toBipolar(input);
        Matrix bipolarMatrix = Matrix.toRowMatrix(bipolarInput);
        Matrix transposeBipolarMatrix = bipolarMatrix.transpose();
        Matrix multiplyMatrix = transposeBipolarMatrix.multiply(bipolarMatrix);
        Matrix subtractMatrix = multiplyMatrix.subtract(Matrix.identity(weightMatrix.getData().length));

        if (Driver.mode == Driver.Mode.VERBOSE) {
            StringBuffer builder = new StringBuffer();
            builder.append("<-- Calculate Contribution Matrix -->") //
                    .append("[1] Obtain bipolar matrix for input \n").append(bipolarMatrix) //
                    .append("[2] Transpose bipolar matrix:\n").append(transposeBipolarMatrix) //
                    .append("[3] (Transpose bipolar matrix) * (bipolar matrix):\n").append(multiplyMatrix) //
                    .append("[4] Contribution matrix = [3] - (Identity matrix):\n").append(subtractMatrix) //
                    .append("<-- Update Weight Matrix -->").append("current weight matrix:\n") //
                    .append(weightMatrix.toString("N", "N"));
            System.out.println(builder.toString());
        }

        weightMatrix = weightMatrix.add(subtractMatrix);
        if (Driver.mode == Driver.Mode.VERBOSE) {
            System.out.println("New weight matrix = (Contribution matrix) + (Current weight matrix)\n" + weightMatrix.toString("N", "N"));
        }
    }

    public double[] run(double[] input){
        double[] bipolarInput = toBipolar(input);
        double[] output = new double[input.length];
        Matrix bipolarMatrix = Matrix.toRowMatrix(input);

        if (Driver.mode == Driver.Mode.VERBOSE) {
            StringBuffer builder = new StringBuffer();
            builder.append("<-- run -->") //
                .append("[1] Weight matrix:\n").append(weightMatrix.toString("N","N")) //
                .append("[2] Obtain bipolar matrix for input\n").append(bipolarMatrix) //
                .append("[3] Dot product bipolar matrix & each of the columns in weight matrix");
            System.out.println(builder.toString());
        }

        IntStream.range(0, input.length).forEach(column ->  {
            Matrix columnMatrix = weightMatrix.getColumnMatrix(column);
            double dotProductResult = bipolarMatrix.dotProduct(columnMatrix);
            if (Driver.mode == Driver.Mode.VERBOSE) {
                System.out.println("[3." + String.format("%02d", column) + "] (bipolar matrix . (weight matrix column " + String.format("%02d", column) + ") = ");
            }

            if (dotProductResult > 0) {
                output[column] = 1.0;
                if (Driver.mode == Driver.Mode.VERBOSE) {
                    System.out.println(" " + dotProductResult + "  > 0  ==>  1");
                }
            } else {
                output[column] = 0;
                if (Driver.mode == Driver.Mode.VERBOSE) {
                    System.out.println(" " + dotProductResult + "  <= 0  ==>  0");
                }
            }
        });

        return output;
    }

    static double[] toBipolar(double[] pattern) {
        double[] bipolarPattern = new double[pattern.length];
        IntStream.range(0, pattern.length).forEach(row -> {
            if (pattern[row] == 0) {
                bipolarPattern[row] = -1.0;
            } else {
                bipolarPattern[row] = 1.0;
            }
        });
        return bipolarPattern;
    }

    static double[] fromBipolar(double[] bipolarPattern) {
        double[] pattern = new double[bipolarPattern.length];
        IntStream.range(0, bipolarPattern.length).forEach(row -> {
            if (bipolarPattern[row] == 1.0) {
                pattern[row] = 1.0;
            } else {
                pattern[row] = 0.0;
            }
        });
        return pattern;
    }
}

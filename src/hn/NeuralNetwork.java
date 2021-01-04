package hn;

import java.util.stream.IntStream;

public class NeuralNetwork {
    private Matrix weightMatrix;

    public NeuralNetwork(int size) {
        weightMatrix = new Matrix(size, size);
    }

    public Matrix getWeightMatrix() { return weightMatrix; }

    public void getEnergy(double[] pattern) {
        double energy = 0;
        for (int row = 0; row < weightMatrix.getData().length; row++) {
            for (int col = 0; col < weightMatrix.getData()[row].length; col++) {
                energy -= weightMatrix.getData()[row][col] * pattern[row] * pattern[col];
            }
        }

        System.out.println("Energy is " + energy);
    }

    public void train(double[] input) {
        double[] bipolarInput = toBipolar(input);
        Matrix bipolarMatrix = Matrix.toRowMatrix(bipolarInput);
        Matrix transposeBipolarMatrix = bipolarMatrix.transpose();
        Matrix multiplyMatrix = transposeBipolarMatrix.multiply(bipolarMatrix);
        Matrix subtractMatrix = multiplyMatrix.subtract(Matrix.identity(weightMatrix.getData().length));

        String previousWeightMatrix = weightMatrix.toString("N", "N");
        weightMatrix = weightMatrix.add(subtractMatrix);
        getEnergy(input);
        if (Main.verboseMode) {
            StringBuilder builder = new StringBuilder();
            builder.append("<-- Calculate Contribution Matrix -->") //
                    .append("[1] Obtain bipolar matrix for input \n").append(bipolarMatrix) //
                    .append("[2] Transpose bipolar matrix:\n").append(transposeBipolarMatrix) //
                    .append("[3] (Transpose bipolar matrix) * (bipolar matrix):\n").append(multiplyMatrix) //
                    .append("[4] Contribution matrix = [3] - (Identity matrix):\n").append(subtractMatrix) //
                    .append("<-- Update Weight Matrix -->").append("current weight matrix:\n") //
                    .append(previousWeightMatrix);
            System.out.println(builder.toString());
            System.out.println("New weight matrix = (Contribution matrix) + (Current weight matrix)\n" + weightMatrix.toString("N", "N"));
        }
    }

    public double[] run(double[] input){
        double[] bipolarInput = toBipolar(input);
        double[] output = new double[input.length];
        Matrix bipolarMatrix = Matrix.toRowMatrix(bipolarInput);

        if (Main.verboseMode) {
            StringBuilder builder = new StringBuilder();
            builder.append("<-- run -->") //
                .append("[1] Weight matrix:\n").append(weightMatrix.toString("N","N")) //
                .append("[2] Obtain bipolar matrix for input\n").append(bipolarMatrix) //
                .append("[3] Dot product bipolar matrix & each of the columns in weight matrix");
            System.out.println(builder.toString());
        }

        IntStream.range(0, input.length).forEach(column ->  {
            Matrix columnMatrix = weightMatrix.getColumnMatrix(column);
            double dotProductResult = bipolarMatrix.dotProduct(columnMatrix);
            output[column] = dotProductResult > 0 ? 1.0 : 0;

            if (Main.verboseMode) {
                System.out.println(String.format("[3.%1$02d] (bipolar matrix . (weight matrix column %1$02d) = ", column));
                System.out.println(String.format(" %.1f  %s", dotProductResult, dotProductResult > 0 ? "> 0  ==>  1" : "<= 0  ==>  0"));
            }
        });
        getEnergy(input);

        return output;
    }

    public static double[] toBipolar(double[] pattern) {
        double[] bipolarPattern = new double[pattern.length];
        IntStream.range(0, pattern.length).forEach(row -> {
            bipolarPattern[row] = pattern[row] == 0 ? -1.0 : 1.0;
        });
        return bipolarPattern;
    }
}

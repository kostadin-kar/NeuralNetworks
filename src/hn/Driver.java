package hn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.StringCharacterIterator;

public class Driver {
    static final int NUM_OF_ROWS_IN_DRAWING_BOARD = 5;
    static enum Mode { DEFAULT, VERBOSE};
    static Driver.Mode mode = Mode.DEFAULT;

    public static void main(String[] args) throws IOException {
        System.out.println("> Enter # of neurons (must be divisible by 5 - [# of neurons] = [number of columns] X [5 rows]): ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int size = Integer.valueOf(reader.readLine());
        NeuralNetwork network = new NeuralNetwork(size);

        double[] input = new double[size];
        double[] output = new double[size];
        boolean flag = true;
        while (flag) {
            System.out.println("> What do you want to do (train, run, clear, change_mode, exit) ?");
            String command = reader.readLine();
            switch (command) {
                case "train":
                    System.out.println("> Provide training pattern: ");
                    input = getInput(new StringCharacterIterator(reader.readLine()), size);
                    network.train(input);
                    System.out.println(Matrix.getMatrix(input, NUM_OF_ROWS_IN_DRAWING_BOARD).toPackedString());
                    System.out.println("Done training on above pattern");
                    break;
                case "run":
                    System.out.println("> Provide input pattern: ");
                    input = getInput(new StringCharacterIterator(reader.readLine()), size);
                    output = network.run(input);
                    System.out.println("Input pattern:");
                    System.out.println(Matrix.getMatrix(input, NUM_OF_ROWS_IN_DRAWING_BOARD).toPackedString());
                    System.out.println("Output pattern:");
                    System.out.println(Matrix.getMatrix(output, NUM_OF_ROWS_IN_DRAWING_BOARD).toPackedString());
                    break;
                case "clear":
                    network.getWeightMatrix().clear();
                    break;
                case "change_mode":
                    System.out.println("> Specify running mode (default, verbose) ?");
                    if (reader.readLine().equals("verbose")) {
                        mode = Mode.VERBOSE;
                    } else {
                        mode = Mode.DEFAULT;
                    }
                    break;
                case "exit":
                    flag = false;
                    break;
                default:
                    System.out.println("> Invalid command");
            }
        }
        System.exit(0);
    }

    static double[] getInput(StringCharacterIterator iterator, int size) {
        double[] input = new double[size];
        while (iterator.getIndex() < iterator.getEndIndex()) {
            input[iterator.getIndex()] = Double.parseDouble(String.valueOf(iterator.current()));
            iterator.next();
        }
        return input;
    }
}

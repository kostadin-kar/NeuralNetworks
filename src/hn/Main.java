package hn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.StringCharacterIterator;

public class Main {
    static final int NUM_OF_ROWS_IN_DRAWING_BOARD = 5;
    public static boolean verboseMode = false;

    public static void main(String[] args) throws IOException {
        System.out.println("> Enter # of neurons (must be divisible by 5 - [# of neurons] = [number of columns] X [5 rows]): ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int size = Integer.parseInt(reader.readLine());
        NeuralNetwork network = new NeuralNetwork(size);

        while (true) {
            System.out.println("> What do you want to do (train, run, clear, change_mode, exit) ?");
            String command = reader.readLine();
            switch (command) {
                case "train":
                    train(reader, network, size);
                    break;
                case "run":
                    run(reader, network, size);
                    break;
                case "clear":
                    network.getWeightMatrix().clear();
                    break;
                case "change_mode":
                    verboseMode = !verboseMode;
                    System.out.println(String.format("> Changed running mode to %s", verboseMode ? "verbose" : "succinct"));
                    break;
                case "exit":
                    System.exit(0);
                default:
                    System.out.println("> Invalid command");
            }
        }
    }

    private static void run(BufferedReader reader, NeuralNetwork network, int size) throws IOException {
        System.out.println("> Provide input pattern: ");

        double[] input = getInput(new StringCharacterIterator(reader.readLine()), size);
        double[] output = network.run(input);

        System.out.println("Input pattern:");
        System.out.println(Matrix.getMatrix(input, NUM_OF_ROWS_IN_DRAWING_BOARD).toPackedString());
        System.out.println("Output pattern:");
        System.out.println(Matrix.getMatrix(output, NUM_OF_ROWS_IN_DRAWING_BOARD).toPackedString());
    }

    private static void train(BufferedReader reader, NeuralNetwork network, int size) throws IOException {
        System.out.println("> Provide training pattern: ");

        double[] input = getInput(new StringCharacterIterator(reader.readLine()), size);
        network.train(input);

        System.out.println(Matrix.getMatrix(input, NUM_OF_ROWS_IN_DRAWING_BOARD).toPackedString());
        System.out.println("Done training on above pattern");
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

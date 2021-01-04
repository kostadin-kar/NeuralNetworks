package hn;

import java.text.StringCharacterIterator;

public class Test {
    public static void main(String[] args) {
        NeuralNetwork network = new NeuralNetwork(63);
        double[] input = Main.getInput(new StringCharacterIterator("111111100010000001000000100000010000001000000100000010001111111"), 63);
        double[] bipolar = NeuralNetwork.toBipolar(input);

        System.out.println(network.getWeightMatrix().toString());
        network.getEnergy(bipolar);
        network.train(bipolar);
        System.out.println(network.getWeightMatrix().toString());
        network.getEnergy(bipolar);
    }

//    static double Energy(NeuralNetwork neuralNetwork) {
//        double synsum = 0;
//        for(int i=0;i<63;i++) {
//            for(int j=0;j<63;j++) {
//                synsum = synsum - neuralNetwork.getEnergy();[i][j]*nodes[j]*nodes[i];
//            }
//        }
//        return synsum;
//    }

}

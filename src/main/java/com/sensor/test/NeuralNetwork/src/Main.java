//package com.sensor.test.NeuralNetwork.src;
//
//import com.sensor.test.NeuralNetwork.src.common.GenericReader;
//import com.sensor.test.NeuralNetwork.src.common.thread_center.ThreadPoolCenter;
//import com.sensor.test.NeuralNetwork.src.model.NeuralNetwork;
//import com.sensor.test.NeuralNetwork.src.model.Tuple;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//public class Main {
//
//
//    public static void main(String args[]) {
//
//        /**
//         * input layer size - input size
//         * first layer -
//         * hidden layer -
//         * output - number of classes
//         * learning rate
//         */
//        NeuralNetwork nn = new NeuralNetwork(2, 10, 10, 2, 0.005);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////        System.out.println(nn.getInputLayerWeights().length);
//
//        List<Tuple> trainingSet = GenericReader.init("/data1.csv", GenericReader::createTuple);
//        Collections.shuffle(trainingSet);
//        for (int i = 0; i < 20000; i++) {
//            nn.errTraining = 0;
//            Tuple k = null;
//            for (Tuple input : trainingSet) {
////                System.out.println(Arrays.toString(input.getY()));
//                nn.learn(input.getDataVector(), input.getY());
////                System.out.println(nn.errTraining);
//                k = input;
////                nn.setLr(1/(i+200));
//            }
//            if (i % 10 == 0) {
//                System.out.println(Arrays.toString(k.getY()) + " " + Arrays.toString(nn.getOutputLayerActivated()));
//                System.out.println((double) nn.errTraining / (double) trainingSet.size());
//            }
//
//
//        }
//
//        List<Tuple> testingSet = GenericReader.init("/mnist_test.csv", GenericReader::createTuple);
//        Collections.shuffle(testingSet);
//
//        nn.errTraining = 0;
//        Tuple k = null;
//        int i = 0;
//        for (Tuple input : testingSet) {
////                System.out.println(Arrays.toString(input.getY()));
//            nn.predict(input.getDataVector(), input.getY());
////                System.out.println(nn.errTraining);
//            k = input;
//            i++;
//        }
//
////        System.out.println(Arrays.toString(k.getY()) + " " + Arrays.toString(nn.getOutputLayerActivated()));
//        System.out.println((double) nn.errTraining / (double) testingSet.size());
//
//        ThreadPoolCenter.closeThreadPool();
//
//    }
//}

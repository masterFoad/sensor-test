package com.sensor.test.NeuralNetwork.src.model;

import com.sensor.test.NeuralNetwork.src.util.*;

import java.io.*;
import java.util.Arrays;
import java.util.OptionalDouble;

public class NeuralNetwork implements Serializable {

    private double firstLayerBias[];
    private double hiddenLayerBias[];
    private double outputLayerBias[];
    private double[] firstLayer;
    private double[] firstLayerActivated;
    private double[] hiddenLayer;
    private double[] hiddenLayerActivated;
    private double[] outputLayer;
    private double[] outputLayerActivated;
    private double[][] inputLayerWeights;
    private double[][] hiddenLayerWeights;
    private double[][] outputLayerWeights;
    private double lr;
    public int errTraining;

    public NeuralNetwork(int inputSize, int firstLayerSize, int hiddenLayerSize, int outputLayerSize, double lr) {
        //inputLayer = new double[inputSize];
        firstLayer = new double[firstLayerSize];
        firstLayerActivated = new double[firstLayerSize];
        hiddenLayer = new double[hiddenLayerSize];
        hiddenLayerActivated = new double[hiddenLayerSize];
        outputLayer = new double[outputLayerSize];
        outputLayerActivated = new double[outputLayerSize];
        inputLayerWeights = Matrix.random(inputSize, firstLayerSize);
        hiddenLayerWeights = Matrix.random(firstLayerSize, hiddenLayerSize);
        outputLayerWeights = Matrix.random(hiddenLayerSize, outputLayerSize);
        firstLayerBias = Matrix.random(firstLayerSize);
        hiddenLayerBias = Matrix.random(hiddenLayerSize);
        outputLayerBias = Matrix.random(outputLayerSize);
        this.lr = lr;
    }

    public void predict(double[] inputs, int[] y) {
        forwardFeed(inputs);
//        System.out.println(sqrdError(y));
        if (sqrdError(y) > 0.1) {
            errTraining++;
        }

    }

    public void learn(double[] inputs, int[] y) {
        forwardFeed(inputs);
        backPropogate(inputs, y);

//        System.out.println(sqrdError(y));
        if (sqrdError(y) > 0.05) {
            errTraining++;
        }

    }

    public double sqrdError(int[] expectedOutputs) {
        int size = outputLayer.length;
        double[] sum = new double[size];
        for (int i = 0; i < size; i++) {
            sum[i] += expectedOutputs[i] - outputLayerActivated[i];
            sum[i] = sum[i] * sum[i] * 0.5;
        }
        return Arrays.stream(sum).sum();
    }

    /**
     * the derivative of the cost function/dActivation
     * <p>
     * //     * @param activatedOutputlayer
     *
     * @param y
     * @return
     */
    public double[] costDerivative(int[] y) {
        double[] deltas = new double[outputLayer.length];
        for (int i = 0; i < outputLayer.length; i++) {
            deltas[i] = outputLayerActivated[i] - y[i];
        }
        return deltas;
    }

    /**
     * forward pass through the network
     *
     * @param input
     */
    public double[] forwardFeed(double[] input) {
        firstLayer = Matrix.multiply(input, inputLayerWeights);
        for (int i = 0; i < firstLayer.length; i++) {
            firstLayerActivated[i] = sigmoid(firstLayer[i] + firstLayerBias[i]);
        }
        hiddenLayer = Matrix.multiply(firstLayerActivated, hiddenLayerWeights);
        for (int i = 0; i < hiddenLayer.length; i++) {
            hiddenLayerActivated[i] = sigmoid(hiddenLayer[i] + hiddenLayerBias[i]);
        }
        outputLayer = Matrix.multiply(hiddenLayerActivated, outputLayerWeights);
        for (int i = 0; i < outputLayer.length; i++) {
            outputLayerActivated[i] = sigmoid(outputLayer[i] + outputLayerBias[i]);
        }

        double[] acti = new double[3];
        OptionalDouble max = Arrays.stream(outputLayerActivated).max();
        if (max.isPresent()) {
            for (int i = 0; i < outputLayerActivated.length; i++) {
                if (outputLayerActivated[i] == max.getAsDouble()) {
                    acti[i] = max.getAsDouble();
                }
            }
        }
        return acti;
    }

    /**
     * backpropogation where 0 is the output later
     *
     * @param
     * @return
     */
    public void backPropogate(double[] input, int[] y) {


        double[] costToActivationDer = hadamard_product(
                costDerivative(y),
                Arrays.stream(outputLayerActivated).map(a -> a * (1 - a)).toArray());
        /**
         * now we have the deltaC/deltaA, now we must find the change in the W in the layer before the last
         */
        //first delta
        double[][] dCdw1 = Matrix.multiply(hiddenLayerActivated, costToActivationDer);


        //second delta
        double[] delta2 = hadamard_product(
                Matrix.multiply(costToActivationDer, Matrix.transpose(outputLayerWeights)),
                Arrays.stream(hiddenLayerActivated).map(a -> a * (1 - a)).toArray());

        double[][] dCdw2 = Matrix.multiply(firstLayerActivated, delta2);


        //third delta
        double[] delta3 = hadamard_product(
                Matrix.multiply(delta2, Matrix.transpose(hiddenLayerWeights)),
                Arrays.stream(firstLayerActivated).map(a -> a * (1 - a)).toArray());

        double[][] dCdw3 = Matrix.multiply(input, delta3);

        /**
         * updating weights
         */
        int index = 0;
        for (double[] u : outputLayerWeights) {
            for (int i = 0; i < u.length; i++) {
                u[i] = u[i] - lr * dCdw1[index][i];
            }
            index++;
        }

        index = 0;
        for (double[] u : hiddenLayerWeights) {
            for (int i = 0; i < u.length; i++) {
                u[i] = u[i] - lr * dCdw2[index][i];
            }
            index++;
        }

        index = 0;
        for (double[] u : inputLayerWeights) {
            for (int i = 0; i < u.length; i++) {
                u[i] = u[i] - lr * dCdw3[index][i];
            }
            index++;
        }


        /**
         * updating biases
         */
        for (int i = 0; i < firstLayerBias.length; i++) {
            firstLayerBias[i] = firstLayerBias[i] - lr * delta3[i];
        }

        for (int i = 0; i < hiddenLayerBias.length; i++) {
            hiddenLayerBias[i] = hiddenLayerBias[i] - lr * delta2[i];
        }

        for (int i = 0; i < outputLayerBias.length; i++) {
            outputLayerBias[i] = outputLayerBias[i] - lr * costToActivationDer[i];
        }
    }


    public double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public double sigmoidPrime(double x) {
        double s = sigmoid(x);
        return s * (1 - s);
    }


    public double[] getFirstLayer() {
        return firstLayer;
    }

    public double[] getOutputLayer() {
        return outputLayer;
    }

    public double[][] getInputLayerWeights() {
        return inputLayerWeights;
    }

    public double[][] getOutputLayerWeights() {
        return outputLayerWeights;
    }

    public double[][] getHiddenLayerWeights() {
        return hiddenLayerWeights;
    }

    public double[] getOutputLayerActivated() {
        return outputLayerActivated;
    }

    public void setLr(double lr) {
        this.lr = lr;
    }

    public double[] hadamard_product(double[] v1, double[] v2) {
        double[] pro = new double[v1.length];
        for (int i = 0; i < v1.length; i++) {
            pro[i] = v1[i] * v2[i];
        }
        return pro;
    }


    public static NeuralNetwork loadNN(String path) throws IOException, ClassNotFoundException {
        ObjectInputStream objectinputstream = null;
        FileInputStream streamIn;
        try {
            streamIn = new FileInputStream(path);
            objectinputstream = new ObjectInputStream(streamIn);
            return (NeuralNetwork) objectinputstream.readObject();
        } finally {
            if (objectinputstream != null) {
                try {
                    objectinputstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

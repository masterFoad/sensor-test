package com.sensor.test.NeuralNetwork.src.model;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Tuple implements Comparable<Tuple>{

    private static int id = 0;
    private int num;
    private double[] dataVector;
    private boolean[] isCorrectlyClassified;
    private int[] y;
    private double weight;
    private Long creationDate;


    public Tuple(int dim) {
        dataVector = new double[dim];
        y = new int[3];
        creationDate = System.currentTimeMillis();
    }

    public Tuple(double[] dataVector, int classNum, int size) {
        this.num = id;
        id++;
        this.dataVector = dataVector;
        y = new int[size];
        y[classNum] = 1;
//        System.out.println(Arrays.toString(y));
    }

    public void setY(String s) {
        s = s.trim();
        switch (s) {
            case "sitting":
                y[0] = 1;
                break;

            case "walking":
                y[1] = 1;
                break;

            case "running":
                y[2] = 1;
                break;
        }
    }

    public Tuple createResultClone(double[] dataVector, int classNum) {
        Tuple clone = new Tuple(dataVector.length);
        clone.setDataVector(dataVector);
        return clone;
    }

    public int[] getY() {
        return y;
    }

    public double[] getDataVector() {
        return dataVector;
    }

    public void addPoint(double p, int index) {
        this.dataVector[index] = p;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean[] getIsCorrectlyClassified() {
        return isCorrectlyClassified;
    }

    public void setDataVector(double[] dataVector) {
        this.dataVector = dataVector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple)) return false;

        Tuple tuple = (Tuple) o;

        return Arrays.equals(dataVector, tuple.dataVector);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(dataVector);
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "dataVector=" + Arrays.toString(dataVector) +
                ", isCorrectlyClassified=" + isCorrectlyClassified +
                ", classNum=" + y +
                ", weight=" + weight +
                '}';
    }

    public int getNum() {
        return num;
    }

    public static void resetId() {
        id = 0;
    }

    @Override
    public int compareTo(Tuple o) {
        return -creationDate.compareTo(o.creationDate);
    }
}

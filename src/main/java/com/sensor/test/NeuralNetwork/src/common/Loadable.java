package com.sensor.test.NeuralNetwork.src.common;


@FunctionalInterface
public interface Loadable<T> {

    T create(String[] metaData);
}

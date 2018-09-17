package com.sensor.test.NeuralNetwork.src.common.thread_center;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolCenter {

    private static ExecutorService executor;

    static{
        executor = new ThreadPoolExecutor(4, 4,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
    }


    public static ExecutorService getExecutor(){

        return executor;
    }

    public static void closeThreadPool() {
        executor.shutdown();
    }



}

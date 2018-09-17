package com.sensor.test.linker.datalogic;

import com.sensor.test.NeuralNetwork.src.common.thread_center.ThreadPoolCenter;

public class ThreadController {

    public static volatile int counter = 0;

    void ThreadController() {
    }

    public synchronized static void initProcesses () {
        if(counter == 0) {
            counter++;
//            WatchAndLearn.getInstance();
//            WatchAndLearn.getInstance().startLiveFeedProcess();
        }
    }

    public static void closeThreads() {
        ThreadPoolCenter.closeThreadPool();
    }
}

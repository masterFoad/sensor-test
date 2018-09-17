//package com.sensor.test.linker.datalogic;
//
////import com.sensor.test.NeuralNetwork.src.common.thread_center.ThreadPoolCenter;
//
//import com.sensor.test.NeuralNetwork.src.common.thread_center.ThreadPoolCenter;
//import com.sensor.test.NeuralNetwork.src.model.NeuralNetwork;
//import com.sensor.test.NeuralNetwork.src.model.Tuple;
//import com.sensor.test.linker.common.AccelerometerData;
//import com.sensor.test.linker.common.IOManager;
//import com.sensor.test.util.DBUtil;
//import com.sensor.test.util.DataAccess;
//
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.PriorityQueue;
//import java.util.concurrent.Executors;
//import java.util.concurrent.PriorityBlockingQueue;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import static mm.constants.Constants.INPUT_SIZE;
//import static mm.constants.Constants.NN_SAVE_PATH;
//
//public class WatchAndLearn {
//
//    private volatile PriorityQueue<AccelerometerData> S1Dataload = new PriorityQueue<>(30);
//    private volatile PriorityQueue<AccelerometerData> S2Dataload = new PriorityQueue<>(30);
//    private static Object lock = new Object();
//    private volatile PriorityQueue<Tuple> tuplePredictionQ = new PriorityQueue<>(30);
//    private volatile NeuralNetwork NN;
//    private volatile double[] liveActivity;
//    private volatile DataAccess dataAccess = new DataAccess();
//
//    private WatchAndLearn() throws IOException, ClassNotFoundException {
//
//        NN = NeuralNetwork.loadNN(NN_SAVE_PATH);
//
//    }
//
//
//    public void startLiveFeedProcess() {
//
//        ThreadPoolCenter.getExecutor().execute(() -> {
//            while (true) {
//                if (!getTuplePredictionQ().isEmpty()) {
//                    addActivity(getNN().forwardFeed(getLatestTuple().getDataVector()));
//                }
//
//            }
//        });
//
//
//        ThreadPoolCenter.getExecutor().execute(() -> {
//            while (true) {
//                if (getS1QSize() >= 10 && getS2QSize() >= 10) {
//                    Logger logger = Logger.getLogger(WatchAndLearn.class.getName());
//                    var tToPredict = mergeLiveFeed();
////                    WatchAndLearn.addActivity(getNN().forwardFeed(tToPredict.getDataVector()));
////                            setLiveActivity();
//
////                    System.out.print(
////                            Arrays.toString(
////                                    NN.getOutputLayerActivated()
////                            )
////                    );
//                    logger.log(Level.FINEST, "Predicting " + getNN().getOutputLayerActivated());
//                }
//            }
//        });
//
//        ThreadPoolCenter.getExecutor().execute(() -> {
//            while (true) {
//                if (getLiveActivity() != null) {
//                    Logger logger = Logger.getLogger(WatchAndLearn.class.getName());
//                    System.out.print(
//                            Arrays.toString(
//                                    getNN().getOutputLayerActivated()
//                            )
//                    );
//                    logger.log(Level.FINEST, "Predicting " + getNN().getOutputLayerActivated());
//                }
//            }
//        });
//
//    }
//
//    private static WatchAndLearn instance = null;
//
//    public static WatchAndLearn getInstance() {
//        WatchAndLearn result = instance;
//        if (result == null) {
//            synchronized (lock) {
//                result = instance;
//                if (result == null) {
//                    try {
//                        instance = result = new WatchAndLearn();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//        return result;
//    }
//
//
//    public Tuple mergeLiveFeed() {
////        synchronized (lock) {
//        var t = new Tuple(INPUT_SIZE);
//        var index = 0;
//        for (int i = 0; i < 10; i++) {
//            t.addPoint(getFromS1Q().getX(), index);
//            index++;
//            t.addPoint(getFromS1Q().getY(), index);
//            index++;
//            t.addPoint(getFromS1Q().getZ(), index);
//            index++;
//            t.addPoint(getFromS2Q().getX(), index);
//            index++;
//            t.addPoint(getFromS2Q().getY(), index);
//            index++;
//            t.addPoint(getFromS2Q().getZ(), index);
//            index++;
//        }
//        synchronized (lock) {
//            tuplePredictionQ.add(t);
//        }
//        return t;
////        }
//    }
//
//    public void addToTupleQ(Tuple t) {
//        synchronized (lock) {
//            tuplePredictionQ.add(t);
//        }
//    }
//
//    public PriorityQueue<Tuple> getTuplePredictionQ() {
//        synchronized (lock) {
//            return this.tuplePredictionQ;
//        }
//    }
//
//    public Tuple getLatestTuple() {
//        synchronized (lock) {
//            return tuplePredictionQ.poll();
//        }
//    }
//
//    public void addToS1Queue(AccelerometerData ad) {
//        synchronized (lock) {
//            S1Dataload.add(ad);
//        }
//    }
//
//    public void addToS2Queue(AccelerometerData ad) {
//        synchronized (lock) {
//            S2Dataload.add(ad);
//        }
//    }
//
//    public AccelerometerData getFromS1Q() {
//        synchronized (lock) {
//            return S1Dataload.poll();
//        }
//    }
//
//    public AccelerometerData getFromS2Q() {
//        synchronized (lock) {
//            return S2Dataload.poll();
//        }
//    }
//
//    public int getS1QSize() {
//        synchronized (lock) {
//            return S1Dataload.size();
//        }
//    }
//
//    public int getS2QSize() {
//        synchronized (lock) {
//            return S2Dataload.size();
//        }
//    }
//
//    public void setLiveActivity(double[] a) {
//        synchronized (lock) {
//            this.liveActivity = a;
//            lock.notifyAll();
//        }
//    }
//
//    public double[] getLiveActivity() {
//        synchronized (lock) {
//            return this.liveActivity;
//        }
//    }
//
//    public void addActivity(double[] y) {
//        if (y != null) {
//            try {
//                this.dataAccess.addActivity(y);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    public synchronized NeuralNetwork getNN() {
//        return NN;
//    }
//}

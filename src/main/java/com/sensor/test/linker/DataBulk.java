package com.sensor.test.linker;

import com.google.gson.Gson;
import com.sensor.test.NeuralNetwork.src.common.thread_center.ThreadPoolCenter;
import com.sensor.test.NeuralNetwork.src.model.NeuralNetwork;
import com.sensor.test.NeuralNetwork.src.model.Tuple;
import com.sensor.test.linker.common.AccelerometerData;
import com.sensor.test.linker.common.ErrorModel;
import com.sensor.test.linker.common.IOManager;
import com.sensor.test.linker.datalogic.ThreadController;
import com.sensor.test.util.DataAccess;
import com.sensor.test.util.RestUtils;
import mm.constants.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import static mm.constants.Constants.INPUT_SIZE;

//import com.sensor.test.linker.datalogic.WatchAndLearn;

@WebServlet(name = "DataBulk")
public class DataBulk extends HttpServlet {

//    private NeuralNetwork loadNN;

    public static PriorityBlockingQueue<AccelerometerData> st1q = new PriorityBlockingQueue<>();
    public static PriorityBlockingQueue<AccelerometerData> st2q = new PriorityBlockingQueue<>();
    public static PriorityBlockingQueue<Tuple> tupleQ = new PriorityBlockingQueue<>();
    public static NeuralNetwork NN;
//    public static TextToSpeechClient textToSpeechClient;
//    public static SynthesisInput input;
//    public static VoiceSelectionParams voice;
//    public static AudioConfig audioConfig;

    static {
        try {
            NN = NeuralNetwork.loadNN(Constants.NN_SAVE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


//        try {
//            textToSpeechClient = TextToSpeechClient.create();
//
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//
//
//        // Build the voice request; languageCode = "en_us"
//        voice = VoiceSelectionParams.newBuilder().setLanguageCode("en-US")
//                .setSsmlGender(SsmlVoiceGender.FEMALE)
//                .build();
//
//        // Select the type of audio file you want returned
//        audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3) // MP3 audio.
//                .build();


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("LoadNN") != null) {
//            loadNN = NeuralNetwork.loadNN("C:\\Users\\Foad\\Desktop\\saveNN\\NN2018-09-14 22-49-44.ser");
        }
        if (req.getParameter("StopThreads") != null) {
//            loadNN = NeuralNetwork.loadNN("C:\\Users\\Foad\\Desktop\\saveNN\\NN2018-09-14 22-49-44.ser");
            ThreadPoolCenter.closeThreadPool();
        }

//        RestUtils.postActionToCloud("walking");

//        try {
//            textToSpeechClient.close();
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
    }

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        IOManager iom = new IOManager(req, resp);

        try {
            if (!iom.getJsonRequest().has("lightmeter")) {
                AccelerometerData sensedData = new Gson().fromJson(iom.getJsonRequest(), AccelerometerData.class);
                sensedData.setCurrentTime();
                if (sensedData.getAccelerometer() != null && sensedData.getAccelerometer().size() != 0) {

//                    System.out.println("*********************************");
                    ThreadController.initProcesses();

//                    if (sensedData.getDeviceuid().trim().equals("b0:b4:48:be:b3:85")) {
//                        WatchAndLearn.getInstance().addToS1Queue(sensedData);
//                    } else {
//                        WatchAndLearn.getInstance().addToS2Queue(sensedData);
//                    }

                    if (sensedData.getDeviceuid().trim().equals("b0:b4:48:be:b3:85")) {
                        st1q.add(sensedData); //hand sensor
                    } else {
                        st2q.add(sensedData); //leg sensor
                    }


//                    System.out.println(tupleQ.size());
//                    System.out.println("SIZESOF SHUT " + st1q.size() + " nsnsns " + st2q.size());

                    mergeLiveFeed();
                    if (!tupleQ.isEmpty()) {
                        String activityToSend = null;
//                        iom.getDataAccess().addToDBSTRING(Arrays.toString();
//                        System.out.println();
                        double[] activities = NN.forwardFeed(tupleQ.poll().getDataVector());
                        for (int i = 0; i < activities.length; i++) {
                            if (activities[i] != 0.0) {
                                if (i == 0) {
                                    activityToSend = "sitting";
                                }
                                if (i == 1) {
                                    activityToSend = "walking";
                                }
                                if (i == 2) {
                                    activityToSend = "running";
                                }
                            }
                        }
//                        Logger logger = Logger.getLogger(DataBulk.class.getName());
//                        logger.log(Level.FINEST, );
                        System.out.println(Arrays.toString(activities));
                        System.out.println(activityToSend + " " + new Date());
                        RestUtils.postActionToCloud(activityToSend);
                    }


//                    if (WatchAndLearn.getInstance().getLiveActivity() != null) {
//                        System.out.println("Activity " + Arrays.toString(WatchAndLearn.getInstance().getLiveActivity()));
//                    } else {
//                        System.out.print("activity is still null");
//                    }


                    iom.setResponseMessage(new ErrorModel() {
                        @Override
                        public int getCode() {
                            return 200;
                        }

                        @Override
                        public String getMessage() {
                            return "SUCCESS+\n";
                        }
                    });

                    iom.SendJsonResponse();
                } else {
                    iom.setResponseMessage(new ErrorModel() {
                        @Override
                        public int getCode() {
                            return 666;
                        }

                        @Override
                        public String getMessage() {
                            return "FAILED+\n";
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (iom != null) {
                iom.closeConnection();
            }
        }

    }


    public void mergeLiveFeed() {
//        synchronized (lock) {
        var t = new Tuple(INPUT_SIZE);
        var index = 0;
        if (st1q.size() > 24 && st2q.size() > 24) {

            for (int i = 0; i < INPUT_SIZE / 6; i++) {

                t.addPoint(st1q.poll().getX(), index);
                index++;
                t.addPoint(st1q.poll().getY(), index);
                index++;
                t.addPoint(st1q.poll().getZ(), index);
                index++;
                t.addPoint(st2q.poll().getX(), index);
                index++;
                t.addPoint(st2q.poll().getY(), index);
                index++;
                t.addPoint(st2q.poll().getZ(), index);
                index++;
            }
//            st1q.clear();
//            st2q.clear();
            tupleQ.add(t);
        }


//        return t;
//        }
    }


//    public static void readActivity(String text) {
//
//        // Set the text input to be synthesized
//
//        input = SynthesisInput.newBuilder().setText(text).build();
//        // Perform the text-to-speech request
//        SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
//
//        // Get the audio contents from the response
//        ByteString audioContents = response.getAudioContent();
//
////        File temp = null;
////        try {
////            temp = File.createTempFile("tempfile", ".mp3");
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        temp.deleteOnExit();
//
//
//        Media m = new Media(audioContents.toString());
////            try (OutputStream out = new FileOutputStream(temp)) {
////                out.write(audioContents.toByteArray());
////                System.out.println("Audio content written to file \"output.mp3\"");
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//
//        MediaPlayer mediaPlayer = new MediaPlayer(m);
//        mediaPlayer.play();
//
//    }


//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//        IOManager iom = new IOManager(req, resp);
//
//        try {
//            if (!iom.getJsonRequest().has("lightmeter")) {
//                AccelerometerData sensedData = new Gson().fromJson(iom.getJsonRequest(), AccelerometerData.class);
//                sensedData.setCurrentTime();
////                System.out.println("HERE");
//                if (sensedData.getAccelerometer() != null && sensedData.getAccelerometer().size() != 0) {
//
////                    System.out.println("*********************************");
//                    ThreadController.initProcesses();
//
////                    if (sensedData.getDeviceuid().trim().equals("b0:b4:48:be:b3:85")) {
////                        WatchAndLearn.getInstance().addToS1Queue(sensedData);
////                    } else {
////                        WatchAndLearn.getInstance().addToS2Queue(sensedData);
////                    }
////                    System.out.println("****************");
//                    if (sensedData.getDeviceuid().trim().equals("b0:b4:48:be:b3:85")) {
//                        st1q.add(sensedData); //hand sensor
//                    } else {
//                        st2q.add(sensedData); //leg sensor
//                    }
//
//
//                    if (st2q.size() > 20) {
//                        var ad = st2q.poll();
//                        var t = new Tuple(3);
//                        int dataCounter = 0;
//                        t.addPoint(ad.getX(), dataCounter);
//                        dataCounter++;
//                        t.addPoint(ad.getY(), dataCounter);
//                        dataCounter++;
//                        t.addPoint(ad.getZ(), dataCounter);
//                        dataCounter++;
//                        tupleQ.add(t);
//                        st2q.clear();
//                    }
//
//
////                    System.out.println(tupleQ.size());
////                    System.out.println("SIZESOF SHUT " + st1q.size() + " nsnsns " + st2q.size());
//
////                    mergeLiveFeed();
//                    if (!tupleQ.isEmpty()) {
//                        String activityToSend = null;
////                        iom.getDataAccess().addToDBSTRING(Arrays.toString();
////                        System.out.println();
//                        double[] activities = NN.forwardFeed(tupleQ.poll().getDataVector());
//                        for (int i = 0; i < activities.length; i++) {
//                            if (activities[i] != 0.0) {
//                                if (i == 0) {
//                                    activityToSend = "sitting";
//                                }
//                                if (i == 1) {
//                                    activityToSend = "walking";
//                                }
//                                if (i == 2) {
//                                    activityToSend = "running";
//                                }
//                            }
//                        }
////                        Logger logger = Logger.getLogger(DataBulk.class.getName());
////                        logger.log(Level.FINEST, );
//                        System.out.println(Arrays.toString(activities));
//                        System.out.println(activityToSend + " " + new Date());
////                        readActivity(activityToSend);
//                    }
//
//
////                    if (WatchAndLearn.getInstance().getLiveActivity() != null) {
////                        System.out.println("Activity " + Arrays.toString(WatchAndLearn.getInstance().getLiveActivity()));
////                    } else {
////                        System.out.print("activity is still null");
////                    }
//
//
//                    iom.setResponseMessage(new ErrorModel() {
//                        @Override
//                        public int getCode() {
//                            return 200;
//                        }
//
//                        @Override
//                        public String getMessage() {
//                            return "SUCCESS+\n";
//                        }
//                    });
//
//                    iom.SendJsonResponse();
//                } else {
//                    iom.setResponseMessage(new ErrorModel() {
//                        @Override
//                        public int getCode() {
//                            return 666;
//                        }
//
//                        @Override
//                        public String getMessage() {
//                            return "FAILED+\n";
//                        }
//                    });
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (iom != null) {
//                iom.closeConnection();
//            }
//        }
//
//    }
}

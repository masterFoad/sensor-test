package com.sensor.test.linker;

import com.google.gson.Gson;
import com.sensor.test.NeuralNetwork.src.model.LearningSession;
import com.sensor.test.NeuralNetwork.src.model.LearningSessionSingleSensor;
import com.sensor.test.NeuralNetwork.src.model.NeuralNetwork;
import com.sensor.test.linker.common.AccelerometerData;
import com.sensor.test.linker.common.ErrorModel;
import com.sensor.test.linker.common.IOManager;
import javafx.util.Pair;
import org.apache.commons.lang.ArrayUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static com.sensor.test.linker.common.ServerUtils.getClosestK;
import static com.sensor.test.linker.common.ServerUtils.search;
import static mm.constants.Constants.INPUT_SIZE;

@WebServlet(name = "SensorLinkerServlet")
public class SensorLinkerServlet extends HttpServlet {
    private NeuralNetwork loadNN = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var iom = new IOManager(req, resp);


        try {


            var walkingSet = mergingProcess(iom, "walking"); //750
            var runningSet = mergingProcess(iom, "running"); // 800
            var sittingSet = mergingProcess(iom, "sitting"); // 700-800
            //2200 database size
            /// 10 -> 220... every vector of size 60
            // 2200 / 4


            var nn = new NeuralNetwork(INPUT_SIZE, 50, 25, 3, 0.005);
            var session = new LearningSession(nn, 1, walkingSet, sittingSet, runningSet);
            session.startLearningSession();
            System.out.println("Training Error : "+session.getTrainingError());
            session.startTestingSession();
            System.out.println("Testing Error : "+session.getTestingError());
            session.saveNN();

            nn = new NeuralNetwork(INPUT_SIZE, 120, 60, 3, 0.005);
            session = new LearningSession(nn, 800, walkingSet, sittingSet, runningSet);
            session.startLearningSession();
            System.out.println("Training Error : "+session.getTrainingError());
            session.startTestingSession();
            System.out.println("Testing Error : "+session.getTestingError());
            session.saveNN();


//            nn = new NeuralNetwork(INPUT_SIZE, 30, 30, 3, 0.005);
//            session = new LearningSession(nn, 10000, walkingSet, sittingSet, runningSet);
//            session.startLearningSession();
//            System.out.println("Training Error : "+session.getTrainingError());
//            session.startTestingSession();
//            System.out.println("Testing Error : "+session.getTestingError());
//            session.saveNN();
//
//            nn = new NeuralNetwork(INPUT_SIZE, 30, 30, 3, 0.005);
//            session = new LearningSession(nn, 10000, walkingSet, sittingSet, runningSet);
//            session.startLearningSession();
//            System.out.println("Training Error : "+session.getTrainingError());
//            session.startTestingSession();
//            System.out.println("Testing Error : "+session.getTestingError());
//            session.saveNN();
//
//            INPUT_SIZE = 6;
//            nn = new NeuralNetwork(INPUT_SIZE, 40, 30, 3, 0.005);
//            session = new LearningSession(nn, 25000, walkingSet, sittingSet, runningSet);
//            session.startLearningSession();
//            System.out.println("Training Error : "+session.getTrainingError());
//            session.startTestingSession();
//            System.out.println("Testing Error : "+session.getTestingError());
//            session.saveNN();
//
//            INPUT_SIZE = 12;
//            nn = new NeuralNetwork(INPUT_SIZE, 50, 25, 3, 0.005);
//            session = new LearningSession(nn, 20000, walkingSet, sittingSet, runningSet);
//            session.startLearningSession();
//            System.out.println("Training Error : "+session.getTrainingError());
//            session.startTestingSession();
//            System.out.println("Testing Error : "+session.getTestingError());
//            session.saveNN();
//
//            INPUT_SIZE = 18;
//            nn = new NeuralNetwork(INPUT_SIZE, 50, 25, 3, 0.005);
//            session = new LearningSession(nn, 20000, walkingSet, sittingSet, runningSet);
//            session.startLearningSession();
//            System.out.println("Training Error : "+session.getTrainingError());
//            session.startTestingSession();
//            System.out.println("Testing Error : "+session.getTestingError());
//            session.saveNN();
//
//            INPUT_SIZE = 36;
//            nn = new NeuralNetwork(INPUT_SIZE, 50, 25, 3, 0.005);
//            session = new LearningSession(nn, 20000, walkingSet, sittingSet, runningSet);
//            session.startLearningSession();
//            System.out.println("Training Error : "+session.getTrainingError());
//            session.startTestingSession();
//            System.out.println("Testing Error : "+session.getTestingError());
//            session.saveNN();



//            iom.getDataAccess().InsertToMergedData(dataPairs);
        } finally {
            iom.closeConnection();
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {


        IOManager iom = new IOManager(request, response);

        try {
            if (!iom.getJsonRequest().has("lightmeter")) {
                AccelerometerData sensedData = new Gson().fromJson(iom.getJsonRequest(), AccelerometerData.class);
                if (sensedData.getAccelerometer() != null && sensedData.getAccelerometer().size() != 0) {
                    iom.getDataAccess().addSensorData(sensedData);
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


    public ArrayList<Pair<AccelerometerData, AccelerometerData>> mergingProcess(IOManager iom, String activity) {
        try {
            var st1data = iom.getDataAccess().getSensor1Data(activity);
            var st2data = iom.getDataAccess().getSensor2Data(activity);
            var dataPairs = new ArrayList<Pair<AccelerometerData, AccelerometerData>>(700);

            var counter = 0;
            var originalSize = st1data.size();
            for (var e : st2data) {
                if (!st1data.isEmpty()) {
                    counter++;
                    var index = search(e.submitDateDouble, st1data);
                    dataPairs.add(new Pair<>(st1data.get(index), e));
                    st1data.remove(index);
                    if (counter >= originalSize) {
                        break;
                    }
                } else {
                    break;
                }
            }

            return dataPairs;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}

package com.sensor.test.NeuralNetwork.src.model;

import com.sensor.test.linker.common.AccelerometerData;
import javafx.util.Pair;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static mm.constants.Constants.INPUT_SIZE;

public class LearningSessionSingleSensor implements Serializable {

    public static int counter = 0;
    private NeuralNetwork nn;
    private ArrayList<Tuple> trainingSet;
    private ArrayList<Tuple> testingSet;
    private int iterations;
    private double trainingError;
    private double testingError;
    private ArrayList<Tuple> allPreparedSet;



    public LearningSessionSingleSensor(NeuralNetwork nn, int iterations, ArrayList<AccelerometerData> accelerometerDataArrayList) {
        System.out.println("Session number : "+ counter);
        counter++;
        this.nn = nn;
        this.iterations = iterations;
        trainingSet = new ArrayList<>();
        testingSet = new ArrayList<>();
        trainingSet = transformOriginalDataTOChunks(accelerometerDataArrayList);
        buildTestingSet();
        System.out.print("Iterations : " + iterations);
        System.out.println("training set size : " + trainingSet.size());
        System.out.println("testing set size : " + testingSet.size());
    }




    public void buildTestingSet() {
        testingSet = extractSubset(trainingSet, 0.12);
        Collections.shuffle(trainingSet);
        Collections.shuffle(testingSet);
    }


    public void KFoldCrossValidation(int k) {
        for (int i = 0; i < k; i++) {


        }
    }

    public <T> ArrayList<T> extractSubset(ArrayList<T> from, double sizePercent) {
        var subset = new ArrayList<T>();
        int size = (int) (from.size() * sizePercent);
        for (int i = 0; i < size; i++) {
            subset.add(from.get(i));
            from.remove(i);
        }
        return subset;
    }


//    public void buildTestingSet(){
//        trainingSet.subList()
//    }


    public void startLearningSession() {
//        Collections.shuffle(trainingSet);

        System.out.println("Starting Learning Session " + LocalDateTime.now());


        for (int i = 0; i < iterations; i++) {
            nn.errTraining = 0;
            Tuple k = null;
            Collections.shuffle(trainingSet);
            for (Tuple input : trainingSet) {
//                System.out.println(Arrays.toString(input.getY()));
                nn.learn(input.getDataVector(), input.getY());
//                System.out.println(nn.errTraining);
                k = input;
//                nn.setLr(1/(i+200));
            }
            if (i % 20 == 0) {
//                System.out.println(Arrays.toString(k.getY()) + " " + Arrays.toString(nn.getOutputLayerActivated()));
                System.out.print("Iteration "+ i+" ");
                System.out.println((double) nn.errTraining / (double) trainingSet.size());
            }
            if ( i == 7000) {
                break;
            }

            setTrainingError(nn.errTraining);
        }
        System.out.println("Finished Learning Session " + LocalDateTime.now());

    }


    public ArrayList<Tuple> transformOriginalDataTOChunks(ArrayList<AccelerometerData> odsep) {
        var listOfTuples = new ArrayList<Tuple>();
        var t = new Tuple(3);
        var dataCounter = 0;
        for (int i = 0; i < odsep.size(); i++) {
            t.addPoint(odsep.get(i).getX(), dataCounter);
            dataCounter++;
            t.addPoint(odsep.get(i).getY(), dataCounter);
            dataCounter++;
            t.addPoint(odsep.get(i).getZ(), dataCounter);
            dataCounter++;

            if (dataCounter == INPUT_SIZE) {
                t.setY(odsep.get(i).activity);
                listOfTuples.add(t);
                t = new Tuple(INPUT_SIZE);
                dataCounter = 0;
            }


        }
        return listOfTuples;
    }

    public void startTestingSession() {
//        Collections.shuffle(testingSet);
        System.out.println("Starting Testing Session " + LocalDateTime.now());

        nn.errTraining = 0;
        Tuple k = null;
        int i = 0;
        for (Tuple input : testingSet) {
//                System.out.println(Arrays.toString(input.getY()));
            nn.predict(input.getDataVector(), input.getY());
//                System.out.println(nn.errTraining);
            k = input;
            i++;
        }

//        System.out.println(Arrays.toString(k.getY()) + " " + Arrays.toString(nn.getOutputLayerActivated()));
//        System.out.println((double) nn.errTraining / (double) testingSet.size());

        System.out.println("Finished Testing Session " + LocalDateTime.now());
        setTestingError(nn.errTraining / testingSet.size());
    }


    public NeuralNetwork getNn() {
        return nn;
    }

    public void setNn(NeuralNetwork nn) {
        this.nn = nn;
    }

    public ArrayList<Tuple> getTrainingSet() {
        return trainingSet;
    }

    public void setTrainingSet(ArrayList<Tuple> trainingSet) {
        this.trainingSet = trainingSet;
    }

    public ArrayList<Tuple> getTestingSet() {
        return testingSet;
    }

    public void setTestingSet(ArrayList<Tuple> testingSet) {
        this.testingSet = testingSet;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public double getTrainingError() {
        return trainingError;
    }

    public void setTrainingError(double trainingError) {
        this.trainingError = trainingError;
    }

    public double getTestingError() {
        return testingError;
    }

    public void setTestingError(double testingError) {
        this.testingError = testingError;
    }


    public void saveNN() {
        FileOutputStream fout;
        try {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            fout = new FileOutputStream("C:\\Users\\Foad\\Desktop\\saveNN\\NN" + dateFormat.format(date) + ".ser");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(nn);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

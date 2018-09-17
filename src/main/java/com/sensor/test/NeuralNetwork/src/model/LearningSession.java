package com.sensor.test.NeuralNetwork.src.model;

import com.sensor.test.linker.common.AccelerometerData;
import javafx.util.Pair;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static mm.constants.Constants.INPUT_SIZE;

public class LearningSession implements Serializable {

    public static int counter = 0;
    private NeuralNetwork nn;
    private ArrayList<Tuple> trainingSet;
    private ArrayList<Tuple> testingSet;
    private int iterations;
    private double trainingError;
    private double testingError;
    private ArrayList<Pair<AccelerometerData, AccelerometerData>> odWalking;
    private ArrayList<Pair<AccelerometerData, AccelerometerData>> odSitting;
    private ArrayList<Pair<AccelerometerData, AccelerometerData>> odRunning;
    private ArrayList<Pair<ArrayList<Tuple>, ArrayList<Tuple>>> validationSet;
    private ArrayList<Tuple> allPreparedSet;
    private ArrayList<Tuple> aSet;
    private ArrayList<Tuple> bSet;
    private ArrayList<Tuple> cSet;


    public LearningSession(NeuralNetwork nn, int iterations, ArrayList<Pair<AccelerometerData, AccelerometerData>> originalData1, ArrayList<Pair<AccelerometerData, AccelerometerData>> originalData2, ArrayList<Pair<AccelerometerData, AccelerometerData>> originalData3) {
        System.out.println("Session number : "+ counter);
        counter++;
        this.nn = nn;
        this.iterations = iterations;
        trainingSet = new ArrayList<>();
        testingSet = new ArrayList<>();
        odWalking = originalData1;
        odSitting = originalData2;
        odRunning = originalData3;
        aSet = transformOriginalDataTOChunks(odSitting);
        bSet = transformOriginalDataTOChunks(odWalking);
        cSet = transformOriginalDataTOChunks(odRunning);
        buildTrainingSet(
                aSet,
                bSet,
                cSet
        );
        buildTestingSet();
        validationSet = new ArrayList<>();
        allPreparedSet = new ArrayList<>();
        buildPreparedSet(aSet, bSet, cSet);
        System.out.print("Iterations : " + iterations);
        System.out.println("full set size : " + allPreparedSet.size());
        System.out.println("training set size : " + trainingSet.size());
        System.out.println("testing set size : " + testingSet.size());
    }


    public void buildPreparedSet(ArrayList<Tuple> a, ArrayList<Tuple> b, ArrayList<Tuple> c) {
        allPreparedSet.addAll(a);
        allPreparedSet.addAll(b);
        allPreparedSet.addAll(c);
        Collections.shuffle(allPreparedSet);
    }

    public void buildTrainingSet(ArrayList<Tuple> a, ArrayList<Tuple> b, ArrayList<Tuple> c) {
        trainingSet.addAll(a);
        trainingSet.addAll(b);
        trainingSet.addAll(c);
        Collections.shuffle(trainingSet);
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

            setTrainingError(nn.errTraining);
        }
        System.out.println("Finished Learning Session " + LocalDateTime.now());

    }


    public ArrayList<Tuple> transformOriginalDataTOChunks(ArrayList<Pair<AccelerometerData, AccelerometerData>> odsep) {
        var listOfTuples = new ArrayList<Tuple>();
        var t = new Tuple(INPUT_SIZE);
        var dataCounter = 0;
        for (int i = 0; i < odsep.size(); i++) {
            t.addPoint(odsep.get(i).getKey().getX(), dataCounter);
            dataCounter++;
            t.addPoint(odsep.get(i).getKey().getY(), dataCounter);
            dataCounter++;
            t.addPoint(odsep.get(i).getKey().getZ(), dataCounter);
            dataCounter++;
            t.addPoint(odsep.get(i).getValue().getX(), dataCounter);
            dataCounter++;
            t.addPoint(odsep.get(i).getValue().getY(), dataCounter);
            dataCounter++;
            t.addPoint(odsep.get(i).getValue().getZ(), dataCounter);
            dataCounter++;

            if (dataCounter == INPUT_SIZE) {
                t.setY(odsep.get(i).getKey().activity);
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

package com.sensor.test.util;

import com.sensor.test.linker.common.AccelerometerData;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DataInterface {
    boolean addSensorData(AccelerometerData ad) throws SQLException;

    void closeConnection() throws SQLException;

    ArrayList<AccelerometerData> getSensor1Data(String activity) throws SQLException;

    ArrayList<AccelerometerData> getSensor2Data(String activity) throws SQLException;

    ArrayList<AccelerometerData> getSensor2Data() throws SQLException;

    void InsertToMergedData(ArrayList<Pair<AccelerometerData, AccelerometerData>> bulk) throws SQLException;

    boolean InsertToLiveTableFeed() throws SQLException;

    void addActivity(double[] y) throws SQLException;

    void addToDBSTRING(String s) throws SQLException;
}
package com.sensor.test.util;

import com.sensor.test.linker.common.AccelerometerData;
import com.sensor.test.linker.common.IOManager;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataAccess implements DataInterface {

    private Connection c;

    public DataAccess() {

//        Logger logger = Logger.getLogger(DataAccess.class.getName());
//        logger.log(Level.INFO, "DataAccess c'tor: attempting connection...");
        c = DBUtil.getConnection();
        if (c == null) {
//            logger.log(Level.SEVERE, "Connection Failed");
        } else {
//            logger.log(Level.INFO, "Connection Established");
        }
    }

    @Override
    public boolean addSensorData(AccelerometerData ad) throws SQLException {
        Logger logger = Logger.getLogger(DataAccess.class.getName());
        logger.log(Level.INFO, "addSensorData starting...");
        String[] dateAndTime = ad.getSubmitDate().split(" ");
        String query = "INSERT INTO `sensor1-test`(`sensorName`, `x`, `y`, `z`, `submitDate`, `devuid`) VALUES ('" + ad.getDevicename() + "'," + ad.getX() + "," + ad.getY() + "," + ad.getZ() + "," + "'" + dateAndTime[0] + " " + dateAndTime[1] + "'" + ",'" + ad.getDeviceuid() + "')";
        PreparedStatement stm = c.prepareStatement(query);
        int result = stm.executeUpdate();
        stm.close();
        if (result == 0) {//if row update count is 0, insertion failed
            return false;
        }
        return true;
    }


    @Override
    public ArrayList<AccelerometerData> getSensor1Data(String activity) throws SQLException {
        Logger logger = Logger.getLogger(DataAccess.class.getName());
        logger.log(Level.INFO, "getting " + "ST-1" + " data");
        String query = "SELECT `sensorName`,`x`,`y`,`z`,UNIX_TIMESTAMP(`submitDate`) as `submitDate`,`devuid`,`num`,`activity` FROM `s1-data` WHERE `activity` = '" + activity + "' ORDER BY `submitDate` ASC";
        PreparedStatement stm = c.prepareStatement(query);
        ResultSet rs = stm.executeQuery();
        rs.last();
        var i = rs.getRow();
        rs.beforeFirst();
        var toReturn = new ArrayList<AccelerometerData>(i);
        var index = 0;
        while (rs.next()) {
            double[] xyz = {rs.getDouble("x"),
                    rs.getDouble("y"),
                    rs.getDouble("z")};
            toReturn.add(
                    new AccelerometerData(
                            rs.getString("sensorName"),
                            xyz,
                            rs.getDouble("submitDate"),
                            rs.getString("activity"),
                            rs.getInt("num")
                    )
            );
            index++;
        }
        stm.close();
        return toReturn;
    }

    @Override
    public ArrayList<AccelerometerData> getSensor2Data(String activity) throws SQLException {

//        Logger logger = Logger.getLogger(DataAccess.class.getName());
//        logger.log(Level.INFO, "getting " + "ST-2" + " data");
        var query = "SELECT `sensorName`,`x`,`y`,`z`,UNIX_TIMESTAMP(`submitDate`) as `submitDate`,`devuid`,`num`,`activity` FROM `s2-data` WHERE `activity` = '" + activity + "' ORDER BY `submitDate` ASC";
        var stm = c.prepareStatement(query);
        var rs = stm.executeQuery();
        rs.last();
        var i = rs.getRow();
        rs.beforeFirst();
        var toReturn = new ArrayList<AccelerometerData>(i);
        var index = 0;
        while (rs.next()) {
            double[] xyz = {rs.getDouble("x"),
                    rs.getDouble("y"),
                    rs.getDouble("z")};
            toReturn.add(
                    new AccelerometerData(
                            rs.getString("sensorName"),
                            xyz,
                            rs.getDouble("submitDate"),
                            rs.getString("activity"),
                            rs.getInt("num")
                    )
            )
            ;
            index++;
        }
        stm.close();
        return toReturn;

    }

    @Override
    public ArrayList<AccelerometerData> getSensor2Data() throws SQLException {

//        Logger logger = Logger.getLogger(DataAccess.class.getName());
//        logger.log(Level.INFO, "getting " + "ST-2" + " data");
        var query = "SELECT `sensorName`,`x`,`y`,`z`,UNIX_TIMESTAMP(`submitDate`) as `submitDate`,`devuid`,`num`,`activity` FROM `s2-data` ORDER BY `submitDate` ASC";
        var stm = c.prepareStatement(query);
        var rs = stm.executeQuery();
        rs.last();
        var i = rs.getRow();
        rs.beforeFirst();
        var toReturn = new ArrayList<AccelerometerData>(i);
        var index = 0;
        while (rs.next()) {
            double[] xyz = {rs.getDouble("x"),
                    rs.getDouble("y"),
                    rs.getDouble("z")};
            toReturn.add(
                    new AccelerometerData(
                            rs.getString("sensorName"),
                            xyz,
                            rs.getDouble("submitDate"),
                            rs.getString("activity"),
                            rs.getInt("num")
                    )
            )
            ;
            index++;
        }
        stm.close();
        return toReturn;

    }

    @Override
    public void InsertToMergedData(ArrayList<Pair<AccelerometerData, AccelerometerData>> bulk) throws SQLException {
        Logger logger = Logger.getLogger(DataAccess.class.getName());
        logger.log(Level.INFO, "Inserting into merged data table");

//        try (
//                Connection connection = database.getConnection();
//                PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
//        ) {
//            int i = 0;
//
//            for (Entity entity : entities) {
//                statement.setString(1, entity.getSomeProperty());
//                // ...
//
//                statement.addBatch();
//                i++;
//
//                if (i % 1000 == 0 || i == entities.size()) {
//                    statement.executeBatch(); // Execute every 1000 items.
//                }
//            }
//        }


        StringBuilder mySql = new StringBuilder("INSERT INTO `merged-data`(`x1`, `y1`, `z1`, `x2`, `y2`, `z2`, `activity`) VALUES (?,?,?,?,?,?,?)");

        for (int i = 0; i < bulk.size() - 1; i++) {
            mySql.append(", (?,?,?,?,?,?,?)");
        }


        mySql.append(";"); //also add the terminator at the end of sql statement
        PreparedStatement myStatement = c.prepareStatement(mySql.toString());


        for (int i = 0; i < bulk.size(); i++) {
            myStatement.setString(7 * i + 1, String.valueOf(bulk.get(i).getKey().getX()));
            myStatement.setString(7 * i + 2, String.valueOf(bulk.get(i).getKey().getY()));
            myStatement.setString(7 * i + 3, String.valueOf(bulk.get(i).getKey().getZ()));

            myStatement.setString(7 * i + 4, String.valueOf(bulk.get(i).getValue().getX()));
            myStatement.setString(7 * i + 5, String.valueOf(bulk.get(i).getValue().getY()));
            myStatement.setString(7 * i + 6, String.valueOf(bulk.get(i).getValue().getZ()));

            myStatement.setString(7 * i + 7, bulk.get(i).getValue().getActivity());
        }

        myStatement.executeUpdate();
    }


    public void addActivity(double[] y) throws SQLException {
        Logger logger = Logger.getLogger(DataAccess.class.getName());
        logger.log(Level.INFO, "addSensorData starting...");
        String query = "INSERT INTO `activity`(`running`, `sitting`, `walking`, ) VALUES (y[2], y[0],y[1])";
        PreparedStatement stm = c.prepareStatement(query);
        int result = stm.executeUpdate();
        stm.close();
    }

    @Override
    public void addToDBSTRING(String s) throws SQLException {
        Logger logger = Logger.getLogger(DataAccess.class.getName());
        logger.log(Level.INFO, "addSensorData starting...");
        String query = "INSERT INTO `infotable`(`info`, `timestampOfActivity`) VALUES (`" + s + "`,NOW())";
        PreparedStatement stm = c.prepareStatement(query);
        int result = stm.executeUpdate();
        stm.close();
    }

    @Override
    public boolean InsertToLiveTableFeed() throws SQLException {
        return false;
    }

    @Override
    public void closeConnection() throws SQLException {
//        Logger logger = Logger.getLogger(DataAccess.class.getName());
//        logger.log(Level.INFO, "DataAccess closing connection");
        if (c != null)
            c.close();
    }
}

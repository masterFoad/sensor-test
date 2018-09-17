package com.sensor.test.linker.common;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "sensor1-test")
@org.hibernate.annotations.Table(
        appliesTo = "sensor1-test", indexes =
@org.hibernate.annotations.Index(
        name = "MY_INDEX",
        columnNames = {"num", "deviceuid", "x", "y", "z", "submitDate"}
)
)
public class AccelerometerData implements java.io.Serializable, Iteratable, Comparable<AccelerometerData> {


    @Column(name = "deviceuid")
    private String deviceuid;
    private String devicename;
    private ArrayList<Double> accelerometer;
    @Column(name = "submitDate")
    private String submitDate;
    public Double submitDateDouble;
    public String activity;
    public double[] xyz;
    private boolean isVisited;
    private int id;

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public void setAccelerometer(ArrayList<Double> accelerometer) {
        this.accelerometer = accelerometer;
    }

    public Double getSubmitDateDouble() {
        return submitDateDouble;
    }

    public void setSubmitDateDouble(Double submitDateDouble) {
        this.submitDateDouble = submitDateDouble;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public double[] getXyz() {
        return xyz;
    }

    public void setXyz(double[] xyz) {
        this.xyz = xyz;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AccelerometerData(String deviceuid, String devicename, ArrayList<Double> accelerometer, String submitDate) {
        this.deviceuid = deviceuid;
        this.devicename = devicename;
        this.accelerometer = accelerometer;
        //todo automatic date upon add.
//        this.submitDate = LocalDateTime.now().toString();
        this.submitDate = submitDate;
        this.submitDateDouble = (double) (System.currentTimeMillis());
    }

    public AccelerometerData(String devicename, double[] xyz, Double submitDateDouble, String activity, int num) {
        this.devicename = devicename;
//        this.accelerometer = accelerometer;
        this.submitDateDouble = submitDateDouble;
        this.activity = activity;
        this.xyz = xyz;
        this.accelerometer = new ArrayList<>();
        accelerometer.add(xyz[0]);
        accelerometer.add(xyz[1]);
        accelerometer.add(xyz[2]);
        this.id = num;
    }

    public void setCurrentTime() {
        submitDateDouble = (double) (System.currentTimeMillis());
    }

    public AccelerometerData() {
        this.accelerometer = new ArrayList<>(3);
    }


    public String getDeviceuid() {
        return deviceuid;
    }

    public void setDeviceuid(String deviceuid) {
        this.deviceuid = deviceuid;
    }

    public ArrayList<Double> getAccelerometer() {
        return accelerometer;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    public String getDevicename() {
        return devicename;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public double getX() {
        return this.accelerometer.get(0);
    }

    public double getY() {
        return this.accelerometer.get(1);
    }

    public double getZ() {
        return this.accelerometer.get(2);
    }


    @Override
    public String toString() {
        return "AccelerometerData{" +
                "deviceuid='" + deviceuid + '\'' +
                ", devicename='" + devicename + '\'' +
                ", accelerometer=" + accelerometer +
                ", submitDate='" + submitDate + '\'' +
                '}';
    }

    @Override
    public boolean isVisited() {
        return isVisited;
    }

    @Override
    public void setVisited(boolean flag) {
        isVisited = flag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccelerometerData)) return false;
        AccelerometerData that = (AccelerometerData) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public String printData() {
        return getX() + " " + getY() + " " + getZ();
    }

    @Override
    public int compareTo(AccelerometerData o) {
        return submitDateDouble.compareTo(o.submitDateDouble);
    }
}

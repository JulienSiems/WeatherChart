package model;

/**
 * Created by Julien on 05.10.2015.
 */
public class TimeData {
    private int time;
    private double data;

    public TimeData() {
    }

    public TimeData(double data, int date) {
        this.data = data;
        this.time = time;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}

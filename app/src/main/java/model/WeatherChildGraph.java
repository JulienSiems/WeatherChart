package model;

import java.util.List;

/**
 * Created by Julien on 29.09.2015.
 */
public class WeatherChildGraph  {

    // list containing all the information for the graphs
    private List<TimeData> data;
    private String property;
    private int backgroundColor;

    public WeatherChildGraph(List<TimeData> data, String property) {
        this.data = data;
        this.property = property;
    }

    public List<TimeData> getData() {
        return data;
    }

    public void setData(List<TimeData> data) {
        this.data = data;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}

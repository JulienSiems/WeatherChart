package model;

import java.util.List;

/**
 * Created by Julien on 29.09.2015.
 */
public class WeatherChildGraph  {

    // list containing all the information for the graphs
    private List<TimeData> propMin;
    private List<TimeData> propMax;
    private String property;
    private int backgroundColor;

    public WeatherChildGraph(List<TimeData> propMax, List<TimeData> propMin, String property) {
        this.propMax = propMax;
        this.propMin = propMin;
        this.property = property;
    }

    public List<TimeData> getPropMin() {
        return propMin;
    }

    public void setPropMin(List<TimeData> propMin) {
        this.propMin = propMin;
    }

    public List<TimeData> getPropMax() {
        return propMax;
    }

    public void setPropMax(List<TimeData> propMax) {
        this.propMax = propMax;
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

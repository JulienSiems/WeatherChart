package model;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.example.julien.weatherapp2.R;

import java.util.List;


public class Weather implements ParentListItem{

    private int temperature;
    private int humidity;
    private String skyCondition;
    private String cityName;
    private List<WeatherChildGraph> weatherChildGraphs;
    private int thumbnailResource;
    private int backgroundColor;
    private int expandPanelColor;
    private int time;
    private boolean isExpanded = false;

    public Weather() {
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setIsExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getExpandPanelColor() {
        return expandPanelColor;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getSkyCondition() {
        return skyCondition;
    }

    public void setChartBackgroundColor(int colorId) {
        for (WeatherChildGraph item : weatherChildGraphs) {
            item.setBackgroundColor(colorId);
        }
    }

    public void setSkyCondition(String skyCondition) {

        skyCondition = skyCondition.toLowerCase();
        this.skyCondition = skyCondition.toLowerCase();

        if (skyCondition.contains("cloud")) {
            backgroundColor = R.color.grey;
            expandPanelColor = R.color.grey_dark;
            setChartBackgroundColor(R.color.grey_really_dark);

            if (skyCondition.contains("mostly") || skyCondition.contains("partly"))
                setThumbnailResource(R.drawable.sunny_and_cloudy);
            else
                setThumbnailResource(R.drawable.cloudy);
        }
        else if (skyCondition.contains("rain") ||
                (skyCondition.contains("showers") && !skyCondition.contains("snow"))) {
            backgroundColor = R.color.blue;
            expandPanelColor = R.color.blue_dark;
            setChartBackgroundColor(R.color.blue_really_dark);

            if (skyCondition.contains("light"))
                setThumbnailResource(R.drawable.rainy_little_rain);
            else if (skyCondition.contains("moderate"))
                setThumbnailResource(R.drawable.rainy_moderate_rain);
            else if (skyCondition.contains("heavy"))
                setThumbnailResource(R.drawable.rainy);
            else
                setThumbnailResource(R.drawable.rainy_little_rain);

        } else if (skyCondition.contains("snow")) {
            backgroundColor = R.color.grey;
            expandPanelColor = R.color.grey_dark;
            setChartBackgroundColor(R.color.grey_really_dark);

            if (skyCondition.contains("light") || skyCondition.contains("flurries"))
                setThumbnailResource(R.drawable.snow_light);
            else
                setThumbnailResource(R.drawable.snow_heavy);
        } else if (skyCondition.contains("wind")) {
            backgroundColor = R.color.grey;
            expandPanelColor = R.color.grey_dark;
            setChartBackgroundColor(R.color.grey_really_dark);
            setThumbnailResource(R.drawable.windy_small);
        } else {
            backgroundColor = R.color.orange;
            expandPanelColor = R.color.orange_dark;
            setChartBackgroundColor(R.color.orange_really_dark);
            setThumbnailResource(R.drawable.sunny);
        }
    }

    public int getThumbnailResource() {
        return thumbnailResource;
    }

    private void setThumbnailResource(int thumbnailResource) {
        this.thumbnailResource = thumbnailResource;
    }

    @Override
    public List<?> getChildItemList() {

        return weatherChildGraphs;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public List<WeatherChildGraph> getWeatherChildGraphs() {
        return weatherChildGraphs;
    }

    public void setWeatherChildGraphs(List<WeatherChildGraph> weatherChildGraphs) {
        this.weatherChildGraphs = weatherChildGraphs;
    }
}

package model;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.example.julien.weatherapp2.R;

import java.util.List;

/**
 * Created by Julien on 28.09.2015.
 */
public class Weather implements ParentListItem{

    private int temperature;
    private int humidity;
    private String skyCondition;
    private String cityName;
    private List<WeatherChildGraph> weatherChildGraphs;
    private int thumbnailResource;
    private int backgroundColor;
    private int expandPanelColor;
    private boolean isExpanded = false;

    public Weather() {
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
        this.skyCondition = skyCondition;

        if (skyCondition.contains("cloud")) {
            backgroundColor = R.color.grey;
            expandPanelColor = R.color.grey_dark;
            setChartBackgroundColor(R.color.grey_really_dark);

            if (skyCondition.contains("scattered") || skyCondition.contains("few"))
                setThumbnailResource(R.drawable.sunny_and_cloudy);
            else
                setThumbnailResource(R.drawable.cloudy);
        }
        else if (skyCondition.contains("rain")) {
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

        } else if (skyCondition.contains("snowy")) {
            backgroundColor = R.color.grey;
            expandPanelColor = R.color.grey_dark;
            setChartBackgroundColor(R.color.grey_really_dark);

            if (skyCondition.contains("light"))
                setThumbnailResource(R.drawable.snow_light);
            else
                setThumbnailResource(R.drawable.snow_heavy);
        } else {
            backgroundColor = R.color.orange;
            expandPanelColor = R.color.orange_dark;
            setChartBackgroundColor(R.color.orange_really_dark);
            setThumbnailResource(R.drawable.sunny);
        }

        /*

        if (skyCondition.equals("moderate rain")) {
            backgroundColor = R.color.blue;
            expandPanelColor = R.color.blue_dark;
            setChartBackgroundColor(R.color.blue_really_dark);
            setThumbnailResource(R.drawable.rainy);
        }
        else if (skyCondition.equals("light rain")) {
            backgroundColor = R.color.blue;
            expandPanelColor = R.color.blue_dark;
            setChartBackgroundColor(R.color.blue_really_dark);
            setThumbnailResource(R.drawable.rainy_little_rain);
        }
        else if (skyCondition.contains("clouds")) {
            backgroundColor = R.color.grey;
            expandPanelColor = R.color.grey_dark;
            setChartBackgroundColor(R.color.grey_really_dark);
            setThumbnailResource(R.drawable.cloudy);
        }
        else if (skyCondition.contains("clear")) {
            backgroundColor = R.color.orange;
            expandPanelColor = R.color.orange_dark;
            setChartBackgroundColor(R.color.orange_really_dark);
            setThumbnailResource(R.drawable.sunny);
        }
        else {
            backgroundColor = R.color.orange;
            expandPanelColor = R.color.orange_dark;
            setChartBackgroundColor(R.color.orange_really_dark);
            setThumbnailResource(R.drawable.sunny);
        }
        this.skyCondition = skyCondition;
        */
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

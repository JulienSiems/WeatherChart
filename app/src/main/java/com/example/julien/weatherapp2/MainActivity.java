package com.example.julien.weatherapp2;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import data.WeatherExpandableAdapter;
import data.WeatherParentViewHolder;
import model.TimeData;
import model.Weather;
import model.WeatherChildGraph;

public class MainActivity extends AppCompatActivity {

    private RecyclerView weatherList;
    private EditText cityInput;
    private Button submitButton, cancelButton;
    private List<ParentListItem> weathers = new ArrayList();
    private ArrayList<String> cityNames = new ArrayList(Arrays.asList("Paris", "Washington",
            "Berlin", "Peking", "Oslo", "Geneva", "Bangkok", "Aberdeen", "Tallinn", "Cologne"));

    // , "Tartu", "Oldenburg", "Dubai", "Peking", "Oslo", "Geneva", "Bangkok", "Aberdeen", "Tallinn", "Cologne"
    private RequestQueue queue;
    private WeatherExpandableAdapter weatherExpandableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);

        requestWeatherData();

        weatherList = (RecyclerView) findViewById(R.id.weatherList);
        weatherList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {

                // brings the weather item list back to its initial order
                Collections.sort(weathers, new Comparator<ParentListItem>() {
                    @Override
                    public int compare(ParentListItem lhs, ParentListItem rhs) {
                        Weather lw = (Weather) lhs;
                        Weather rw = (Weather) rhs;

                        int index1 = cityNames.indexOf(lw.getCityName());
                        int index2 = cityNames.indexOf(rw.getCityName());
                        if (index1 == -1 || index2 == -1)
                            return 0;
                        else if (index1 < index2)
                            return -1;
                        else
                            return 1;
                    }
                });

                weatherExpandableAdapter = new WeatherExpandableAdapter(MainActivity.this, weathers);
                weatherExpandableAdapter.setExpandCollapseListener(expandCollapseListener);
                weatherList.setAdapter(weatherExpandableAdapter);
            }
        });


    }

    ExpandableRecyclerAdapter.ExpandCollapseListener expandCollapseListener = new ExpandableRecyclerAdapter.ExpandCollapseListener() {
        @Override
        public void onListItemExpanded(int i) {
            final WeatherParentViewHolder weatherParentViewHolder = weatherExpandableAdapter.weatherParentViewHolders.get(i);
            final RotateAnimation rotateAnim = new RotateAnimation(0, 180,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);

            rotateAnim.setDuration(600);
            rotateAnim.setFillAfter(true);
            weatherParentViewHolder.arrow.startAnimation(rotateAnim);
            Weather clickedWeather = (Weather) weatherExpandableAdapter.parentItemList.get(i);
            clickedWeather.setIsExpanded(true);
            weatherExpandableAdapter.notifyParentItemChanged(i);
        }

        @Override
        public void onListItemCollapsed(int i) {
            final WeatherParentViewHolder weatherParentViewHolder = weatherExpandableAdapter.weatherParentViewHolders.get(i);
            final RotateAnimation rotateAnim = new RotateAnimation(0, 180,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);

            rotateAnim.setDuration(600);
            rotateAnim.setFillAfter(true);
            weatherParentViewHolder.arrow.startAnimation(rotateAnim);
            Weather clickedWeather = (Weather) weatherExpandableAdapter.parentItemList.get(i);
            clickedWeather.setIsExpanded(false);
            weatherExpandableAdapter.notifyParentItemChanged(i);
        }
    };

    private void requestWeatherData() {
        JsonObjectRequest request;

        // reset the weather List before retrieving new data

        weathers = new ArrayList<>();
        for (String city : cityNames) {
            request = new JsonObjectRequest(
                    "http://api.openweathermap.org/data/2.5/forecast?q=" + city + //API KEY
                            "&units=metric&APPID=a957e9ff0700914f4f97c1739ea4171b",
                    listener, errorListener);
            queue.add(request);
        }
    }

    private Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {

            Weather weather = new Weather();
            List<WeatherChildGraph> weatherChildGraphs = new ArrayList();

            ArrayList<TimeData> temperatures = new ArrayList();
            ArrayList<TimeData> humidities = new ArrayList();

            try {
                //retrieve the information from the json object and fill them into the object
                weather.setCityName(response.getJSONObject("city").getString("name"));
                JSONArray weatherDataArray = response.getJSONArray("list");
                JSONObject currentWeather = weatherDataArray.getJSONObject(0);

                for (int i = 0; i < 12; i++) {
                    JSONObject weatherPredict = weatherDataArray.getJSONObject(i);

                    TimeData temperatureTimeData = new TimeData();
                    temperatureTimeData.setTime(weatherPredict.getInt("dt"));
                    temperatureTimeData.setData(weatherPredict.getJSONObject("main").getDouble("temp"));
                    temperatures.add(temperatureTimeData);

                    TimeData humidityTimeData = new TimeData();
                    humidityTimeData.setTime(weatherPredict.getInt("dt"));
                    humidityTimeData.setData(weatherPredict.getJSONObject("main").getDouble("humidity"));
                    humidities.add(humidityTimeData);
                }

                weatherChildGraphs.add(new WeatherChildGraph(temperatures, getResources().getString(R.string.temperature)));
                weatherChildGraphs.add(new WeatherChildGraph(humidities, getResources().getString(R.string.humidity)));
                weather.setWeatherChildGraphs(weatherChildGraphs);

                JSONObject weatherObject = currentWeather.getJSONObject("main");
                weather.setTemperature((int) weatherObject.getDouble("temp"));
                weather.setHumidity((int) weatherObject.getDouble("humidity"));
                weather.setSkyCondition(currentWeather
                        .getJSONArray("weather")
                        .getJSONObject(0)
                        .getString("description"));
                //weather.setThumbnailResource(R.drawable.sunny_and_cloudy);

                weathers.add(weather);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void addWeatherItem() {

        // custom Dialog
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.add_city_dialog);
        dialog.setTitle("Add city");

        // set the custom dialog components - text, image and button
        cityInput = (EditText) dialog.findViewById(R.id.enterCity);
        submitButton = (Button) dialog.findViewById(R.id.submit);
        cancelButton = (Button) dialog.findViewById(R.id.cancel_dialog);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String s = cityInput.getText().toString();
                cityNames.add(cityInput.getText().toString());
                requestWeatherData();
                weatherExpandableAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.addWeather) {
            addWeatherItem();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

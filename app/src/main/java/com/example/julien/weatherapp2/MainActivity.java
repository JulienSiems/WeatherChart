package com.example.julien.weatherapp2;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private ArrayList<String> cityNames = new ArrayList(Arrays.asList("Paris", "Washington","Berlin", "Peking", "Oslo", "Geneva", "Bangkok", "Aberdeen", "Tallinn", "Cologne"));
    private int counter = 0;

    // "Berlin", "Peking", "Oslo", "Geneva", "Bangkok", "Aberdeen", "Tallinn", "Cologne"
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

                // brings the weather item list into the same order as the city list order
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
                counter++;

                // only set the adapter once all the weather items have been loaded
                if (counter == cityNames.size()) {
                    weatherExpandableAdapter = new WeatherExpandableAdapter(MainActivity.this, weathers);
                    weatherExpandableAdapter.setExpandCollapseListener(expandCollapseListener);
                    weatherList.setAdapter(weatherExpandableAdapter);
                    counter = 0;
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addWeatherItem();
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
                    "https://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20weather.bylocation%20WHERE%20location%3D%27"
                            + city +
                            "%27%20AND%20unit%3D%22c%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=",
                    listener, errorListener);
            queue.add(request);
        }
    }

    private Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {

            Weather weather = new Weather();
            List<WeatherChildGraph> weatherChildGraphs = new ArrayList();

            ArrayList<TimeData> temperatureMax = new ArrayList<>();
            ArrayList<TimeData> temperatureMin = new ArrayList<>();

            ArrayList<TimeData> humidities = new ArrayList();

            try {
                //retrieve the information from the json object and fill them into the object
                JSONObject channel = response.getJSONObject("query")
                                                .getJSONObject("results")
                                                .getJSONObject("weather")
                                                .getJSONObject("rss")
                                                .getJSONObject("channel");
                weather.setCityName(channel.getJSONObject("location").getString("city"));
                JSONObject weatherItem = channel.getJSONObject("item");
                JSONArray weatherDataArray = weatherItem.getJSONArray("forecast");

                DateFormat dfs = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);

                for (int i = 0; i < weatherDataArray.length(); i++) {
                    JSONObject weatherPredict = weatherDataArray.getJSONObject(i);

                    TimeData temperatureTimeDataMax = new TimeData();
                    String date = weatherPredict.getString("date");
                    Date intDate = dfs.parse(date);
                    long time = intDate.getTime()/1000;

                    temperatureTimeDataMax.setTime((int) time);
                    temperatureTimeDataMax.setData(weatherPredict.getInt("high"));
                    temperatureMax.add(temperatureTimeDataMax);


                    TimeData temperatureTimeDataMin = new TimeData();
                    temperatureTimeDataMin.setTime((int) time);
                    temperatureTimeDataMin.setData(weatherPredict.getInt("low"));
                    temperatureMin.add(temperatureTimeDataMin);
                }

                weatherChildGraphs.add(new WeatherChildGraph(temperatureMax, temperatureMin, getResources().getString(R.string.temperature)));
                //weatherChildGraphs.add(new WeatherChildGraph(humidities, getResources().getString(R.string.humidity)));
                weather.setWeatherChildGraphs(weatherChildGraphs);


                JSONObject currentWeather = weatherItem.getJSONObject("condition");
                weather.setTemperature((int) currentWeather.getDouble("temp"));
                weather.setHumidity(channel.getJSONObject("atmosphere").getInt("humidity"));
                weather.setSkyCondition(currentWeather.getString("text"));

                weathers.add(weather);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
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

        // set the on click listener to submit the new city
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

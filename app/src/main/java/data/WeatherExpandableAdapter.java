package data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.example.julien.weatherapp2.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Weather;
import model.WeatherChildGraph;

/**
 * Created by Julien on 28.09.2015.
 */
public class WeatherExpandableAdapter extends ExpandableRecyclerAdapter<WeatherParentViewHolder, WeatherChildViewHolder> {

    private LayoutInflater mInflater;
    public List<? extends ParentListItem> parentItemList;
    public Map<Integer, WeatherParentViewHolder> weatherParentViewHolders = new HashMap<>();

    public WeatherExpandableAdapter(Context context, List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflater = LayoutInflater.from(context);
        this.parentItemList = parentItemList;
    }

    @Override
    public void onBindChildViewHolder(WeatherChildViewHolder weatherChildViewHolder, int i, Object o) {
        WeatherChildGraph weatherChildGraph = (WeatherChildGraph) o;
        weatherChildViewHolder.setLineChart(weatherChildGraph);
        weatherChildViewHolder.backgroundLayout.setBackgroundResource(weatherChildGraph.getBackgroundColor());
    }

    @Override
    public void onBindParentViewHolder(WeatherParentViewHolder weatherParentViewHolder, int i, ParentListItem parentListItem) {
        Weather weather = (Weather) parentListItem;
        weatherParentViewHolder.cityName.setText(weather.getCityName());
        weatherParentViewHolder.temperature.setText(String.valueOf(weather.getTemperature()) + "°C");
        weatherParentViewHolder.humidity.setText(String.valueOf(weather.getHumidity()) + "%");
        weatherParentViewHolder.thumbnailImage.setImageResource(weather.getThumbnailResource());
        weatherParentViewHolder.panelLayout.setBackgroundResource(weather.getExpandPanelColor());
        weatherParentViewHolder.internalLayout.setBackgroundResource(weather.getBackgroundColor());
        weatherParentViewHolder.isExpanded(weather.isExpanded());
        weatherParentViewHolders.put(i, weatherParentViewHolder);
    }

    @Override
    public WeatherChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.weather_child_graph, viewGroup, false);
        return new WeatherChildViewHolder(view);
    }

    @Override
    public WeatherParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.weather_parent_listitem, viewGroup, false);
        return new WeatherParentViewHolder(view);
    }

    @Override
    public void onParentListItemExpanded(int position) {
        super.onParentListItemExpanded(position);
    }

    private void closeAllExcept(int position) {
        for (int i = 0; i < parentItemList.size(); i++) {
            if (i != position)
                collapseParent(i);
        }
    }

}

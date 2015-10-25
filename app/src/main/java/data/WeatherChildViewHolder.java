package data;

import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.example.julien.weatherapp2.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.TimeData;
import model.WeatherChildGraph;

/**
 * Created by Julien on 28.09.2015.
 */
public class WeatherChildViewHolder extends ChildViewHolder {

    private LineChart lineChart;
    public RelativeLayout backgroundLayout;

    public WeatherChildViewHolder(View itemView) {
        super(itemView);
        lineChart = (LineChart) itemView.findViewById(R.id.weatherChart);
        backgroundLayout = (RelativeLayout) itemView.findViewById(R.id.chartLayout);
    }

    public void setLineChart(WeatherChildGraph weatherChildGraph) {

        lineChart.setDescription("");

        List<TimeData> timeDatas = weatherChildGraph.getData();
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < timeDatas.size(); i++) {
            TimeData timeDataItem = timeDatas.get(i);
            Entry entry = new Entry((int) timeDataItem.getData(), i);
            entries.add(entry);
        }

        LineDataSet lineDataSet = new LineDataSet(entries, weatherChildGraph.getProperty());
        lineDataSet.setDrawCubic(true);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setLineWidth(3.0f);
        lineDataSet.setColor(Color.WHITE);
        lineDataSet.enableDashedLine(10.0f, 10.0f, 0.0f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setFillColor(Color.WHITE);
        //lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawValues(false);

        // Setting the hours for labels
        DateFormat df = new SimpleDateFormat("dd");
        ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i < timeDatas.size(); i++) {
            TimeData timeDataItem = timeDatas.get(i);
            Date time = new Date((long) timeDataItem.getTime() * 1000);
            String hour = df.format(time);
            labels.add(hour);
        }

        LineData data = new LineData(labels, lineDataSet);
        lineChart.setTouchEnabled(false);
        lineChart.animateX(2000);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setLabelCount(4, true);
        lineChart.getAxisLeft().setTextColor(Color.WHITE);
        lineChart.getXAxis().setTextColor(Color.WHITE);
        lineChart.getLegend().setTextColor(Color.WHITE);

        // makes the background transparent
        lineChart.setDrawGridBackground(false);

        lineChart.setHardwareAccelerationEnabled(true);

        lineChart.animateY(2000);
        lineChart.setData(data);
    }
}

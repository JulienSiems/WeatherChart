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

        List<TimeData> timeDatasMax = weatherChildGraph.getPropMax();
        List<TimeData> timeDatasMin = weatherChildGraph.getPropMin();

        ArrayList<Entry> entriesMax = new ArrayList<>();
        ArrayList<Entry> entriesMin = new ArrayList<>();

        for (int i = 0; i < timeDatasMax.size(); i++) {
            TimeData timeDataMax = timeDatasMax.get(i);
            Entry entry = new Entry((int) timeDataMax.getData(), i);
            entriesMax.add(entry);

            TimeData timeDataMin = timeDatasMin.get(i);
            entry = new Entry((int) timeDataMin.getData(), i);
            entriesMin.add(entry);
        }

        LineDataSet lineDataSetMax = new LineDataSet(entriesMax, "High " + weatherChildGraph.getProperty());
        LineDataSet lineDataSetMin = new LineDataSet(entriesMin, "Low " + weatherChildGraph.getProperty());

        int blue = 0xff3f51b5;

        lineDataSetMax.setDrawCubic(true);
        lineDataSetMax.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSetMax.setLineWidth(4.0f);
        lineDataSetMax.setColor(Color.RED);
        lineDataSetMax.enableDashedLine(10.0f, 10.0f, 0.0f);
        lineDataSetMax.setDrawCircles(false);
        lineDataSetMax.setDrawValues(false);
        lineDataSetMax.setFillColor(Color.RED);

        lineDataSetMin.setDrawCubic(true);
        lineDataSetMin.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSetMin.setLineWidth(4.0f);
        lineDataSetMin.setColor(blue);
        lineDataSetMin.enableDashedLine(10.0f, 10.0f, 0.0f);
        lineDataSetMin.setDrawCircles(false);
        lineDataSetMin.setDrawValues(false);
        lineDataSetMin.setFillColor(blue);

        // Setting the hours for labels
        DateFormat df = new SimpleDateFormat("dd");
        ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i < timeDatasMax.size(); i++) {
            TimeData timeDataItem = timeDatasMax.get(i);
            Date time = new Date((long) timeDataItem.getTime() * 1000);
            String hour = df.format(time);
            labels.add(hour);
        }

        LineData data = new LineData(labels, lineDataSetMax);
        data.addDataSet(lineDataSetMin);

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

package data;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.example.julien.weatherapp2.R;

/**
 * Created by Julien on 28.09.2015.
 */
public class WeatherParentViewHolder extends ParentViewHolder{

    public TextView cityName;
    public TextView temperature;
    public TextView humidity;
    public ImageView thumbnailImage;
    public RelativeLayout internalLayout;
    public RelativeLayout panelLayout;
    public ImageView arrow;

    public WeatherParentViewHolder(View itemView) {
        super(itemView);

        cityName = (TextView) itemView.findViewById(R.id.city);
        temperature = (TextView) itemView.findViewById(R.id.temperature);
        humidity = (TextView) itemView.findViewById(R.id.humidity);
        thumbnailImage = (ImageView) itemView.findViewById(R.id.thumbnailImage);
        internalLayout = (RelativeLayout) itemView.findViewById(R.id.internalLayout);
        panelLayout = (RelativeLayout) itemView.findViewById(R.id.panelLayout);
        arrow = (ImageView) itemView.findViewById(R.id.showGraphArrow);
    }

    public void isExpanded(boolean b) {
        if (b == true)
            arrow.setImageResource(R.drawable.arrow_up);
        else
            arrow.setImageResource(R.drawable.arrow_down);
    }
}

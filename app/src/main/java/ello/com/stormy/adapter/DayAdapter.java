package ello.com.stormy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

import ello.com.stormy.R;
import ello.com.stormy.weather.Day;

/**
 * Created by venkatgonuguntala on 10/14/15.
 */

/**
 *
 * View Holder Design Pattern ->  https://www.codeofaninja.com/2013/09/android-viewholder-pattern-example.html
 */
public class DayAdapter extends BaseAdapter {

    //This adapter needs to know the context and the adapter it needs to match.
    private Context mContext;
    private Day[]mDays;

    public DayAdapter(Context context, Day[] days){
        mContext = context;
        mDays = days;
    }
    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;//This can be used to tag items for easy reference and we are not using this.
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Here is where mapping occurs.
        ViewHolder holder;
        if(convertView == null){
            //brand new
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.dayLabel = (TextView) convertView.findViewById(R.id.dayNameLabel);
            holder.temperatureLable = (TextView) convertView.findViewById(R.id.temperatureLabel);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //Now we need to set the data
        Day day = mDays[position];

        holder.iconImageView.setImageResource(day.getIconId());
        if(position == 0){
            holder.dayLabel.setText("Today");
        }
        else if(position == 1){
            holder.dayLabel.setText("Tommorrow");
        }
        else {
            holder.dayLabel.setText(day.getDayOfTheWeek());
        }
        holder.temperatureLable.setText(day.getTemperatureMax() + "");

        return convertView;
    }

    public static class ViewHolder {
        ImageView iconImageView; //public by default
        TextView temperatureLable;
        TextView dayLabel;
    }
}

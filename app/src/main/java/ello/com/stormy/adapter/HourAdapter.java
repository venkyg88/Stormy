package ello.com.stormy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

import ello.com.stormy.R;
import ello.com.stormy.weather.Hour;

/**
 * Created by venkatgonuguntala on 11/14/15.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    private Hour[] mHours;

    public HourAdapter(Hour[] hours){
        mHours = hours;
    }
    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_list_item, parent, false);
        HourViewHolder viewHolder = new HourViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
        holder.bindHour(mHours[position]);
    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder {

        public TextView mTemperatureLabel;
        public TextView mTimeLabel;
        public TextView mSummaryLabel;
        public ImageView mImageView;

        public HourViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
            mSummaryLabel = (TextView) itemView.findViewById(R.id.summaryLabel);
            mTimeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
            mTemperatureLabel = (TextView) itemView.findViewById(R.id.hourlyTemperatureLabel);
        }

        public void bindHour(Hour hour){
            mTemperatureLabel.setText(hour.getTemperature()+"");
            mTimeLabel.setText(hour.getHour());
            mSummaryLabel.setText(hour.getSummary());
            mImageView.setImageResource(hour.getIconId());
        }
    }
}

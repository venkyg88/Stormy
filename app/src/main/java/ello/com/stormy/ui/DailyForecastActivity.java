package ello.com.stormy.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Arrays;

import butterknife.Bind;
import ello.com.stormy.R;
import ello.com.stormy.adapter.DayAdapter;
import ello.com.stormy.weather.Day;

public class DailyForecastActivity extends ListActivity {

    private Day mdays[];
    private String mCity;
    private String mState;

    private TextView mLocationLabel;

    //@Bind(R.id.dailyLocationLabel) TextView mLocationLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        mLocationLabel = (TextView) findViewById(R.id.dailyLocationLabel);

        Intent intent = getIntent();

        Parcelable parcelables[] = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        mdays = Arrays.copyOf(parcelables, parcelables.length, Day[].class);

        mCity = intent.getStringExtra("City");
        mState = intent.getStringExtra("State");
        mLocationLabel.setText(mCity+", "+mState);
        DayAdapter dayAdapter = new DayAdapter(this, mdays);
        setListAdapter(dayAdapter);

    }


    /*@Override
    protected void onResume() {
        super.onResume();
        mLocationLabel.setText(mCity+", "+mState);
    }*/
}

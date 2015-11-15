package ello.com.stormy.ui;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import ello.com.stormy.R;
import ello.com.stormy.adapter.HourAdapter;
import ello.com.stormy.weather.Hour;

public class HourlyForcastActivity extends AppCompatActivity {

    private Hour[] mHours;

    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forcast);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);

        Intent intent = getIntent();
        Parcelable[] parcelables =  intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
        mHours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);

        HourAdapter adapter = new HourAdapter(mHours);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

    }
}

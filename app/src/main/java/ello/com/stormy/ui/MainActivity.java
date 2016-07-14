package ello.com.stormy.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ello.com.stormy.weather.Current;
import ello.com.stormy.R;
import ello.com.stormy.weather.Day;
import ello.com.stormy.weather.Forecast;
import ello.com.stormy.weather.Hour;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener{

    public final static String TAG = MainActivity.class.getSimpleName();
    public final static String DAILY_FORECAST = "DAILY_FORECAST";
    public final static String HOURLY_FORECAST = "HOURLY_FORECAST";

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;

    private Forecast mForecast;

    private String mStateName;
    private String mCityName;

    @Bind(R.id.timeLabel) TextView mTimeLable;
    @Bind(R.id.precipValue) TextView mPrecipValue;
    @Bind(R.id.iconImageView) ImageView mIconImageView;
    @Bind(R.id.humidityValue) TextView mHumidityValue;
    @Bind(R.id.temperatureLabel) TextView mTemperatureLabel;
    @Bind(R.id.summaryLabel) TextView mSummaryLabel;
    @Bind(R.id.refreshImageView) ImageView mRefreshImageView;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;
    @Bind(R.id.locationLabel) TextView mLocationLabel;

    private double latitue;
    private double longitute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();*/
        buildGoogleApiClient();

        mProgressBar.setVisibility(View.INVISIBLE);



        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitue, longitute);
            }
        });

        Log.d(TAG, "Main UI code is running");
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**this method is called once the mGoogleApiClient is connected **/

    @Override
    public void onConnected(Bundle bundle) {
        //provide a simple way of getting device location and is suited for
        //and is suited for applications that do not require a fine grained location and that do not need location updates.
        //get the best and the most recently used location currently available. Which may be null in rare cases when location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null){
            latitue = mLastLocation.getLatitude();
            longitute = mLastLocation.getLongitude();
        }
        getForecast(latitue, longitute);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "googleApiClient connection has been suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "googleApiClient connection has been failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("LOG_TAG",location.toString());
        latitue = location.getLatitude();
        longitute = location.getLongitude();
    }



    private void getForecast(double latitue, double longitute) {

        //Code to retrieve location address from latitude and logitude - reverse Geocoding
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latitue, longitute, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            Log.i("City", addresses.get(0).getLocality());
            mStateName = addresses.get(0).getAdminArea();
            mCityName = addresses.get(0).getLocality();
        }
        //----------------------------------------------------

        String apiKey ="735ded0959471c92a8476b357e1ecf8f";

        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey + "/" + latitue + "," + longitute;

        if (isNetworkAvailable()) {
            toggleRefresh();
            OkHttpClient client = new OkHttpClient(); //This is our main client object
            //next we need to build the request that the client will send to the server forecast
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            //Next thing we need is a call object, we gonna put this request in call object, so the data to save is call
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    //Now we have an execute method from our call class and it returns a response object.
                    //Lets store that response in an object
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    try {
                        //Response response = call.execute();  removing becasue it's synchronous
                        //After we get response, lets do something with it :-)
                        //check if the request was successful
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });

                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        }else{
            Toast.makeText(this, R.string.error_network, Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if(mProgressBar.getVisibility() == View.INVISIBLE){
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        Current current = mForecast.getCurrent();
        mTemperatureLabel.setText(current.getTemperature() + "");
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPredicipitate() + "%");
        mTimeLable.setText("At " + current.getFormattedTime() + " it will be");
        mSummaryLabel.setText(current.getSummary());

        mLocationLabel.setText(mCityName+", "+mStateName);


        Drawable drawable = getResources().getDrawable(current.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();

        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));

        return forecast;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];
        for(int i = 0; i < data.length(); i++){
            JSONObject hourJson = data.getJSONObject(i);
            Hour hour = new Hour();

            hour.setSummary(hourJson.getString("summary"));
            hour.setIcon(hourJson.getString("icon"));
            hour.setTimeZone(timezone);
            hour.setTime(hourJson.getLong("time"));
            hour.setTemperature(hourJson.getDouble("temperature"));

            hours[i] = hour;
        }
        return hours;

    }

    private Day[] getDailyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] days = new Day[data.length()];
        for(int i = 0; i < data.length(); i++){
            JSONObject dayJson = data.getJSONObject(i);
            Day day = new Day();

            day.setIcon(dayJson.getString("icon"));
            day.setSummary(dayJson.getString("summary"));
            day.setTemperatureMax(dayJson.getDouble("temperatureMax"));
            day.setTime(dayJson.getLong("time"));
            day.setTimeZone(timezone);

            days[i] = day;
        }
        return days;

    }


    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG,"From JSON: " + timezone);

        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setPredicipitate(currently.getDouble("precipProbability"));
        current.setIcon(currently.getString("icon"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTime(currently.getLong("time"));
        current.setTimeZone(timezone);

        Log.d(TAG, current.getFormattedTime());

        return current;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    @OnClick(R.id.dailyButton)
    public void startDailyActivity(View view){
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST, mForecast.getDailyForecast());
        intent.putExtra("City", mCityName);
        intent.putExtra("State", mStateName);
        startActivity(intent);
    }

    @OnClick(R.id.hourlyButton)
    public void startHourlyActivity(View view){
        Intent intent = new Intent(this, HourlyForcastActivity.class);
        intent.putExtra(HOURLY_FORECAST, mForecast.getHourlyForecast());
        startActivity(intent);
    }


}

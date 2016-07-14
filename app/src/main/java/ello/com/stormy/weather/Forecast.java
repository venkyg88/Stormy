package ello.com.stormy.weather;

import ello.com.stormy.R;

/**
 * Created by venkatgonuguntala on 10/9/15.
 */
public class Forecast {

    private Current mCurrent;
    private Hour[] mHourlyForecast;
    private Day[] mDailyForecast;

    public Current getCurrent() {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public Hour[] getHourlyForecast() {
        return mHourlyForecast;
    }

    public void setHourlyForecast(Hour[] hourlyForecast) {
        mHourlyForecast = hourlyForecast;
    }

    public Day[] getDailyForecast() {
        return mDailyForecast;
    }

    public void setDailyForecast(Day[] dailyForecast) {
        mDailyForecast = dailyForecast;
    }

    public static int getIconId(String imageIcon) {
        int iconId = R.drawable.clear_day;

        if (imageIcon.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        }
        else if (imageIcon.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        }
        else if (imageIcon.equals("rain")) {
            iconId = R.drawable.rain;
        }
        else if (imageIcon.equals("snow")) {
            iconId = R.drawable.snow;
        }
        else if (imageIcon.equals("sleet")) {
            iconId = R.drawable.sleet;
        }
        else if (imageIcon.equals("wind")) {
            iconId = R.drawable.wind;
        }
        else if (imageIcon.equals("fog")) {
            iconId = R.drawable.fog;
        }
        else if (imageIcon.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        }
        else if (imageIcon.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        }
        else if (imageIcon.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }
        return iconId;
    }
}

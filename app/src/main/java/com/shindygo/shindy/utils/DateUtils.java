package com.shindygo.shindy.utils;

import android.content.Context;
import android.content.res.Resources;

import com.shindygo.shindy.Api;
import com.shindygo.shindy.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {
    /** One second (in milliseconds) */
    private static final int _A_SECOND = 1000;
    /** One minute (in milliseconds) */
    private static final int _A_MINUTE = 60 * _A_SECOND;
    /** One hour (in milliseconds) */
    private static final int _AN_HOUR = 60 * _A_MINUTE;
    /** One day (in milliseconds) */
    private static final int _A_DAY = 24 * _AN_HOUR;
    /** One week (in milliseconds) */
    private static final int _A_WEEK = 7 * _A_DAY;
    /** One month (in milliseconds) */
    private static final int _A_MONTH = 30 * _A_DAY;
    /** One year (in milliseconds) */
    private static final int _A_YEAR = 12 * _A_MONTH;


    public static String getTimeAgo(long time) {
        if (time < 1000000000000L)
            // if timestamp given in seconds, convert to millis
            time *= 1000;

        Context context = Api.getContext();

        final long now = Calendar.getInstance().getTimeInMillis();
        if (time > now || time <= 0) return "";


        final Resources res = context.getResources();
        final long time_difference = now - time;
        if (time_difference < _A_MINUTE*2)
            return res.getString(R.string.just_now);
        else if (time_difference < 50 * _A_MINUTE)
            return res.getString(R.string.time_ago,
                    res.getQuantityString(R.plurals.minutes, (int) time_difference / _A_MINUTE, time_difference / _A_MINUTE));
        else if (time_difference < 24 * _AN_HOUR)
            return res.getString(R.string.time_ago,
                    res.getQuantityString(R.plurals.hours, (int) time_difference / _AN_HOUR, time_difference / _AN_HOUR));
        else if (time_difference < 48 * _AN_HOUR)
            return res.getString(R.string.yesterday);
        else if (time_difference < 7 * _A_DAY)
            return res.getString(R.string.time_ago,
                    res.getQuantityString(R.plurals.days, (int) time_difference / _A_DAY,  time_difference / _A_DAY));
        else if (time_difference < 4 * _A_WEEK)
            return res.getString(R.string.time_ago,
                    res.getQuantityString(R.plurals.weeks, (int) time_difference / _A_WEEK,  time_difference / _A_WEEK));
        else if (time_difference < 12 * _A_MONTH)
            return res.getString(R.string.time_ago,
                    res.getQuantityString(R.plurals.months, (int) time_difference / _A_MONTH,  time_difference / _A_MONTH));
        else if (time_difference >= 12 * _A_MONTH)
            return res.getString(R.string.time_ago,
                    res.getQuantityString(R.plurals.years, (int) time_difference / _A_YEAR,  time_difference / _A_YEAR));
        else
            return res.getString(R.string.time_ago,
                    res.getQuantityString(R.plurals.days, (int) time_difference / _A_DAY, time_difference / _A_DAY));
    }

    public static String getTimeAgo(String dateString, String dateFormat) throws ParseException{
        return getTimeAgo(new SimpleDateFormat(dateFormat).parse(dateString).getTime());
    }



}


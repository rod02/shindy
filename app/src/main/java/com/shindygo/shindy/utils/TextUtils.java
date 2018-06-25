package com.shindygo.shindy.utils;

import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextUtils {
    public static final int MALE = 1;
    public static final int FEMALE = 0;

    /*
    @    "yyyy-MM-dd";
     */
    public static final String SDF_1 = "yyyy-MM-dd";
    public static final String SDF_2 = "EEE MMM d,  yyyy";
    public static final String SDF_3 = "HH:mm:ss";
    public static final String SDF_4 = "h:mm a";
    public static final String SDF_5 = "yyyy-MM-dd HH:mm:ss";


    public static String getTimeDuration(String startTime, String endTime){

        return startTime + " - " + endTime;
    }
    public static String getTimeDuration(Event event){
        return TextUtils.getTimeDuration(event.getStartTime(),event.getEndTime());
    }

    public static String getRemainingStocks(Event event, int gender){
        int m = 0;
        int f = 0;
        int n = 0;
        switch (gender){
            case MALE:
                m = subtractString(event.getMax_male(), event.getJoinMale());
                if (m>0){
                    f =subtractString(event.getMax_female(), event.getJoinFemale());
                    if(f<0){
                        m = m+f;
                    }
                }
                n = m;
                break;

            case FEMALE:
                f =subtractString(event.getMax_female(), event.getJoinFemale());
                if (f>0){
                    m =subtractString(event.getMax_female(), event.getJoinFemale());
                    if(m<0){
                        f = f+m;
                    }
                }
                n = m;
                break;
        }
        if (n<0){
            n = 0;
        }
        return String.valueOf(n);
    }

    public static int getRemainingStocks(Event event){
        int maxTicket = addString(event.getMax_male(), event.getMax_female());
        int soldTickets = subtractString(event.getJoinMale(), event.getJoinFemale());
        return maxTicket-soldTickets;
    }



    static  int subtractString(String x, String y){
        try {
            return Integer.parseInt(x) - Integer.parseInt(y);

        }catch (Exception e){
            return 0;
        }

    }

    static  int addString(String x, String y){
        try {
            return Integer.parseInt(x) + Integer.parseInt(y);

        }catch (Exception e){
            return 0;
        }

    }

    public static int parseInt(String s){
        try{
            return Integer.parseInt(s);
        }catch (NumberFormatException e){

        }
        return 0;
    }

    public static String formatDate(String format, Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String formatDate(String date, String formatFrom,String formatTo) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(formatFrom);
        return formatDate(formatTo, sdf.parse(date));
    }

    public static String formatTime(String time, String formatFrom,String formatTo) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(formatFrom);
        return formatDate(formatTo, sdf.parse(time));
    }


    public static String formatTime(Event event, String formatFrom, String formatTo) throws ParseException{
        return getTimeDuration(formatTime(event.getStartTime(),formatFrom,formatTo),
                formatTime(event.getEndTime(),formatFrom,formatTo)
                );
    }
}

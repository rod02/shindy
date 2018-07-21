package com.shindygo.shindy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shindygo.shindy.Api;
import com.shindygo.shindy.R;
import com.shindygo.shindy.utils.DateUtils;
import com.shindygo.shindy.utils.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class UserAvailability {


    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("fbid")
    @Expose
    public String fbid;
    @SerializedName("date_not_available")
    @Expose
    public String day;
    @SerializedName("start_time")
    @Expose
    public String startTime;
    @SerializedName("end_time")
    @Expose
    public String endTime;
    @SerializedName("timezone")
    @Expose
    public String timezone;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public UserAvailability(String id, String fbid, String day, String startTime, String endTime) {
        this.id = id;
        this.fbid = fbid;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public UserAvailability(){}

    public  Map<String,String> toMap() {
        Map<String,String> map = new HashMap<>();
        map.put("fbid",fbid);
        map.put("date_not_available",day);
        map.put("start_time",startTime);
        map.put("end_time",endTime);
        map.put("timezone",timezone);

        return  map;
    }

    public String getDayString() {
        if(getDay()==null || getDay().equals(""))return null;
       return Api.getContext().getResources().getStringArray(R.array.days)[Integer.parseInt(getDay())];
    }

    public String getTime() {
        SimpleDateFormat sdf1 = new SimpleDateFormat(TextUtils.SDF_3);
        try {
            Calendar starttime = Calendar.getInstance();
            try {
                starttime.setTime(sdf1.parse(getStartTime()));

            }catch (Exception e){
                sdf1 = new SimpleDateFormat(TextUtils.SDF_4);
                starttime.setTime(sdf1.parse(getStartTime()));
            }
            Calendar endTime =  Calendar.getInstance();
            endTime.setTime(sdf1.parse(getEndTime()));
            String time = starttime.get(Calendar.HOUR) +" - "
                    + endTime.get(Calendar.HOUR) + " "
                    + (endTime.get(Calendar.AM_PM)==Calendar.AM? "AM":"PM") + " "
                   // + endTime.getTimeZone().getDisplayName(false, TimeZone.SHORT);
                    + getTimezone();
            return time;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return getStartTime() +" - " + getEndTime() ;
    }
}

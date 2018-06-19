package com.shindygo.shindy.main.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anton Kyrychenko on 002 02.04.18.
 */
public class Respo {
    @SerializedName("status")
    @Expose
    String status;

    public String getStatus() {
        return status;
    }
    public boolean isSuccses(){
        return   status!=null&&status.equals("success");
    }
}

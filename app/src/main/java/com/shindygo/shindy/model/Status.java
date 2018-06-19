package com.shindygo.shindy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("result")
    @Expose
    public String result;

    public String getStatus() {
        return status;
    }
    public boolean isSucsses(){
        return status.equals("success");
    }

    public String getResult() {
        return result;
    }
}

package com.shindygo.shindy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateEventCallBack {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("eventid")
    @Expose
    private String eventid;
    @SerializedName("eventocde")
    @Expose
    private String eventocde;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getEventocde() {
        return eventocde;
    }

    public void setEventocde(String eventocde) {
        this.eventocde = eventocde;
    }

}
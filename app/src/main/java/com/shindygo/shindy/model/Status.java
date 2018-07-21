package com.shindygo.shindy.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("result")
    @Expose
    public String result;
    @SerializedName("invitecode")
    @Expose
    public String invitecode;


    public String getInvitecode() {
        return invitecode;
    }

    public void setInvitecode(String invitecode) {
        this.invitecode = invitecode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }
    public boolean isSuccessful(){
        return status.equals("success");
    }

    public String getResult() {
        return result;
    }


    @Override
    public String toString() {
        try {
            return new GsonBuilder()
                .setLenient()
                .create().toJson(this);

        }catch (JsonSyntaxException e){
            return "{status:"+status+",result:"+result+"}";
        }

    }
}

package com.shindygo.shindy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpenedgeSetupResponse {
    @SerializedName("sealedSetupParameters")
    @Expose
    public String sealedSetupParameters;
    @SerializedName("actionUrl")
    @Expose
    public String actionUrl;
    @SerializedName("errorMessage")
    @Expose
    public String errorMessage;
    @SerializedName("paid")
    @Expose
    public String paid;

    public String getSealedSetupParameters() {
        return sealedSetupParameters;
    }

    public void setSealedSetupParameters(String sealedSetupParameters) {
        this.sealedSetupParameters = sealedSetupParameters;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public OpenedgeSetupResponse(String sealedSetupParameters, String actionUrl) {
        this.sealedSetupParameters = sealedSetupParameters;
        this.actionUrl = actionUrl;
    }

    public OpenedgeSetupResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public OpenedgeSetupResponse() {
    }
}

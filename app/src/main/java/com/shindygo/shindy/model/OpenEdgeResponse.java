package com.shindygo.shindy.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class OpenEdgeResponse {

    @SerializedName("response_code")
    @Expose
    public String responseCode;
    @SerializedName("response_code_text")
    @Expose
    public String responseCodeText;
    @SerializedName("secondary_response_code")
    @Expose
    public String secondaryResponseCode;
    @SerializedName("time_stamp")
    @Expose
    public String timeStamp;
    @SerializedName("retry_recommended")
    @Expose
    public String retryRecommended;
    @SerializedName("order_id")
    @Expose
    public String orderId;
    @SerializedName("authorized_amount")
    @Expose
    public String authorizedAmount;
    @SerializedName("captured_amount")
    @Expose
    public String capturedAmount;
    @SerializedName("credit_card_verification_response")
    @Expose
    public String creditCardVerificationResponse;
    @SerializedName("original_authorized_amount")
    @Expose
    public String originalAuthorizedAmount;
    @SerializedName("requested_amount")
    @Expose
    public String requestedAmount;
    @SerializedName("bank_approval_code")
    @Expose
    public String bankApprovalCode;

    @SerializedName("expire_month")
    @Expose
    public String expireMonth;
    @SerializedName("expire_year")
    @Expose
    public String expireYear;
    @SerializedName("span")
    @Expose
    public String span;
    @SerializedName("card_brand")
    @Expose
    public String cardBrand;
    @SerializedName("batch_id")
    @Expose
    public String batchId;
    @SerializedName("card_type")
    @Expose
    public String cardType;


    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseCodeText() {
        return responseCodeText;
    }

    public void setResponseCodeText(String responseCodeText) {
        this.responseCodeText = responseCodeText;
    }

    public String getSecondaryResponseCode() {
        return secondaryResponseCode;
    }

    public void setSecondaryResponseCode(String secondaryResponseCode) {
        this.secondaryResponseCode = secondaryResponseCode;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getRetryRecommended() {
        return retryRecommended;
    }

    public void setRetryRecommended(String retryRecommended) {
        this.retryRecommended = retryRecommended;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAuthorizedAmount() {
        return authorizedAmount;
    }

    public void setAuthorizedAmount(String authorizedAmount) {
        this.authorizedAmount = authorizedAmount;
    }

    public String getCapturedAmount() {
        return capturedAmount;
    }

    public void setCapturedAmount(String capturedAmount) {
        this.capturedAmount = capturedAmount;
    }

    public String getCreditCardVerificationResponse() {
        return creditCardVerificationResponse;
    }

    public void setCreditCardVerificationResponse(String creditCardVerificationResponse) {
        this.creditCardVerificationResponse = creditCardVerificationResponse;
    }

    public String getOriginalAuthorizedAmount() {
        return originalAuthorizedAmount;
    }

    public void setOriginalAuthorizedAmount(String originalAuthorizedAmount) {
        this.originalAuthorizedAmount = originalAuthorizedAmount;
    }

    public String getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(String requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public String getBankApprovalCode() {
        return bankApprovalCode;
    }

    public void setBankApprovalCode(String bankApprovalCode) {
        this.bankApprovalCode = bankApprovalCode;
    }

    public String getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(String expireMonth) {
        this.expireMonth = expireMonth;
    }

    public String getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(String expireYear) {
        this.expireYear = expireYear;
    }

    public String getSpan() {
        return span;
    }

    public void setSpan(String span) {
        this.span = span;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public static OpenEdgeResponse fromForm(String html) {
        JSONObject jsonObject = new JSONObject();
        html = html.replace("\"","");
        String[] s = html.split("&");
        if(s!=null){
            for (String p : s){
                Log.d("openEdgeSerialize", p);
               String[] np=  p.split("=");
                try {

                    jsonObject.put(np[0],np[1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return OpenEdgeResponse.fromJson(jsonObject);
    }

    private static OpenEdgeResponse fromJson(JSONObject jsonObject) {
        return new Gson().fromJson(jsonObject.toString(),OpenEdgeResponse.class);
    }

    public boolean isRetryRecommended() {
        try {
            return getRetryRecommended().contains("true");

        } catch (NullPointerException e) {
            return false;
        }
    }
}

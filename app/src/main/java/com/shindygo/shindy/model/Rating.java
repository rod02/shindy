package com.shindygo.shindy.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anton Kyrychenko on 004 04.04.18.
 */
public class Rating {





        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("eventid")
        @Expose
        public String eventid;
        @SerializedName("user_fbid")
        @Expose
        public String userFbid;
        @SerializedName("feedback")
        @Expose
        public String feedback;
        @SerializedName("rating")
        @Expose
        public String rating;
        @SerializedName("host_review")
        @Expose
        public String hostReview;
        @SerializedName("rate_date")
        @Expose
        public String rateDate;
        @SerializedName("update_date")
        @Expose
        public String updateDate;
        @SerializedName("fullname")
        @Expose
        public String fullname;


    public String getId() {
        return id;
    }

    public String getEventid() {
        return eventid;
    }

    public String getUserFbid() {
        return userFbid;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getRating() {
        return rating;
    }

    public String getHostReview() {
        return hostReview;
    }

    public String getRateDate() {
        return rateDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getFullname() {
        return fullname;
    }
}
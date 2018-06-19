package com.shindygo.shindy.model;

/**
 * Created by Anton Kyrychenko on 017 17.04.18.
 */


import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reply implements Parcelable
{

    @SerializedName("reply_id")
    @Expose
    public String replyId;
    @SerializedName("reply_comment")
    @Expose
    public String replyComment;
    @SerializedName("reply_date")
    @Expose
    public String replyDate;
    @SerializedName("fbid")
    @Expose
    public String fbid;
    @SerializedName("fullname")
    @Expose
    public String fullname;
    @SerializedName("photo")
    @Expose
    public String photo;
    public final static Parcelable.Creator<Reply> CREATOR = new Creator<Reply>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Reply createFromParcel(Parcel in) {
            return new Reply(in);
        }

        public Reply[] newArray(int size) {
            return (new Reply[size]);
        }

    }
            ;

    protected Reply(Parcel in) {
        this.replyId = ((String) in.readValue((String.class.getClassLoader())));
        this.replyComment = ((String) in.readValue((String.class.getClassLoader())));
        this.replyDate = ((String) in.readValue((String.class.getClassLoader())));
        this.fbid = ((String) in.readValue((String.class.getClassLoader())));
        this.fullname = ((String) in.readValue((String.class.getClassLoader())));
        this.photo = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Reply() {
    }

    public Reply(String replyComment,  String fullname, String photo) {
        this.replyComment = replyComment;
        this.replyDate = "Right Now";
        this.fullname = fullname;
        this.photo = photo;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(replyId);
        dest.writeValue(replyComment);
        dest.writeValue(replyDate);
        dest.writeValue(fbid);
        dest.writeValue(fullname);
        dest.writeValue(photo);
    }

    public int describeContents() {
        return 0;
    }


    public String getReplyId() {
        return replyId;
    }

    public String getReplyComment() {
        return replyComment;
    }

    public String getReplyDate() {
        return replyDate;
    }

    public String getFbid() {
        return fbid;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPhoto() {
        return photo;
    }
}
package com.shindygo.shindy.model;

/**
 * Created by Anton Kyrychenko on 003 03.04.18.
 */


import java.io.Serializable;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Discussion implements Serializable, Parcelable
{

    @SerializedName("discussion_id")
    @Expose
    private String discussionId;
    @SerializedName("eventid")
    @Expose
    private String eventid;
    @SerializedName("user_fbid")
    @Expose
    private String userFbid;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("comment_date")
    @Expose
    private String commentDate;
    @SerializedName("update_date")
    @Expose
    private String updateDate;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("num_likes")
    @Expose
    private String num_likes;
    @SerializedName("like_status")
    @Expose
    private String like_status;
    @SerializedName("reply")
    @Expose
    public List<Reply> reply = null;

    public final static Parcelable.Creator<Discussion> CREATOR = new Creator<Discussion>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Discussion createFromParcel(Parcel in) {
            return new Discussion(in);
        }

        public Discussion[] newArray(int size) {
            return (new Discussion[size]);
        }

    };
    private final static long serialVersionUID = -8343549692591948110L;

    protected Discussion(Parcel in) {
        this.discussionId = ((String) in.readValue((String.class.getClassLoader())));
        this.eventid = ((String) in.readValue((String.class.getClassLoader())));
        this.userFbid = ((String) in.readValue((String.class.getClassLoader())));
        this.comment = ((String) in.readValue((String.class.getClassLoader())));
        this.commentDate = ((String) in.readValue((String.class.getClassLoader())));
        this.updateDate = ((String) in.readValue((String.class.getClassLoader())));
        this.fullname = ((String) in.readValue((String.class.getClassLoader())));
        this.photo = ((String) in.readValue((String.class.getClassLoader())));
        this.num_likes = ((String) in.readValue((String.class.getClassLoader())));
        this.like_status = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.reply, (com.shindygo.shindy.model.Reply.class.getClassLoader()));
    }

    public List<Reply> getReply() {
        return reply;
    }


    public boolean isLiked (){

        return like_status.equals("1");
    }

    public void setLike_status(String like_status) {
        this.like_status = like_status;
    }

    public String getLike_status() {
        return like_status;
    }

    public Discussion() {
    }

    public void setNum_likes(String num_likes) {
        this.num_likes = num_likes;
    }

    public String getNum_likes() {

        return num_likes;
    }

    public String getDiscussionId() {
        return discussionId;
    }

    public void setDiscussionId(String discussionId) {
        this.discussionId = discussionId;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getUserFbid() {
        return userFbid;
    }

    public void setUserFbid(String userFbid) {
        this.userFbid = userFbid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(discussionId);
        dest.writeValue(eventid);
        dest.writeValue(userFbid);
        dest.writeValue(comment);
        dest.writeValue(commentDate);
        dest.writeValue(updateDate);
        dest.writeValue(fullname);
        dest.writeValue(photo);
        dest.writeValue(num_likes);
        dest.writeList(reply);
    }

    public int describeContents() {
        return 0;
    }

}
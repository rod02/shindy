package com.shindygo.shindy.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyInvitesUser implements Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("eventid")
    @Expose
    private String eventid;
    @SerializedName("eventname")
    @Expose
    private String eventname;
    @SerializedName("fbid")
    @Expose
    private String fbid;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("invitedate")
    @Expose
    private String invitedate;
    @SerializedName("invitestatus")
    @Expose
    private String invitestatus;
    @SerializedName("anonymous_invite")
    @Expose
    private String anonymousInvite;
    @SerializedName("offer_to_pay")
    @Expose
    private String offerToPay;
    @SerializedName("attendingstatus")
    @Expose
    private String attendingstatus;
    public final static Parcelable.Creator<MyInvitesUser> CREATOR = new Creator<MyInvitesUser>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MyInvitesUser createFromParcel(Parcel in) {
            return new MyInvitesUser(in);
        }

        public MyInvitesUser[] newArray(int size) {
            return (new MyInvitesUser[size]);
        }

    }
            ;

    protected MyInvitesUser(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.eventid = ((String) in.readValue((String.class.getClassLoader())));
        this.eventname = ((String) in.readValue((String.class.getClassLoader())));
        this.fbid = ((String) in.readValue((String.class.getClassLoader())));
        this.fullname = ((String) in.readValue((String.class.getClassLoader())));
        this.photo = ((String) in.readValue((String.class.getClassLoader())));
        this.invitedate = ((String) in.readValue((String.class.getClassLoader())));
        this.invitestatus = ((String) in.readValue((String.class.getClassLoader())));
        this.anonymousInvite = ((String) in.readValue((String.class.getClassLoader())));
        this.offerToPay = ((String) in.readValue((String.class.getClassLoader())));
        this.attendingstatus = ((String) in.readValue((String.class.getClassLoader())));
    }

    public MyInvitesUser() {
    }

    public String getAttendingstatus() {
        return attendingstatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
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

    public String getInvitedate() {
        return invitedate;
    }

    public void setInvitedate(String invitedate) {
        this.invitedate = invitedate;
    }

    public String getInvitestatus() {
        return invitestatus;
    }

    public void setInvitestatus(String invitestatus) {
        this.invitestatus = invitestatus;
    }

    public String getAnonymousInvite() {
        return anonymousInvite;
    }

    public void setAnonymousInvite(String anonymousInvite) {
        this.anonymousInvite = anonymousInvite;
    }

    public String getOfferToPay() {
        return offerToPay;
    }

    public void setOfferToPay(String offerToPay) {
        this.offerToPay = offerToPay;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(eventid);
        dest.writeValue(eventname);
        dest.writeValue(fbid);
        dest.writeValue(fullname);
        dest.writeValue(photo);
        dest.writeValue(invitedate);
        dest.writeValue(invitestatus);
        dest.writeValue(anonymousInvite);
        dest.writeValue(offerToPay);
        dest.writeValue(attendingstatus);
    }

    public int describeContents() {
        return 0;
    }

}
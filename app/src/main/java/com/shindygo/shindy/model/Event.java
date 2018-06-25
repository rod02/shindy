package com.shindygo.shindy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Event implements Parcelable {



    @SerializedName("invitation_id")
    @Expose
    private String invitationId;

    @SerializedName("eventid")
    @Expose
    private String eventid;
    @SerializedName("eventcode")
    @Expose
    private String eventcode;
    @SerializedName("eventname")
    @Expose
    private String eventname;
    @SerializedName("fulladdress")
    @Expose
    private String fulladdress;
    @SerializedName("long")
    @Expose
    private String _long;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("zipcode")
    @Expose
    private String zipcode;
    @SerializedName("image")
    @Expose
    public List<Image> image = null;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("ticketprice")
    @Expose
    private String ticketprice;
    @SerializedName("user_fbid")
    @Expose
    private String userFbid;
    @SerializedName("representative")
    @Expose
    private String representative;
    @SerializedName("createdate")
    @Expose
    private String createdate;
    @SerializedName("modifydate")
    @Expose
    private String modifydate;
    @SerializedName("expirydate")
    @Expose
    private String expirydate;
    @SerializedName("scheduleid")
    @Expose
    private String scheduleid;
    @SerializedName("sched_startdate")
    @Expose
    private String schedStartdate;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("sched_enddate")
    @Expose
    private String schedEnddate;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("custom_price")

    @Expose
    private String customPrice;
    @SerializedName("spot_available")
    @Expose
    private String spotAvailable;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("host_review")
    @Expose
    private String hostReview;
    @SerializedName("max_male")
    @Expose
    private String max_male;

    @SerializedName("max_female")
    @Expose
    private String max_female;

    @SerializedName("website_url")
    @Expose
    private String website_url;

    @SerializedName("blockcode")
    @Expose
    private String blockCode;

    @SerializedName("likecode")
    @Expose
    private String likecode;
    @SerializedName("like_status")
    @Expose
    private String like_status;

    @SerializedName("createdby")
    @Expose
    private String createdby;
    @SerializedName("private_host")
    @Expose
    private String private_host;
    @SerializedName("private_host_fbid")
    @Expose
    private String privateHostFbId;
    @SerializedName("invitedby")
    @Expose
    private String invitedby;

    @SerializedName("block_status")
    @Expose
    private String block_status;
    @SerializedName("invited_status")
    @Expose
    private String invited_status;
    @SerializedName("attendingstatus")
    @Expose
    private String attendingstatus;
    @SerializedName("offer_to_pay")
    @Expose
    private String offer_to_pay;
    @SerializedName("invited_by_id")
    @Expose
    private String invited_by_id;
    @SerializedName("invitecode")
    @Expose
    private String invitecode;


    @SerializedName("join_male")
    @Expose
    String joinMale;

    @SerializedName("joinfemale")
    @Expose
    String joinFemale;

    @SerializedName("number_joinevent")
    @Expose
    String numberJoinEvent;

    @SerializedName("guest_invite_friend")
    @Expose
    String ableGuestInvite;               //1 and 0

    public String getNumberJoinEvent() {
        return numberJoinEvent;
    }

    public void setNumberJoinEvent(String numberJoinEvent) {
        this.numberJoinEvent = numberJoinEvent;
    }

    public String getAbleGuestInvite() {
        return ableGuestInvite;
    }

    public void setAbleGuestInvite(String ableGuestInvite) {
        this.ableGuestInvite = ableGuestInvite;
    }

    public boolean isAbleGuestInvite(){
        try {
           return ableGuestInvite.equals("1");
        }catch (NullPointerException e){
            return  false;
        }
    }

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    public String getPrivateHostFbId() {
        return privateHostFbId;
    }

    public void setPrivateHostFbId(String privateHostFbId) {
        this.privateHostFbId = privateHostFbId;
    }

    public void setInvited_by_id(String invited_by_id) {
        this.invited_by_id = invited_by_id;
    }

    public void setInvitecode(String invitecode) {
        this.invitecode = invitecode;
    }

    public String getJoinMale() {
        return joinMale;
    }

    public void setJoinMale(String joinMale) {
        this.joinMale = joinMale;
    }

    public String getJoinFemale() {
        return joinFemale;
    }

    public void setJoinFemale(String joinFemale) {
        this.joinFemale = joinFemale;
    }

    public String getInvitecode() {
        return invitecode;
    }

    public String getInvited_by_id() {
        return invited_by_id;
    }

    public String getOffer_to_pay() {
        return offer_to_pay;
    }

    public void setOffer_to_pay(String offer_to_pay) {
        this.offer_to_pay = offer_to_pay;
    }

    private boolean blocked;

    public String getLike_status() {
        return like_status;
    }

    public String getInvited_status() {
        return invited_status;
    }

    public String getAttendingstatus() {
        if (attendingstatus==null)
            attendingstatus="";
        return attendingstatus;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public void setLike_status(String like_status) {
        this.like_status = like_status;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public void setPrivate_host(String private_host) {
        this.private_host = private_host;
    }

    public void setInvitedby(String invitedby) {
        this.invitedby = invitedby;
    }

    public void setInvited_status(String invited_status) {
        this.invited_status = invited_status;
    }

    public void setAttendingstatus(String attendingstatus) {
        this.attendingstatus = attendingstatus;
    }

    public String getPrivate_host() {
        return private_host==null?"":private_host;
    }

    public String getCreatedby() {

        return createdby == null? "" : createdby;
    }

    public String getBlock_status() {
        if (block_status==null)
            block_status="";
        return block_status;
    }

    public void setBlock_status(String block_status) {
        this.block_status = block_status;
    }

    public void setLike_status(boolean liked){
        like_status = liked?"1":"0";
    }
    public boolean isLiked(){
        if (like_status==null||like_status.equals(""))
                like_status="0";
        return like_status.equals("1");
    }
    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getBlockCode() {
        return blockCode;
    }
    public void setLikecode(String likecode) {
        this.likecode = likecode;
    }


    public String getLikecode() {

        return likecode;
    }
        public void set_long(String _long) {
        this._long = _long;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setMax_male(String max_male) {
        this.max_male = max_male;
    }

    public void setMax_female(String max_female) {
        this.max_female = max_female;
    }

    public void setWebsite_url(String website_url) {
        this.website_url = website_url;
    }

    public String getRating() {
        if (rating==null||rating.equals(""))
            rating="0";
        return rating;
    }

    public String getMax_male() {
        return max_male;
    }

    public String getMax_female() {
        return max_female;
    }

    public String getWebsite_url() {
        return website_url;
    }



    public Event() {
        this.invitationId = "";
        this.eventid = "";
        this.eventcode = "";
        this.eventname = "";
        this.fulladdress = "";
        this._long = "";
        this.lat = "";
        this.zipcode = "";

        this.description = "";
        this.notes = "";
        this.ticketprice = "";
        this.userFbid = "";
        this.representative = "";
        this.createdate = "";
        this.modifydate = "";
        this.expirydate = "";
        this.scheduleid ="";
        this.schedStartdate = "";
        this.startTime = "";
        this.schedEnddate = "";
        this.endTime = "";
        this.customPrice = "";
        this.spotAvailable = "";
        this.max_female = "";
        this.max_male = "";
        this.rating = "";
        this.website_url = "";
        this.likecode = "";
        this.ableGuestInvite = "";
        this.joinFemale = "";
        this.joinMale = "";
        this.createdby = "";


    }


    public Map<String ,Object> toMap (){
        Map<String ,Object> map = new HashMap<>();
        map.put("invitation_id",invitationId);

        if (eventid!=null)
        map.put("eventid",eventid);
        map.put("eventname",eventname);
        map.put("fulladdress",fulladdress);
        map.put("long",_long);
        map.put("lat",lat);
        map.put("zipcode",zipcode);
        map.put("description",description);
        map.put("notes",notes);
        map.put("ticketprice",ticketprice);
        map.put("user_fbid",userFbid);
        map.put("representative",representative);
        map.put("createdate",createdate);
        map.put("sched_enddate",schedEnddate);
        map.put("modifydate",modifydate);
        map.put("expirydate",expirydate);
        map.put("sched_startdate",schedStartdate);
        map.put("scheduleid",scheduleid);
        map.put("start_time",startTime);
        map.put("end_time",endTime);
        map.put("customPrice",customPrice);
        map.put("spotAvailable",spotAvailable);
        map.put("fbid",userFbid);
        map.put("max_male",max_male);
        map.put("max_female",max_female);
        map.put("rating",rating);
        map.put("website_url",website_url);
        map.put("likecode",likecode);
        map.put("guest_invite_friend",ableGuestInvite);
     //   map.put("join_male",joinMale);
     //   map.put("joinfemale",joinFemale);
        map.put("createdby",createdby);
        map.put("private_host_fbid",privateHostFbId);
        map.put("private_host",private_host);


        return map;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getEventcode() {
        return eventcode;
    }

    public void setEventcode(String eventcode) {
        this.eventcode = eventcode;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getFulladdress() {
        return fulladdress;
    }

    public void setFulladdress(String fulladdress) {
        this.fulladdress = fulladdress;
    }

    public String getLong() {
        return _long == null? "0" : _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

    public String getLat() {

        return lat == null? "0" : lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getImage() {
        if (image==null||image.size()==0||image.get(0).imagePath==null)
            return "";
        return image.get(0).imagePath;
    }
    public List<Image> getImages() {

        return image;
    }


    public void setImage(List<Image> image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTicketprice() {
        return ticketprice;
    }

    public void setTicketprice(String ticketprice) {
        this.ticketprice = ticketprice;
    }

    public String getUserFbid() {
        return userFbid;
    }

    public void setUserFbid(String userFbid) {
        this.userFbid = userFbid;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getModifydate() {
        return modifydate;
    }

    public void setModifydate(String modifydate) {
        this.modifydate = modifydate;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
    }

    public String getScheduleid() {
        return scheduleid;
    }

    public void setScheduleid(String scheduleid) {
        this.scheduleid = scheduleid;
    }

    public String getSchedStartdate() {
        return schedStartdate;
    }

    public void setSchedStartdate(String schedStartdate) {
        this.schedStartdate = schedStartdate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSchedEnddate() {
        return schedEnddate;
    }

    public void setSchedEnddate(String schedEnddate) {
        this.schedEnddate = schedEnddate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCustomPrice() {
        return customPrice;
    }

    public void setCustomPrice(String customPrice) {
        this.customPrice = customPrice;
    }

    public String getSpotAvailable() {
        return spotAvailable;
    }

    public void setSpotAvailable(String spotAvailable) {
        this.spotAvailable = spotAvailable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getInvitedby() {
        return invitedby==null?"":invitedby;
    }

    public String getHostReview() {
        return hostReview;
    }

    public void setHostReview(String hostReview) {
        this.hostReview = hostReview;
    }

    protected Event(Parcel in) {
        if (image==null||image.size()==0)
            image=new ArrayList<>();
        eventid = in.readString();
        eventcode = in.readString();
        eventname = in.readString();
        fulladdress = in.readString();
        _long = in.readString();
        lat = in.readString();
        zipcode = in.readString();
        in.readList(this.image, (Image.class.getClassLoader()));
        description = in.readString();
        notes = in.readString();
        ticketprice = in.readString();
        userFbid = in.readString();
        representative = in.readString();
        createdate = in.readString();
        modifydate = in.readString();
        expirydate = in.readString();
        scheduleid = in.readString();
        schedStartdate = in.readString();
        startTime = in.readString();
        schedEnddate = in.readString();
        endTime = in.readString();
        customPrice = in.readString();
        spotAvailable = in.readString();
        max_male = in.readString();
        max_female = in.readString();
        rating = in.readString();
        website_url = in.readString();
        likecode=in.readString();
        like_status = in.readString();
        createdby =in.readString();
        invitedby=in.readString();
        private_host=in.readString();
        block_status = in.readString();
        invited_status = in.readString();
        attendingstatus = in.readString();
        offer_to_pay = in.readString();
        invited_by_id = in.readString();
        invitecode = in.readString();
        privateHostFbId = in.readString();
        hostReview = in.readString();
        invitationId = in.readString();
        joinFemale = in.readString();
        joinMale = in.readString();
        numberJoinEvent = in.readString();



    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(eventid);
        parcel.writeString(eventcode);
        parcel.writeString(eventname);
        parcel.writeString(fulladdress);
        parcel.writeString(_long);
        parcel.writeString(lat);
        parcel.writeString(zipcode);
        parcel.writeList(image);
        parcel.writeString(description);
        parcel.writeString(notes);
        parcel.writeString(ticketprice);
        parcel.writeString(userFbid);
        parcel.writeString(representative);
        parcel.writeString(createdate);
        parcel.writeString(modifydate);
        parcel.writeString(expirydate);
        parcel.writeString(scheduleid);
        parcel.writeString(schedStartdate);
        parcel.writeString(startTime);
        parcel.writeString(schedEnddate);
        parcel.writeString(endTime);
        parcel.writeString(customPrice);
        parcel.writeString(spotAvailable);
        parcel.writeString(max_male);
        parcel.writeString(max_female);
        parcel.writeString(rating);
        parcel.writeString(website_url);
        parcel.writeString(likecode);
        parcel.writeString(like_status);
        parcel.writeString(createdby);
        parcel.writeString(invitedby);
        parcel.writeString(private_host);
        parcel.writeString(block_status);
        parcel.writeString(invited_status);
        parcel.writeString(attendingstatus);
        parcel.writeString(offer_to_pay);
        parcel.writeString(invited_by_id);
        parcel.writeString(invitecode);

        parcel.writeString(privateHostFbId);
        parcel.writeString(hostReview);
        parcel.writeString(invitationId);
        parcel.writeString(joinFemale);
        parcel.writeString(joinMale);
        parcel.writeString(numberJoinEvent);
    }


    public String toJSONObject() {
        return new Gson().toJson(this, Event.class);
    }

    public String toJSON() {
        return new Gson().toJson(this, Event.class);
    }

    public boolean isOfferToPay() {
        try {
            return getOffer_to_pay().equals("1");
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }
}
package com.shindygo.shindy.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shindygo.shindy.Api;
import com.shindygo.shindy.R;
import com.shindygo.shindy.utils.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.http.FieldMap;

public class User {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("fbid")
    @Expose
    private String fbid;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("email_address")
    @Expose
    private String emailAddress;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("age_pref")
    @Expose
    private String agePref;
    @SerializedName("religion")
    @Expose
    private String religion;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("gender_pref")
    @Expose
    private String genderPref;
    @SerializedName("show_my_gender_pref")
    @Expose
    private String showMyGenderPref;
    @SerializedName("invite_me_other_share_gender_pref")
    @Expose
    private String inviteMeOtherShareGenderPref;

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("zipcode")
    @Expose
    private String zipcode;
    @SerializedName("availability")
    @Expose
    private String availability;
    @SerializedName("joineddate")
    @Expose
    private String joineddate;
    @SerializedName("updatedate")
    @Expose
    private String updatedate;
    @SerializedName("markasfavorite")
    @Expose
    private String markasfavorite;
    @SerializedName("allow_anonymous_invite")
    @Expose
    private String allowAnonymousInvite;
    private boolean anonymous_invite;

    private boolean offer_to_pay;


    public boolean checked =false;


    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fbid",fbid);
        jsonObject.put("email_address",emailAddress);
        jsonObject.put("gender","");
        jsonObject.put("fullname",fullname);
        return jsonObject;
    }



    public Map<String,Object> toMap(){


        Map <String,Object> jsonObject  = new HashMap<>();
        jsonObject.put("fbid",fbid);
        jsonObject.put("email_address",emailAddress);
        jsonObject.put("gender","");
        jsonObject.put("fullname",fullname);
        jsonObject.put("photo",photo);
        jsonObject.put("about",about);
        jsonObject.put("age",age);
        jsonObject.put("age_pref",agePref);
        jsonObject.put("religion",religion);
        jsonObject.put("gender",gender);
        jsonObject.put("gender_pref",genderPref);
        jsonObject.put("address",address);
        jsonObject.put("distance",distance);
        jsonObject.put("zipcode",zipcode);
        jsonObject.put("availability",availability);
        jsonObject.put("joineddate",joineddate);
        jsonObject.put("updatedate",updatedate);

        return  jsonObject;
    }

    public User(String fbid, String fullname, String emailAddress) {
        this.fbid = fbid;
        this.fullname = fullname;
        this.emailAddress = emailAddress;
        this.photo = "";
        this.about = "";
        this.age = "";
        this.agePref = "";
        this.religion = "";
        this.gender = "";
        this.genderPref = "";
        this.address = "";
        this.distance = "";
        this.zipcode = "";
        this.availability = "";
        this.joineddate = "";
        this.updatedate = "";

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAgePref() {
        return agePref;
    }

    public void setAgePref(String agePref) {
        this.agePref = agePref;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGenderPref() {
        return genderPref;
    }

    public void setGenderPref(String genderPref) {
        this.genderPref = genderPref;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getJoineddate() {
        return joineddate;
    }

    public void setJoineddate(String joineddate) {
        this.joineddate = joineddate;
    }

    public String getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(String updatedate) {
        this.updatedate = updatedate;
    }

    public String getMarkasfavorite() {
        return markasfavorite;
    }

    public boolean isMarkAsFavorite(){
        try {
            return getMarkasfavorite().equals("1");
        }catch (NullPointerException e){
            return false;
        }

    }

    public void setMarkasfavorite(String markasfavorite) {
        this.markasfavorite = markasfavorite;
    }


    public boolean isAnonymous_inviteB() {
        return anonymous_invite;
    }

    public int isAnonymous_invite() {
        return anonymous_invite?1:0;
    }

    public void setAnonymous_invite(boolean anonymous_invite) {
        this.anonymous_invite = anonymous_invite;
    }

    public boolean isOffer_to_payB() {
        return offer_to_pay ;
    }
    public int isOffer_to_pay() {
        return offer_to_pay?1:0 ;
    }

    public void setOffer_to_pay(boolean offer_to_pay) {
        this.offer_to_pay = offer_to_pay;
    }



    /**
     * Getter for the User that is currently logged in to the application.
     * @return The User that is currently logged in to the application.
     */
    public static User getCurrentUser()
    {
        final SharedPreferences sharedPref = Api.getContext().getSharedPreferences("set", Context.MODE_PRIVATE);

        return User.fromSharedPref(sharedPref);
    }
    private static User fromSharedPref(SharedPreferences sharedPref){

        User user = new User(sharedPref.getString("fbid", ""),
                sharedPref.getString("name", ""),
                sharedPref.getString("email", ""));
        user.setAbout(sharedPref.getString("about",""));
        user.setPhoto(sharedPref.getString("photo",""));
        user.setAge(sharedPref.getString("age",""));
        user.setAgePref(String.valueOf(sharedPref.getInt("prefAge",0)));
        user.setDistance(String.valueOf(sharedPref.getInt("spDistance",0)));
        user.setReligion(String.valueOf(sharedPref.getInt("spReligion",0)));
        user.setGenderPref(String.valueOf(sharedPref.getInt("spGender",0)));
        user.setAvailability(String.valueOf(sharedPref.getInt("spAva",0)));
        user.setGender(sharedPref.getString("gender",""));
        user.setAddress(sharedPref.getString("address",""));
        user.setZipcode(sharedPref.getString("zipCode",""));
        user.setJoineddate(sharedPref.getString("joinedDate",""));
        user.setUpdatedate(sharedPref.getString("updateDate",""));
        return user;
    }
    public static String getCurrentUserId() {
        final SharedPreferences sharedPref = Api.getContext().getSharedPreferences("set", Context.MODE_PRIVATE);
        return sharedPref.getString("fbid", "");
    }

    /**
     * Setter for the User that is currently logged in to the application.
     * @param user The user that is currently logged in to the application.
     */
    public static void setCurrentUser(User user) {
        SharedPreferences sharedPref =  Api.getContext().getSharedPreferences("set", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", user.getFullname());
        editor.putString("fbid", user.getFbid());
        editor.putString("photo", user.getPhoto());
        editor.putString("url", user.getPhoto());
        editor.putString("email", user.getEmailAddress());
        editor.putString("about", user.getAbout());
        editor.putString("age", user.getAge());
        editor.putInt("prefAge", TextUtils.parseInt(user.getAgePref()));
        editor.putInt("spDistance", TextUtils.parseInt(user.getDistance()));
        editor.putInt("spReligion", TextUtils.parseInt(user.getReligion()));
        editor.putInt("spGender", TextUtils.parseInt(user.getGenderPref()));
        editor.putInt("spAva", TextUtils.parseInt(user.getAvailability()));
        editor.putString("gender", user.getGender());
        editor.putString("address", user.getAddress());
        editor.putString("zipCode", user.getZipcode());
        editor.putString("joinedDate", user.getJoineddate());
        editor.putString("updateDate", user.getUpdatedate());
        editor.apply();
    }

    public String getReligonAsText() {
        Resources res = Api.getContext().getResources();
        try{
            return res.getStringArray(R.array.religion)[Integer.parseInt(getReligion())];
        }catch (Exception e){
            return "";
        }

    }


    public String getGenderPrefAsText() {
        Resources res = Api.getContext().getResources();
        try{
            return res.getStringArray(R.array.gender_preference)[Integer.parseInt(getGenderPref())];
        }catch (Exception e){
            return "both";
        }

    }

    public String getShowMyGenderPref() {
        return showMyGenderPref;
    }

    public void setShowMyGenderPref(String showMyGenderPref) {
        this.showMyGenderPref = showMyGenderPref;
    }

    public String getInviteMeOtherShareGenderPref() {
        return inviteMeOtherShareGenderPref;
    }

    public void setInviteMeOtherShareGenderPref(String inviteMeOtherShareGenderPref) {
        this.inviteMeOtherShareGenderPref = inviteMeOtherShareGenderPref;
    }

    public String getAllowAnonymousInvite() {
        return allowAnonymousInvite;
    }

    public void setAllowAnonymousInvite(String allowAnonymousInvite) {
        this.allowAnonymousInvite = allowAnonymousInvite;
    }

    public boolean showMyGender(){
        return (getShowMyGenderPref()==null || getShowMyGenderPref().equals("")? false :true );
    }
}
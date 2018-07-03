package com.shindygo.shindy;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.shindygo.shindy.interfaces.ShindiServer;
import com.shindygo.shindy.model.Filter;
import com.shindygo.shindy.model.User;
import com.shindygo.shindy.model.UserAvailability;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anton Kyrychenko on 015 15.03.18.
 */

public class Api {
    ShindiServer shindiServer;
    Retrofit retrofit ;
    private SharedPreferences sharedPref ;
    static Context context;
    private String fbid;
    public Api(@NonNull Context context) {
        this.context = context;


        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Incept("shindy@admin", "orange@123"))
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://shindygo.com/rest_webservices/restapicontroller/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        shindiServer = retrofit.create(ShindiServer.class);
        sharedPref = context.getSharedPreferences("set", Context.MODE_PRIVATE);
        fbid = sharedPref.getString("fbid", "");
    }

    public void getUsers( Callback<List<User>> user){
        Call<List<User>> getUsers = shindiServer.getUsers();
        getUsers.enqueue(user);
    }
    void checkUser(User user,Callback<Object> callback){
        Call<Object> add = shindiServer.checkUser(user.toMap() );
        add.enqueue(callback);
    }
    void updateUser(User user,Callback<Object> callback)
    {
        Call<Object> add = shindiServer.updateUser(user.toMap() );
        add.enqueue(callback);
    }
    void getUserByID(Callback<User> callback)
    {
        final SharedPreferences sharedPref = context.getSharedPreferences("set", Context.MODE_PRIVATE);
        final String fbid = sharedPref.getString("fbid", "");
        Call<User> getUsers = shindiServer.getUsersbyId(fbid);
        getUsers.enqueue(callback);
    }
    public  void searchBlocedUserF(String  text,Filter filter,   Callback<List<User>> callback){

        if (filter!=null) {
            int ageto = filter.getAgeTo();
            int agefrom = filter.getAgeFrom();
            int gender= filter.getGenderPos();
            int genderPref = filter.getGenderPref();
            int religion = filter.getReligionPos();
            int distance = filter.getDistancePos();


            final String fbid = sharedPref.getString("fbid", "");
            Call<List<User>>getUsers = shindiServer.getBlockedUsersFilter(fbid,text,distance==(0)?null:String.valueOf(distance),agefrom==(0)?null:String.valueOf(agefrom),ageto==(0)?null:String.valueOf(ageto),genderPref,gender==0?null:String.valueOf(gender),religion);
            getUsers.enqueue(callback);
        } else {
            getBlockedUsers(text, callback);
        }

    }


    public void search(String text, Filter filter, int filterBy, Callback<ResponseBody> callback) {

        if (filter!=null) {
            int ageto = filter.getAgeTo();
            int agefrom = filter.getAgeFrom();
            int gender= filter.getGenderPos();
            int genderPref = filter.getGenderPref();
            int religion = filter.getReligionPos();
            int distance = filter.getDistancePos();
            String sfilter="";
            switch (filterBy){
                case 0: sfilter =null;
                    break;
                case 1:sfilter = "favorite";
                    break;
                case 2:sfilter = "friend";
                    break;
            }

            shindiServer.search(fbid,text,sfilter,distance==(0)?null:String.valueOf(distance),
                    agefrom==(0)?null:String.valueOf(agefrom),ageto==(0)?null:String.valueOf(ageto),
                    genderPref,gender==0?null:String.valueOf(gender),religion)
                    .enqueue(callback);
        } else {
            searchUser1(text,filterBy,callback);
        }
    }

    public  void searchUser1(String  text,int filterBy,   Callback<ResponseBody> callback){
        String filter="";
        switch (filterBy){
            case 0: filter =null;
                break;
            case 1:filter = "favorite";
                break;
            case 2:filter = "friend";
                break;
        }

        final String fbid = sharedPref.getString("fbid", "");
       shindiServer.searchUser1(fbid,text,filter).enqueue(callback);
    }
    public  void searchUserF(String  text,Filter filter, int filterBy,   Callback<List<User>> callback){

        if (filter!=null) {
            int ageto = filter.getAgeTo();
            int agefrom = filter.getAgeFrom();
            int gender= filter.getGenderPos();
            int genderPref = filter.getGenderPref();
            int religion = filter.getReligionPos();
            int distance = filter.getDistancePos();
            String sfilter="";
            switch (filterBy){
                case 0: sfilter =null;
                    break;
                case 1:sfilter = "favorite";
                    break;
                case 2:sfilter = "friend";
                    break;
            }
            Log.e("API", "fbid "+fbid);
            Call<List<User>>getUsers = shindiServer.searchUserFilter(fbid,text,sfilter,distance==(0)?null:String.valueOf(distance),agefrom==(0)?null:String.valueOf(agefrom),ageto==(0)?null:String.valueOf(ageto),genderPref,gender==0?null:String.valueOf(gender),religion);
            getUsers.enqueue(callback);
        } else {
            Log.e("API", "fbid "+fbid);

            searchUser(text,filterBy,callback);
        }

    }

   public  void searchUser(String  text,int filterBy,   Callback<List<User>> callback){
        String filter="";
       switch (filterBy){
           case 0: filter =null;
           break;
           case 1:filter = "favorite";
           break;
           case 2:filter = "friend";
           break;
       }

        final String fbid = sharedPref.getString("fbid", "");
        Call<List<User>>getUsers = shindiServer.searchUser(fbid,text,filter);
        getUsers.enqueue(callback);
    }
       public void blockUser(String myId,String toBlockId,Callback<Object> callback)
       {
           Map<String,String> map = new HashMap<>();
           map.put("user_fbid",myId);
           map.put("friend_fbid",toBlockId);
           Call<Object> add = shindiServer.blockUser(map);
           add.enqueue(callback);
       }
    public void blockUser2(String toBlockId,Callback<Object> callback)
    {
        Map<String,String> map = new HashMap<>();
        map.put("user_fbid",fbid);
        map.put("friend_fbid",toBlockId);
        Call<Object> add = shindiServer.blockUser(map);
        add.enqueue(callback);
    }


    public void unblockUser(String myId,String toBlockId,Callback<Object> callback)
    {
        Map<String,String> map = new HashMap<>();
        map.put("user_fbid",fbid);
        map.put("friend_fbid",toBlockId);
        Call<Object> add = shindiServer.unblockUser(map);
        add.enqueue(callback);
    }
       public void getBlockedUsers( String name,  Callback<List<User>> user) {

           final String fbid = sharedPref.getString("fbid", "");
           Call<List<User>> getUsers = shindiServer.getBlockedUsers(fbid,name);
           getUsers.enqueue(user);
       }
    public void addFavoriteUser(String myId,String toBlockId,Callback<Object> callback)
    {
        Map<String,String> map = new HashMap<>();
        map.put("user_fbid",myId);
        map.put("friend_fbid",toBlockId);
        Call<Object> add = shindiServer.addfavoriteUser(map);
        add.enqueue(callback);
    }
    public void RemoveFavoriteUser(String myId,String toBlockId,Callback<Object> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("user_fbid", myId);
        map.put("friend_fbid", toBlockId);
        Call<Object> add = shindiServer.unfavoriteUser(map);
        add.enqueue(callback);
    }


    public void fetchNewUsers(String myId, Callback<List<User>> callback) {
        Call<List<User>> fetchNewUsers = shindiServer.fetchNewUsers(myId);
        fetchNewUsers.enqueue(callback);
    }


    public void likeUserToGroup(String myId, String friendFbId, Callback<JSONObject> callback) {
        Call<JSONObject> likeUserToGroup = shindiServer.likeUserToGroup(myId, friendFbId);
        likeUserToGroup.enqueue(callback);
    }


    public void notAvailableTime(UserAvailability availability, Callback<ResponseBody> callback) {
        Call<ResponseBody> notAvailableTime = shindiServer.notAvailableTime(availability.toMap());
        notAvailableTime.enqueue(callback);
    }
    public void deleteUserAvailableTime(String fbid, String id, Callback<ResponseBody> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("fbid", fbid);
        map.put("id", id);
        Call<ResponseBody> notAvailableTime = shindiServer.deleteUserAvailableTime(map);
        notAvailableTime.enqueue(callback);
    }

    public void fetchUserNotAvailableTime(String fbid, Callback<List<UserAvailability>> callback) {
        shindiServer.fetchUserNotAvailableTime(fbid).enqueue(callback);
    }


    public static void initialized(Context applicationContext) {
        if(isInitialized()){
            return;
        }
        instance = new Api(applicationContext);
    }

    private static boolean isInitialized() {
        return instance!=null;
    }

    private static Api instance;

    public static Api getInstance() {
        if(instance!=null)
            return instance;
        return new Api(context);
    }

    public static Context getContext() {
        return context;
    }

}

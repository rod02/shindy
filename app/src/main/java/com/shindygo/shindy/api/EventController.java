package com.shindygo.shindy.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.shindygo.shindy.Incept;
import com.shindygo.shindy.interfaces.ShindiServer;
import com.shindygo.shindy.main.model.Respo;
import com.shindygo.shindy.model.CreateEventCallBack;
import com.shindygo.shindy.model.Discussion;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.InviteEvent;
import com.shindygo.shindy.model.MyInvites;
import com.shindygo.shindy.model.OpenedgeSetupResponse;
import com.shindygo.shindy.model.Rating;
import com.shindygo.shindy.model.Reply;
import com.shindygo.shindy.model.Status;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anton Kyrychenko on 028 28.03.18.
 */

public class EventController {
    ShindiServer shindiServer;
    Retrofit retrofit ;
    private SharedPreferences sharedPref ;
    Context context;
    private String fbid;

    public EventController(Context context) {
        this.context = context;
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Incept("shindy@admin", "orange@123"))
                .build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://shindygo.com/rest_webservices/eventapicontroller/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
        shindiServer = retrofit.create(ShindiServer.class);
        sharedPref = context.getSharedPreferences("set", Context.MODE_PRIVATE);
        fbid = sharedPref.getString("fbid", "");
    }
    public void getEvents(Callback<List<Event>> callback){

            Call<List<Event>> call = shindiServer.getEventList();
            call.enqueue(callback);
    }
    public void unblockEvent(String eventId,String blockCode,Callback<Respo> callback){
        if (eventId!=null)
        shindiServer.unblockEvent(fbid,eventId,blockCode).enqueue(callback);
        else
            Toast.makeText(context,"impossible to delete: null ID",Toast.LENGTH_SHORT).show();

    }
    public void blockEvent(String toBlockId,Callback<Object> callback)
    {
        Map<String,String> map = new HashMap<>();
        map.put("user_fbid",fbid);
        map.put("eventid",toBlockId);
        Call<Object> add = shindiServer.blockEvent(map);
        add.enqueue(callback);
    }
    public void getBlockedEvents(Callback<List<Event>> event) {
        Call<List<Event>> getEvents = shindiServer.getBlockedEvents(fbid);
        getEvents.enqueue(event);
    }
    public void likeEvent(String toLikeId,Callback<Object> callback)
    {
        Map<String,String> map = new HashMap<>();
        map.put("user_fbid",fbid);
        map.put("eventid",toLikeId);
        Call<Object> add = shindiServer.likeEvent(map);
        add.enqueue(callback);
    }
    public void getLikedEvents(Callback<List<Event>> event) {
        Call<List<Event>> getEvents = shindiServer.getLikedEvents(fbid);
        getEvents.enqueue(event);
    }

    public void getDiscussEvents(String eventId,Callback<List<Discussion>> event) {
        Call<List<Discussion>> getEvents = shindiServer.getDiscussEvents(eventId,fbid);
        getEvents.enqueue(event);
    }
    public void createEvent(Event event,Callback<CreateEventCallBack> callback){

        event.setUserFbid(fbid);
        shindiServer.createEvent(event.toMap()).enqueue(callback);

    }


    public void unlikeEvent(String toLikeId,String likeID,Callback<Object> callback)
    {
        Map<String,String> map = new HashMap<>();
        map.put("user_fbid",fbid);
        map.put("eventid",toLikeId);
        map.put("likecode",likeID);
        Call<Object> add = shindiServer.unlikeEvent(map);
        add.enqueue(callback);
    }
    public void rateEvent(String toLikeId,String rate,String host,String feedback,Callback<Object> callback)
    {
        Map<String,String> map = new HashMap<>();
        map.put("user_fbid",fbid);
        map.put("eventid",toLikeId);
        map.put("rate",rate);
        map.put("host_review",host);
        map.put("feedback",feedback);
        Call<Object> add = shindiServer.rateEvent(map);
        add.enqueue(callback);
    }
    public void getEventRateList(String id ,Callback<List<Rating>> event) {
        Call<List<Rating>> getEvents = shindiServer.getEventRate(id);
        getEvents.enqueue(event);
    }
    public void addDiscussElement(String toLikeId,String comment,Callback<Object> callback)
    {
        Map<String,String> map = new HashMap<>();
        map.put("user_fbid",fbid);
        map.put("eventid",toLikeId);
        map.put("comment",comment);
        Call<Object> add = shindiServer.addDiscussElement(map);
        add.enqueue(callback);
    }
    public void likeDiscussElement(String toLikeId,Callback<Object> callback)
    {
        Map<String,String> map = new HashMap<>();
        map.put("commentid",toLikeId);
        map.put("user_fbid",fbid);
        Call<Object> add = shindiServer.likeDiscussElement(map);
        add.enqueue(callback);
    }
    public void unlikeDiscussElement(String toLikeId,Callback<Object> callback)
    {
        Map<String,String> map = new HashMap<>();
        map.put("commentid",toLikeId);
        map.put("user_fbid",fbid);
        Call<Object> add = shindiServer.dislikeDiscussElement(map);
        add.enqueue(callback);
    }

    public void sendinvite(InviteEvent inviteEvent,Callback<Status> responseCallback) {
        inviteEvent.setInvite_by(fbid);
        Call<Status> invite = shindiServer.sendInvite(inviteEvent.toMap());
        invite.enqueue(responseCallback);
    }

    public void getWhoIsInvited_Event(String toLikeId,Callback<MyInvites> callback)
    {
        Map<String,String> map = new HashMap<>();
        map.put("user_fbid",fbid);
        map.put("eventid",toLikeId);
        Call<MyInvites> add = shindiServer.getWhoIsInvited_Event(map);
        add.enqueue(callback);

    }
    public void getInvitedEvent(Callback<List<Event>> callback)
    {
        Map<String,String> map = new HashMap<>();
        map.put("user_fbid",fbid);
        Call<List<Event>> add = shindiServer.InvitedEvent(map);
        add.enqueue(callback);

    }
    public void getAttendingEvent(Callback<List<Event>> callback)
    {
        Map<String,String> map = new HashMap<>();
        map.put("user_fbid",fbid);
        Call<List<Event>> add = shindiServer.attendingEvent(map);
        add.enqueue(callback);

    }
    public void EnterInviteCode(String eventCode,Callback<Status> callback)
    {
        Map<String,String> map = new HashMap<>();
        map.put("attendee_id",fbid);
        map.put("invitecode",eventCode);
        Call<Status> add = shindiServer.InviteCode(map);
        add.enqueue(callback);

    }
    public void pushImage(String eventid,String image,Callback<Object> callback)
    {
        Map<String,String> map = new HashMap<>();
        map.put("eventid",eventid);
        map.put("image[]",image);
        Call<Object> add = shindiServer.pushImage(map);
        add.enqueue(callback);

    }

    public void joinIamInEvent(String eventid,String invitecode,Callback<Status> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("eventid", eventid);
        map.put("attendee_id", fbid);
        map.put("invitecode", invitecode);
        Call<Status> add = shindiServer.joinIamInEvent(map);
        add.enqueue(callback);

    }


    public void getEventListCreatedByUser(Callback<List<Event>> callback){

        shindiServer.getEventCreatedByUser(fbid).enqueue(callback);
    }
    public void postReply(String eventid,String disscId,String reply,Callback<Status> callback){
        shindiServer.replyEvent(eventid,fbid,disscId,reply).enqueue(callback);
    }

    public  void getReplyList (String discusId, Callback<List<Reply>> callback){
        shindiServer.getReplyList(fbid,discusId).enqueue(callback);
    }
    public void leaveEvent(String eventid,Callback<Status> callback){
        shindiServer.leaveEvent(eventid,fbid).enqueue(callback);
    }
    public void get_attending_event(Callback<List<Event>> callback){
        shindiServer.get_attending_event(fbid).enqueue(callback);
    }
    public void getEventsReview(Callback<List<Event>> callback){
        shindiServer.getEventReview(fbid).enqueue(callback);
    }
    public void inviteByEmail(String eventid,String user_email,String note,Callback<Status> callback){
        shindiServer.sendInviteByEmail(eventid,user_email,fbid,note).enqueue(callback);
    }
    public void cancelInvite(String eventid,String id,Callback<Status> callback){
        shindiServer.cancelInvite(id,eventid,fbid).enqueue(callback);
    }

    public void fetchCreatedEvents(String fbId, Callback<List<Event>> callback) {
        shindiServer.getEventCreatedByUser(fbId).enqueue(callback);
    }
    public void fetchEvents(String fbId, Callback<List<Event>> callback) {
        shindiServer.fetchEvents(fbId).enqueue(callback);
    }

    public void updateEvent(Event event, Callback<JSONObject> callback) {

        Call<JSONObject>updateEvent = shindiServer.updateEvent(event.toMap());
        updateEvent.enqueue(callback);
    }


    public void checkout(String fbid, String eventid, String chargeTotal, Callback<OpenedgeSetupResponse> callback){
        Map<String,String> map = new HashMap<>();
        map.put("fbid",fbid);
        map.put("charge_total",chargeTotal);
        map.put("eventid",eventid);
        shindiServer.checkoutEvent(map).enqueue(callback);

    }
}

package com.shindygo.shindy.interfaces;

import android.support.annotation.Nullable;

import com.shindygo.shindy.main.model.Respo;
import com.shindygo.shindy.model.CreateEventCallBack;
import com.shindygo.shindy.model.Discussion;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.MyInvites;
import com.shindygo.shindy.model.OpenedgeSetupResponse;
import com.shindygo.shindy.model.Rating;
import com.shindygo.shindy.model.Reply;
import com.shindygo.shindy.model.Status;
import com.shindygo.shindy.model.User;
import com.shindygo.shindy.model.UserAvailability;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Anton Kyrychenko on 015 15.03.18.
 */

public interface ShindiServer {
    @Headers({
            "API-key: shindykey456"
    })
    @GET("userlist/api-key/shindykey456 ")
    Call<List<User>> getUsers();
    @Headers({
            "API-key: shindykey456"
    })
    @GET("userlist/fbid/{id}/api-key/shindykey456 ")
    Call<User> getUsersbyId(@Path("id") String id);

    @Headers({
            "API-key: shindykey456",
            "Authorization: Basic c2hpbmR5QGFkbWluOm9yYW5nZUAxMjM="
    })
    @FormUrlEncoded
    @POST("check_user")
    Call<Object> checkUser(@FieldMap Map<String ,Object> user);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("update_user")
    Call<Object> updateUser(@FieldMap Map<String ,Object> user);


    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("blokeduser ")
    Call<Object> blockUser(@FieldMap Map<String ,String> user);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("userblockedlist")
    Call<List<User>> getBlockedUsersFilter(@Field("user_fbid") String id,@Field("fullname") String text,

                                     @Nullable @Field("")String  distance,
                                     @Nullable @Field("min_age")String  ageto,
                                     @Nullable @Field("max_age")String  agefrom,
                                     @Nullable @Field("gender_pref")int  genderPref,
                                     @Nullable @Field("gender")String  gender,
                                     @Nullable @Field("religion")int  religion);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("userblockedlist")
    Call<List<User>> getBlockedUsers(@Field("user_fbid") String id ,@Field("fullname") String name);



    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("SearchAlluserinshindy")
    Call<ResponseBody> searchUser1(@Field("user_fbid") String fbID,@Field("fullname") String text,@Nullable @Field("filterby")String filter);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("SearchAlluserinshindy")
    Call<List<User>> searchUser(@Field("user_fbid") String fbID,@Field("fullname") String text,@Nullable @Field("filterby")String filter);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("SearchAlluserinshindy")
    Call<List<User>> searchUserFilter(@Field("user_fbid") String fbID,@Field("fullname") String text,
                                      @Nullable @Field("filterby")String filter,
                                      @Nullable @Field("")String  distance,
                                      @Nullable @Field("min_age")String  ageto,
                                      @Nullable @Field("max_age")String  agefrom,
                                      @Nullable @Field("gender_pref")int  genderPref,
                                      @Nullable @Field("gender")String  gender,
                                      @Nullable @Field("religion")int  religion);
    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("SearchAlluserinshindy")
    Call<ResponseBody> search(@Field("user_fbid") String fbID,@Field("fullname") String text,
                                      @Nullable @Field("filterby")String filter,
                                      @Nullable @Field("")String  distance,
                                      @Nullable @Field("min_age")String  ageto,
                                      @Nullable @Field("max_age")String  agefrom,
                                      @Nullable @Field("gender_pref")int  genderPref,
                                      @Nullable @Field("gender")String  gender,
                                      @Nullable @Field("religion")int  religion);




    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("unblocked")
    Call<Object> unblockUser(@FieldMap Map<String ,String> user);




    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("addfavoriteuser")
    Call<Object> addfavoriteUser(@FieldMap Map<String ,String> user);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("unfavoriteuser")
    Call<Object> unfavoriteUser(@FieldMap Map<String ,String> user);


    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("notavailbletime")
    Call<ResponseBody> notAvailableTime(@FieldMap Map<String, String> map);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("delete_time_user")
    Call<ResponseBody> deleteUserAvailableTime(@FieldMap Map<String, String> map);

    @Headers({"API-key: shindykey456"})
    @GET("notavailable_timelist/fbid/{id}/api-key/shindykey456 ")
    Call<List<UserAvailability>> fetchUserNotAvailableTime(@Path("id") String fbid);


    @Headers({"API-key: shindykey456"})
    @GET("eventlist")
    Call<List<Event>> getEventList();



    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("block_event")
    Call<Object> blockEvent(@FieldMap Map<String ,String> user);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("unblock_event")
    Call<Respo> unblockEvent(@Field("user_fbid") String user, @Field("eventid") String eventId, @Field("blockcode") String blockCode);



    @Headers({"API-key: shindykey456"})
    @GET("eventblockedlist")
    Call<List<Event>> getBlockedEvents(@Query("user_fbid") String id );

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("like_event")
    Call<Object> likeEvent(@FieldMap Map<String ,String> user);
    @Headers({"API-key: shindykey456"})
    @GET("eventlikelist")
    Call<List<Event>> getLikedEvents(@Query("user_fbid") String id );

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("create_event")
    Call<CreateEventCallBack> createEvent(@FieldMap Map<String ,Object> event);


    @Headers({"API-key: shindykey456"})
    @GET("geteventdiscussion")
    Call<List<Discussion>> getDiscussEvents(@Query("eventid") String id,@Query("user_fbid") String fbid );



    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("unlike_event")
    Call<Object> unlikeEvent(@FieldMap Map<String ,String> user);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("rate_event")
    Call<Object> rateEvent(@FieldMap Map<String ,String> user);

    @Headers({"API-key: shindykey456"})
    @GET("eventratelist")
    Call<Object> getEventRateList(@Query("eventid") String id );

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("addeventdiscussion")
    Call<Object> addDiscussElement(@FieldMap Map<String ,String> user);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("like_discussion")
    Call<Object> likeDiscussElement(@FieldMap Map<String ,String> user);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("unlike_discussion")
    Call<Object> dislikeDiscussElement(@FieldMap Map<String ,String> user);

    @Headers({"API-key: shindykey456"})
    @GET("eventratelist")
    Call<List<Rating>> getEventRate(@Query("eventid") String id );

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("whoisinvited_event")
    Call<MyInvites> getWhoIsInvited_Event(@FieldMap Map<String ,String> user);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("sendinvite")
    Call<ResponseBody> sendInvite(@FieldMap Map<String ,Object> user);


    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("attending_event")
    Call<List<Event>> attendingEvent(@FieldMap Map<String ,String> user);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("invited_event")
    Call<List<Event>> InvitedEvent(@FieldMap Map<String ,String> user);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("enterEventcode")
    Call<Status> InviteCode(@FieldMap Map<String ,String> user);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("uploadeventimage")
    Call<Object> pushImage(@FieldMap Map<String ,String> user);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("acceptinvitation")
    Call<Status> joinIamInEvent(@FieldMap Map<String ,String> user);

    @Headers({"API-key: shindykey456"})
    @GET("eventlistcreatedbyuser")
    Call<List<Event>> getEventCreatedByUser(@Query("user_fbid") String id );


    @Headers({"API-key: shindykey456"})
    @GET("replydiscussionlist")
    Call<List<Reply>> getReplyList(@Query("user_fbid") String id,@Query("discussion_id") String discId);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("replyeventdiscussion")
    Call<Status> replyEvent(@Field("eventid")String eventId,@Field("user_fbid")String fbId,@Field("discussion_id")String disccus,@Field("reply_comment")String reply);


    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("leave_event")
    Call<Status> leaveEvent(@Field("eventid")String eventId,@Field("attendee_id")String fbId);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("attending_event")
    Call<List<Event>> get_attending_event(@Field("user_fbid") String id );


    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("sendinviteby_email")
    Call<Status> sendInviteByEmail(@Field("eventid")String eventId,@Field("user_email")String user_email,@Field("invited_by")String invite_by,@Field("note")String note);

    @Headers({"API-key: shindykey456"})
    @FormUrlEncoded
    @POST("cancelinvitation")
    Call<Status> cancelInvite(@Field("attendee_id")String fbId,@Field("eventid")String eventId,@Field("invite_by")String invite_by);
    @Headers({"API-key: shindykey456"})
    @GET("eventreviewlist")
    Call<List<Event>> getEventReview(@Query("user_fbid") String id );



    @Headers({
            "API-key: shindykey456",
            "Authorization: Basic c2hpbmR5QGFkbWluOm9yYW5nZUAxMjM="
    })
    @FormUrlEncoded
    @POST("newuserlist")
    Call<List<User>> fetchNewUsers(@Field("user_fbid") String myId);


    @Headers({
            "API-key: shindykey456",
            "Authorization: Basic c2hpbmR5QGFkbWluOm9yYW5nZUAxMjM="
    })
    @FormUrlEncoded
    @POST("adduseras_liketogroup")
    Call<JSONObject> likeUserToGroup(@Field("user_fbid") String myId, @Field("friend_fbid") String friend_fbId );




    @Headers({
            "API-key: shindykey456",
            "Authorization: Basic c2hpbmR5QGFkbWluOm9yYW5nZUAxMjM="
    })
    @FormUrlEncoded
    @POST("update_event")
    Call<JSONObject> updateEvent(@FieldMap Map<String, Object> event);



    @FormUrlEncoded
    @POST("paypage/")
    Call<ResponseBody> paypage(@Field("sealedSetupParameters") String sealedSetupParameters);


    @FormUrlEncoded
    @POST("transactions")
    Call<ResponseBody> setupRequest(@FieldMap Map<String, String> request);


}

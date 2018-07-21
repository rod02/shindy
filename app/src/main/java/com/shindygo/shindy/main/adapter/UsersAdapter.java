package com.shindygo.shindy.main.adapter;

/**
 * Created by User on 16.03.2018.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shindygo.shindy.Api;
import com.shindygo.shindy.R;
import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.interfaces.ClickCard;
import com.shindygo.shindy.interfaces.ClickEventCard;
import com.shindygo.shindy.interfaces.ClickUser;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.InviteEvent;
import com.shindygo.shindy.model.Status;
import com.shindygo.shindy.model.User;
import com.shindygo.shindy.utils.GlideImage;
import com.shindygo.shindy.utils.MySharedPref;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

/**
 * Created by Sergey on 24.10.2017.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersHolder> {
    Context context;
    List<User> users = new ArrayList<>();
    ClickUser clickUser;
    ClickCard clickCard;
    String id;
    List<Event> eventList;


    public UsersAdapter(List<User> friendList, List<Event> eventList, ClickUser clickUser, ClickCard clickCard) {
        this.users = friendList;
        this.clickUser = clickUser;
        this.clickCard = clickCard;
        this.eventList = eventList;
    }

    @Override
    public UsersHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, null);
        final SharedPreferences sharedPref = context.getSharedPreferences("set", Context.MODE_PRIVATE);
        id = sharedPref.getString("fbid", "");


        return new UsersHolder(view);
    }

    @Override
    public void onBindViewHolder(final UsersHolder holder, final int position) {
        holder.bindModel(users.get(position), clickUser, clickCard);


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UsersHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView avatar, arrow;
        LinearLayout linearLayout, bar, profile, favorite, message;
        RecyclerView recyclerView;
        Api api = new Api(context);
        ImageView star, favoriteStar;
        TextView textView;
        private String choosenEvent;

        @BindView(R.id.pay)
        ImageView pay;
        @BindView(R.id.anonim)
        ImageView anonim;
        @BindView(R.id.ll_bam)
        LinearLayout layBam;
        private int isAnon=0;
        private int isPay=0;

        public UsersHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
            arrow = v.findViewById(R.id.iv_arrow);
            textView = v.findViewById(R.id.tv_Bam);
            name = v.findViewById(R.id.tv_name_age);
            avatar = v.findViewById(R.id.iv_avatar);
            linearLayout = v.findViewById(R.id.ll_main);
            bar = v.findViewById(R.id.main);
            profile = v.findViewById(R.id.ll_profile);
            recyclerView = v.findViewById(R.id.rv_event_user);
            favorite = v.findViewById(R.id.ll_favorite);
            star = v.findViewById(R.id.iv_star);
            favoriteStar = v.findViewById(R.id.star_favorite);
            message = v.findViewById(R.id.ll_message);
        }

        public void bindModel(final User user, final ClickUser clickUser, final ClickCard clickCard) {
            name.setText(user.getFullname() + ", " + user.getAge());
           // Glide.with(context).load(user.getPhoto()).into(avatar);
            GlideImage.load(context, user.getPhoto(), avatar);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bar.setVisibility(bar.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
            });

            pay.setColorFilter(ContextCompat.getColor(context, R.color.darker_gray));
            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(isPay==0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View payforin = LayoutInflater.from(context)
                                .inflate(R.layout.alert_pay_for_invitee, null, false);
                        builder.setView(payforin);
                        Button agree = payforin.findViewById(R.id.btn_ok);
                        Button close = payforin.findViewById(R.id.close);
                        Button cancel = payforin.findViewById(R.id.btn_cancel);

                        final AlertDialog alert = builder.show();
                        agree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isPay==0){
                                    isPay=1;
                                    pay.setColorFilter(ContextCompat.getColor(context, R.color.green_online));
                                }
                                else {
                                    isPay=0;
                                    pay.setColorFilter(ContextCompat.getColor(context, R.color.darker_gray));
                                }
                                alert.dismiss();
                            }
                        });
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.cancel();
                            }
                        });
                        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


                    } else {
                        isPay=0;
                        pay.setColorFilter(ContextCompat.getColor(context, R.color.darker_gray));
                    }


                }


            });
            anonim.setColorFilter(ContextCompat.getColor(context, R.color.darker_gray));
            anonim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                               /* if (isAnon==1){
                                    isAnon=0;
                                    anonim.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                                }
                                else {
                                    isAnon=1;
                                    anonim.setColorFilter(ContextCompat.getColor(getContext(), R.color.darker_gray));
                                }

*/
                    if(MySharedPref.showInviteAnonymousAlert() && isAnon==0 ){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View payforin = LayoutInflater.from(context)
                                .inflate(R.layout.alert_mark_anonymous, null, false);
                        builder.setView(payforin);
                        Button btnOk = payforin.findViewById(R.id.btn_ok);
                        final CheckBox checkBox = payforin.findViewById(R.id.checkbox);
                        Button cancel = payforin.findViewById(R.id.close);

                        final AlertDialog alert = builder.show();
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MySharedPref.setInviteAnonymous(!checkBox.isChecked());
                                if (isAnon==0){
                                    isAnon=1;
                                    anonim.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
                                }
                                else {
                                    isAnon=0;
                                    anonim.setColorFilter(ContextCompat.getColor(context, R.color.darker_gray));
                                }
                                alert.dismiss();
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                            }
                        });

                        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    }else {
                        if (isAnon==0){
                            isAnon=1;
                            anonim.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
                        }
                        else {
                            isAnon=0;
                            anonim.setColorFilter(ContextCompat.getColor(context, R.color.darker_gray));
                        }

                    }

                }
            });
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*textView.setVisibility(View.VISIBLE);
                    EventController eventController = new EventController(context);
                    eventController.sendinvite(new InviteEvent(choosenEvent, user.getFbid(), isAnon, isPay), new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            if (response != null) {
                                Log.v("sendInvite", response.body().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textView.setVisibility(View.GONE);

                        }
                    }, 2000);
*/

                    EventUserAdapter adapter = (EventUserAdapter) recyclerView.getAdapter();
                    EventController eventController = new EventController(context);
                    final List<Event> events = adapter.getSelectedItems();
                    for (int i = 0; i < events.size(); i++) {
                        final int finalI = i;
                        eventController.sendinvite(new InviteEvent(events.get(i).getEventid(), user.getFbid(), isAnon, isPay), new Callback<Status>() {
                            @Override
                            public void onResponse(Call<Status> call, Response<Status> response) {
                                try {
                                    if (response != null) {
                                        Log.v("sendInvite", response.toString());
                                        Status status = response.body();
                                        Log.v("sendInvite", status.result);
                                        Log.v("sendInvite", status.status);
                                        Log.v("sendInvite", status.toString());

                                        layBam.setVisibility(View.VISIBLE);

                                    }

                                    if(finalI == events.size()-1){
                                        if(response.message().equalsIgnoreCase("ok")){
                                            // mPopupWindow.dismiss();
                                            //Snackbar.make(getView(), R.string.invite_sent_bam, Snackbar.LENGTH_LONG).show();


                                        }
                                    }

                                }catch (NullPointerException e){

                                }


                            }

                            @Override
                            public void onFailure(Call<Status> call, Throwable t) {
                                t.printStackTrace();
                                try {
                                    layBam.setVisibility(View.VISIBLE);

                                    // bar.setVisibility(View.GONE);
                                }catch (NullPointerException e){
                                }
                            }
                        });


                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                layBam.setVisibility(GONE);
                                bar.setVisibility(View.GONE);
                            }catch (NullPointerException e){

                            }

                        }
                    }, 2000);
                }
            });
            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        clickCard.Click(true);

                    }catch (NullPointerException e){

                    }
                }
            });
            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        clickUser.Click(user);

                    }catch (NullPointerException e){

                    }
                }
            });
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (user.getMarkasfavorite().equals("0")) {
                        api.addFavoriteUser(id, user.getFbid(), new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                Toast.makeText(context, "User favorite!", Toast.LENGTH_SHORT).show();
                                star.setColorFilter(ContextCompat.getColor(context, R.color.navigation_notification_yellow));
                                favoriteStar.setColorFilter(ContextCompat.getColor(context, R.color.navigation_notification_yellow));
                                user.setMarkasfavorite("1");
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        star.setColorFilter(null);
                        favoriteStar.setColorFilter(null);
                        api.RemoveFavoriteUser(id, user.getFbid(), new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                Toast.makeText(context, "User remove from favorite!", Toast.LENGTH_SHORT).show();
                                user.setMarkasfavorite("0");
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(context, 0, false));
            recyclerView.setAdapter(new EventUserAdapter(new ClickEventCard() {
                @Override
                public void Click(boolean b, String eventId) {
                    choosenEvent = eventId;
                    arrow.setVisibility(View.VISIBLE);
                }
            }, eventList));
            if (user.getMarkasfavorite().equals("1")) {
                star.setColorFilter(ContextCompat.getColor(context, R.color.navigation_notification_yellow));
                favoriteStar.setColorFilter(ContextCompat.getColor(context, R.color.navigation_notification_yellow));
            } else {
                star.setColorFilter(null);
                favoriteStar.setColorFilter(null);
            }
        }
    }

}

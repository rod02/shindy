package com.shindygo.shindy.main.adapter;

/**
 * Created by User on 16.03.2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shindygo.shindy.R;
import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.interfaces.Click;
import com.shindygo.shindy.interfaces.ClickCard;
import com.shindygo.shindy.interfaces.ClickEvent;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.utils.GlideImage;
import com.shindygo.shindy.utils.TextUtils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sergey on 24.10.2017.
 */

public class ReviewEventAdapter extends RecyclerView.Adapter<ReviewEventAdapter.EventHolder> {
    private static final String TAG = "ReviewEventAdapter";
    Context context;
    List<Event> list;
    String id;
    ClickEvent clickEvent;


    public ReviewEventAdapter(List<Event> list, ClickEvent clickEvent) {
        if (list != null)
            this.list = list;
        else this.list = new ArrayList<>();
        this.clickEvent = clickEvent;
    }

    //    public ChooseEventAdapter(List<User> friendList, ClickUser clickEvent ) {
//        this.list = friendList;
//        this.clickEvent = clickEvent;
//    }
    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        final SharedPreferences sharedPref = context.getSharedPreferences("set", Context.MODE_PRIVATE);
        id = sharedPref.getString("fbid", "");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_event, null);

        return new EventHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventHolder holder, final int position) {
        holder.bindModel(list.get(position), clickEvent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {

/*
        @BindView(R.id.cv_main)
        CardView cvMain;*/
        @BindView(R.id.iv_avatar)
        RoundedImageView ivAvatar;
        @BindView(R.id.tv_exprires)
        TextView tvExprires;
        @BindView(R.id.tv_eventName)
        TextView tvEventName;

        @BindView(R.id.tv_address)
        TextView tvLocation;

        @BindView(R.id.tv_quick_review)
        TextView tvQuickReview;

        @BindView(R.id.tv_date)
        TextView tvDate;


        @BindView(R.id.ll_review)
        LinearLayout llReview;

        @BindView(R.id.ll_content)
        LinearLayout llContent;

        @BindView(R.id.lay_quick_review)
        LinearLayout llQuickReview;
        @BindView(R.id.btn_submit)
        Button btnSubmit;
        @BindView(R.id.rb_event)
        RatingBar rbEvent;
        @BindView(R.id.rb_host)
        RatingBar rbHost;
        @BindView(R.id.et_comment)
        EditText etComment;

        /*
        @BindView(R.id.tv_host)
        TextView tvHost;
        @BindView(R.id.tv_invited)
        TextView tvInvited;
        @BindView(R.id.tv_offer)
        TextView tvOffer;*//*
        @BindView(R.id.indicator)
        ImageView indicator;
        @BindView(R.id.text)
        TextView text;

        @BindView(R.id.ll_open_rate)
        LinearLayout llOpenRate;*/

        public EventHolder(View v) {
            super(v);


            ButterKnife.bind(this, v);

           /* indicator.setImageResource(R.drawable.persons);
            indicator.setColorFilter(Color.parseColor("#FFC000"));

            text.setVisibility(View.VISIBLE);
            text.setText("Quick Review");
            */

        }

        public void bindModel(final Event e, final ClickEvent clickEvent) {

            //   Glide.with(context).load(e.getImage()).into(ivAvatar);
            tvEventName.setText(e.getEventname());


            try {
                /*
                llOpenRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickEvent.Click(e);
                }
            });
                if (e.getPrivate_host() != null || !e.getPrivate_host().equals("")) {
                    tvHost.setVisibility(View.VISIBLE);
                    tvHost.setText("Private host: " + e.getPrivate_host());
                } else
                    tvHost.setVisibility(View.GONE);

                if (e.getInvitedby().toString().length() > 0) {
                    tvInvited.setVisibility(View.VISIBLE);
                    tvInvited.setText("Invited by: " + e.getInvitedby());
                } else
                    tvInvited.setVisibility(View.GONE);

                tvOffer.setVisibility(e.getOffer_to_pay().equals("1") ? View.VISIBLE : View.GONE);
*/


                if (e.getSchedStartdate() != null){
                    String schedStartDate = e.getSchedStartdate();
                    try {
                        schedStartDate = TextUtils.formatDate(e.getSchedStartdate(), TextUtils.SDF_1, TextUtils.SDF_2);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    String timeDuration = TextUtils.getTimeDuration(e);
                    try {
                        timeDuration = TextUtils.formatTime(e, TextUtils.SDF_3, TextUtils.SDF_4);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    tvDate.setText(context.getString(R.string.event_sched_n_n,
                            schedStartDate,
                            timeDuration));}
                else
                    tvDate.setText("");

                String location = e.getFulladdress();
                if(location==null || location.equals("")) {
                    tvLocation.setText("");
                }else tvLocation.setText(context.getString(R.string.location_n, location));

                //tvDate.setText(e.getSchedStartdate());
                tvExprires.setText(context.getString(R.string.expires_n, e.getExpirydate()));
                tvEventName.setText(e.getEventname());
                // Glide.with(context).load(e.getImage()).into(ivAvatar);
                GlideImage.load(context, e.getImage(), ivAvatar);


                tvQuickReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* if(!v.isSelected()){
                           *//* showReviewForm(llReview, e, new ClickCard() {
                                @Override
                                public void Click(boolean b) {
                                    tvQuickReview.setSelected(false);
                                }
                            });*//*

                        }else{
                            //llReview.removeViewAt(2);
                        }*/
                        llQuickReview.setVisibility(!v.isSelected()? View.VISIBLE: View.GONE);

                        v.setSelected(!v.isSelected()? true: false);


                    }
                });
                llContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickEvent.openEvent(e);
                    }
                });


                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            if(sending)return;

                            String rating_event = String.valueOf(rbEvent.getRating());
                            String rating_host = String.valueOf(rbHost.getRating());
                            EventController eventController = new EventController(context);
                            eventController.rateEvent(e.getEventid(), rating_event, rating_host, etComment.getText().toString(), new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {
                                    Log.d(TAG, "submit review response : " + response.toString());
                                    Toast.makeText(context, "Thank you for review)", Toast.LENGTH_SHORT).show();
                                    sending = false;
                                    llQuickReview.setVisibility(View.GONE);

                                    tvQuickReview.setSelected(false);
                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {
                                    t.printStackTrace();
                                    sending = false;

                                }
                            });
                            sending = true;
                        }catch (NullPointerException e){
                            e.printStackTrace();
                            sending = false;

                        }

                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        boolean sending = false;

        private void showReviewForm(final LinearLayout llReview, final Event e, final ClickCard c) {
            final View view = LayoutInflater.from(context).inflate(R.layout.quick_review, null);

            final RatingBar rbEvent = view.findViewById(R.id.rb_event);
            final RatingBar rbHost = view.findViewById(R.id.rb_host);
            final EditText etComment = view.findViewById(R.id.et_comment);
            Button btnSubmit = view.findViewById(R.id.btn_submit);


            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        if(sending)return;

                        String rating_event = String.valueOf(rbEvent.getRating());
                        String rating_host = String.valueOf(rbHost.getRating());
                        EventController eventController = new EventController(context);
                        eventController.rateEvent(e.getEventid(), rating_event, rating_host, etComment.getText().toString(), new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                Log.d(TAG, "submit review response : " + response.toString());
                                Toast.makeText(context, "Thank you for review)", Toast.LENGTH_SHORT).show();
                                sending = false;
                                llReview.removeView(view);
                                c.Click(true);
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                t.printStackTrace();
                                sending = false;

                            }
                        });
                        sending = true;
                    }catch (NullPointerException e){
                        e.printStackTrace();
                        sending = false;

                    }

                }
            });

            llReview.addView(view);

        }

        // slide the view from below itself to the current position
        public void slideUp(final View view){
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    0,  // fromYDelta
                    -view.getHeight());                // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            animate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(animate);
        }

        // slide the view from its current position to below itself
        public void slideDown(View view){
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    -view.getHeight(),                 // fromYDelta
                    0); // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);

            view.startAnimation(animate);
        }
    }


}

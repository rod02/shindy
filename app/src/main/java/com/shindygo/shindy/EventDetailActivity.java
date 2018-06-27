package com.shindygo.shindy;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rahimlis.badgedtablayout.BadgedTabLayout;
import com.rd.PageIndicatorView;
import com.shindygo.shindy.activity.SendInviteActivity;
import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.main.model.Respo;
import com.shindygo.shindy.model.Discussion;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.Image;
import com.shindygo.shindy.model.Status;
import com.shindygo.shindy.utils.MapsUtil;
import com.shindygo.shindy.utils.OnSwipeTouchListener;
import com.shindygo.shindy.utils.PageFragment2;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventDetailActivity extends AppCompatActivity implements MapsFragment.OnFragmentInteractionListener,
        DiscussFragment.OnFragmentInteractionListener, ReviewDetailEventFragment.OnFragmentInteractionListener {


    private static final String TAG = EventDetailActivity.class.getSimpleName();

    private static final int DISCUSSION = 1;
    private static final int REVIEW = 2;
    static final int REPLY = 3;



    @BindView(R.id.taab)
    BadgedTabLayout taab;
    @BindView(R.id.container)
    ViewPager container;
    @BindView(R.id.tv_eventName)
    TextView tvEventName;
    @BindView(R.id.tv_hosted_by)
    TextView tvHostedBy;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.tv_man)
    TextView tvMan;
    @BindView(R.id.tv_woman)
    TextView tvWoman;
    @BindView(R.id.human)
    LinearLayout human;
    @BindView(R.id.ll_block_event)
    LinearLayout llBlockEvent;
    @BindView(R.id.ll_like)
    LinearLayout llLike;
    @BindView(R.id.iv_like)
    ImageView ivLike;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.stop)
    ImageView stop;

    @BindView(R.id.send_invite)
    LinearLayout sendInvite;

    @BindView(R.id.whoInvited)
    LinearLayout whoInvited;
    @BindView(R.id.iam_in)
    LinearLayout iamIn;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.all)
    LinearLayout all;
    @BindView(R.id.menu)
    RelativeLayout menu;
    @BindView(R.id.iam_in_image)
    ImageView iamInImage;
    @BindView(R.id.card_comments)
    CardView cvCommentBox;
    @BindView(R.id.ivSend)
    ImageView ivSend;
    @BindView(R.id.etComment)
    EditText etComment;

    @BindView(R.id.iv_back)
    ImageView ivBack;

    private boolean hided = false;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    Event event;
    PagerAdapter pagerAdapter;
    EventController eventController;
    int topH;
    float prev = 0;
    private ViewPager pager;
    private SharedPreferences sharedPref;
    private String fbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);
        eventController = new EventController(this);
        menu.setVisibility(View.GONE);
        sharedPref = getSharedPreferences("set", Context.MODE_PRIVATE);
        fbid = sharedPref.getString("fbid", "");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        event = (Event) getIntent().getExtras().getParcelable("event");
        if (event == null) {
            Toast.makeText(getApplicationContext(), "Sorry, some problem with Event", Toast.LENGTH_SHORT).show();
            finish();
        }

        menu.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        show();
                                        menu.setVisibility(View.GONE);
                                        return true;
                                    }
                                }

        );

        top.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeTop() {

                DiscussFragment.changeVisibility();
                hide();
                menu.setVisibility(View.VISIBLE);
            }
        });
       // ratingBar.setNumStars(5);
        if (event.getHostReview() != null)
            ratingBar.setRating(Float.parseFloat(event.getHostReview()));

        if (event.isLiked())
            ivLike.setColorFilter(ContextCompat.getColor(EventDetailActivity.this, R.color.navigation_notification_red));
        if (event.getBlock_status().equals("1"))
            ((ImageView) llBlockEvent.findViewById(R.id.stop)).setColorFilter(ContextCompat.getColor(EventDetailActivity.this, R.color.navigation_notification_red));
        if (event.getAttendingstatus().equals("1"))
            iamInImage.setColorFilter(ContextCompat.getColor(EventDetailActivity.this, R.color.green_online));
            tvMan.setText(event.getMax_male());
        tvWoman.setText(event.getMax_female());
        tvRate.setText(event.getHostReview());
        tvEventName.setText(event.getEventname());
        tvHostedBy.setText(getString(R.string.hosted_by_n , event.getPrivate_host()));
        container.setAdapter(mSectionsPagerAdapter);
        container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position){
                    case DISCUSSION:
                        cvCommentBox.setVisibility(View.VISIBLE);

                        break;
                        default:
                            cvCommentBox.setVisibility(View.GONE);

                            break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(v.getTag()!=null){
                        int tag = (int) v.getTag();
                        switch (tag){
                            case DISCUSSION:
                                sendCommentDiscussion();
                                v.setTag(DISCUSSION);

                                break;
                            case REPLY:
                                sendDiscussionReply();
                                v.setTag(DISCUSSION);

                                break;

                        }
                    }else {
                        sendCommentDiscussion();
                    }

                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }
        });
        taab.setupWithViewPager(container);

        whoInvited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventDetailActivity.this, MyInvitesActivity.class).putExtra("event", event));
            }
        });
        iamIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event.getAttendingstatus().equals("0")) {
                    if(event.getOffer_to_pay().equals("1"))
                    {
                        new EventController(EventDetailActivity.this).joinIamInEvent(event.getEventid(),event.getInvitecode(), new Callback<Status>() {
                            @Override
                            public void onResponse(Call<Status> call, Response<Status> response) {
                                Status status = response.body();
                                if(status.getStatus().equals("success")) {
                                    setEventAttandeeStatus();
                                    Toast.makeText(EventDetailActivity.this, "You are in", Toast.LENGTH_LONG).show();
                                }
                                else
                                    Toast.makeText(EventDetailActivity.this, status.getResult(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<Status> call, Throwable t) {
                            }
                        });
                    }
                    else {
                        FragmentManager fm = getSupportFragmentManager();
                        Fragment fragment = new PurchaseFragment();
                        fm.beginTransaction()
                                .replace(R.id.frame, fragment)
                                .addToBackStack("my_fragment")
                                .commit();
                        findViewById(R.id.frame).setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    eventController.leaveEvent(event.getEventid(), new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            Toast.makeText(EventDetailActivity.this, "You leave event successfully", Toast.LENGTH_SHORT).show();
                            event.setAttendingstatus("0");
                            iamInImage.setColorFilter(null);
                        }

                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {

                        }
                    });
                }
            }
        });
        llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!event.isLiked()) {
                    event.setLike_status(true);
                    eventController.likeEvent(event.getEventid(), new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            Toast.makeText(EventDetailActivity.this, "Event liked", Toast.LENGTH_SHORT).show();
                            ivLike.setColorFilter(ContextCompat.getColor(EventDetailActivity.this, R.color.navigation_notification_red));
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {

                        }
                    });
                } else {
                    //todo unlike
                    event.setLike_status(false);
                    eventController.unlikeEvent(event.getEventid(), "", new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            Toast.makeText(EventDetailActivity.this, "Event disliked", Toast.LENGTH_SHORT).show();
                            ivLike.setColorFilter(ContextCompat.getColor(EventDetailActivity.this, R.color.gray_tint));
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Toast.makeText(EventDetailActivity.this, "Event not disliked", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        llBlockEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(event.getInvitedby()))
                if (event.getBlock_status().equals("0")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(EventDetailActivity.this).create();

                    alertDialog.setTitle("Do you want to block "+event.getInvitedby()+" or current event?");

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                           dialog.cancel();

                        } });

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Block event", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog1, int id) {

                            AlertDialog.Builder dialog = new AlertDialog.Builder(EventDetailActivity.this);
                            dialog.setTitle("Block this event")
                                    .setMessage(Html.fromHtml("<b>" + "This will be put in your black list and you will not be invited again.You can unblock this later" + "</b>"))
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialoginterface, int i) {
                                            dialoginterface.cancel();
                                        }
                                    })
                                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialoginterface, int i) {


                                            ((ImageView) llBlockEvent.findViewById(R.id.stop)).setColorFilter(ContextCompat.getColor(EventDetailActivity.this, R.color.navigation_notification_red));


                                            eventController.blockEvent(event.getEventid(), new Callback<Object>() {
                                                @Override
                                                public void onResponse(Call<Object> call, Response<Object> response) {
                                                    Toast.makeText(EventDetailActivity.this, "Event blocked successfully", Toast.LENGTH_SHORT).show();
                                                    event.setBlock_status("1");
                                                    finish();
                                                }

                                                @Override
                                                public void onFailure(Call<Object> call, Throwable t) {

                                                }
                                            });
                                        }
                                    }).show();

                        }});

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Block user", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            new Api(EventDetailActivity.this).blockUser2(event.getInvited_by_id(), new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {
                                    Toast.makeText(EventDetailActivity.this, "User blocked successfully!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {
                                    Toast.makeText(EventDetailActivity.this, "Some problems at BACKEND!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }});
                    alertDialog.show();

                } else {
                    eventController.unblockEvent(event.getEventid(), "", new Callback<Respo>() {
                        @Override
                        public void onResponse(Call<Respo> call, Response<Respo> response) {
                            Toast.makeText(EventDetailActivity.this, "Event unblocked successfully", Toast.LENGTH_SHORT).show();
                            ((ImageView) llBlockEvent.findViewById(R.id.stop)).setColorFilter(null);
                            event.setBlock_status("0");

                        }

                        @Override
                        public void onFailure(Call<Respo> call, Throwable t) {

                        }
                    });
                }
            else
                    Toast.makeText(EventDetailActivity.this, "You cant block this event, maybe it's your event?", Toast.LENGTH_SHORT).show();

            }
        });

        final PageIndicatorView pageIndicatorView = findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setCount(event.getImages().size()); // specify total count of indicators
        pager = findViewById(R.id.pager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelected(position);

            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void sendDiscussionReply() {
        Fragment fragment = getPagerFragment(DISCUSSION);
        if (fragment instanceof DiscussFragment){
            DiscussFragment discussFragment = (DiscussFragment) fragment;
            discussFragment.sendDiscussionReply((Discussion) etComment.getTag(), etComment.getText().toString());
            etComment.setText("");
            etComment.setTag(null);
            hideKeyboard();

        }
    }

    private void sendCommentDiscussion() {
        Fragment fragment = getPagerFragment(DISCUSSION);
        if (fragment instanceof DiscussFragment){
            DiscussFragment discussFragment = (DiscussFragment) fragment;
            discussFragment.sendMessage(etComment.getText().toString());
            etComment.setText("");
            hideKeyboard();

        }
    }

    private Fragment getPagerFragment(int position) {
        return getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + position);

    }

    public void show()
    {
        hided = false;
        menu.setVisibility(View.GONE);
        DiscussFragment.changeVisibility();
        if (top.getHeight() == 0) {
            ValueAnimator anim = ValueAnimator.ofInt(0, topH);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
                    layoutParams.height = val;
                    top.setLayoutParams(layoutParams);
                }
            });
            anim.setDuration(1000);
            anim.start();
        }
    }
    public void hide() {
        if (topH == 0)
            topH = top.getHeight();
        menu.setVisibility(View.VISIBLE);
        hided = true;
        if(top.getHeight()==topH) {
            ValueAnimator anim = ValueAnimator.ofInt(top.getHeight(), 0);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
                    layoutParams.height = val;
                    top.setLayoutParams(layoutParams);
                }
            });
            anim.setDuration(1000);
            anim.start();
        }
    }


    public void hideKeyboard() {
        try {

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void showKeyBoard() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    public Event getEvent() {
        return event;
    }
    public void setEventAttandeeStatus() {
        event.setAttendingstatus("1");
        iamInImage.setColorFilter(ContextCompat.getColor(EventDetailActivity.this, R.color.green_online));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @OnClick(R.id.send_invite)
    public void onViewClicked() {
        startActivity(new Intent(EventDetailActivity.this, SendInviteActivity.class).putExtra("event", event));


    }

    public EditText getCommentBox() {
        return etComment;
    }
    public ImageView getSendButton() {
        return ivSend;
    }



    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MapsFragment.newInstance(String.valueOf(position), "1");
                case 1:
                    return DiscussFragment.newInstance(String.valueOf(position), "2");
                case 2:

                    return ReviewDetailEventFragment.newInstance(String.valueOf(position), "3");
                default:
                    return MapsFragment.newInstance(String.valueOf(position), "1");
            }
        }

        @Override
        public int getCount() {

            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.details);
                case 1:
                    return getString(R.string.discussion);
                case 2:
                    return getString(R.string.review);
            }
            return null;
        }
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment2.newInstance(position, event.getImages().get(position).imagePath);
        }

        @Override
        public int getCount() {
            return event.getImages().size();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MapsUtil.MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        MapsFragment fragment = (MapsFragment) getPagerFragment(0);
                        //Request location updates:
                        fragment.requestLocationUpdates(true);

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

}

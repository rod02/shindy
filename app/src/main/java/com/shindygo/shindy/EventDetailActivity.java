package com.shindygo.shindy;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.widget.ShareDialog;
import com.rahimlis.badgedtablayout.BadgedTabLayout;
import com.rd.PageIndicatorView;
import com.shindygo.shindy.activity.SendInviteActivity;
import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.main.model.Respo;
import com.shindygo.shindy.model.Discussion;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.Status;
import com.shindygo.shindy.utils.FileUtils;
import com.shindygo.shindy.utils.GlideImage;
import com.shindygo.shindy.utils.MapsUtil;
import com.shindygo.shindy.utils.OnSwipeTouchListener;
import com.shindygo.shindy.utils.PageFragment2;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

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
    private static final int MAX_IMAGE_SIZE = 1280;


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
    @BindView(R.id.rl_parent)
    RelativeLayout rlMain;
    @BindView(R.id.btn_email)
    Button btnEmail;
    @BindView(R.id.btn_fb)
    Button btnFb;
    @BindView(R.id.btn_twitter)
    Button btnTwitter;
    TwitterLoginButton loginButton;

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
    private PopupWindow messPopup;
    CallbackManager callbackManager;

    Uri shareAbleUri;


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
        callbackManager = CallbackManager.Factory.create();
        loginButton = new TwitterLoginButton(this);
        loginButton.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                shareTwitter();
            }

            @Override
            public void failure(TwitterException e) {
                Log.d(TAG, "Twitter login failure " + e.getLocalizedMessage());
                Toast.makeText(EventDetailActivity.this,"login failed", Toast.LENGTH_LONG).show();
            }
        });

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
            tvMan.setText(event.getMaleSpot());
        tvWoman.setText(event.getFeMaleSpot());
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
                    if(event.getOffer_to_pay().equals("1")) {
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
                                Log.d(TAG, "ImIn onFailure "+t.getLocalizedMessage());
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
                            Log.d(TAG, "ImIn onFailure leaveEvent "+t.getLocalizedMessage());

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

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeEmail();
            }
        });

        btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFb();
            }
        });
        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTwitter();
            }
        });
        getShareableImage(event.getImage());
    }

    private void getShareableImage(String imageUrl) {
        Glide // execute this on UI thread!
                .with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        //shareAbleUri = bitmapToUriConverter(resource);
                        new SaveAsFileTask().execute(resource);
                    }
                });
        ;

    }
    public Uri bitmapToUriConverter(Bitmap mbitmap) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize
           // options.inSampleSize = calculateInSampleSize(options, 100, 100);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
          /*  Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 200, 200,
                    true);*/
            Bitmap newBitmap = Bitmap.createScaledBitmap(mbitmap, mbitmap.getWidth(), mbitmap.getHeight(), true);
            File file = new File(FileUtils.getFilename());
            OutputStream out = new FileOutputStream(file);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);

        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
        return uri;
    }
    class SaveAsFileTask extends AsyncTask<Bitmap, Void, File> {
        @Override protected File doInBackground(Bitmap... params) {
            OutputStream out = null;
            File file = null;
            try {
                /*File target = new File(FileUtils.getFilename());
                OutputStream out = new FileOutputStream(target);
                out.write((byte[])params[0]);*/
                final BitmapFactory.Options options = new BitmapFactory.Options();
                // Calculate inSampleSize
                // options.inSampleSize = calculateInSampleSize(options, 100, 100);

                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;
          /*  Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 200, 200,
                    true);*/
                Bitmap mbitmap = params[0];
                Bitmap newBitmap = Bitmap.createScaledBitmap(mbitmap, mbitmap.getWidth(), mbitmap.getHeight(), true);
                file = new File(FileUtils.getFilename());
                out = new FileOutputStream(file);
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                return file;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return file;
        }
        @Override protected void onPostExecute(@Nullable File result) {
            //Uri uri = FileProvider.getUriForFile(EventDetailActivity.this, "com.shindygo.shindy.fileprovider", result);
            shareAbleUri  =Uri.fromFile(result);;
        }
    }
    private void shareTwitter() {
        final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                .getActiveSession();
        if(session==null || session.getAuthToken().isExpired()){
            loginButton.callOnClick();
            return;
        }
        final Intent intent = new ComposerActivity.Builder(EventDetailActivity.this)
                .session(session)
                .image(shareAbleUri)
                .text(event.getEventname())
                .hashtags("#shindy")
                .createIntent();
        startActivity(intent);
    }

    private void shareFb() {
        /*ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .add
                .build();
        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setImageUrl(Uri.parse(event.getImage()))
                .
                 .build();

        ShareContent shareContent = new ShareMediaContent.Builder()
                .
                .addMedium(sharePhoto)

                .build();*/

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://shindygo.com/"))
                .build();
    /*    SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ted))
                .setCaption(event.getEventname())
                .build();
        */
   /*     ShareContent content = new ShareMediaContent.Builder()

                .addMedium(sharePhoto)

                .build();*/
     /*   ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                .putString("og:type", "event.normal")
                .putString("og:title", event.getEventname())
                .putString("og:description", event.getDescription())
                .putPhoto("og:image",sharePhoto)
           *//*         .putInt("fitness:duration:value", 100)
                    .putString("fitness:duration:units", "s")
                    .putInt("fitness:distance:value", 12)
                    .putString("fitness:distance:units", "km")
                    .putInt("fitness:speed:value", 5)
                    .putString("fitness:speed:units", "m/s")*//*
                .build();
        ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                .setActionType("og.shares")
                .putObject("event:normal", object)
                .build();
        ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                .setPreviewPropertyName("event:normal")
                .setAction(action)
                .build();*/


        ShareDialog shareDialog = new ShareDialog(EventDetailActivity.this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d(TAG, "fbShare onSuccess" +result.toString());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "fbShare onCancel" );
                Toast.makeText(EventDetailActivity.this, "Facebook share cancelled", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "fbShare onError "+ error.toString() );

                Toast.makeText(EventDetailActivity.this, "Facebook share failed", Toast.LENGTH_LONG).show();

            }
        });
        shareDialog.show(content);

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



    private void composeEmail() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.popup_invite_by_email, null);
        final TextView textView = customView.findViewById(R.id.textView2);
        final EditText email = customView.findViewById(R.id.editText);
        final EditText note = customView.findViewById(R.id.editText2);
        ImageView imageView = customView.findViewById(R.id.imgEvent);
        Button send = customView.findViewById(R.id.send);
        TextView title,date;
        title = customView.findViewById(R.id.title);
        date = customView.findViewById(R.id.date);
        title.setText(event.getEventname());
        date.setText(event.getEventSched());
        Glide.with(EventDetailActivity.this).load(event.getImage()).into(imageView);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(note.getWindowToken(),0);
                if(!TextUtils.isEmpty(email.getText().toString()))
                    new EventController(EventDetailActivity.this).inviteByEmail(event.getEventid(), email.getText().toString(), note.getText().toString(), new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            Status status = response.body();
                            if(!status.getStatus().equals("success")) {
                                Toast.makeText(EventDetailActivity.this, status.getResult(), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(EventDetailActivity.this, "Invite sent", Toast.LENGTH_SHORT).show();
                                messPopup.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {

                        }
                    });
                else
                    Toast.makeText(EventDetailActivity.this, "Enter correct email", Toast.LENGTH_SHORT).show();
            }
        });
        messPopup = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );
        if (Build.VERSION.SDK_INT >= 21) {
            messPopup.setElevation(5.0f);
        }

        ImageView closeButton = customView.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messPopup.dismiss();
            }
        });

        messPopup.showAtLocation(rlMain, Gravity.CENTER, 0, 0);
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);

    }
}

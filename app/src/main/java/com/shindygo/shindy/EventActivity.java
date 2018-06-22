package com.shindygo.shindy;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.IpCons;
import com.google.gson.Gson;
import com.rd.PageIndicatorView;
import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.model.CreateEventCallBack;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.Image;
import com.shindygo.shindy.model.User;
import com.shindygo.shindy.utils.FontUtils;
import com.shindygo.shindy.utils.ImageFragment;
import com.shindygo.shindy.utils.ImageUtils;
import com.shindygo.shindy.utils.TextUtils;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class EventActivity extends Fragment  {

    private static final String TAG = "EventActivity";
    private static final int RQC_IMG_CHOOSER = IpCons.RC_IMAGE_PICKER;
    private static final int RQ_IMG = 142;
    private static final int RQ_LOCATION = 143;
    static int PAGE_COUNT = 5;
    public static final String EXTRA_EVENT_ID = "event_id";
    public static final String EXTRA_MODEL = "extra_model";

    private static final int UPDATE = 1;
    private static final int CREATE = 0;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.imgDateStart)
    ImageView imgDateStart;
    @BindView(R.id.tvDateStart)
    TextView tvDateStart;
    @BindView(R.id.tvTimeStart)
    TextView tvTimeStart;
    @BindView(R.id.imgDateEnd)
    ImageView imgDateEnd;
    @BindView(R.id.tvDateEnd)
    TextView tvDateEnd;
    @BindView(R.id.tvTimeEnd)
    TextView tvTimeEnd;
    @BindView(R.id.imgExpiry)
    ImageView imgExpiry;
    @BindView(R.id.tvDateExpire)
    TextView tvDateExpire;
    @BindView(R.id.imgLocation)
    ImageView imgLocation;
    TextInputEditText etLocation;
    @BindView(R.id.imgCoHost)
    ImageView imgCohost;
    TextInputEditText etCoHost;
    @BindView(R.id.imgTicketPrice)
    ImageView imgTicketPrice;
    TextInputEditText etTicketPrice;
    TextInputEditText etMaxMale;
    TextInputEditText etMaxFemale;
    TextInputEditText etWebsite;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.bt_save)
    Button btnSave;
    @BindView(R.id.pbLayout)
    FrameLayout pbLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    @BindView(R.id.bt_add)
    Button btAdd;
    @BindView(R.id.imgCancel)
    ImageView imgCancel;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.et_zip)
    TextView etZipcode;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.cpAbleInvite)
    CheckBox cpAbleInvite;

 /*   @BindView(R.id.bt_add)
    Button btAdd;*/
    List<String> images64 = new ArrayList<>();
    List<Image> images = new ArrayList<>();

    Uri linkIm;
    private double lon, lat;
    private String zipcode;

    TextInputEditText etEventName;




    private PopupWindow mPopupWindow;
    private RelativeLayout mRelativeLayout;
    private Api api;
    private Event event ;
    private String eventId;
    private boolean saving;

    private boolean update;

    ViewPager pager;
    PagerAdapter pagerAdapter;
    //OnFragmentInteractionListener activityListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args =getArguments();
        if (args != null) {
            eventId  =  args.getString(EXTRA_EVENT_ID);
            String json   =  args.getString(EXTRA_MODEL);
            event = new Gson().fromJson(json, Event.class);


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_event, container, false);
        ButterKnife.bind(this, view);
        showProgressBar(true);

        FontUtils.setFont(title, FontUtils.Be_Bright);
        Activity mActivity = getActivity();

        if(event == null){
            //
            event = null;
            eventId = "";

        }else{
            back.setImageDrawable(ContextCompat.getDrawable(back.getContext(), R.drawable.left_arrow));

            update =true;
            btnSave.setText(R.string.save);
            title.setText(event.getEventname());
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity mActivity = getActivity();
                if(mActivity instanceof MainActivity){
                   // ((MainActivity) getActivity()).openenDrawer();
                }else{
                    getFragmentManager().popBackStack();
                }
            }
        });
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvLeft.setText(131-charSequence.length() + " character left");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvLeft.setText(131-editable.length() + " character left");

            }
        });
        final Calendar calendar = Calendar.getInstance();
        //final Date date = calendar.getTime();
        View.OnClickListener onClickDateView = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog dialog =  new DatePickerDialog(getContext(), dateSetListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setId(v.getId());
                dialog.show();
            }
        };

        tvDateStart.setOnClickListener(onClickDateView);
        tvDateEnd.setOnClickListener(onClickDateView);
        tvDateExpire.setOnClickListener(onClickDateView);
        tvTimeEnd.setOnClickListener(new View. OnClickListener() {

            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        try {

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR, selectedHour);
                            calendar.set(Calendar.MINUTE, selectedMinute);
                            //  calendar.set(Calendar.SECOND, dayOfMonth);
                            SimpleDateFormat sdf = new SimpleDateFormat(TextUtils.SDF_4);

                           // ((TextView)getView().findViewById(v.getId()))
                            ((TextView)v).setText(sdf.format(calendar.getTime()));
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                //mTimePicker.findViewById(com.android.internal.R.id.timePicker).setId(v.getId());

                mTimePicker.show();
                //mTimePicker.findViewById(com.android.internal.R.id.timePicker).setId(v.getId());
            }
        });
        tvTimeStart.setOnClickListener(new View. OnClickListener() {

            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        try {

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR, selectedHour);
                            calendar.set(Calendar.MINUTE, selectedMinute);
                            //  calendar.set(Calendar.SECOND, dayOfMonth);
                            SimpleDateFormat sdf = new SimpleDateFormat(TextUtils.SDF_4);

                            // ((TextView)getView().findViewById(v.getId()))
                            ((TextView)v).setText(sdf.format(calendar.getTime()));
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                //mTimePicker.findViewById(com.android.internal.R.id.timePicker).setId(v.getId());

                mTimePicker.show();
                //mTimePicker.findViewById(com.android.internal.R.id.timePicker).setId(v.getId());
            }
        });



        etCoHost = view.findViewById(R.id.et_co_host);
        etEventName = view.findViewById(R.id.etEventName);
        etLocation = view.findViewById(R.id.etLocation);
        etMaxFemale = view.findViewById(R.id.et_max_female);
        etMaxMale = view.findViewById(R.id.et_max_male);
        etWebsite = view.findViewById(R.id.etWeb);
        etTicketPrice = view.findViewById(R.id.et_ticket_price);
        etTicketPrice.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {

            }
            public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {

            }

            public void afterTextChanged(Editable arg0) {
                if (arg0.length() > 0) {
                    int count = -1;

                    String str = etTicketPrice.getText().toString();
                    etTicketPrice.setOnKeyListener(new View.OnKeyListener() {


                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            int count = -1;

                            if (keyCode == KeyEvent.KEYCODE_DEL) {
                                count--;
                                InputFilter[] fArray = new InputFilter[1];
                                fArray[0] = new InputFilter.LengthFilter(100);
                                etTicketPrice.setFilters(fArray);
                                //change the edittext's maximum length to 100.
                                //If we didn't change this the edittext's maximum length will
                                //be number of digits we previously entered.
                            }
                            return false;
                        }
                    });
                    char t = str.charAt(arg0.length() - 1);
                    if (t == '.') {
                        count = 0;
                    }
                    if (count >= 0) {
                        if (count == 2) {
                            InputFilter[] fArray = new InputFilter[1];
                            fArray[0] = new InputFilter.LengthFilter(arg0.length());
                            etTicketPrice.setFilters(fArray);
                            //prevent the edittext from accessing digits
                            //by setting maximum length as total number of digits we typed till now.
                        }
                        count++;
                    }
                }
            }
        });




     /*   btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });
*/
        imgLocation.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              Intent i = new Intent(getContext(), MapsActivity.class);
                                              startActivityForResult(i, RQ_LOCATION);
                                          }
                                      }
        );

       btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if(getActivity() instanceof  MainActivity){
                    if(!((MainActivity) getActivity()).isConnectedOrConnecting(getActivity())){
                        ((MainActivity) getActivity()).showDialog("require internet");
                        return;
                    }
                }*/

                if(saving){
                    return;
                }
                saving = true;
               // if(progressBar.getVisibility()== View.VISIBLE)return;
                showProgressBar(true);

                saveToRemote(create());

            }
        });

        try {
            images = event.getImages();
            PAGE_COUNT = images.size();
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        final PageIndicatorView pageIndicatorView = view.findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setCount(PAGE_COUNT); // specify total count of indicators
        pager =view.findViewById(R.id.pager);
        pagerAdapter =new MyFragmentPagerAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()      {

            @Override
            public void onPageSelected ( int position){
                pageIndicatorView.setSelected(position);
            }

            @Override
            public void onPageScrolled ( int position, float positionOffset,
                                         int positionOffsetPixels){
            }

            @Override
            public void onPageScrollStateChanged ( int state){
            }
        });


        if(update)setDataToViews(event);
        showProgressBar(false);



        return view;
    }

    private void setDataToViews(Event event) {
        etEventName.setText(event.getEventname());
        etLocation.setText(event.getFulladdress());
        etZipcode.setText(event.getZipcode());
        etDescription.setText(event.getDescription());
        etTicketPrice.setText(event.getTicketprice());/*
        //event.setRepresentative(etCoHost.getTag()==null?"": (String) etCoHost.getTag());
        SimpleDateFormat sdf1 = new SimpleDateFormat(TextUtils.SDF_1);*/
        //event.setCreateDate(sdf1.format(new Date()));
        tvDateStart.setText(event.getSchedStartdate());
        tvDateEnd.setText(event.getSchedEnddate());
        tvTimeStart.setText(event.getStartTime());
        tvTimeEnd.setText(event.getEndTime());
        tvDateExpire.setText(event.getExpirydate());
        etMaxMale.setText(event.getMax_male());
        etMaxFemale.setText(event.getMax_female());
        etMaxFemale.setText(event.getWebsite_url());
       try{
           cpAbleInvite.setChecked(Integer.parseInt(event.getAbleGuestInvite())==1);
       }catch (Exception e){
           e.printStackTrace();
       }
  /*      try {
            if(pageViews==null)pageViews = new ArrayList<>();
            Log.v(TAG, "setviews setviews");

            pageViews.addAll(Image.toPage(event));
            if(pageViews!=null){
                Log.v(TAG, "setviews pagviews != null");
                Log.v(TAG, "page.res " + pageViews.get(0).res  );
                Log.v(TAG, "page.data " + pageViews.get(0).data  );

                imageSlider(pageViews);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        *//*/event.setImage(Image.from(pageViews));
        event.setNotes("");
        event.setEventId("");*/
    }

    private void showProgressBar(boolean show) {
        //if(getView()==null)return;
        try{
            Log.v(TAG, "showProgressBar "+ show);
            pbLayout.setVisibility(show? View.VISIBLE: View.GONE);
            progressBar.setVisibility(show? View.VISIBLE: View.GONE);

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }


    private void saveToRemote(final Event event) {
        EventController api = new EventController(getContext());
        Callback<JSONObject> callback =new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                Log.v(TAG,"onResponse");
                if(response!=null){
                    Log.v(TAG,"onResponse " +response.message());
                    try {
                        Log.v(TAG,"onResponse " +response.isSuccessful());

                        if(response.isSuccessful()){
                            for (int i = 0; i < images.size() ; i++) {
                                Image image = images.get(i);
                                if(image.id ==null || image.id.equals("0")){
                                    try {
                                        images64.add(ImageUtils.img64fromPath(image.imagePath));
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }


                            if (images64.size() == 0) {
                                showProgressBar(false);

                                if(getView()!=null){
                                    if (!update) {
                                        Snackbar.make(getView().findViewById(R.id.rl),
                                                R.string.event_successfully_created, Snackbar.LENGTH_LONG).show();
                                    }else{
                                        Snackbar.make(getView().findViewById(R.id.rl), R.string.event_successfully_updated, Snackbar.LENGTH_LONG).show();
                                    }
                                    ( (AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStack();

                                }
                                return;
                            }
                                showProgressBar(true);
                            for (final String url : images64) {
                                new EventController(getContext()).pushImage(event.getEventid(), url, new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {
                                        if (url == images64.get(images64.size() - 1)) {
                                            showProgressBar(false);

                                            if(getView()!=null){
                                                if (!update) {
                                                    Snackbar.make(getView().findViewById(R.id.rl),
                                                            R.string.event_successfully_created, Snackbar.LENGTH_LONG).show();
                                                }else{
                                                    Snackbar.make(getView().findViewById(R.id.rl), R.string.event_successfully_updated, Snackbar.LENGTH_LONG).show();
                                                }
                                                ( (AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStack();

                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Object> call, Throwable t) {
                                        t.printStackTrace();;
                                        showProgressBar(false);

                                    }
                                });
                            }

                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();

                    }
                }
                showProgressBar(false);
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e(TAG, "failed to create event");
                Log.e(TAG, t.getMessage());
                showProgressBar(false);

                try {

                    if(getView()!=null)
                        Snackbar.make(getView().findViewById(R.id.rl),
                                R.string.please_try_again, Snackbar.LENGTH_LONG).show();

                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        };
        if(update){
           // Api.getInstance().updateEvent(event, callback);
            api.updateEvent(event, callback);

        }else {
            api.createEvent(event, new Callback<CreateEventCallBack>() {
                @Override
                public void onResponse(Call<CreateEventCallBack> call, Response<CreateEventCallBack> response) {
                    Log.v(TAG,"onResponse");
                    if(response!=null){
                        Log.v(TAG,"onResponse " +response.message());
                        try {
                            Log.v(TAG,"onResponse " +response.isSuccessful());

                            if(response.isSuccessful()){
                                if(getView()!=null){
                                    if (!update) {
                                        Snackbar.make(getView().findViewById(R.id.rl),
                                                R.string.event_successfully_created, Snackbar.LENGTH_LONG).show();
                                    }else{
                                        Snackbar.make(getView().findViewById(R.id.rl), R.string.event_successfully_updated, Snackbar.LENGTH_LONG).show();
                                    }
                                    ( (AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStack();

                                }


                            }
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                    showProgressBar(false);
                }

                @Override
                public void onFailure(Call<CreateEventCallBack> call, Throwable t) {
                    Log.e(TAG, "failed to create event");
                    Log.e(TAG, t.getMessage());
                    showProgressBar(false);

                    try {

                        if(getView()!=null)
                            Snackbar.make(getView().findViewById(R.id.rl),
                                    R.string.please_try_again, Snackbar.LENGTH_LONG).show();

                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void openImageChooser() {
        ImagePicker.create(EventActivity.this)
             //   .returnMode(ReturnMode.ALL) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                .folderMode(true) // set folder mode (false by default)
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Choose Images")
               // .imageLoader(new GrayscaleImageLoader())
                .start(RQC_IMG_CHOOSER); // image selection title
    }


    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(TextUtils.SDF_1);

                ((TextView)getView().findViewById(view.getId())).setText(sdf.format(calendar.getTime()));
            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }

    };


    private Event create() {
        if(!update){
            event = new Event();
            event.setEventid("");
        }
        event.setUserFbid(User.getCurrentUserId());
        event.setEventname(getText(etEventName));
        event.setFulladdress(getText(etLocation));
        event.setLat(String.valueOf(lat));
        event.setLong(String.valueOf(lon));
        /*String longitude;	                //Longitude (return on map api)
        String lat;                         //Latitude (return on map api)
        String zipCode;                 //*	zipcode (return on map api)*/

        List<Image> image;	                //url image path

        event.setZipcode(getText(etZipcode));

        event.setDescription(getText(etDescription));
  //      String notes;                   //	sample event notes
        event.setTicketprice(getText(etTicketPrice));
        event.setRepresentative(etCoHost.getTag()==null?"": (String) etCoHost.getTag());
        SimpleDateFormat sdf1 = new SimpleDateFormat(TextUtils.SDF_1);
        event.setCreatedate(sdf1.format(new Date()));

        event.setExpirydate(getText(tvDateExpire));
       // String expiryDate;              //	2018-02-01
        event.setSchedStartdate(getText(tvDateStart));

        //  String schedStartDate;         //	2018-01-01
        event.setStartTime(getText(tvTimeStart));
        event.setSchedEnddate(getText(tvDateEnd));
        event.setEndTime(getText(tvTimeEnd));
        event.setMax_male(getText(etMaxMale));
        event.setMax_female(getText(etMaxFemale));
        event.setWebsite_url(getText(etWebsite));
        event.setAbleGuestInvite(cpAbleInvite.isChecked()? "1":"0");
        try {
           // event.setImage(Image.from(pageViews));
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        event.setNotes("");
    return event;

    }

    String getText(TextView tv){
        try {
            return tv.getText().toString();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult requestCode:" +requestCode
                                    + " resultCode:"+resultCode
                                    + "  intent=="+String.valueOf(data==null));
        /*
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            //List<Image> images = Image.from(ImagePicker.getImages(data));
            try {
                if(pageViews==null)pageViews = new ArrayList<>();
                Log.v(TAG, "onActivityResult ImagePicker");

                pageViews.addAll(Image.fromImagePickerToPage(ImagePicker.getImages(data)));
                if(pageViews!=null){
                    Log.v(TAG, "onActivityResult pagviews != null");
                    Log.v(TAG, "page.res " + pageViews.get(0).res  );
                    Log.v(TAG, "page.data " + pageViews.get(0).data  );

                    imageSlider(pageViews);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
*/


        switch (requestCode) {
            case RQ_LOCATION: {
                if (resultCode == RESULT_OK) {
                    lat = data.getDoubleExtra("result_lat", 0);
                    lon = data.getDoubleExtra("result_lon", 0);
                    zipcode = data.getStringExtra("result_zipcode");
                    if (zipcode == null)
                        zipcode = "0000";

                    etLocation.setText(data.getStringExtra("result_text"));
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
                break;
            }
            case RQC_IMG_CHOOSER:

                if (resultCode == RESULT_OK) {
                    if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
                        // Get a list of picked images
                        //List<Image> images = Image.from(ImagePicker.getImages(data));

                        List<com.esafirm.imagepicker.model.Image> images = ImagePicker.getImages(data);
/*                        try {
                            if(pageViews==null)pageViews = new ArrayList<>();
                            Log.v(TAG, "onActivityResult ImagePicker");

                            pageViews.addAll(Image.fromImagePickerToPage(ImagePicker.getImages(data)));
                            if(pageViews!=null){
                                Log.v(TAG, "onActivityResult pagviews != null");
                                Log.v(TAG, "page.res " + pageViews.get(0).res  );
                                Log.v(TAG, "page.data " + pageViews.get(0).data  );

                                imageSlider(pageViews);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }*/
                        Log.d(TAG, "resp image: ");

                        if (images!=null){

                            for (int i = 0; i <images.size() ; i++) {
                                String path = images.get(i).getPath();
                                Log.d(TAG, "resp image: "+path);
                                this.images.add(new Image("0",path));
                            }
                            pagerAdapter.notifyDataSetChanged();
                        }



                     /*   try {
                          *//*  Log.d(TAG, "resp image: "+images.get(0).getPath());
                            final Uri imageUri = data.getData();
                            linkIm = imageUri;

                            final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
                            images64.add(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
                            pagerAdapter.notifyDataSetChanged();*//*

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }
*/

                    }

                } else {
                    Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //To avoid memory leak ,you should release the res
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            activityListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ImageFragment.newInstance(position, images.get(position));
        }

        @Override
        public int getCount() {
            return images.size();
        }

    }

}

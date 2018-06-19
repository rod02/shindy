package com.shindygo.shindy;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rd.PageIndicatorView;
import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.dialog.DatePickerFragment;
import com.shindygo.shindy.dialog.TimePicker;
import com.shindygo.shindy.model.CreateEventCallBack;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.utils.PageFragment;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class HostNewFragment extends Fragment {
    ViewPager pager;
    PagerAdapter pagerAdapter;
    List<Bitmap> bitmaps;
    static int PAGE_COUNT = 5;
    private static final int RESULT_LOAD_IMG = 142;
    @BindView(R.id.tv_time_start)
    TextView tvTimeStart;
    Unbinder unbinder;
    int day, month, year;
    @BindView(R.id.tv_endtime)
    TextView tvEndtime;
    Event event;
    @BindView(R.id.create)
    Button create;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_details)
    EditText etDetails;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.et_co_hosts)
    EditText etCoHosts;
    Date startDate, endDate;
    @BindView(R.id.location)
    EditText location;

    @BindView(R.id.bt_add)
    Button btAdd;
    List<String> images64 = new ArrayList<>();
    Uri linkIm;
    String zipcode;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    private double lon, lat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.host_new_event_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        ivLocation.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              Intent i = new Intent(getContext(), MapsActivity.class);
                                              startActivityForResult(i, 1);
                                          }
                                      }
        );
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    event = new Event();
                    event.setEventname(etName.getText().toString().trim());
                    event.setLat(String.valueOf(lat));
                    event.setLong(String.valueOf(lon));
                    event.setFulladdress(location.getText().toString());
                    event.setZipcode(zipcode == null ? "00000" : zipcode);
                    event.setDescription(etDetails.getText().toString().trim());
                    event.setCustomPrice(etPrice.getText().toString().trim());
                    event.setRating("0");
                    event.setRepresentative(etCoHosts.getText().toString().trim());
                    if (endDate != null && startDate != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                        event.setSchedEnddate(date.format(endDate));
                        event.setSchedStartdate(date.format(startDate));
                        SimpleDateFormat time = new SimpleDateFormat("h:mm a");
                        event.setStartTime(time.format(startDate));
                        event.setEndTime(time.format(endDate));

                        if (event.getEventname().length() < 4 || event.getEndTime() == null || event.getStartTime() == null) {
                            Toast.makeText(getContext(), "Emty field", Toast.LENGTH_SHORT).show();
                        } else
                            new EventController(getContext()).createEvent(event, new Callback<CreateEventCallBack>() {
                                @Override
                                public void onResponse(Call<CreateEventCallBack> call, Response<CreateEventCallBack> response) {
                                    Log.v("response", response.message());
                                    CreateEventCallBack callBack = response.body();
                                    if (images64.size() == 0) {
                                        ((MainActivity) getActivity()).selectMenu(0);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    for (final String url : images64) {
                                        new EventController(getContext()).pushImage(callBack.getEventid(), url, new Callback<Object>() {
                                            @Override
                                            public void onResponse(Call<Object> call, Response<Object> response) {
                                                if (url == images64.get(images64.size() - 1)) {
                                                    ((MainActivity) getActivity()).selectMenu(0);
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Object> call, Throwable t) {

                                            }
                                        });
                                    }


                                }

                                @Override
                                public void onFailure(Call<CreateEventCallBack> call, Throwable t) {
                                    t.printStackTrace();
                                    Toast.makeText(getContext(), "Connection error", Toast.LENGTH_SHORT).show();
                                }
                            });
                    } else {
                        Toast.makeText(getContext(), "Select time", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Need more data", Toast.LENGTH_SHORT).show();

                }


            }
        });
        tvTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeText(true);
            }
        });
        tvEndtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeText(false);
            }
        });



                    final PageIndicatorView pageIndicatorView = view.findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setCount(PAGE_COUNT); // specify total count of indicators
                    pager =view.findViewById(R.id.pager);
                    pagerAdapter =new

                    MyFragmentPagerAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()

                    {

                        @Override
                        public void onPageSelected ( int position){
                        pageIndicatorView.setSelected(position);
//                top.setText(text[position]);
//                if(position==0||position==PAGE_COUNT-1)
//                {
//                    top.setBackgroundResource(R.mipmap.loving_bg);
//                    top.setTextColor(getResources().getColor(R.color.white));
//                }
//                else
//                {
//                    top.setBackgroundResource(R.color.white);
//                    top.setTextColor(getResources().getColor(R.color.blue));
//                }

                    }

                        @Override
                        public void onPageScrolled ( int position, float positionOffset,
                        int positionOffsetPixels){
                    }

                        @Override
                        public void onPageScrollStateChanged ( int state){
                    }
                    });

        return view;
                }

        @Override
        public void onActivityResult ( int requestCode, int resultCode, Intent data){


            switch (requestCode) {
                case 1: {
                    if (resultCode == RESULT_OK) {
                        lat = data.getDoubleExtra("result_lat", 0);
                        lon = data.getDoubleExtra("result_lon", 0);
                        zipcode = data.getStringExtra("result_zipcode");
                        if (zipcode == null)
                            zipcode = "0000";

                        location.setText(data.getStringExtra("result_text"));
                    }
                    if (resultCode == Activity.RESULT_CANCELED) {
                        //Write your code if there's no result
                    }
                    break;
                }
                case RESULT_LOAD_IMG:

                    if (resultCode == RESULT_OK) {
                        try {
                            final Uri imageUri = data.getData();
                            linkIm = imageUri;
                            final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
                            images64.add(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
                            pagerAdapter.notifyDataSetChanged();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }

        void setTimeText ( final boolean isStartTime){
            DatePickerFragment newFragment = new DatePickerFragment();
            newFragment.setListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    day = datePicker.getDayOfMonth();
                    month = datePicker.getMonth();
                    year = datePicker.getYear();
                    TimePicker newFragment = new TimePicker();
                    newFragment.setListener(new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, month, day, i, i1);
                            Date date = calendar.getTime();
                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd/yyyy | h:mm a");
                            if (isStartTime) {
                                tvTimeStart.setText(simpleDateFormat.format(date));
                                startDate = date;
                            } else {
                                tvEndtime.setText(simpleDateFormat.format(date));
                                endDate = date;
                            }
                        }
                    });
                    newFragment.show(getChildFragmentManager(), "timePicker");
                }
            });
            newFragment.show(getChildFragmentManager(), "datePicker");

        }



        @OnClick(R.id.back)
        public void onViewClicked () {
            ((MainActivity) getActivity()).openedDrawer();
        }

        private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

            public MyFragmentPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                return PageFragment.newInstance(position, images64.get(position));
            }

            @Override
            public int getCount() {
                return images64.size();
            }

        }
    }



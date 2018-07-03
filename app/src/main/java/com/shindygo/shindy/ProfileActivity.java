package com.shindygo.shindy;


import android.animation.ObjectAnimator;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import com.shindygo.shindy.adapter.UserAvailabilityAdapter;
import com.shindygo.shindy.dialog.TimePicker;
import com.shindygo.shindy.model.User;
import com.shindygo.shindy.model.UserAvailability;
import com.shindygo.shindy.utils.GlideImage;
import com.shindygo.shindy.utils.TextUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class ProfileActivity extends Fragment {

    private static final String TAG = ProfileActivity.class.getSimpleName();

    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.tv_name_age)
    TextView tvNameAge;
    @BindView(R.id.et_zip)
    EditText etZip;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.bt_save)
    Button btSave;
    @BindView(R.id.tv_preview)
    TextView tvPreview;
    @BindView(R.id.et_about)
    EditText etAbout;

    @BindView(R.id.sp_distance)
    Spinner spDistance;
    @BindView(R.id.sp_religion)
    Spinner spReligion;
    @BindView(R.id.sp_gender)
    Spinner gender;
    @BindView(R.id.sp_avaiba)
    Spinner spAvaiba;
    @BindView(R.id.btn_add_availability)
    Button btnAddAvailability;
    @BindView(R.id.rv_not_available)
    RecyclerView rvListNotAvailable;
    @BindView(R.id.sw_allow_anonym)
    Switch swAllowAnonymInvite;
    @BindView(R.id.sw_gender_show)
    Switch swShowMyGender;
    @BindView(R.id.sw_religion_show)
    Switch swShowReligion;
    @BindView(R.id.sp_religion_able_invite)
    Spinner spReligionAbleInvite;
    @BindView(R.id.sp_invite_other_same_gender_pref)
    Spinner spInviteSameGenderPref;


    @BindView(R.id.rl)
    RelativeLayout rl;
    /*@BindView(R.id.et_age)
    EditText etAge;*/



    @BindView(R.id.tv_left)
    TextView tvLeft;

    @BindView(R.id.sp_age)
    Spinner spAge;

    @BindView(R.id.exl_age)
    ExpandableLinearLayout exlAge;
    @BindView(R.id.exl_dist)
    ExpandableLinearLayout exlDist;
    @BindView(R.id.exl_gender)
    ExpandableLinearLayout exlGender;
    @BindView(R.id.exl_religion)
    ExpandableLinearLayout exlReligion;
    @BindView(R.id.exl_avail)
    ExpandableLinearLayout exlAvail;
    @BindView(R.id.rl_button_age)
    RelativeLayout rlAge;
    @BindView(R.id.rl_button_dist)
    RelativeLayout rlDist;
    @BindView(R.id.rl_button_gender)
    RelativeLayout rlGender;
    @BindView(R.id.rl_button_religion)
    RelativeLayout rlReligion;
    @BindView(R.id.rl_button_avail)
    RelativeLayout rlAvail;
    @BindView(R.id.button_age)
    RelativeLayout ageExpIndicator;
    @BindView(R.id.button_distance)
    RelativeLayout distExpIndicator;
    @BindView(R.id.button_gender)
    RelativeLayout genderExpIndicator;
    @BindView(R.id.button_religion)
    RelativeLayout relExpIndicator;
    @BindView(R.id.button_avail)
    RelativeLayout availExpIndicator;

    @BindView(R.id.logout)
    LinearLayout logout;
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    @BindView(R.id.tv_join_group)
    TextView tvJoinGroup;

    private PopupWindow mPopupWindow;
    private RelativeLayout mRelativeLayout;
    private Api api;
    User user;
    List<UserAvailability> userAvailabilities = new ArrayList<>();
    UserAvailabilityAdapter userAvailabilityAdapter ;


    public static final String BASE_URL = "http://shindygo.com/rest_webservices/restapicontroller/";

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().contains(myString) || spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_profile, container, false);
        ButterKnife.bind(this, view);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        api = new Api(getContext());

        api.getUserByID(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    Log.e(TAG, response.toString());
                    user = response.body();
                    //Glide.with(getApplicationContext()).load(user.getPhoto()).into(imageView2);
                    tvNameAge.setText(user.getFullname());
                    etZip.setText(user.getZipcode());
                    etAbout.setText(user.getAbout());
                    swAllowAnonymInvite.setChecked(user.allowAnonymousInvite());
                  //  etAge.setText(user.getAgePref());
                    spAge.setSelection(getIndex(spAge, user.getAgePref()));
                    spDistance.setSelection(Integer.parseInt(user.getDistance()));
                    spReligion.setSelection(Integer.parseInt(user.getReligion()));
                    swShowReligion.setChecked(user.showMyReligion());
                    spReligionAbleInvite.setSelection(Integer.parseInt(user.getInviteMeOtherReligion()));
                    gender.setSelection(Integer.parseInt(user.getGenderPref()));
                    swShowMyGender.setChecked(user.showMyGender());
                    spAvaiba.setSelection(getIndex(spAvaiba, user.getAvailability()));
                    spInviteSameGenderPref.setSelection(Integer.parseInt(user.getInviteMeOtherShareGenderPref()));

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("124231", t.getMessage());
            }
        });
        api.fetchUserNotAvailableTime(User.getCurrentUserId(), new Callback<List<UserAvailability>>() {
            @Override
            public void onResponse(Call<List<UserAvailability>> call, Response<List<UserAvailability>> response) {
             //   Log.d(TAG, "onResponse");
              //  Log.d(TAG, "onResponse " + response.toString());
                try {
                    if (response.message().equalsIgnoreCase("ok")) {

                        userAvailabilities = response.body();
                       // Log.d(TAG, "onResponseuser Availabilities size " + userAvailabilities.size());

                        if(userAvailabilities !=null){
                            setUserAvailabilities(userAvailabilities);
                        }

                    }

                 //   Log.d(TAG, "onResponse " + userAvailabilities.size());


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Call<List<UserAvailability>> call, Throwable t) {
                Log.d(TAG, "onFailure List UserAvailability");
                Log.d(TAG, "onFailure "+ t.getLocalizedMessage());

            }
        });

        View.OnClickListener onClickLogout = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickLogout();

            }
        };
        logout.setOnClickListener(onClickLogout);
        tvLogout.setOnClickListener(onClickLogout);
        final SharedPreferences sharedPref = getContext().getSharedPreferences("set", Context.MODE_PRIVATE);
        final String url = sharedPref.getString("url", "");
        Glide.with(getContext()).load(url).into(imageView2);
   /*     tvNameAge.setText(sharedPref.getString("name", ""));
        etZip.setText(sharedPref.getString("zip", ""));
        etAbout.setText(sharedPref.getString("about", "")); //TODO убрать шаред преф и настроять норм отображение спиннеров
        //spAge.setSelection(sharedPref.getInt("spAge", 0));
        spDistance.setSelection(sharedPref.getInt("spDistance", 0));
        spReligion.setSelection(sharedPref.getInt("spReligion", 0));
        gender.setSelection(sharedPref.getInt("spGender", 0));
        spAvaiba.setSelection(sharedPref.getInt("spAva", 0));*/
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                attemptSave();
            }
        });

        etAbout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvLeft.setText(131-editable.length() + " character left");
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("about", editable.toString());
                editor.apply();
            }
        });
        etZip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("zip", editable.toString());
                editor.apply();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).openedDrawer();
            }
        });
        tvPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.profile_popup, null);
                ImageView imageView = customView.findViewById(R.id.imageView2);
                GlideImage.load(getContext(), url,imageView);
                //Glide.with(getContext()).load(url).into(imageView);
                TextView name = customView.findViewById(R.id.tv_name);
                name.setText(sharedPref.getString("name", ""));
                TextView about = customView.findViewById(R.id.tv_desc);
                about.setText(user.getAbout());
                TextView city = customView.findViewById(R.id.tv_city);
                city.setText(user.getAddress());
                TextView pref = customView.findViewById(R.id.tv_pref);
                pref.setText("Preference: "+gender.getItemAtPosition(Integer.parseInt(user.getGenderPref())));
                TextView religion = customView.findViewById(R.id.tv_religon);
                religion.setText(spReligion.getItemAtPosition(Integer.parseInt(user.getReligion())).toString());


                LinearLayout menubar = customView.findViewById(R.id.menubar);
                menubar.setVisibility(View.GONE);
                mPopupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                if (Build.VERSION.SDK_INT >= 21) {
                    mPopupWindow.setElevation(5.0f);
                }

                // Get a reference for the custom view close button
                ImageView closeButton = customView.findViewById(R.id.back);
                mRelativeLayout = view.findViewById(R.id.rl);
                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });

                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER, 0, 0);


            }
        });



        exlAge.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                createRotateAnimator(ageExpIndicator, 0f, 180f).start();

            }

            @Override
            public void onPreClose() {
                createRotateAnimator(ageExpIndicator, 180f, 0f).start();

            }
        });
        exlDist.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                createRotateAnimator(distExpIndicator, 0f, 180f).start();

            }

            @Override
            public void onPreClose() {
                createRotateAnimator(distExpIndicator, 180f, 0f).start();

            }
        });
        exlGender.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                createRotateAnimator(genderExpIndicator, 0f, 180f).start();

            }

            @Override
            public void onPreClose() {
                createRotateAnimator(genderExpIndicator, 180f, 0f).start();

            }
        });
        exlReligion.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                createRotateAnimator(relExpIndicator, 0f, 180f).start();

            }

            @Override
            public void onPreClose() {
                createRotateAnimator(relExpIndicator, 180f, 0f).start();

            }
        });
        exlAvail.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                createRotateAnimator(availExpIndicator, 0f, 180f).start();

            }

            @Override
            public void onPreClose() {
                createRotateAnimator(availExpIndicator, 180f, 0f).start();

            }
        });
        final View.OnClickListener onClickExpToggle = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id){
                    case R.id.rl_button_age:{
                        onClickExpToggleButton(exlAge);
                        break;
                    }
                    case R.id.rl_button_dist:{
                        onClickExpToggleButton(exlDist);
                        break;
                    }
                    case R.id.rl_button_gender:{
                        onClickExpToggleButton(exlGender);
                        break;
                    }
                    case R.id.rl_button_religion:{
                        onClickExpToggleButton(exlReligion);
                        break;
                    }
                    case R.id.rl_button_avail:{
                        onClickExpToggleButton(exlAvail);
                        break;
                    }
                    default:
                        break;
                }
            }
        };

        //holder.buttonLayout.setRotation(expandState.get(position) ? 180f : 0f);
        rlAge.setOnClickListener(onClickExpToggle);
        rlDist.setOnClickListener(onClickExpToggle);
        rlGender.setOnClickListener(onClickExpToggle);
        rlReligion.setOnClickListener(onClickExpToggle);
        rlAvail.setOnClickListener(onClickExpToggle);

        //exlAge.setExpanded(expandState.get(exlAge));

        btnAddAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAvailabilityDialog();
            }
        });

        tvJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        return view;
    }

    private void setUserAvailabilities(List<UserAvailability> userAvailabilities) {
        userAvailabilityAdapter = new UserAvailabilityAdapter(userAvailabilities);
        rvListNotAvailable.setLayoutManager(new LinearLayoutManager(getContext()));
        rvListNotAvailable.setAdapter(userAvailabilityAdapter);
     //   userAvailabilityAdapter.notifyDataSetChanged();
        exlAvail.initLayout(); // Recalculate size of children
        userAvailabilityAdapter.notifyItemInserted(userAvailabilities.size() - 1);


    }

    private void showAddAvailabilityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.alert_add_not_available, null, false);
        Button close = view.findViewById(R.id.close);
        Button btnAdd = view.findViewById(R.id.btn_add);
        final TextInputEditText etTimeFrom = view.findViewById(R.id.et_time_from);
        final TextInputEditText etTimeTo = view.findViewById(R.id.et_time_to);
        TextInputLayout tlFrom = view.findViewById(R.id.til_time_from);
        TextInputLayout tlTo = view.findViewById(R.id.til_time_to);
        final Spinner spDays = view.findViewById(R.id.sp_days);
        etTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker newFragment = new TimePicker();
                newFragment.setListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH), i, i1);
                        Date date = calendar.getTime();
                      //  final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd/yyyy | h:mm a");

                        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
                        etTimeFrom.setText(simpleDateFormat.format(date));
                    }
                });
                newFragment.show(getChildFragmentManager(), "timePicker");

            }
        });

        etTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker newFragment = new TimePicker();
                newFragment.setListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH), i, i1);
                        Date date = calendar.getTime();
                        //  final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd/yyyy | h:mm a");

                        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TextUtils.SDF_4);
                        etTimeTo.setText(simpleDateFormat.format(date));
                    }
                });
                newFragment.show(getChildFragmentManager(), "timePicker");

            }
        });

        builder.setView(view);
        final AlertDialog dialog = builder.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String days = String.valueOf(spDays.getSelectedItemPosition());
                String timeFrom = etTimeFrom.getText().toString();
                String timeTo = etTimeTo.getText().toString();
                try {
                    if(days.equals("0")) {
                        showAlert(getString(R.string.please_select_day));
                        return;
                    }
                    if(timeFrom.equals("0") || timeTo.equals("") ) {
                        showAlert(getString(R.string.please_select_time));
                        return;
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                SimpleDateFormat sdf1 =new SimpleDateFormat(TextUtils.SDF_4);
                SimpleDateFormat sdf2 =new SimpleDateFormat(TextUtils.SDF_6);


                UserAvailability availability = new UserAvailability();
                availability.setFbid(User.getCurrentUserId());
                availability.setDay(days);
                availability.setTimezone(sdf1.getTimeZone().getDisplayName(false, TimeZone.SHORT));
                try {
                    availability.setStartTime(sdf2.format(sdf1.parse(timeFrom)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    availability.setEndTime(sdf2.format(sdf1.parse(timeTo)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                addAvailability(availability);

                dialog.dismiss();
            }
        });

    }

    private void addAvailability(final UserAvailability availability) {
        Api.getInstance().notAvailableTime(availability, new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d(TAG,"onResponse");
                    Log.d(TAG,"onResponse " + response.toString());
                    try {
                        Log.d(TAG,"onResponse " + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(response.message().equalsIgnoreCase("ok")){
                        if(userAvailabilityAdapter!=null){
                            userAvailabilities.add(availability);
                           // userAvailabilityAdapter.notifyDataSetChanged();
                            userAvailabilityAdapter.notifyItemInserted(userAvailabilities.size() - 1);

                            exlAvail.initLayout(); // Recalculate size of children

                        }else{
                            userAvailabilities = new ArrayList<UserAvailability>();
                            userAvailabilities.add(availability);
                            setUserAvailabilities(userAvailabilities);
                        }

                        exlAvail.expand();
                    }

                }catch (NullPointerException e){
                    Log.d(TAG,"onResponse " + "null views");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    void showAlert(String message){
        AlertDialog.Builder builder =  new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private void attemptSave() {

        SharedPreferences sharedPref = getContext().getSharedPreferences("set", Context.MODE_PRIVATE);
        User user = new User(sharedPref.getString("fbid", ""), sharedPref.getString("name", ""), sharedPref.getString("email", ""));
        if (etZip.getText().toString().length() > 0)
            user.setZipcode(etZip.getText().toString());
        if (etAbout.getText().toString().length() > 0)
            user.setAbout(etAbout.getText().toString());
        user.setAgePref(spAge.getSelectedItem().toString());
        user.setDistance(String.valueOf(spDistance.getSelectedItemPosition()));
        user.setReligion("" + spReligion.getSelectedItemPosition());
        user.setGenderPref("" + gender.getSelectedItemPosition());
        user.setAvailability(spAvaiba.getSelectedItem().toString());
        user.setAllowAnonymousInvite(swAllowAnonymInvite.isChecked()? "1":"0");
        user.setShowMyGenderPref(swShowMyGender.isChecked()? "1":"0");
        user.setShowMyReligion(swShowReligion.isChecked()? "1":"0");
        user.setInviteMeOtherReligion(String.valueOf(spReligionAbleInvite.getSelectedItemPosition()));

        user.setInviteMeOtherShareGenderPref(String.valueOf(spInviteSameGenderPref.getSelectedItemPosition()));

        SharedPreferences.Editor editor = sharedPref.edit();
        //         editor.putInt("prefAge", Integer.parseInt(etAge.getText().toString()));
        editor.putInt("spDistance", spDistance.getSelectedItemPosition());
        editor.putInt("spReligion", spReligion.getSelectedItemPosition());
        editor.putInt("spGender", gender.getSelectedItemPosition());
        editor.putInt("spAva", spAvaiba.getSelectedItemPosition());

        editor.apply();

        api.updateUser(user, new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("onResponse", response.toString());
                getFragmentManager().beginTransaction().remove(ProfileActivity.this).commit();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("onFailure", call.request().body().toString());
                Log.e("onFailure", t.getMessage());

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void OnClickLogout() {
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        if (getActivity()!=null)
        getActivity().finish();
    }



    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    private void onClickExpToggleButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }
}

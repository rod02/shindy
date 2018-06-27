package com.shindygo.shindy;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import com.shindygo.shindy.model.User;
import com.shindygo.shindy.utils.GlideImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class ProfileActivity extends Fragment {

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
    @BindView(R.id.sw_allow_anonym)
    Switch swAllowAnonymInvite;
    @BindView(R.id.sw_gender_show)
    Switch swShowMyGender;
    @BindView(R.id.sw_religion_show)
    Switch swInviteOther;
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

    private PopupWindow mPopupWindow;
    private RelativeLayout mRelativeLayout;
    private Api api;
    User user;
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
                    Log.e("124231", response.toString());
                    user = response.body();
                    //Glide.with(getApplicationContext()).load(user.getPhoto()).into(imageView2);
                    tvNameAge.setText(user.getFullname());
                    etZip.setText(user.getZipcode());
                    etAbout.setText(user.getAbout());
                  //  etAge.setText(user.getAgePref());
                    spAge.setSelection(getIndex(spAge, user.getAgePref()));
                    spDistance.setSelection(Integer.parseInt(user.getDistance()));
                    spReligion.setSelection(Integer.parseInt(user.getReligion()));
                    gender.setSelection(Integer.parseInt(user.getGenderPref()));
                    spAvaiba.setSelection(getIndex(spAvaiba, user.getAvailability()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("124231", t.getMessage());
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
                SharedPreferences sharedPref = getContext().getSharedPreferences("set", Context.MODE_PRIVATE);
                User user = new User(sharedPref.getString("fbid", ""), sharedPref.getString("name", ""), sharedPref.getString("email", ""));
                if (etZip.getText().toString().length() > 0)
                    user.setZipcode(etZip.getText().toString());
                if (etAbout.getText().toString().length() > 0)
                    user.setAbout(etAbout.getText().toString());
                user.setAgePref((String) spAge.getSelectedItem());
                user.setDistance(String.valueOf(spDistance.getSelectedItemPosition()));
                user.setReligion("" + spReligion.getSelectedItemPosition());
                user.setGenderPref("" + gender.getSelectedItemPosition());
                user.setAvailability(spAvaiba.getSelectedItem().toString());
                user.setAllowAnonymousInvite(swAllowAnonymInvite.isChecked()? "1":"0");
                user.setShowMyGenderPref(swShowMyGender.isChecked()? "1":"0");
                user.setInviteMeOtherShareGenderPref(swInviteOther.isChecked()? "1":"0");

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
                        Log.e("124231", "asdgdsg");
                        getFragmentManager().beginTransaction().remove(ProfileActivity.this).commit();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.e("124231", "asdgdsg");
                    }
                });
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
                GlideImage.load(url,imageView);
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
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
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
        return view;
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

package com.shindygo.shindy;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.rahimlis.badgedtablayout.BadgedTabLayout;
import com.shindygo.shindy.fragment.MessagesFragment;
import com.shindygo.shindy.main.MyShindigsFragment;
import com.shindygo.shindy.main.NewUsersFragment;
import com.shindygo.shindy.main.UsersFragment;
import com.shindygo.shindy.model.User;
import com.shindygo.shindy.utils.ConnectIntentService;
import com.shindygo.shindy.utils.FontUtils;
import com.shindygo.shindy.utils.GlideImage;
import com.shindygo.shindy.utils.MySharedPref;

import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements LikedEventFragment.OnFragmentInteractionListener, BlockedEventFragment.OnFragmentInteractionListener,
        EventReviewFragment.OnFragmentInteractionListener,
        ReviewDetailEventFragment.OnFragmentInteractionListener,
        MessagesFragment.OnFragmentInteractionListener,
        EventFeedbackActivity.OnFragmentInteractionListener,  NavigationView.OnNavigationItemSelectedListener, MyShindigsFragment.OnFragmentInteractionListener, UsersFragment.OnFragmentInteractionListener, NewUsersFragment.OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();


    private long lastPressed = Calendar.getInstance().getTimeInMillis();
    private final long BACK_PRESSED_EXIT_THRESHOLD = 3000; //in millis



    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    BadgedTabLayout tabs;
    @BindView(R.id.container)
    ViewPager container;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    ImageView imageViewMenu;
    TextView menuName;
    BroadcastReceiver broadcastReceiver;



    public  boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connection = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        if(!connection)
            showAlert();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
    public void showAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Your internet connection is gone!")
                .setMessage("Check, and press OK!")
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                isConnected(MainActivity.this);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void hideKeyboard() {
        try {

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getParent().getCurrentFocus().getWindowToken(),0);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setSupportActionBar(toolbar);
        Profile.getCurrentProfile();
        // Api.initialized(getApplicationContext().getApplicationContext());
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ComponentName comp = new ComponentName(context.getPackageName(),
                        ConnectIntentService.class.getName());
                isConnected(context);
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        try {
            registerReceiver(broadcastReceiver,intentFilter);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (Profile.getCurrentProfile()==null){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        imageViewMenu = navView.getHeaderView(0).findViewById(R.id.imageView);
        navView.setNavigationItemSelectedListener(this);
        navView.getHeaderView(0).findViewById(R.id.imageView);

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                if (slideOffset > 0){
                    shutDownKeyBoard();
                }
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        final SharedPreferences sharedPref = getSharedPreferences("set", Context.MODE_PRIVATE);
        final String url = sharedPref.getString("url", "");
        //Glide.with(getApplicationContext()).load(url).into(imageViewMenu);
        try {
            GlideImage.load(this, url, imageViewMenu);

        }catch (Exception e){
            e.printStackTrace();
        }

        menuName = navView.getHeaderView(0).findViewById(R.id.menuName);
        menuName.setText(sharedPref.getString("name",""));
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabs.setupWithViewPager(mViewPager);
        //
        tabs.setBadgeText(2, String.valueOf(MySharedPref.getNewUsersCount()));

//        tabLayout.setTabFont(ResourcesCompat.getFont(this, R.font.trench));
//        tabs.setBadgeTruncateAt(TextUtils.TruncateAt.MIDDLE);
        FontUtils.setFont(navView, FontUtils.Roboto_Thin);
        FontUtils.setFont(drawer, FontUtils.Roboto_Light);
        FontUtils.setFont(title, FontUtils.Be_Bright);
        if(!MySharedPref.isProfileSetupDone()){
            openProfileSetup();
        }
    }


    private void shutDownKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    void selectMenu(int i ) {
        onNavigationItemSelected(navView.getMenu().getItem(i));
        navView.getMenu().getItem(i).setChecked(true);
    }

    void openedDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiver);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private Fragment getPagerFragment(int position) {
        return getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + position);

    }

    /*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fm = getSupportFragmentManager();
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_profile_preferences: {
                openProfileSetup();
                break;
            }
            case R.id.nav_logout: {
                LoginManager.getInstance().logOut();
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);

                MySharedPref.clear();
                MySharedPref.clearUser();
                finish();
                break;
            }
            case R.id.nav_users_mgmt: {

                Fragment fragment = new UsersMGMTActivity();
                fm.beginTransaction()
                        .replace(R.id.frame, fragment)
                        .addToBackStack("my_fragment")
                        .commit();
                break;
            }
            case R.id.nav_shindigs: {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                break;
            }
            case R.id.nav_event_mgmt: {
                Fragment fragment1 = new EventFeedbackActivity();
                fm.beginTransaction()
                        .replace(R.id.frame, fragment1)
                        .addToBackStack("my_fragment")
                        .commit();
                break;
            }
            case R.id.nav_host_new_event: {

                Fragment fragment1 = new EventActivity();
                fm.beginTransaction()
                        .replace(R.id.frame, fragment1)
                        .addToBackStack("host_new")
                        .commit();
                break;
            }
            case R.id.nav_events: {

                Fragment fragment1 = new EventsListFragment();
                fm.beginTransaction()
                        .replace(R.id.frame, fragment1)
                        .addToBackStack("event_list")
                        .commit();
                break;
            }
            case R.id.nav_messages: {
//                Fragment fragment=new ChatMenuFragment();
//                fm.beginTransaction()
//                        .replace(R.id.frame,fragment)
//                        .commit();


            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void openProfileSetup() {

        Fragment fragment = new ProfileActivity();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack("my_fragment")
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return MyShindigsFragment.newInstance(String.valueOf(position), "1");
                case 1:
                    return UsersFragment.newInstance(String.valueOf(position), "2");
                case 2:
                    return NewUsersFragment.newInstance(String.valueOf(position), "3");
                default:
                    return MyShindigsFragment.newInstance(String.valueOf(position), "1");
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.my_shindigs);
                case 1:
                    return getString(R.string.users);
                case 2:
                    return getString(R.string.new_users);
            }
            return null;
        }
    }


    public void setTabBadgeText(int index, String text){
        tabs.setBadgeText(index, text);
        //Log.v(TAG, "setTabBadgeText; "+text);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

                getSupportFragmentManager().popBackStack();
                navView.getMenu().getItem(0).setChecked(true);

            }
            else{
                try {
                    Fragment fragment = getPagerFragment(tabs.getSelectedTabPosition());
                    if(fragment instanceof NewUsersFragment ){
                        ((NewUsersFragment)fragment).mPopupWindow.dismiss();
                        ((NewUsersFragment)fragment).mPopupWindow = null;

                    }else if(fragment instanceof UsersFragment ) {
                        ((UsersFragment) fragment).mPopupWindow.dismiss();
                        ((UsersFragment) fragment).mPopupWindow=null;
                    }else
                        promptExit();
                }catch (Exception e){
                    e.printStackTrace();
                    promptExit();
                }

            }
        }

    }


    void promptExit(){

        long now = Calendar.getInstance().getTimeInMillis();
        if(now - lastPressed < BACK_PRESSED_EXIT_THRESHOLD){
            super.onBackPressed();
            return;
        }
        lastPressed = now;
        Toast.makeText(MainActivity.this, R.string.prompt_double_back_exit, Toast.LENGTH_LONG).show();
    }

    void closeDrawer(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}

package com.shindygo.shindy;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.twitter.sdk.android.core.Twitter;

public class App extends Application {
    private static Context mContext;


    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Api.initialized(getApplicationContext());
        Twitter.initialize(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());

    }
}

package com.shindygo.shindy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.shindygo.shindy.Api;

public class MySharedPref {
    private static final String SHARED_PREF = "my_shared_pref";
    private static final String NEW_USERS_COUNT = "new_users_count";


    private static  SharedPreferences getPref(){
        return  Api.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

    }
    private static  SharedPreferences.Editor getEditor(){
        return  getPref().edit();

    }


    public static  int getNewUsersCount(){

        return  getPref().getInt(NEW_USERS_COUNT, 0);


    }
    public static  void setNewUsersCount(int count){
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(NEW_USERS_COUNT, count);
        editor.apply();


    }


}
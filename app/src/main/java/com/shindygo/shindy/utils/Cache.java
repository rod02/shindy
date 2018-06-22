package com.shindygo.shindy.utils;

import android.util.Log;


import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Cache {
    private static HashMap <String, Event> myAttendingEventsList = new HashMap<String, Event>();

    private static HashMap <String, Event> myInvitedEventsList = new HashMap<String, Event>();

    private static HashMap <String, User> usersList;
    private static List<User> newUsers;

    public static List<User> getNewUsers() {
        return newUsers;
    }

    public static void setNewUsers(List<User> newUsers) {
        Cache.newUsers = newUsers;
    }

    public static HashMap<String, Event> getMyAttendingEventsList() {
        return myAttendingEventsList;
    }

    public static void setMyAttendingEventsList(HashMap<String, Event> myAttendingEventsList) {
        Cache.myAttendingEventsList = myAttendingEventsList;
    }

    public static HashMap<String, Event> getMyInvitedEventsList() {
        return myInvitedEventsList;
    }

    public static void setMyInvitedEventsList(HashMap<String, Event> myInvitedEventsList) {
        Cache.myInvitedEventsList = myInvitedEventsList;
    }

    public static HashMap<String, User> getUsersList() {
        return usersList;
    }

    public static void setUsersList(HashMap<String, User> usersList) {
        Cache.usersList = usersList;
    }

    public static void setMyAttendingEventsList(List<Event> eventsList) {
        if(eventsList==null)return;
        for (int i = 0; i < eventsList.size() ; i++) {
            try {
                Event event = eventsList.get(i);
                myAttendingEventsList.put(event.getEventid(),event);
            }catch (NullPointerException e){
                Log.d("Cache", "null events");

            }
        }


    }
    public static void setMyInvitedEventsList(List<Event> eventsList) {
        if(eventsList==null)return;
        for (int i = 0; i < eventsList.size() ; i++) {
            try {
                Event event = eventsList.get(i);
                myInvitedEventsList.put(event.getEventid(),event);
            }catch (NullPointerException e){
                Log.d("Cache vitedEventsList ", "null events");

            }
        }

    }

    public static List<Event> getEventsAsList(HashMap<String, Event> map){
        List<Event> list = new ArrayList<>();
        Event[] n = map.values().toArray(new Event[map.values().size()]);
        list = Arrays.asList(n);
        return list;
    }

}

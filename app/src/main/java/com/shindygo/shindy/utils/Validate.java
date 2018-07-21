package com.shindygo.shindy.utils;

public class Validate {
    public static String string(String s) {
        try {
            if(s==null || s.equals("")) return "0";
            return s;
        }catch (NullPointerException e){
            return "0";
        }

    }
    public static String nullString(String s) {
        if(s==null) return "";
        return s;
    }
}

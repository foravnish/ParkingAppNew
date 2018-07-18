package com.newwebinfotech.rishabh.parkingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rimus-PC on 6/29/2017.
 */

public class Appsetting {
    public static final String REPORT_SERVER_ERROR = "";
    SharedPreferences preferences;
    public static final String FCMDEVICEID = "FCMDEVICEID";
    public static final String MYPRIMARYPHONENO = "MYPRIMARYPHONENO";
    public static final String MYNAME = "MYNAME";
    private static String PREF_NAME = "PARKING";

    public Appsetting(Context conty) {
        preferences = conty.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

    }

    public void saveString(String key, String value)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key)
    {
        String result="";
        result = preferences.getString(key, "");
        return result;
    }


}

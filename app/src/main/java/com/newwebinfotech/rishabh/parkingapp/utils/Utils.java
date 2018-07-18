package com.newwebinfotech.rishabh.parkingapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.newwebinfotech.rishabh.parkingapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rimus-PC on 6/30/2017.
 */

public class Utils {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;


    public static void showToast(Context context, String msg) {
        Toast toast= Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    public static String LoadPref(Context context, String key) {
        return LoadPref(context, key, "");
    }

    public static String LoadPref(Context context, String key, String defaultValue) {
        if (context != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String data = sharedPreferences.getString(key, defaultValue);
            return data;

        }
        return "";
    }

    public static void SavePref(Context context, String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (context != null) {
            editor.putInt(key, value);
            editor.apply();
        }
    }

    public static void SavePref(Context context, String key, String name) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (context != null && name != null) {
            editor.putString(key, name);
            editor.apply();
        }
    }


    public static int LoadPrefInteger(Context context, String key) {
        return LoadPrefInteger(context, key, 0);
    }

    public static int LoadPrefInteger(Context context, String key, int defaultValue) {
        if (context != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            int data = sharedPreferences.getInt(key, defaultValue);
            return data;
        }
        return 0;
    }

    public static Bitmap getUserBitmapMarker(Context context, View view) {
        return createDrawableFromView(context, view);
    }
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    public static boolean Checkdate(String systemdate, String msgdate)
    {
        boolean b = false;
        String inputPattern = "yyyy-MM-dd HH:mm:ss";

        String outputPattern = "yyyy-MM-dd";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date1 = null;
        Date date2=null;
        String str_date1 = null;
        String str_date2=null;

        try {
            date1 = inputFormat.parse(systemdate);
            str_date1 = outputFormat.format(date1);
            date2 = inputFormat.parse(msgdate);
            str_date2 = outputFormat.format(date2);
            if (str_date1.compareTo(str_date2)== 0)
            {
                // System.out.println("date2 is Greater than m date1");
                b=true;

            }

            // Log.i("mini", "Converted Date Today:" + str);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return b;
    }

    public static String getStaticCurrentIPAddress() {

        return "http://services";
    }

    public static double parseDouble(String res) {
        try {
            return Double.parseDouble(res);
        }catch (NumberFormatException | NullPointerException exc){
            return 0;
        }
    }

}

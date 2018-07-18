package com.newwebinfotech.rishabh.parkingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;


/**
 * Created by Abhijit on 13-Dec-16.
 */

public class AppUser {
    public static final String COMMON = "pI";
    private static final String MY_LATTITUDE = "myLat" + COMMON;
    private static final String MY_LONGITUDE = "myLng" + COMMON;
    private static final String MAP_ZOOM_SCALE = "mapScale" + COMMON;
    private static final String MAP_RADIUS_SCALE = "mapRadiusScale" + COMMON;
    private static final String MAP_APP_VIEW = "mapAppView" + COMMON;
    private static final String LAST_UPDATE_TIME = "luptime" + COMMON;
    private static final String LAST_UPDATE_DATE = "lupdate" + COMMON;
    private static final String USER_IMAGE = "uImage" + COMMON;
    private static final String EMPLOYEE_USERID = "eUserId" + COMMON;
    private static final String COMPANY_USERID = "compId" + COMMON;
    private static final String APP_USER_NAME = "appUserName" + COMMON;
    private static final String EMPLOYEE_USER_COMPID = "eUserCompId"+COMMON;


    public static String LoadPref(Context context, String key) {
        if (context != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String data = sharedPreferences.getString(key, "");
            return data;
        }
        return "";
    }

    public static String getName(Context act) {
        return LoadPref(act, APP_USER_NAME);
    }

    public static void setName(Context act,String name) {
        Utils.SavePref(act,APP_USER_NAME,name);
    }

    public static String getImage(Context act) {
        return LoadPref(act, USER_IMAGE);
    }


    public static void setMyLatLng(Context act, Location location) {
        Utils.SavePref(act, MY_LATTITUDE, String.valueOf(location.getLatitude()));
        Utils.SavePref(act, MY_LONGITUDE, String.valueOf(location.getLongitude()));
    }

    public static Location getMyLocation(Context context) {
        String lattitude = LoadPref(context, MY_LATTITUDE);
        String longitude = LoadPref(context, MY_LONGITUDE);
        Location location = new Location("local");
        if (lattitude.equals("") && longitude.equals("")) {
            return null;
        } else {
            location.setLatitude(Double.parseDouble(lattitude));
            location.setLongitude(Double.parseDouble(longitude));
            return location;
        }
    }

    public static void setMapZoomProgress(Context context, int mapZoomProgress) {
        Utils.SavePref(context, MAP_ZOOM_SCALE, mapZoomProgress);
    }

    public static int getMapZoomProgress(Context context) {
        return Utils.LoadPrefInteger(context, MAP_ZOOM_SCALE);
    }

    public static void setMapRadiusProgress(Context context, int mapRadiusProgress) {
        Utils.SavePref(context, MAP_RADIUS_SCALE, mapRadiusProgress);
    }

    public static int getMapRadiusProgress(Context context, int defaultValue) {
        return Utils.LoadPrefInteger(context, MAP_RADIUS_SCALE, defaultValue);
    }

    public static void setAppViewType(Context context, int value) {
        Utils.SavePref(context, MAP_APP_VIEW, value);
    }


    public static void setLastLocationDate(Context context, String mLastUpdateDate) {
        Utils.SavePref(context, LAST_UPDATE_DATE, mLastUpdateDate);
    }

    public static String getLastLocationDate(Context context) {
        return Utils.LoadPref(context, LAST_UPDATE_DATE);
    }

    public static void setLastLocationTime(Context context, String mLastUpdateTime) {
        Utils.SavePref(context, LAST_UPDATE_TIME, mLastUpdateTime);
    }

    public static String getLastLocationTime(Context context) {
        return Utils.LoadPref(context, LAST_UPDATE_TIME);
    }

    public static void setImage(Context context, String s) {
        Utils.SavePref(context, USER_IMAGE, s);
    }


    public static void setUserId(Context context, String outputMsg) {
        Utils.SavePref(context, EMPLOYEE_USERID, outputMsg);
    }

    public static String getUserId(Context context) {
        return Utils.LoadPref(context, EMPLOYEE_USERID);
    }

    public static void setUserCompanyId(Context context, String outputMsg) {
        Utils.SavePref(context, EMPLOYEE_USER_COMPID, outputMsg);
    }

    public static String getUserCompanyId(Context context) {
        return Utils.LoadPref(context, EMPLOYEE_USER_COMPID);
    }

    public static void setCompanyId(Context context, String outputMsg) {
        Utils.SavePref(context, COMPANY_USERID, outputMsg);

    }

    public static String getCompanyId(Context context) {
        return Utils.LoadPref(context, COMPANY_USERID);
    }

}
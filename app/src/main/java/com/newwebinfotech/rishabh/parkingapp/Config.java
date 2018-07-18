package com.newwebinfotech.rishabh.parkingapp;

/**
 * Created by Rishabh on 13-05-2017.
 */

public class Config {

    public static final String PREF_FILE = "ParkingAppPref";
    public static final String PREF_KEY_PRIMARY_MOBILE = "mobile_no";
    public static final String PREF_KEY_SHARE_PRIMARY_MOBILE = "share_primary_number";
    public static final String PREF_KEY_TOKEN = "token";
    public static final String PREF_KEY_VICHEL = "Vichel";

    public static final String BASE_URL = "http://parkingeye.in/app/parking_eye/api/";
    public static final String REGISTER_USER = BASE_URL + "addUser.php";
    public static final String SEARCH_USER = BASE_URL + "searchUser.php";
    public static final String GET_PROFILE_INFO = BASE_URL + "getUser.php";
    public static final String VERSION_CHECK_URL = BASE_URL + "check_version.php";
    public static final String DELETE_USER = BASE_URL + "deleteUser.php";
    public static final String UPDATE_USER = BASE_URL + "updateUser.php";
    public static final String SHARE_PRIMARY_NUMBER = BASE_URL + "sharePrimaryMobile.php";
    public static final String LOGIN_USER = BASE_URL + "login.php";
    public static final String CHANGE_PASSWORD = BASE_URL + "changePassword.php";
    public static final String LOGOUT_URL = BASE_URL + "logout.php";
    public static final String FORGET_PASSWORD_OTP_URL = BASE_URL + "forgetPasswordOtp.php";
    public static final String UPDATE_FORGET_PASSWORD_OTP_URL = BASE_URL + "updateForgetPassword.php";

    public static final String KEY_VERSION = "version_name";
    public static final String APP_VERSION = "AV1.0.4";



//    public static final String parking = "http://parkingeye.in/app/parking_eye/api/";
    public static final String REPORT_SERVER_ERROR = BASE_URL;
    public static final String MULTI_USER_TRACKING = BASE_URL+"track_multiple_users.php";
    public static final String SAVE_USER_LOCATION = BASE_URL+"saveLoc.php";// primaryMobile, lat, lang
    public static final String TRACK_RESPONSE = BASE_URL+"track_response.php";// primaryMobile, lat, lang


    //public static final String KEY_USER_ID = "userid";

}

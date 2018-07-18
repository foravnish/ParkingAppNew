package com.newwebinfotech.rishabh.parkingapp.utils;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by Abhijit on 13-Dec-16.
 */

public class RequestBuilder {
    public static RequestBody Default(String userId) {
        return new FormBody.Builder()
                .addEncoded("userId", userId)
                .build();
    }


    public static RequestBody DefaultCompany(String companyId) {
        return new FormBody.Builder()
                .addEncoded("companyId", companyId)
                .build();
    }



    public static RequestBody FeedbackDetail(String userId, String feedback) {
        return new FormBody.Builder()
                .addEncoded("userId", userId)
                .addEncoded("feedback", feedback)
                .build();
    }

    public static RequestBody ErrorReport(String report) {
        return new FormBody.Builder()
                .addEncoded("reportLog", report)
                .build();
    }


    public static RequestBody SaveLocation(String primaryMobile, String lat, String lang) {
        return new FormBody.Builder()
                .addEncoded("primaryMobile", primaryMobile)
                .addEncoded("lat", lat)
                .addEncoded("lang", lang)
                .build();

    }

    public static RequestBody SetPhoneNo(String user_phoneno) {
        return new FormBody.Builder()
                .addEncoded("user_phoneno", user_phoneno)
                .build();
    }
}

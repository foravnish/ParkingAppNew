package com.newwebinfotech.rishabh.parkingapp.utils;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Abhijit on 13-Dec-16.
 */

public class ApiCall {
    public static final String IO_EXCEPTION = "IOException";

    //GET network request
    public static String GET(String url){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch (IOException e){
            return IO_EXCEPTION+" "+e.toString();
        }
    }

    //POST network request
    public static String POST( String url, RequestBody body){
        return POST(url,15,body);
    }
    public static String POST(String url,long timeOut, RequestBody body){
        String res = null;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .build();
        // socket timeout
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            res=response.body().string();
        } catch (IOException e) {
            return IO_EXCEPTION+" "+e.toString();
        }
        return res;
    }



    public static String upload(String url,RequestBody formBody){
        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder().url(url).post(formBody).build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            return IO_EXCEPTION+" "+e.toString();
        }
    }
}
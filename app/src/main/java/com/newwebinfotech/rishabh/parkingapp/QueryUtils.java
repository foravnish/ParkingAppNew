package com.newwebinfotech.rishabh.parkingapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rishabh on 13-05-2017.
 */

public class QueryUtils {

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("URL Building Error", "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();

                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("ERROR CODE", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("IO EXCEPTION", "Problem retrieving the JSON results.", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static String sendPostRequest(String requestURL, HashMap<String, String> postDataParams) {
        //creating a url
        URL url;

        StringBuilder sb = new StringBuilder();
        try {
            //initializing url
            url = new URL(requestURL);

            //creating connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //configuring connection properties
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //creating output stream
            OutputStream os = conn.getOutputStream();

            //writing parameters to the request
            //using a method getPostDataString
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                String response;

                //reading server response
                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }
            }
        } catch (Exception e) {
            Log.v("JSON Response", sb.toString());
            e.printStackTrace();
            return null;
        }
        Log.v("JSON Response", sb.toString());
        return sb.toString();
    }

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static String submitUserRegistration(HashMap<String, String> map) {

        String statusJSON;
        try {
            statusJSON = QueryUtils.sendPostRequest(Config.REGISTER_USER, map);
        } catch (Exception e) {
            return null;
        }


        return statusJSON;
    }

    public static String searchVehicleNo(HashMap<String, String> map) {
        String statusJSON;
        try {
            statusJSON = QueryUtils.sendPostRequest(Config.SEARCH_USER, map);
        } catch (Exception e) {
            return null;
        }


        return statusJSON;
    }

    public static String getProfileInfo(HashMap<String, String> map) {
        String statusJSON;
        try {
            statusJSON = QueryUtils.sendPostRequest(Config.GET_PROFILE_INFO, map);
        } catch (Exception e) {
            return null;
        }


        return statusJSON;
    }

    public static String getVersion(HashMap<String, String> map, String feverVersionCheckUrl) {

        String statusJSON = QueryUtils.sendPostRequest(feverVersionCheckUrl, map);

        //String status = extractVersionCheckSuccessFromJson(statusJSON);

        return statusJSON;

    }

    public static String extractVersionCheckSuccessFromJson(String statusJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(statusJSON)) {
            return "5";
        }

        try {
            JSONObject baseJsonObject = new JSONObject(statusJSON);

            String success = baseJsonObject.optString("Success", "5");

            return success;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "5";

    }

    public static String extractVersionCheckFromJson(String statusJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(statusJSON)) {
            return "5";
        }

        try {
            JSONObject baseJsonObject = new JSONObject(statusJSON);

            String post = baseJsonObject.optString("post","5");

            JSONObject jsonObject = new JSONObject(post);

            String success = jsonObject.optString("version_status", "5");

            return success;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "5";

    }

    public static String deleteProfile(HashMap<String, String> map) {
        String statusJSON;
        try {
            statusJSON = QueryUtils.sendPostRequest(Config.DELETE_USER, map);
        } catch (Exception e) {
            return null;
        }


        return statusJSON;
    }

    public static String updateUserProfile(HashMap<String, String> map) {

        String statusJSON;
        try {
            statusJSON = QueryUtils.sendPostRequest(Config.UPDATE_USER, map);
        } catch (Exception e) {
            return null;
        }


        return statusJSON;

    }

    public static String updateUserSharePrimaryMobile(HashMap<String, String> map) {
        String statusJSON;
        try {
            statusJSON = QueryUtils.sendPostRequest(Config.SHARE_PRIMARY_NUMBER, map);
        } catch (Exception e) {
            return null;
        }


        return statusJSON;
    }

    public static String LoginUser(HashMap<String, String> map) {
        String statusJSON="";
        try {
            statusJSON = QueryUtils.sendPostRequest(Config.LOGIN_USER, map);
        } catch (Exception e) {
            return null;
        }


        return statusJSON;
    }

    public static String changePassword(HashMap<String, String> map) {
        String statusJSON;
        try {
            statusJSON = QueryUtils.sendPostRequest(Config.CHANGE_PASSWORD, map);
        } catch (Exception e) {
            return null;
        }


        return statusJSON;
    }

    public static String logout(HashMap<String, String> map) {

        String statusJSON;
        try {
            statusJSON = QueryUtils.sendPostRequest(Config.LOGOUT_URL, map);
        } catch (Exception e) {
            return null;
        }


        return statusJSON;

    }

    public static String extractLogoutSuccessFromJson(String statusJSON) {

        if (TextUtils.isEmpty(statusJSON)) {
            return "";
        }

        try {
            JSONObject baseJsonObject = new JSONObject(statusJSON);

            String success = baseJsonObject.optString("Success", "");

            return success;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";

    }

    public static String extractLogoutErrorFromJson(String statusJSON) {

        if (TextUtils.isEmpty(statusJSON)) {
            return "Something went wrong. Please try again.";
        }

        try {
            JSONObject baseJsonObject = new JSONObject(statusJSON);

            String success = baseJsonObject.optString("error", "Something went wrong. Please try again.");

            return success;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "Something went wrong. Please try again.";

    }

    public static String forgetPasswordOtp(HashMap<String, String> map) {
        String statusJSON;
        try {
            statusJSON = QueryUtils.sendPostRequest(Config.FORGET_PASSWORD_OTP_URL, map);
        } catch (Exception e) {
            return "";
        }

        return statusJSON;
    }

    public static String updateForgetPasswordOtp(HashMap<String, String> map) {

        String statusJSON;
        try {
            statusJSON = QueryUtils.sendPostRequest(Config.UPDATE_FORGET_PASSWORD_OTP_URL, map);
        } catch (Exception e) {
            return "";
        }

        return statusJSON;

    }
}

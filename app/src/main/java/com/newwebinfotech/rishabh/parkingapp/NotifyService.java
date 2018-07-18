package com.newwebinfotech.rishabh.parkingapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.Getseter;
import com.newwebinfotech.rishabh.parkingapp.model.JSONParser;
import com.newwebinfotech.rishabh.parkingapp.utils.ApiCall;
import com.newwebinfotech.rishabh.parkingapp.utils.L;
import com.newwebinfotech.rishabh.parkingapp.utils.RequestBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

public class NotifyService extends Service {
    private static final long INTERNET_TIMEOUT = 8;//8sec
    private static final long CLIENT_UPDATE_TIME = 10000;//10sec
    Notification noti;
    GPSTracker gps;
    // SessionManager session;
    String name, M = "0", La = null, LI = null;
    Intent intent;
    Integer checkin, Count = 0;
    String Address;
    static String temp;
    static NotificationManager notificationManager;
    static Integer xyz = 0;
    List<String> list, l1;
    double latitude, longitude;
    List<android.location.Address> addresses;
    private static AsyncQueryHandler mQueryHandler;
    //  Users user;
    final Handler handler = new Handler();
    // AlertDialogManager alert = new AlertDialogManager();
    private String filePath = null, Address1 = "", Address2 = "", City = "", State = "", Country = "", County = "", PIN = "", Area = "";
    SharedPreferences sp;
    private Runnable runb;
    private SetLocation setLocation;

    @Override
    public void onStart(Intent intent, int startId) {

        gps = new GPSTracker(NotifyService.this);

        if (runb == null) {
            startTracking();
        }
    }



    private void startTracking() {
        runb = new Runnable() {
            public void run() {
                ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                        .isConnectedOrConnecting();
                boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                        .isConnectedOrConnecting();
                sp = getApplicationContext().getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);

                System.out.println(is3g + " net " + isWifi);

                if (!is3g && !isWifi) {

                } else {

                    ConnectivityManager manager1 = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    boolean is3g1 = manager1.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                            .isConnectedOrConnecting();
                    boolean isWifi1 = manager1.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                            .isConnectedOrConnecting();
                    gps.getLocation();

                    if (gps.canGetLocation()) {
                        latitude = gps.getLatitude();
                        Log.d("dgfdhfdddhfgh", String.valueOf(latitude));

                        if (latitude != 0.0) {

//                            new HttpAsyncTask1().execute("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "");
                            latitude = gps.getLatitude();
                            longitude = gps.getLongitude();

                            executeLogger(latitude,longitude);

                            La = String.valueOf(latitude);
                            LI = String.valueOf(longitude);
                            if (!is3g1 && !isWifi1) {
                                Toast.makeText(getApplicationContext(), "Kindly On Mobile Data", Toast.LENGTH_LONG).show();

                            } else {
                                new AsyncCallSoap_m().execute();

                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Location not detected...", Toast.LENGTH_LONG).show();
                            //	gps.showSettingsAlert();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Kindly On GPS.", Toast.LENGTH_LONG).show();
                        //gps.showSettingsAlert();
                    }

                }
                handler.postDelayed(this, CLIENT_UPDATE_TIME);// 60 sec
//              		 handler.postDelayed(this, 1000*10);
            }
        };
        handler.postDelayed(runb, 0);

    }

    private void executeLogger(double latitude, double longitude) {
        if (setLocation != null && setLocation.getStatus() != AsyncTask.Status.FINISHED) {
            L.m("return");
            return;
        }
        setLocation = new SetLocation(getApplicationContext(), String.valueOf(latitude), String.valueOf(longitude));
        if(Build.VERSION.SDK_INT >= 16)
            setLocation.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            setLocation.execute();
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private class HttpAsyncTask1 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        @Override
        protected void onPostExecute(String jsonString) {


            try {
                JSONObject jsonObj = new JSONObject(jsonString);
                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject zero = Results.getJSONObject(0);
                JSONArray address_components = zero.getJSONArray("address_components");
                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject zero2 = address_components.getJSONObject(i);
                    String long_name = zero2.getString("long_name");
                    JSONArray mtypes = zero2.getJSONArray("types");
                    String Type = mtypes.getString(0);
                    if (!TextUtils.isEmpty(long_name) || !long_name.equals(null) || long_name.length() > 0 || !long_name.equals("")) {
                        if (Type.equalsIgnoreCase("street_number")) {
                            Address1 = " Current Location \r\n" + long_name + " ";

                        } else if (Type.equalsIgnoreCase("route")) {
                            Address1 = Address1 + long_name;

                        } else if (Type.equalsIgnoreCase("sublocality")) {
                            Address2 = long_name;
                        } else if (Type.equalsIgnoreCase("locality")) {
                            City = "\r\n" + long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                            County = "\r\n" + long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                            State = "\r\n" + long_name;
                        } else if (Type.equalsIgnoreCase("country")) {
                            Country = long_name;
                        } else if (Type.equalsIgnoreCase("postal_code")) {
                            PIN = "\r\n" + long_name;
                        } else if (Type.equalsIgnoreCase("neighborhood")) {
                            Area = "\r\n" + long_name;
                        }
                        Address = Address1 + Address2 + City + County + State + PIN + Area;
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    class AsyncCallSoap_m extends AsyncTask<String, Void, Integer> {

        protected Integer doInBackground(String... arg0) {
            Integer i;

            Connection cs1 = new Connection();
            i = cs1.Real("Moksh", La, LI);
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            Integer i;

 	/*Connection cs1 = new Connection();
     i = cs1.Real(name,La,LI,Address);*/
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


    private class SetLocation extends AsyncTask<String, Void, String> {
        private final String lat, lang;
        Context context;

        public SetLocation(Context context, String lat, String lang) {
            this.lat = lat;
            this.lang = lang;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
//            params.put("user_phoneno", sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, ""));
            params.put("primaryMobile", sp.getString(Config.PREF_KEY_PRIMARY_MOBILE,""));
            params.put("lat", lat);
            params.put("lang", lang);
            JSONParser jsonParser = new JSONParser();
//            return ApiCall.POST(Config.SAVE_USER_LOCATION, INTERNET_TIMEOUT, RequestBuilder.SaveLocation(sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, ""), lat, lang));
            String result = jsonParser.makeHttpRequest(Config.SAVE_USER_LOCATION, "POST", params);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            L.m(result);
        }
    }

    @Override
    public boolean stopService(Intent name) {
        if(runb!=null){
            handler.removeCallbacks(runb);
        }
        return super.stopService(name);
    }
}
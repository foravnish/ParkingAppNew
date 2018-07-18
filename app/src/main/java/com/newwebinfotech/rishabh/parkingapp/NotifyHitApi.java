package com.newwebinfotech.rishabh.parkingapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.newwebinfotech.rishabh.parkingapp.utils.App;

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
import java.util.List;

import static com.newwebinfotech.rishabh.parkingapp.utils.App.getContext;

/**
 * Created by user on 10/10/2017.
 */

public class NotifyHitApi extends Service {
    Notification noti ;
    // SessionManager session;
    String name, M="0",La = null,LI=null;
    Intent intent;
    Integer checkin,Count=0;
    String Address;
    static String temp;
    static NotificationManager notificationManager;
    static Integer xyz=0;
    List<String> list,l1;
    double latitude, longitude;
    GPSTracker gps;
    List<android.location.Address> addresses;
    private static AsyncQueryHandler mQueryHandler;
    //  Users user;
    final Handler handler = new Handler();
    // AlertDialogManager alert = new AlertDialogManager();
    private String  filePath = null,Address1 = "", Address2 = "", City = "", State = "", Country = "", County = "", PIN = "", Area="";


    SharedPreferences sp;

    @Override
    public void onStart(Intent intent, int startId) {

        final Runnable runb = new Runnable()
        {
            public void run()
            {
                ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                        .isConnectedOrConnecting();
                boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                        .isConnectedOrConnecting();

                System.out.println(is3g + " net " + isWifi);

                if (!is3g && !isWifi)
                {

                }
                else
                {
                    gps = new GPSTracker(NotifyHitApi.this);
                    if(gps.canGetLocation()) {


                        sp = getApplicationContext().getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);

                        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "http://parkingeye.in/app/parking_eye/api/saveLoc.php?primaryMobile="+sp.getString(Config.PREF_KEY_PRIMARY_MOBILE,"")+"&lat="+gps.getLatitude()+"&lang="+gps.getLongitude(), null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("gdfdhfddhbfg",response.toString());
                                // Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                //Toast.makeText(getContext(), "Unable to send location", Toast.LENGTH_SHORT).show();
                            }
                        });

//                        App.getInstance().addToRequestQueue(jsonObjectRequest);


                  //  Toast.makeText(getApplicationContext(), "running ", Toast.LENGTH_SHORT).show();

                    }

                    else {
                        //Toast.makeText(NotifyHitApi.this, "on gps", Toast.LENGTH_SHORT).show();
                    }

                }
//              		 handler.postDelayed(this, 120000);
                handler.postDelayed(this, 1000*20);
            }
        };
        handler.postDelayed(runb, 0);

    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }
//    private class HttpAsyncTask1 extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//
//            return GET(urls[0]);
//        }
//        @Override
//        protected void onPostExecute(String jsonString) {
//
//
//            try{
//                JSONObject jsonObj = new JSONObject(jsonString);
//                JSONArray Results = jsonObj.getJSONArray("results");
//                JSONObject zero = Results.getJSONObject(0);
//                JSONArray address_components = zero.getJSONArray("address_components");
//                for (int i = 0; i < address_components.length(); i++) {
//                    JSONObject zero2 = address_components.getJSONObject(i);
//                    String long_name = zero2.getString("long_name");
//                    JSONArray mtypes = zero2.getJSONArray("types");
//                    String Type = mtypes.getString(0);
//                    if (! TextUtils.isEmpty(long_name) || !long_name.equals(null) || long_name.length() > 0 || !long_name.equals("")) {
//                        if (Type.equalsIgnoreCase("street_number")) {
//                            Address1 = " Current Location \r\n"+ long_name + " ";
//
//                        } else if (Type.equalsIgnoreCase("route")) {
//                            Address1 = Address1 + long_name;
//
//                        } else if (Type.equalsIgnoreCase("sublocality")) {
//                            Address2 = long_name;
//                        } else if (Type.equalsIgnoreCase("locality")) {
//                            City = "\r\n"+long_name;
//                        } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
//                            County = "\r\n"+long_name;
//                        } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
//                            State = "\r\n"+long_name;
//                        } else if (Type.equalsIgnoreCase("country")) {
//                            Country = long_name;
//                        } else if (Type.equalsIgnoreCase("postal_code")) {
//                            PIN = "\r\n"+long_name;
//                        }else if( Type.equalsIgnoreCase("neighborhood")){
//                            Area = "\r\n"+long_name;
//                        }
//                        Address = Address1 +Address2 +City+County+State+PIN+Area;
//                    }
//                }
//            }
//            catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }



//    class AsyncCallSoap_m extends AsyncTask<String, Void, Integer> {
//
//        protected Integer doInBackground(String... arg0) {
//            Integer i;
//
//            Connection cs1 = new Connection();
//            i = cs1.Real("Moksh",La,LI);
//            return 0;
//        }
//        @Override
//        protected void onPostExecute(Integer result) {
//            Integer i;
//
// 	/*Connection cs1 = new Connection();
// 	i = cs1.Real(name,La,LI,Address);*/
//        }
//    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    public void onDestroy(){
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
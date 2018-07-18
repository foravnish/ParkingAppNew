package com.newwebinfotech.rishabh.parkingapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


import com.Getseter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.newwebinfotech.rishabh.parkingapp.model.JSONParser;
import com.newwebinfotech.rishabh.parkingapp.utils.App;
import com.newwebinfotech.rishabh.parkingapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.widget.Toast;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    GPSTracker gps;
    String latitude, longitude, time, name;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    public List<Getseter> DataList = new ArrayList<Getseter>();
    JSONObject jsonObject1;
    MarkerOptions marker;

    Timer T;
    public List<Getseter> arr = new ArrayList<>();
    private View mUserView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        time = getIntent().getStringExtra("time");
        name = getIntent().getStringExtra("name");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sp = getApplicationContext().getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);
        editor = sp.edit();
        gps = new GPSTracker(MapsActivity.this);
//        Log.d("gfdhbgfhfghjfg", String.valueOf(gps.getLatitude()));
//        Log.d("gfdhbgfhfghjfg", String.valueOf(gps.getLongitude()));
        Log.d("gfdhbgfhfghjfg", latitude);
        Log.d("gfdhbgfhfghjfg", longitude);


        Log.d("dgdfgdhddfdhfdgd", latitude);
        Log.d("dgdfgdhddfdhfdgd", longitude);

        mUserView=getUserMarkerView(this,"");

        new SetLocation(getApplicationContext()).execute();

        if (time.equals("never")) {

            // Toast.makeText(getApplicationContext(), "never", Toast.LENGTH_SHORT).show();

            final Dialog dialog = new Dialog(MapsActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.alertdialogcustom);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView text = (TextView) dialog.findViewById(R.id.msg_txv);
            text.setText("User Denied Your Track Request.");
            Button ok = (Button) dialog.findViewById(R.id.btn_ok);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    finish();
                }
            });
            dialog.show();

        }


        //// Start Runnable


//        long timer = Long.parseLong(DataList.get(position).getDelTime().toString());


        ///// End runnable


    }

    // Convert a view to bitmap
    public static View getUserMarkerView(Context context,String mImagePath) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate( R.layout.marker_map, null );
        ImageView ivUserMarker = (ImageView)view.findViewById(R.id.iv_marker_user);
        if (!TextUtils.isEmpty(mImagePath)) {
            ivUserMarker.setImageURI(Uri.parse(mImagePath));
        } else {
            ivUserMarker.setImageResource(R.drawable.profile);
        }
        return view;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        T = new Timer();
        T.cancel();
    }


    private class SetLocation extends AsyncTask<String, Void, String> {
        Context context;
        ProgressDialog progressDialog;
        String url;
        boolean isValidation;
        String time;

        public SetLocation(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
//            progressDialog.show();
            this.time = time;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            params.put("user_phoneno", sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, ""));


            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest(Config.MULTI_USER_TRACKING, "GET", params);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            DataList.clear();
            JSONObject jsonObject = null;
            Log.e("response", ": " + s);
            Log.d("sdfsdfsdgsdg", s.toString());
            try {
                jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equals("1")) {
                    Log.d("fgfhfh", "true");
                    JSONArray jsonArray = jsonObject.getJSONArray("message");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject1 = jsonArray.getJSONObject(i);
                        //if (!jsonObject1.optString("remainTime").equals("0")) {
                        DataList.add(new Getseter(jsonObject1.optString("userName"), jsonObject1.optString("mobile"), jsonObject1.optString("vehicleNo"), jsonObject1.optString("latitude"), jsonObject1.optString("longitude"), jsonObject1.optString("city") + ", " + jsonObject1.optString("country"), jsonObject1.optString("userProfilePicture"), jsonObject1.optString("distance"), jsonObject1.optString("remainTime")));
//                            latitude1[i] =jsonObject1.optString("latitude");
//                            arr.add(new Getseter(jsonObject1.optString("latitude"));
                        Log.d("fghfghfghfgh", jsonObject1.optString("latitude"));


                        // }
                        //else{
                        //   Toast.makeText(getActivity(), "There are no user here...", Toast.LENGTH_SHORT).show();
                        //   break;
                        // }
                    }
                    Log.d("fhfghfg", String.valueOf(DataList.size()));
//                    adapter=new Adapter();
//                    gridview.setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //  startActivity(new Intent(getActivity(),MainScreenActivity.class) );


        }


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        if (getIntent().getStringExtra("type").equals("1")) {

            String s = getIntent().getStringExtra("json");
            Log.d("fdgdfgdfdgdf", s);
            JSONArray jsonArray = null;
            MarkerOptions userMarker = new MarkerOptions();
            userMarker.icon(BitmapDescriptorFactory.fromBitmap(getUserBitmapMarker(this, mUserView)));

            mMap.clear();
            try {
                jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    double lati = Utils.parseDouble(jsonObject1.optString("latitude"));
                    double longLat = Utils.parseDouble(jsonObject1.optString("longitude"));

                    Log.d("dfgddgddfghddgfd", jsonObject1.optString("latitude"));
                    Log.d("dfgddgddfghddgfd", jsonObject1.optString("longitude"));

                    // add marker to Map
                    mMap.addMarker(userMarker
                            .title(jsonObject1.optString("userName"))
                            .position(new LatLng(lati, longLat)));

//                    mMap.addMarker(new MarkerOptions().position(new LatLng(lati, longLat)).title(jsonObject1.optString("userName")).icon(BitmapDescriptorFactory.fromResource(R.drawable.mapdrop)));
                    LatLng sydney = new LatLng(lati, longLat);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(14));


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        }


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
}

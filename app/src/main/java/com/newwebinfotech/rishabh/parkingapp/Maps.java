package com.newwebinfotech.rishabh.parkingapp;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Getseter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.newwebinfotech.rishabh.parkingapp.model.JSONParser;
import com.newwebinfotech.rishabh.parkingapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Maps extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final int REQUEST_LOCATION = 0;
    private Location mLastLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private static final String TAG = "";
    private GoogleMap mMap;
    private int markerCount;
    SharedPreferences sp;
    double latitude,longitude;
    GPSTracker gps;
    Timer T;
    public List<Getseter> DataList=new ArrayList<Getseter>();
    JSONObject jsonObject1;

    String time,name;
    private View mUserView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

//        Application application = (Application) App.getContext();
//        App app = (App) application;
        mUserView=getUserMarkerView(this,"");
        latitude=Utils.parseDouble(getIntent().getStringExtra("latitude"));
        longitude=Utils.parseDouble(getIntent().getStringExtra("longitude"));
        time=getIntent().getStringExtra("time");
        name=getIntent().getStringExtra("name");

        gps = new GPSTracker(Maps.this);

        markerCount=0;
        sp = getApplicationContext().getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);
        //Check If Google Services Is Available
        if (getServicesAvailable()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
          //  Toast.makeText(this, "Google Service Is Available!!", Toast.LENGTH_SHORT).show();
        }

        //Create The MapView Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        ///////////// Time Start



        ///////////// Timer End

        new SetLocation(getApplicationContext()).execute();

        if (time.equals("never")){

            // Toast.makeText(getApplicationContext(), "never", Toast.LENGTH_SHORT).show();

            final Dialog dialog = new Dialog(Maps.this);
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
        T=new Timer();
        T.cancel();
    }
    /**
     * GOOGLE MAPS AND MAPS OBJECTS
     *
     * */

    // After Creating the Maps Set Initial Location
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //Uncomment To Show Google Location Blue Pointer
       // mMap.setMyLocationEnabled(true);
    }



    Marker mk = null;
    // Add A Maps Pointer To The MAp
    public void addMarker(GoogleMap googleMap, double lat, double lon) {

        MarkerOptions userMarker = new MarkerOptions();
        userMarker.icon(BitmapDescriptorFactory.fromBitmap(Utils.getUserBitmapMarker(this, mUserView)));

        double lat1=gps.getLatitude();
        double lon1=gps.getLongitude();
//        mMap.addMarker(new MarkerOptions().position(new LatLng(lat1, lon1)).title("Me").icon(BitmapDescriptorFactory.fromResource(R.drawable.mapdrop2)));

        // add marker to Map
        mMap.addMarker(userMarker
                .title("Me")
                .position(new LatLng(lat1, lon1)));

        if(markerCount==1){
//            animateMarker(mLastLocation,mk);
            Log.d("ghfhfghfgjhfgh", String.valueOf(lat));
            Log.d("ghfhfghfgjhfgh", String.valueOf(lon));
            Log.d("ghfhfghfgjhfgh", String.valueOf(markerCount));
            animateMarker(lat,lon,mk);

        }

        else if (markerCount==0){
            //Set Custom BitMap for Pointer
            int height = 80;
            int width = 45;
//            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.mapdrop);
//            Bitmap b = bitmapdraw.getBitmap();
//            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            mMap = googleMap;

            LatLng latlong = new LatLng(lat, lon);

            mk=mMap.addMarker(userMarker
                    .title(name!=null?name:"")
                    .position(new LatLng(lat1, lon1)));


//            mk= mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(name.toString()).icon(BitmapDescriptorFactory.fromResource(R.drawable.mapdrop)));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 14));
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            //Set Marker Count to 1 after first marker is created
            markerCount=1;

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            //mMap.setMyLocationEnabled(true);
            startLocationUpdates();
        }
    }


    @Override
    public void onInfoWindowClick (Marker marker){
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
    }


    public boolean getServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {

            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cannot Connect To Play Services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    /**
     * LOCATION LISTENER EVENTS
     *
     * */

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
//        startLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getServicesAvailable();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //Method to display the location on UI
    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {


            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {



//                double latitude = mLastLocation.getLatitude();
//                double longitude = mLastLocation.getLongitude();
                addMarker(mMap,latitude,longitude);



                T=new Timer();
                T.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "http://parkingeye.in/app/parking_eye/api/locDefault.php?primaryMobile="+sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, ""),null,  new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        latitude=Utils.parseDouble(response.optString("lat"));
                                        longitude=Utils.parseDouble(response.optString("long"));
                                        Log.d("gdfdghdfdhbfgdddh",response.toString());

                                        Log.d("dgdfgdhddfdhfdgddfgdfg", String.valueOf(latitude));
                                        Log.d("dgdfgdhddfdhfdgddfgdfg", String.valueOf(longitude));

                                        String loc = "" + latitude + " ," + longitude + " ";
                                     //   Toast.makeText(Maps.this,loc, Toast.LENGTH_SHORT).show();

                                        //Add pointer to the map at location
                                        addMarker(mMap,latitude,longitude);

                                        Log.d("fsgvdfgdf", String.valueOf(latitude));
                                        Log.d("fsgvdfgdf", String.valueOf(longitude));

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        Log.d("dfgfdgdfhdfdh",error.toString());
                                    }
                                });


                                jsonObjectRequest.setShouldCache(false);
//                                App.getInstance().addToRequestQueue(jsonObjectRequest);
                            }
                        });
                    }

                }, 1000, 1000*5);






            } else {

                Toast.makeText(this, "Couldn't get the location. Make sure location is enabled on the device",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Creating google api client object
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }
    //Creating location request object
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(AppConstants.UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(AppConstants.FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(AppConstants.DISPLACEMENT);
    }


    //Starting the location updates
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest,  this);
        }
    }

    //Stopping location updates
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        //Toast.makeText(getApplicationContext(), "Location changed!",Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
    }


    public static void animateMarker(double lat,double longi,final Marker marker) {
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(lat, longi);


            final float startRotation = marker.getRotation();

          
            final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(1000); // duration 1 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
                        //marker.setRotation(computeRotation(v, startRotation, destination.getBearing()));
                    } catch (Exception ex) {
                        // I don't care atm..
                    }
                }
            });

            valueAnimator.start();
        }
    }
    private static float computeRotation(float fraction, float start, float end) {
        float normalizeEnd = end - start; // rotate start to 0
        float normalizedEndAbs = (normalizeEnd + 360) % 360;

        float direction = (normalizedEndAbs > 180) ? -1 : 1; // -1 = anticlockwise, 1 = clockwise
        float rotation;
        if (direction > 0) {
            rotation = normalizedEndAbs;
        } else {
            rotation = normalizedEndAbs - 360;
        }

        float result = fraction * rotation + start;
        return (result + 360) % 360;
    }
    private interface LatLngInterpolator {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolator {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
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
            this.time=time;

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
            params.put("user_phoneno", sp.getString(Config.PREF_KEY_PRIMARY_MOBILE,""));


            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest("http://parkingeye.in/app/parking_eye/api/track_multiple_users.php", "GET", params);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            DataList.clear();
            JSONObject jsonObject = null;
            Log.e("response", ": " + s);
            Log.d("sdfsdfsdgsdg",s.toString());
            try {
                jsonObject= new JSONObject(s);
                if (jsonObject.optString("status").equals("1")){
                    Log.d("fgfhfh","true");
                    JSONArray jsonArray=jsonObject.getJSONArray("message");
                    for (int i=0;i<jsonArray.length();i++){
                        jsonObject1=jsonArray.getJSONObject(i);
                        //if (!jsonObject1.optString("remainTime").equals("0")) {
                        DataList.add(new Getseter(jsonObject1.optString("userName"), jsonObject1.optString("mobile"), jsonObject1.optString("vehicleNo"), jsonObject1.optString("latitude"), jsonObject1.optString("longitude"), jsonObject1.optString("city") + ", " + jsonObject1.optString("country"), jsonObject1.optString("userProfilePicture"), jsonObject1.optString("distance"), jsonObject1.optString("remainTime")));
//                            latitude1[i] =jsonObject1.optString("latitude");
//                            arr.add(new Getseter(jsonObject1.optString("latitude"));
                        Log.d("fghfghfghfgh",jsonObject1.optString("latitude"));





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


            //  startActivity(new Intent(getActivity(),MainScreenActivity.class) )//

        }


    }

}


package com.newwebinfotech.rishabh.parkingapp;

/**
 * Created by Admin on 6/23/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Getseter;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.newwebinfotech.rishabh.parkingapp.model.JSONParser;
import com.newwebinfotech.rishabh.parkingapp.model.Model;
import com.newwebinfotech.rishabh.parkingapp.utils.ApiCall;
import com.newwebinfotech.rishabh.parkingapp.utils.ApiCallError;
import com.newwebinfotech.rishabh.parkingapp.utils.AppUser;
import com.newwebinfotech.rishabh.parkingapp.utils.DateTimeUtility;
import com.newwebinfotech.rishabh.parkingapp.utils.L;
import com.newwebinfotech.rishabh.parkingapp.utils.PopupDialog;
import com.newwebinfotech.rishabh.parkingapp.utils.RequestBuilder;
import com.newwebinfotech.rishabh.parkingapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserTracking extends AppCompatActivity implements OnMapReadyCallback
        , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
        , ResultCallback<LocationSettingsResult>, LocationListener, ApiCallError.ErrorTaskWithParamListener, PopupDialog.DialogTaskListener {

    public GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private static final long INTERNET_TIMEOUT = 8;
    private static final long CLIENT_UPDATE_TIME = 10000;// 10 sec
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000; //for 5sec map refresh time interval
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    // for app
    private int DEFAULT_MAP_ZOOM_SCALE = 15;
    public static final int DEFAULT_RANGE_IN_KM = 10;
    private int MAP_RANGE_IN_KM;
    private GoogleMap map;

    private PermissionListener onPermissionListener;
    private ImageButton btnMyLoc;
    private String mLastUpdateDate;
    private String mLastUpdateTime;
    private Location clientLocation = new Location("local");
    private ImageView ivMoreDetail;
    private boolean mClosedApplication;

    private AssignTask assignTask;
    private View mUserView, mClientView;
    protected Location mCurrentLocation;
    private String cMobile,mTime,mName,mClientImage;
    private boolean mCameraMoveStarted;
    SharedPreferences sp;
    private Handler handler;
    private Runnable runb;
    private SetLocation setLocation;


    //test distance under range 28.619120  77.297718
    //test distance over range 28.6198456  77.2980673
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_user_tracking);
        sp = getApplicationContext().getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);
        double latitude = Utils.parseDouble(getIntent().getStringExtra("latitude"));
        double longitude = Utils.parseDouble(getIntent().getStringExtra("longitude"));
        cMobile=getIntent().getStringExtra("cMobile");
        mTime=getIntent().getStringExtra("time");
        mName=getIntent().getStringExtra("name");
        mClientImage=getIntent().getStringExtra("image");

        clientLocation.setLatitude(latitude);
        clientLocation.setLongitude(longitude);
//        clientLocation.setLatitude(Double.parseDouble("28.618282"));
//        clientLocation.setLongitude(Double.parseDouble("77.297800"));
        MAP_RANGE_IN_KM =DEFAULT_RANGE_IN_KM;
        mCurrentLocation = AppUser.getMyLocation(this);
        initToolBar();
        buildGoogleApiClient();
        mUserView = getUserMarkerView(this,"Me", AppUser.getImage(this));
        mClientView = getClientView(this,mName,mClientImage);
        onPermissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (ActivityCompat.checkSelfPermission(UserTracking.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UserTracking.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                createLocationRequest();
                buildLocationSettingsRequest();
                checkGPSEnableSettings();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(UserTracking.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        InitUI();
    }

    private void initToolBar() {
//        ImageButton backButton = (ImageButton) findViewById(R.id.btn_menu_start);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

    }

    private void InitUI() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnMyLoc = (ImageButton) findViewById(R.id.btn_my_location);

        updateClientLocation();
    }

    private void updateClientLocation() {
        handler = new Handler();
        runb = new Runnable() {
            public void run() {
                executeLogger();
                handler.postDelayed(this, CLIENT_UPDATE_TIME); //60sec
//              		 handler.postDelayed(this, 1000*10);
            }
        };
        handler.postDelayed(runb, 0);
    }

    private void executeLogger() {
        if (setLocation != null && setLocation.getStatus() != AsyncTask.Status.FINISHED) {
            L.m("return");
            return;
        }
        setLocation = new SetLocation(getApplicationContext());
        if(Build.VERSION.SDK_INT >= 16)
            setLocation.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            setLocation.execute();
    }




    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void checkGPSEnableSettings() {
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationSettingsRequest);
        result.setResultCallback(this);
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateDate = DateTimeUtility.getDateStampFormatted();
            mLastUpdateTime = DateTimeUtility.getTimeStampFormatted();
//            updateLocationUI();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        if (mGoogleApiClient != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            createLocationRequest();
            buildLocationSettingsRequest();
            checkGPSEnableSettings();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        if (assignTask != null) {
            assignTask.onAttach(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MapFragment f = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
        if (assignTask != null) {
            assignTask.onDetach();
        }
        if(handler!=null&& runb!=null){
            handler.removeCallbacks(runb);
        }
    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED: // show gps enable dialog
                try {
                    status.startResolutionForResult(UserTracking.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Toast.makeText(getApplicationContext(), "Sorry", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case RESULT_OK:
                    startLocationUpdates();
                    break;
            }
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                Log.d("HI", status.toString());
                if (ActivityCompat.checkSelfPermission(UserTracking.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UserTracking.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                mLastUpdateDate = DateTimeUtility.getDateStampFormatted();
                mLastUpdateTime = DateTimeUtility.getTimeStampFormatted();
                updateLocationUI(true);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        AppUser.setMyLatLng(UserTracking.this, location);
//        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        mLastUpdateDate = DateTimeUtility.getDateStampFormatted();
        mLastUpdateTime = DateTimeUtility.getTimeStampFormatted();
        AppUser.setLastLocationDate(UserTracking.this, mLastUpdateDate);
        AppUser.setLastLocationTime(UserTracking.this, mLastUpdateTime);
        updateLocationUI(false);
    }



    private void updateLocationUI(boolean zoomAnimation) {
//        addOfficeMarkerLocationOnMap(clientLocation,"Gennext",true);
        if (mCurrentLocation != null) {
            focusOnLocation(mCurrentLocation, zoomAnimation);
        } else {
            focusOnLocation(AppUser.getMyLocation(UserTracking.this), zoomAnimation);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);

        btnMyLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGPSEnableSettings();
                if (ActivityCompat.checkSelfPermission(UserTracking.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UserTracking.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                mCameraMoveStarted=false;
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//                mLastUpdateDate = DateTimeUtility.getDateStampFormatted();
//                mLastUpdateTime = DateTimeUtility.getTimeStampFormatted();
//                if (mCurrentLocation != null) {
//                    AppUser.setMyLatLng(UserTracking.this, mCurrentLocation);
//                }
//                updateLocationUI(true);
            }
        });

        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(UserTracking.this, R.raw.custom_map_style);
//        map.setMapStyle(style);
        mCameraMoveStarted=false;
        map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                mCameraMoveStarted = false;
            }
        });
        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                mCameraMoveStarted = true;
            }
        });



        if (ActivityCompat.checkSelfPermission(UserTracking.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UserTracking.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            new TedPermission(UserTracking.this)
                    .setPermissionListener(onPermissionListener)
                    .setDeniedMessage(getString(R.string.permission_denied_explanation))
                    .setRationaleMessage(getString(R.string.permission_denied_explanation_message))
                    .setGotoSettingButtonText("setting")
                    .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                    .check();
        }
    }


    @Override
    public void onDialogOkClick(DialogFragment dialog, int task) {
        if (task == 1) {

        } else {
            createLocationRequest();
            buildLocationSettingsRequest();
            checkGPSEnableSettings();
        }
    }

    @Override
    public void onDialogCancelClick(DialogFragment dialog, int task) {

    }



//    private void addLocationMarkerOnMap(Location location, String locationName, boolean markerVisibility) {
////        if (map != null) {
////            map.clear();
////        }
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//
//        if (markerVisibility) {
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//            map.addMarker(markerOptions
//                    .title(locationName)
//                    .position(latLng)
//            );
//        }
//
//        double rangeInKm = Double.parseDouble(String.valueOf(MAP_RANGE_IN_KM)) * 1000;
//        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        map.addCircle(new CircleOptions()
//                .center(latLng)
//                .radius(rangeInKm)
//                .strokeColor(Color.parseColor("#4FC3F7"))
//                .fillColor(Color.parseColor("#054FC3F7")));
//        map.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_MAP_ZOOM_SCALE));
//
//    }


    public void allocateMarkersOnCurrentLocation(Location location) {


        MarkerOptions clientMarker = new MarkerOptions();
        MarkerOptions userMarker = new MarkerOptions();
        clientMarker.icon(BitmapDescriptorFactory.fromBitmap(getUserBitmapMarker(this, mClientView)));
        userMarker.icon(BitmapDescriptorFactory.fromBitmap(getUserBitmapMarker(this, mUserView)));

//        clientMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_gennext_marker));
//        userMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        LatLng clientLatLng = new LatLng(clientLocation.getLatitude(), clientLocation.getLongitude());
        LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        // add marker to Map
        map.addMarker(clientMarker
//                .title(mName!=null?mName:"User")
                .position(clientLatLng));

        map.addMarker(userMarker
                .position(userLatLng));

        map.addMarker(clientMarker).showInfoWindow();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(userLatLng);
        builder.include(clientLatLng);
        final LatLngBounds bounds = builder.build();
        //BOUND_PADDING is an int to specify padding of bound.. try 100.
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 300);
        if(!mCameraMoveStarted) {
            map.animateCamera(cu);
        }
    }

    // Convert a view to bitmap
    public static View getUserMarkerView(Context context, String name, String mImagePath) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.marker_map, null);
        TextView tvUserMarker = (TextView) view.findViewById(R.id.tv_marker_title);
        ImageView ivUserMarker = (ImageView) view.findViewById(R.id.iv_marker_user);
        if (!TextUtils.isEmpty(mImagePath)) {
            Glide.with(context).load(mImagePath).into(ivUserMarker);
//            ivUserMarker.setImageURI(Uri.parse(mImagePath));
        } else {
            ivUserMarker.setImageResource(R.drawable.profile);
        }
        tvUserMarker.setVisibility(View.VISIBLE);
        tvUserMarker.setText(name != null ? name : "");
        return view;
    }

    // Convert a view to bitmap
    // Convert a view to bitmap
    public static View getClientView(Context context, String name, String imageClient) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.marker_map_company, null);
        TextView tvUserMarker = (TextView) view.findViewById(R.id.tv_marker_title);
        ImageView ivClientMarker = (ImageView) view.findViewById(R.id.iv_marker_client);
        if (!TextUtils.isEmpty(imageClient)) {
            Glide.with(context).load(imageClient).into(ivClientMarker);
//            ivClientMarker.setImageURI(Uri.parse(imageClient));
        } else {
            ivClientMarker.setImageResource(R.drawable.profile);
        }
        tvUserMarker.setVisibility(View.VISIBLE);
        tvUserMarker.setText(name != null ? name : "");

        return view;
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

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void addCurcleOnMap(Location location, int zoomScale) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        double rangeInKm = Double.parseDouble(String.valueOf(MAP_RANGE_IN_KM));
        LatLng latLng = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.addCircle(new CircleOptions()
                .center(latLng)
                .radius(rangeInKm * 1000));
        map.animateCamera(CameraUpdateFactory.zoomTo(zoomScale));
    }

    private void focusOnLocation(Location location, boolean zoomAnimation) {
        if (map != null) {
            map.clear();
        }
        if (location != null) {
            if (zoomAnimation) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                map.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_MAP_ZOOM_SCALE));
            }
            allocateMarkersOnCurrentLocation(location);
        }
    }

    private float calculateDistance(Location userLocation, Location clientLocation) {
        float[] results = new float[1];
        Location.distanceBetween(clientLocation.getLatitude(), clientLocation.getLongitude(),
                userLocation.getLatitude(), userLocation.getLongitude(),
                results);
        return results[0];
    }

    public void updateRadiusInMapView(int progress) {
        this.MAP_RANGE_IN_KM = progress;
        if (mCurrentLocation == null) {
            mCurrentLocation = AppUser.getMyLocation(UserTracking.this);
        }
    }

    private void executeTask(String entryType, String date, String timeInOut, String location, int errorCount) {
//        assignTask = new AssignTask(UserTracking.this, entryType, valString(date), valString(timeInOut), location, errorCount);
//        assignTask.execute(Config.MARK_ATTENDANCE);
    }

    public String valString(String txt) {
        return txt != null ? txt.replaceAll("'", "''") : "";
    }


    @Override
    public void onErrorRetryClick(DialogFragment dialog, int errorCount, String[] param) {
        executeTask(param[0], param[1], param[2], param[3], errorCount);
    }

    @Override
    public void onErrorCancelClick(DialogFragment dialog, int task, String[] param) {

    }


    private class AssignTask extends AsyncTask<String, Void, Model> {
        private final String entryType, date, timeInOut, location;
        private Context context;
        private ProgressDialog pDialog;
        private int errorCount;

        public void onAttach(Context context) {
            // TODO Auto-generated method stub
            this.context = context;
        }

        public void onDetach() {
            // TODO Auto-generated method stub
            this.context = null;
        }

        public AssignTask(Context context, String entryType, String date, String timeInOut, String location, int errorCount) {
            this.entryType = entryType;
            this.date = date;
            this.timeInOut = timeInOut;
            this.location = location;
            this.errorCount = errorCount;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (context != null) {
                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Processing, Please wait ...");
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.show();
            }
        }

        @Override
        protected Model doInBackground(String... urls) {
            Model result = new Model();
            String userId = AppUser.getUserId(context);
            String response = ApiCall.POST(urls[0], RequestBuilder.Default(userId));
//            result = JsonParser.defaultParser(response);
            result = JSONParser.defaultSuccessResponse(response);
            return result;
        }


        @Override
        protected void onPostExecute(Model result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (context != null) {
                pDialog.dismiss();
//                if (result.getOutput().equals(Const.SUCCESS)) {
//                    if (!result.getOutputDB().equals(Const.SUCCESS)) {
//
//                    }
//                    PopupAlert.newInstance("Alert", result.getOutputMsg(), PopupAlert.POPUP_DIALOG).show(getSupportFragmentManager(), "popupAlert");
//                    mIsShowSlidingPanel = false;
//                    closePannel();
//                    if (mCurrentLocation != null) {
//                        float resultDistance = calculateDistance(mCurrentLocation, clientLocation);
//                        if (resultDistance < OFFICE_ATTENDENCE_RADIOUS) {
//                            attendanceAllowed(resultDistance);
//                        } else {
//                            attendanceNotAllowed(resultDistance);
//                        }
//                    }
//
//                } else if (result.getOutput().equals(Const.FAILURE)) {
//                    PopupAlert.newInstance("Alert", result.getOutputMsg(), PopupAlert.POPUP_DIALOG)
//                            .show(getSupportFragmentManager(), "popupAlert");
//                } else {
//                    String[] errorSoon = {entryType, date, timeInOut, location};
//                    ApiCallError.newInstance(true, result.getOutput(), result.getOutputMsg(), errorCount, errorSoon, UserTracking.this)
//                            .show(getSupportFragmentManager(), "apiCallError");
//                }
            }
        }
    }


    private class SetLocation extends AsyncTask<String, Void, String> {
        Context context;
        String time;

        public SetLocation(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
//            HashMap<String, String> params = new HashMap<>();
//            params.put("user_phoneno", sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, ""));


//            JSONParser jsonParser = new JSONParser();
            return ApiCall.POST(Config.MULTI_USER_TRACKING,INTERNET_TIMEOUT, RequestBuilder.SetPhoneNo(sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, "")));
//            String result = jsonParser.makeHttpRequest(Config.MULTI_USER_TRACKING, "POST", params);
//            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equals("1")) {
                    Log.d("fgfhfh", "true");
                    JSONArray jsonArray = jsonObject.getJSONArray("message");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if (jsonObject1.optString("mobile").equals(cMobile)) {
                            clientLocation.setLatitude(Utils.parseDouble(jsonObject1.optString("latitude")));
                            clientLocation.setLongitude(Utils.parseDouble(jsonObject1.optString("longitude")));
                        }
//                        DataList.a2dd(new Getseter(jsonObject1.optString("userName"), jsonObject1.optString("mobile"), jsonObject1.optString("vehicleNo"), jsonObject1.optString("latitude"), jsonObject1.optString("longitude"), jsonObject1.optString("city") + ", " + jsonObject1.optString("country"), jsonObject1.optString("userProfilePicture"), jsonObject1.optString("distance"), jsonObject1.optString("remainTime")));
                    }
                    updateLocationUI(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

package com.newwebinfotech.rishabh.parkingapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.iid.FirebaseInstanceId;
import com.newwebinfotech.rishabh.parkingapp.utils.Appsetting;
import com.newwebinfotech.rishabh.parkingapp.utils.ProgressButtonRounded;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A login screen that offers login via mobile no/password.
 */
public class LoginActivity extends AppCompatActivity {

    EditText mobile, password;
    SharedPreferences sp;
    String regid;
    Appsetting appsetings;
    String str="";
    private ProgressButtonRounded btnAction;
    public static final int MULTIPLE_PERMISSIONS = 15;
    String[] permissions= new String[]{

            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA

    };
    private GPSTracker gpsTracker;
    private Location mLocation;
    double latitude, longitude;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private LoginUserAsyncTask loginUserAsyncTask;

    @Override
    protected void onStart() {
        super.onStart();
        if(loginUserAsyncTask!=null){
            loginUserAsyncTask.onAttach(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(loginUserAsyncTask!=null){
            loginUserAsyncTask.onDetach();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);

        checkVersion();

        appsetings = new Appsetting(LoginActivity.this);
        regid = FirebaseInstanceId.getInstance().getToken();
        regid = FirebaseInstanceId.getInstance().getToken();
        LocalBroadcastManager.getInstance(this).registerReceiver(tokenReceiver, new IntentFilter("tokenReceiver"));

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            displayLocationSettingsRequest(LoginActivity.this);

        }else {
            displayLocation();
        }

        // Set up the login form.
        mobile = (EditText) findViewById(R.id.mobile);
        password = (EditText) findViewById(R.id.password);

        sp = getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);

        if(checkPermissions()){

        }else{
            checkPermissions();
        }


        ImageView ivStatus = (ImageView) findViewById(R.id.iv_status);
        ProgressBar pBar = (ProgressBar) findViewById(R.id.progressBar);
        Button btnMain = (Button) findViewById(R.id.btn_action);
        btnAction = ProgressButtonRounded.newInstance(this,ivStatus,pBar,btnMain);
        btnAction.setText("Sign In");

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.getText().toString().isEmpty())
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Please enter mobile no...");
                    //builder.setMessage(getStringImage(bitmap));
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.show();
                }
                else if (password.getText().toString().isEmpty())
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Please enter password");
                    //builder.setMessage(getStringImage(bitmap));
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.show();
                }
                else
                {

                    final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        displayLocationSettingsRequest(LoginActivity.this);

                    }else {
                        displayLocation();

                        ConnectivityManager cm = (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                        if (isConnected) {
                            loginUserAsyncTask = new LoginUserAsyncTask(LoginActivity.this);
                            loginUserAsyncTask.execute();
                        }
                        else
                        {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage(getResources().getString(R.string.alert_no_internet));
                            final AlertDialog alert = builder.create();
                            alert.show();
                        }

                    }

                }
            }
        });

//        Button login = (Button) findViewById(R.id.sign_in_button);
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//            }
//        });

        LinearLayout register = (LinearLayout) findViewById(R.id.ll_signup);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
//                finish();

            }
        });

        LinearLayout policy = (LinearLayout) findViewById(R.id.ll_signup_terms);
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder build = new AlertDialog.Builder(LoginActivity.this);
                build.setCancelable(false);
                build.setTitle("Privacy Policy");
                build.setMessage(getResources().getString(R.string.alert_privacy_policy));

                build.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = build.create();
                alert.show();
            }
        });

        LinearLayout forgetPassword = (LinearLayout) findViewById(R.id.ll_signup_forgot);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgetPasswordDialog();
            }
        });

    }

    private void openForgetPasswordDialog() {

        LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
        View subView = inflater.inflate(R.layout.forget_password_dialog, null);
        final EditText e = (EditText) subView.findViewById(R.id.forget_password_edittext);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(subView);

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = e.getText().toString();
                if (s.isEmpty()||s.length()>10||s.length()<10)
                {
                    Toast.makeText(LoginActivity.this,"Invalid Mobile No.!!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    ConnectivityManager cm = (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                    if (isConnected) {
                        str = s;
                        ForgetPasswordAsyncTask task = new ForgetPasswordAsyncTask();
                        task.execute(s);
                    } else {
                        AlertDialog.Builder build = new AlertDialog.Builder(LoginActivity.this);
                        build.setCancelable(false);
                        build.setMessage("Error connecting to internet. Please, check network connection.");

                        build.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = build.create();
                        alert.show();

                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void openUpdatePasswordDialog() {

        LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
        View subView = inflater.inflate(R.layout.update_forget_password_dialog, null);
        final TextView phone = (TextView) subView.findViewById(R.id.reset_phone_number);
        phone.setText(str);
        phone.setClickable(false);
        final EditText eotp = (EditText) subView.findViewById(R.id.reset_otp);
        final EditText epass = (EditText) subView.findViewById(R.id.reset_password);
        final EditText ecPass = (EditText) subView.findViewById(R.id.reset_confirm_password);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(subView);

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String otp = eotp.getText().toString();
                String pass = epass.getText().toString();
                String cpass = ecPass.getText().toString();

                if (otp.isEmpty()||pass.isEmpty()||cpass.isEmpty())
                {
                    Toast.makeText(LoginActivity.this,"All fields are mandatory.", Toast.LENGTH_LONG).show();
                }
                else if(pass.length()<6)
                {
                    Toast.makeText(LoginActivity.this,"Password should be at least 6 characters long.", Toast.LENGTH_LONG).show();
                }
                else if(!pass.equals(cpass))
                {
                    Toast.makeText(LoginActivity.this,"Password doesn't match confirmation.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    ConnectivityManager cm = (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                    if (isConnected) {
                        UpdateForgetPasswordAsyncTask task = new UpdateForgetPasswordAsyncTask();
                        task.execute(str, otp, pass);
                    } else {
                        AlertDialog.Builder build = new AlertDialog.Builder(LoginActivity.this);
                        build.setCancelable(false);
                        build.setMessage("Error connecting to internet. Please, check network connection.");

                        build.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = build.create();
                        alert.show();

                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private class LoginUserAsyncTask extends AsyncTask<Void, Void, String> {

        HashMap<String, String> map = new HashMap<String, String>();
        private ProgressDialog progress;
        private Context context;

        public LoginUserAsyncTask(Context context) {
            this.context=context;
        }

        public void onAttach(Context context) {
            this.context=context;
        }
        public void onDetach() {
            this.context=null;
        }

        @Override
        protected void onPreExecute() {

//            progress = new ProgressDialog(LoginActivity.this);
//            progress.setMessage("Signing in...");
//            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progress.setIndeterminate(true);
//            progress.setCancelable(false);
//            progress.show();
            btnAction.startAnimation();

            displayLocation();

            map.put("phone", mobile.getText().toString());
            map.put("password", password.getText().toString());
            map.put("latitude", String.valueOf(latitude));
            map.put("longitude", String.valueOf(longitude));
            map.put("deviceid", regid);
            Log.d("dgbvdchbgfdhgfh",regid.toString());
            //map.put("deviceid", "later");

        }

        @Override
        protected String doInBackground(Void... params) {

            String jsonResponse = QueryUtils.LoginUser(map);
//            progress.dismiss();

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            if(context!=null) {
                Log.d("LoginPost", jsonResponse);

                String success = "";
                try {
                    success = extractSuccessFromLoginUser(jsonResponse);
                } catch (Exception e) {

                }


                if (success.equals("1")) {

                    JSONObject baseJsonResponse = null;
                    try {
                        baseJsonResponse = new JSONObject(jsonResponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONArray postJsonArray = baseJsonResponse.optJSONArray("post");

                    JSONObject userData = null;
                    try {
                        userData = postJsonArray.getJSONObject(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    final SharedPreferences sp = getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString(Config.PREF_KEY_PRIMARY_MOBILE, mobile.getText().toString());
                    editor.putString(Config.PREF_KEY_SHARE_PRIMARY_MOBILE, userData.optString("something", "1"));
                    editor.putString(Config.PREF_KEY_TOKEN, userData.optString("token", ""));


                    editor.apply();
                    Log.d("ghdfdhfhghgfgh", userData.optString("token", ""));
                    Log.d("ghdfdhfhghgfgh", sp.getString(Config.PREF_KEY_TOKEN, ""));

                    appsetings.saveString(Appsetting.FCMDEVICEID, regid);
                    String device = appsetings.getString(Appsetting.FCMDEVICEID);
                    appsetings.saveString(Appsetting.MYNAME, userData.optString("userName", "") + "(" + userData.optString("city", "") + ")" + userData.optString("vehicleNo", ""));

                    appsetings.saveString(Appsetting.MYPRIMARYPHONENO, mobile.getText().toString());

                    animateButtonAndRevert();


                } else if (success.equals("0")) {
                    btnAction.revertAnimation();
                    String error = extractErrorFromLoginUser(jsonResponse);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage(error);
                    //builder.setMessage(getStringImage(bitmap));
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.show();

                } else {

                    btnAction.revertAnimation();
                    Log.v("JSON response", "No json response");
                    final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage(R.string.alert_something_went_wrong);
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.show();
                }


            }

        }

        private void animateButtonAndRevert() {
            Handler handler = new Handler();

            Runnable runnableRevert = new Runnable() {
                @Override
                public void run() {
                    startService(new Intent(LoginActivity.this, MyService.class));

                    Intent i = new Intent(LoginActivity.this, MainScreenActivity.class);
                    i.putExtra("logoutcheck","");
                    startActivity(i);
                    finish();
                }
            };

            btnAction.revertSuccessAnimation();
            handler.postDelayed(runnableRevert, 1000);
        }


        String extractSuccessFromLoginUser(String jsonResponse) {

            Log.v("khali h", "Aa gaya");

            // If the JSON string is empty or null, then return early.
            if (jsonResponse.isEmpty() || jsonResponse.equals(null)) {
                Log.v("khali h", "khali h bhai sachi123");
                return "";
            }

            try {

                // Create a JSONObject from the JSON response string
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);

                String success = baseJsonResponse.optString("Success","");
                return success;

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the event JSON results", e);
            }

            return "";

        }

        String extractErrorFromLoginUser(String jsonResponse) {

            try {

                // Create a JSONObject from the JSON response string
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);

                String error = baseJsonResponse.optString("error", getResources().getString(R.string.alert_something_went_wrong));
                return error;

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the event JSON results", e);
            }

            return "";

        }

        String extractPostFromLoginUser(String jsonResponse) {

            try {

                // Create a JSONObject from the JSON response string
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);

                String post = baseJsonResponse.optString("post", getResources().getString(R.string.alert_something_went_wrong));
                return post;

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the event JSON results", e);
            }

            return "";

        }


    }

    BroadcastReceiver tokenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            regid = intent.getStringExtra("token");
            if(regid != null)
            {
                //send token to your server or what you want to do
            }

        }
    };

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(LoginActivity.this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(LoginActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {

                            status.startResolutionForResult(LoginActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    private void displayLocation() {

        gpsTracker = new GPSTracker(getApplicationContext());



        mLocation = gpsTracker.getLocation();
        if(mLocation!=null) {
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
        } else {


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permissions granted.
                } else {

                }
                return;
            }
        }

    }

    private void checkVersion() {
        ConnectivityManager cm = (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            new VersionCheckAsyncTask().execute();
        } else {
            AlertDialog.Builder build = new AlertDialog.Builder(LoginActivity.this);
            build.setCancelable(false);
            build.setMessage("Error connecting to internet. Please, check network connection.");

            build.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = build.create();
            alert.show();

        }
    }

    private class VersionCheckAsyncTask extends android.os.AsyncTask<Void, Void, String> {

        String s;
        private ProgressDialog progress;

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put(Config.KEY_VERSION, Config.APP_VERSION);

            String result = QueryUtils.getVersion(map, Config.VERSION_CHECK_URL);

            //  Log.v("rrrrrr", result);


            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onPostExecute(final String str) {

            progress.dismiss();

            String success = QueryUtils.extractVersionCheckSuccessFromJson(str);
            // Log.v("vvvvvv", success);

            if (success.equals("1")) {
                s = QueryUtils.extractVersionCheckFromJson(str);

                final AlertDialog.Builder build = new AlertDialog.Builder(LoginActivity.this);
                build.setCancelable(false);

                if (s.equals("-1")) {
                    build.setMessage(getResources().getString(R.string.alert_app_maintenance));
                } else if (s.equals("0")) {
                    build.setMessage(getResources().getString(R.string.alert_app_update));
                } else if (s.equals("5")) {
                    build.setMessage(getResources().getString(R.string.alert_something_went_wrong));
                }

                build.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if (s.equals("-1") || s.equals("5")) {
                                    SharedPreferences.Editor editor = sp.edit();

                                    editor.putString(Config.PREF_KEY_PRIMARY_MOBILE, "");
                                    editor.apply();
                                    finishAffinity();
                                } else if (s.equals("0")) {
                                    SharedPreferences.Editor editor = sp.edit();

                                    editor.putString(Config.PREF_KEY_PRIMARY_MOBILE, "");
                                    editor.apply();
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.newwebinfotech.rishabh.parkingapp")));
                                /*final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }*/
                                }
                            }
                        });

                if (!s.equals("1")) {
                    AlertDialog alert = build.create();
                    alert.show();
                }

            } else {
                final AlertDialog.Builder build = new AlertDialog.Builder(LoginActivity.this);
                build.setCancelable(false);
                build.setMessage(getResources().getString(R.string.alert_something_went_wrong));

                build.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finishAffinity();
                            }
                        });

                AlertDialog alert = build.create();
                alert.show();

            }
        }
    }

    private class ForgetPasswordAsyncTask extends AsyncTask<String, Void, String> {

        String s;
        private ProgressDialog progress;

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("primaryMobile", params[0]);

            String result = QueryUtils.forgetPasswordOtp(map);

            //  Log.v("rrrrrr", result);


            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("Processing...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onPostExecute(String jsonResponse) {

            progress.dismiss();

            Log.v("otpPost",jsonResponse);

            String success="";
            try {
                success = extractSuccessFromForgetPasswordOtp(jsonResponse);
            }
            catch (Exception e)
            {

            }


            if (success.equals("1")) {

                String post = extractPostFromForgetPasswordOtp(jsonResponse);

                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setCancelable(false);
                builder.setMessage(post);
                //builder.setMessage(getStringImage(bitmap));
                builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(LoginActivity.this,"Cool...", Toast.LENGTH_LONG).show();


                        openUpdatePasswordDialog();


                    }
                });

                final AlertDialog alert = builder.create();

                alert.show();

            }
            else if(success.equals("0"))
            {
                String error = extractErrorFromForgetPasswordOtp(jsonResponse);

                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setCancelable(false);
                builder.setMessage(error);
                //builder.setMessage(getStringImage(bitmap));
                builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog alert = builder.create();

                alert.show();

            }
            else {

                Log.v("JSON response","No json response");
                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setCancelable(false);
                builder.setMessage(R.string.alert_something_went_wrong);
                builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog alert = builder.create();

                alert.show();
            }




        }

        String extractSuccessFromForgetPasswordOtp(String jsonResponse) {

            Log.v("khali h", "Aa gaya");

            // If the JSON string is empty or null, then return early.
            if (jsonResponse.isEmpty() || jsonResponse.equals(null)) {
                Log.v("khali h", "khali h bhai sachi123");
                return "";
            }

            try {

                // Create a JSONObject from the JSON response string
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);

                String success = baseJsonResponse.optString("Success","");
                return success;

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the event JSON results", e);
            }

            return "";

        }

        String extractErrorFromForgetPasswordOtp(String jsonResponse) {

            try {

                // Create a JSONObject from the JSON response string
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);

                String error = baseJsonResponse.optString("error", getResources().getString(R.string.alert_something_went_wrong));
                return error;

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the event JSON results", e);
            }

            return "";

        }

        String extractPostFromForgetPasswordOtp(String jsonResponse) {

            try {

                // Create a JSONObject from the JSON response string
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);

                String post = baseJsonResponse.optString("post", getResources().getString(R.string.alert_something_went_wrong));
                return post;

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the event JSON results", e);
            }

            return "";

        }

    }

    private class UpdateForgetPasswordAsyncTask extends AsyncTask<String, Void, String> {

        String s;
        private ProgressDialog progress;

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("primaryMobile", params[0]);
            map.put("otp", params[1]);
            map.put("password", params[2]);

            String result = QueryUtils.updateForgetPasswordOtp(map);

            //Log.v("rrv", params[0] + " , " + params[1] + " , " + params[2]);

            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("Updating Password...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onPostExecute(String jsonResponse) {

            progress.dismiss();

            Log.v("otpPost",jsonResponse);

            String success="";
            try {
                success = extractSuccessFromForgetPasswordOtp(jsonResponse);
            }
            catch (Exception e)
            {

            }


            if (success.equals("1")) {

                String post = extractPostFromForgetPasswordOtp(jsonResponse);

                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setCancelable(false);
                builder.setMessage(post);
                //builder.setMessage(getStringImage(bitmap));
                builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                final AlertDialog alert = builder.create();

                alert.show();

            }
            else if(success.equals("0"))
            {
                String error = extractErrorFromForgetPasswordOtp(jsonResponse);

                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setCancelable(false);
                builder.setMessage(error);
                //builder.setMessage(getStringImage(bitmap));
                builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog alert = builder.create();

                alert.show();

            }
            else {

                Log.v("JSON response","No json response");
                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setCancelable(false);
                builder.setMessage(R.string.alert_something_went_wrong);
                builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog alert = builder.create();

                alert.show();
            }




        }

        String extractSuccessFromForgetPasswordOtp(String jsonResponse) {

            Log.v("khali h", "Aa gaya");

            // If the JSON string is empty or null, then return early.
            if (jsonResponse.isEmpty() || jsonResponse.equals(null)) {
                Log.v("khali h", "khali h bhai sachi123");
                return "";
            }

            try {

                // Create a JSONObject from the JSON response string
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);

                String success = baseJsonResponse.optString("Success","");
                return success;

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the event JSON results", e);
            }

            return "";

        }

        String extractErrorFromForgetPasswordOtp(String jsonResponse) {

            try {

                // Create a JSONObject from the JSON response string
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);

                String error = baseJsonResponse.optString("error", getResources().getString(R.string.alert_something_went_wrong));
                return error;

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the event JSON results", e);
            }

            return "";

        }

        String extractPostFromForgetPasswordOtp(String jsonResponse) {

            try {

                // Create a JSONObject from the JSON response string
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);

                String post = baseJsonResponse.optString("post", getResources().getString(R.string.alert_something_went_wrong));
                return post;

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the event JSON results", e);
            }

            return "";

        }

    }


}


package com.newwebinfotech.rishabh.parkingapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.newwebinfotech.rishabh.parkingapp.utils.AppUser;
import com.newwebinfotech.rishabh.parkingapp.utils.ProgressButtonRounded;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.newwebinfotech.rishabh.parkingapp.R.id.imageView;

public class MainActivity extends AppCompatActivity {

    String vehicle1 = "", vehicle2 = "", vehicle3 = "", vehicle4 = "", vehicleType2 = "", vehicleType3 = "", vehicleType4 = "", userName = "", userCity = "", userMobile = "", alternateNo1 = "", alternateNo2 = "", alternateNo3 = "", status = "";
    //boolean shareNo1 = true, shareNo2 = true, shareNo3 = true, shareNo4 = true;

    SharedPreferences sp;
    TextView addStatus;
    FloatingActionButton addMoreVehicle;
    FloatingActionButton addAlternateNo;
    ImageView dp;
    private static final int CAMERA_REQUEST = 1888;

    EditText vehicle1Field, vt1, userNameField, userCityField, userCountryField, userMobileField, alternateNo1Field, email, password, confirmPassword;
    //Appsetting appsetings;
    Button submit;
    Uri resultUri;
    Bitmap bitmap;
    //String regid;
    public static final int MULTIPLE_PERMISSIONS = 15;
    String[] permissions = new String[]{

            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,

    };
    private ProgressButtonRounded btnAction;
    private RegisterUserAsyncTask registerUserAsyncTask;


    @Override
    protected void onStart() {
        super.onStart();
        if (registerUserAsyncTask != null) {
            registerUserAsyncTask.onAttach(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (registerUserAsyncTask != null) {
            registerUserAsyncTask.onDetach();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        //appsetings = new Appsetting(MainActivity.this);
        //regid = FirebaseInstanceId.getInstance().getToken();
        //regid = FirebaseInstanceId.getInstance().getToken();
        //LocalBroadcastManager.getInstance(this).registerReceiver(tokenReceiver,new IntentFilter("tokenReceiver"));

        sp = getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);

        checkVersion();

        initializeFields();

        addMoreVehicle = (FloatingActionButton) findViewById(R.id.fab);
        addMoreVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddVehicleDialog();
            }
        });

        addAlternateNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddAlternateNoDialog();
            }
        });

        addStatus = (TextView) findViewById(R.id.textView3);
        addStatus.setVisibility(View.GONE);
        addStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddStatusDialog();
            }
        });

        dp = (ImageView) findViewById(imageView);
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(cameraIntent, CAMERA_REQUEST);

                CropImage.activity().setAspectRatio(1, 1).start(MainActivity.this);

            }
        });
        if (checkPermissions()) {

        } else {
            checkPermissions();
        }

        ImageView ivStatus = (ImageView) findViewById(R.id.iv_status);
        ProgressBar pBar = (ProgressBar) findViewById(R.id.progressBar);
        Button btnMain = (Button) findViewById(R.id.btn_action);
        btnAction = ProgressButtonRounded.newInstance(this, ivStatus, pBar, btnMain);
        btnAction.setText("Submit");

//        submit = (Button) findViewById(R.id.button);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vehicle1Field.getText().toString().isEmpty() || vt1.getText().toString().isEmpty() || userNameField.getText().toString().isEmpty() || userCityField.getText().toString().isEmpty() || userCountryField.getText().toString().isEmpty() || userMobileField.getText().toString().isEmpty() || alternateNo1Field.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Please, Enter all necessary details.");
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.show();
                } else if ((!userMobileField.getText().toString().isEmpty()) && (userMobileField.getText().toString().length() != 10)) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Please, Enter a 10 digit Primary Mobile No.");
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.show();
                } else if ((!alternateNo1Field.getText().toString().isEmpty()) && (alternateNo1Field.getText().toString().length() != 10)) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Please, Enter a 10 digit Alternate Mobile No. 1");
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.show();
                } else if (vehicle2.isEmpty() != vehicleType2.isEmpty()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Please fill both the fields for vehicle 2.");
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.show();
                } else if (vehicle3.isEmpty() != vehicleType3.isEmpty()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Please fill both the fields for vehicle 3.");
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.show();
                } else if (vehicle4.isEmpty() != vehicleType4.isEmpty()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Please fill both the fields for vehicle 4.");
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.show();
                } else if ((!email.getText().toString().isEmpty()) && (!(email.getText().toString().contains("@")) || !(email.getText().toString().contains(".")))) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Please enter a valid E-mail address.");
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.show();


                } else if (!(password.getText().toString().equals(confirmPassword.getText().toString()))) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Password doesn't match Confirm Password.");
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.show();
                } else if (password.getText().toString().length() < 6) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Password should be atleast 6 characters long.");
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.show();
                } else {
                    ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                    if (isConnected) {
                        registerUserAsyncTask = new RegisterUserAsyncTask(MainActivity.this);
                        registerUserAsyncTask.execute();
                    } else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage(getResources().getString(R.string.alert_no_internet));
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
        });

    }

    private void initializeFields() {

        vt1 = (EditText) findViewById(R.id.editText_vt);
        vehicle1Field = (EditText) findViewById(R.id.editText);
        userNameField = (EditText) findViewById(R.id.editText3);
        userCityField = (EditText) findViewById(R.id.editText4);
        userCountryField = (EditText) findViewById(R.id.user_country);
        userMobileField = (EditText) findViewById(R.id.editText5);
        alternateNo1Field = (EditText) findViewById(R.id.editText6);
        email = (EditText) findViewById(R.id.email_edittext);
        password = (EditText) findViewById(R.id.password_edittext);
        confirmPassword = (EditText) findViewById(R.id.confirm_password_edittext);
        addAlternateNo = (FloatingActionButton) findViewById(R.id.fab2);

    }

    private void openAddStatusDialog() {

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View subView = inflater.inflate(R.layout.add_status_dialog, null);
        final EditText v2 = (EditText) subView.findViewById(R.id.status);
        v2.setText(status);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(subView);

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                status = v2.getText().toString();
                addStatus.setVisibility(View.VISIBLE);
                addStatus.setText(status);
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

    private void openAddVehicleDialog() {

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View subView = inflater.inflate(R.layout.add_more_vehicle_dialog, null);
        final EditText v2 = (EditText) subView.findViewById(R.id.vehicle_two);
        final EditText v3 = (EditText) subView.findViewById(R.id.vehicle_three);
        final EditText v4 = (EditText) subView.findViewById(R.id.vehicle_four);
        final EditText vt2 = (EditText) subView.findViewById(R.id.vt2);
        final EditText vt3 = (EditText) subView.findViewById(R.id.vt3);
        final EditText vt4 = (EditText) subView.findViewById(R.id.vt4);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(subView);

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                vehicle2 = v2.getText().toString();
                vehicle3 = v3.getText().toString();
                vehicle4 = v4.getText().toString();

                vehicleType2 = vt2.getText().toString();
                vehicleType3 = vt3.getText().toString();
                vehicleType4 = vt4.getText().toString();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        v2.setText(vehicle2);
        v3.setText(vehicle3);
        v4.setText(vehicle4);
        vt2.setText(vehicleType2);
        vt3.setText(vehicleType3);
        vt4.setText(vehicleType4);

    }

    private void openAddAlternateNoDialog() {

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View subView = inflater.inflate(R.layout.add_alternate_no_dialog, null);
        final EditText v2 = (EditText) subView.findViewById(R.id.alternate_no_two);
        final EditText v3 = (EditText) subView.findViewById(R.id.alternate_no_three);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(subView);

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alternateNo2 = v2.getText().toString();
                alternateNo3 = v3.getText().toString();


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        v2.setText(alternateNo2);
        v3.setText(alternateNo3);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
/*        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Uri image = data.getData();

            CropImage.activity(image).start(this);

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            dp.setImageBitmap(photo);
        }*/

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    final InputStream imageStream = getContentResolver().openInputStream(resultUri);
                    bitmap = BitmapFactory.decodeStream(imageStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dp.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    private class RegisterUserAsyncTask extends AsyncTask<Void, Void, String> {

        private Context context;
        HashMap<String, String> map = new HashMap<String, String>();
        private ProgressDialog progress;

        public RegisterUserAsyncTask(Context context) {
            this.context = context;
        }

        public void onAttach(Context context) {
            this.context = context;
        }

        public void onDetach() {
            this.context = null;
        }

        @Override
        protected void onPreExecute() {

//            progress = new ProgressDialog(MainActivity.this);
//            progress.setMessage("Registering...");
//            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progress.setIndeterminate(true);
//            progress.setCancelable(false);
//            progress.show();
            btnAction.startAnimation();

            map.put("userName", userNameField.getText().toString());
            map.put("vehicleNo1", (vehicle1Field.getText().toString()).replaceAll("\\s", ""));
            map.put("vehicleNo2", vehicle2.replaceAll("\\s", ""));
            map.put("vehicleNo3", vehicle3.replaceAll("\\s", ""));
            map.put("vehicleNo4", vehicle4.replaceAll("\\s", ""));
            map.put("vehicleType1", vt1.getText().toString());
            map.put("vehicleType2", vehicleType2);
            map.put("vehicleType3", vehicleType3);
            map.put("vehicleType4", vehicleType4);
            map.put("city", userCityField.getText().toString());
            map.put("country", userCountryField.getText().toString());
            map.put("primaryMobile", userMobileField.getText().toString());
            map.put("alternateNumber1", alternateNo1Field.getText().toString());
            map.put("alternateNumber2", alternateNo2);
            map.put("alternateNumber3", alternateNo3);
            map.put("email", email.getText().toString());
            map.put("password", password.getText().toString());
            map.put("deviceId", "later");
            map.put("androidVersion", Config.APP_VERSION);

            //map.put("deviceId", regid);
            //appsetings.saveString(Appsetting.FCMDEVICEID,regid);
            //String device =appsetings.getString(Appsetting.FCMDEVICEID);
            //appsetings.saveString(Appsetting.MYNAME,userNameField.getText().toString()+"("+(userCityField.getText().toString())+")"+(vehicle1Field.getText().toString()).replaceAll("\\s",""));

            map.put("status", status);
            //map.put("profilePicture", "later");
            map.put("profilePicture", getStringImage(bitmap));
            //Log.v("imageBitmap", getStringImage(bitmap));
            map.put("shareNumber", "1");

        }

        @Override
        protected String doInBackground(Void... params) {

            String jsonResponse = QueryUtils.submitUserRegistration(map);
//            progress.dismiss();

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String jsonResponse) {

            if (context != null) {
                String success = "";
                try {
                    success = extractSuccessFromUserRegistration(jsonResponse);
                } catch (Exception e) {

                }


                if (success.equals("1")) {
//                    AppUser.setImage(context,getStringImage(bitmap));

                    //final SharedPreferences sp = getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);

                    //SharedPreferences.Editor editor = sp.edit();

                    //editor.putString(Config.PREF_KEY_PRIMARY_MOBILE, userMobileField.getText().toString());

                    //appsetings.saveString(Appsetting.MYPRIMARYPHONENO,userMobileField.getText().toString());
                    //editor.putString(Config.PREF_KEY_SHARE_PRIMARY_MOBILE, "1");
                    //editor.apply();
                    animateButtonAndRevert(jsonResponse);

                } else if (success.equals("0")) {
                    btnAction.revertAnimation();
                    String error = extractErrorFromUserRegistration(jsonResponse);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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



            /*String success=null;
            try {
                success = QueryUtils.extractSuccessFromUserRegistration(jsonResponse);
            }
            catch (NullPointerException e)
            {
                Log.e("Null Pointer", String.valueOf(e));
            }
            finally {
                if(success.equals(null))
                {

                }
            }


            switch (success) {
                case "0": {
                    String error = QueryUtils.extractErrorFromUserRegistration(jsonResponse);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage(error);
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.show();
                    break;
                }
                case "1": {

                    //Toast.makeText(SatisfactionLifeScaleActivity.this, "Data poch gaya bhai!!", Toast.LENGTH_SHORT).show();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage(getResources().getString(R.string.alert_data_saved_successfully));
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    final AlertDialog alert = builder.create();
                    alert.show();
                    break;
                }
                default: {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Something went wrong.");
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();
                    alert.show();
                    break;
                }
            }*/
            }
        }


        private void animateButtonAndRevert(final String jsonResponse) {
            Handler handler = new Handler();

            Runnable runnableRevert = new Runnable() {
                @Override
                public void run() {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle(getResources().getString(R.string.app_name));
                    builder.setMessage(extractPostFromUserRegistration(jsonResponse));
//                builder.setMessage("Success");
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final SharedPreferences sp = getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sp.edit();

                            dialog.dismiss();
//                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
//                            i.putExtra("logoutcheck", "");
//                            startActivity(i);
                            finish();

//                        editor.putString(Config.PREF_KEY_PRIMARY_MOBILE, userMobileField.getText().toString());
//                        //editor.putString(Config.PREF_KEY_SHARE_PRIMARY_MOBILE, userData.optString("something","1"));
//                        editor.putString(Config.PREF_KEY_TOKEN, userData.optString("token",""));
//
//                        editor.apply();



                            //String no = sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, null);

                        /*if (no != null) {
                            Log.v("Mobile Number-", sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, null));
                            Intent i = new Intent(MainActivity.this, MainScreenActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            builder.setMessage(getResources().getString(R.string.alert_something_went_wrong));
                            final AlertDialog alert = builder.create();

                            alert.show();
                        }*/

                        }
                    });

                    final AlertDialog alert = builder.create();
                    alert.show();
                }
            };

            btnAction.revertSuccessAnimation();
            handler.postDelayed(runnableRevert, 1000);
        }

        String extractSuccessFromUserRegistration(String jsonResponse) {

            Log.v("khali h", "Aa gaya");

            // If the JSON string is empty or null, then return early.
            if (jsonResponse.isEmpty() || jsonResponse.equals(null)) {
                Log.v("khali h", "khali h bhai sachi123");
                return "";
            }

            try {

                // Create a JSONObject from the JSON response string
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);

                String success = baseJsonResponse.optString("Success", "");
                return success;

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the event JSON results", e);
            }

            return "";

        }

        String extractErrorFromUserRegistration(String jsonResponse) {

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

            return null;

        }

        String extractPostFromUserRegistration(String jsonResponse) {

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

    private void checkVersion() {
        ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            new VersionCheckAsyncTask().execute();
        } else {
            AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
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

    private class VersionCheckAsyncTask extends AsyncTask<Void, Void, String> {

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

            progress = new ProgressDialog(MainActivity.this);
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


                //   Log.v("ssssss", s);


                final AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
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
                final AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
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

    /*BroadcastReceiver tokenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            regid = intent.getStringExtra("token");
            if(regid != null)
            {
                //send token to your server or what you want to do
            }

        }
    };*/

    public String getStringImage(Bitmap bmp) {

        String encodedImage = "";

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 50, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            return "";
        }


        return encodedImage;
    }


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(MainActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(MainActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                } else {

                    // no permissions granted.
                }
                return;
            }
        }

    }

}















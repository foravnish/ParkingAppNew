package com.newwebinfotech.rishabh.parkingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class UpdateProfileActivity extends AppCompatActivity {

    String userEmail = "", userName = "", userStatus = "", userPhNo = "", userAltNo1 = "", userAltNo2 = "", userAltNo3 = "", userCity = "", userCountry = "", profilePicture = "", vNo1 = "", vNo2 = "", vNo3 = "", vNo4 = "", vehicleType1 = "",vehicleType2 = "", vehicleType3 = "", vehicleType4 = "";
    EditText name, status, city, country, v1, v2, v3, v4, m1, m2, m3, m4, vt1, vt2, vt3, vt4, email;
    ImageView dp;
    Button b1, b2;

    Bitmap bitmap;
    boolean emptyImage=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_new);

        userName = getIntent().getStringExtra("name");
        userCity = getIntent().getStringExtra("city");
        userCountry = getIntent().getStringExtra("country");
        userStatus = getIntent().getStringExtra("status");
        userPhNo = getIntent().getStringExtra("m1");
        userAltNo1 = getIntent().getStringExtra("m2");
        userAltNo2 = getIntent().getStringExtra("m3");
        userAltNo3 = getIntent().getStringExtra("m4");
        vNo1 = getIntent().getStringExtra("v1");
        vNo2 = getIntent().getStringExtra("v2");
        vNo3 = getIntent().getStringExtra("v3");
        vNo4 = getIntent().getStringExtra("v4");
        vehicleType1 = getIntent().getStringExtra("vt1");
        vehicleType2 = getIntent().getStringExtra("vt2");
        vehicleType3 = getIntent().getStringExtra("vt3");
        vehicleType4 = getIntent().getStringExtra("vt4");
        profilePicture = getIntent().getStringExtra("image");
        userEmail = getIntent().getStringExtra("email");


        name = (EditText) findViewById(R.id.update_profile_username);
        city = (EditText) findViewById(R.id.update_profile_city);
        country = (EditText) findViewById(R.id.update_profile_country);
        status = (EditText) findViewById(R.id.update_profile_status);
        v1 = (EditText) findViewById(R.id.update_profile_vehicle_no_1);
        v2 = (EditText) findViewById(R.id.update_profile_vehicle_no_2);
        v3 = (EditText) findViewById(R.id.update_profile_vehicle_no_3);
        v4 = (EditText) findViewById(R.id.update_profile_vehicle_no_4);
        vt1 = (EditText) findViewById(R.id.update_profile_vehicle_type_1);
        vt2 = (EditText) findViewById(R.id.update_profile_vehicle_type_2);
        vt3 = (EditText) findViewById(R.id.update_profile_vehicle_type_3);
        vt4 = (EditText) findViewById(R.id.update_profile_vehicle_type_4);
        m1 = (EditText) findViewById(R.id.update_profile_primary_mobile);
        m2 = (EditText) findViewById(R.id.update_profile_alternate_mobile1);
        m3 = (EditText) findViewById(R.id.update_profile_alternate_mobile2);
        m4 = (EditText) findViewById(R.id.update_profile_alternate_mobile3);
        email = (EditText) findViewById(R.id.update_profile_email);
        dp = (ImageView) findViewById(R.id.update_profile_picture);

        b1 = (Button) findViewById(R.id.cancel_update);
        b2 = (Button) findViewById(R.id.save_update);


        if(!profilePicture.isEmpty())
        {
            emptyImage=false;
            Glide.with(UpdateProfileActivity.this).load(Config.BASE_URL + profilePicture).asBitmap().placeholder(R.drawable.splash_screen).into(dp);
        }

        name.setText(userName);
        city.setText(userCity);
        country.setText(userCountry);
        status.setText(userStatus);
        v1.setText(vNo1);
        v2.setText(vNo2);
        v3.setText(vNo3);
        v4.setText(vNo4);
        vt1.setText(vehicleType1);
        vt2.setText(vehicleType2);
        vt3.setText(vehicleType3);
        vt4.setText(vehicleType4);
        m1.setText(userPhNo);
        m2.setText(userAltNo1);
        m3.setText(userAltNo2);
        m4.setText(userAltNo3);
        email.setText(userEmail);

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(cameraIntent, CAMERA_REQUEST);

                CropImage.activity().setAspectRatio(1, 1).start(UpdateProfileActivity.this);

            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v1.getText().toString().isEmpty() || vt1.getText().toString().isEmpty() || name.getText().toString().isEmpty() || city.getText().toString().isEmpty() || country.getText().toString().isEmpty() || m1.getText().toString().isEmpty() || m2.getText().toString().isEmpty()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
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
                }
                else if((!m1.getText().toString().isEmpty()) && (m1.getText().toString().length() != 10))
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
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
                }
                else if((!m2.getText().toString().isEmpty()) && (m2.getText().toString().length() != 10))
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
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
                }
                else if (v2.getText().toString().isEmpty()!=vt2.getText().toString().isEmpty())
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
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
                }
                else if (v3.getText().toString().isEmpty()!=vt3.getText().toString().isEmpty())
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
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
                }
                else if (v4.getText().toString().isEmpty()!=vt4.getText().toString().isEmpty())
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
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
                }
                else {
                    ConnectivityManager cm = (ConnectivityManager) UpdateProfileActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                    if (isConnected) {
                        UpdateUserAsyncTask task = new UpdateUserAsyncTask();
                        task.execute();
                    } else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
                        builder.setMessage(getResources().getString(R.string.alert_no_internet));
                        final AlertDialog alert = builder.create();
                        alert.show();
                    }
                }

            }
        });

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

    private class UpdateUserAsyncTask extends AsyncTask<Void, Void, String> {

        HashMap<String, String> map = new HashMap<String, String>();
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {

            progress = new ProgressDialog(UpdateProfileActivity.this);
            progress.setMessage("Updating...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            if(!(dp.getDrawable()==null))
            {
                bitmap = ((BitmapDrawable)dp.getDrawable()).getBitmap();
                map.put("profilePicture", getStringImage(bitmap));
            }
            else
            {
                map.put("profilePicture", "");
            }

            map.put("userName", name.getText().toString());
            map.put("vehicleNo1", (v1.getText().toString()).replaceAll("\\s", ""));
            map.put("vehicleNo2", (v2.getText().toString()).replaceAll("\\s", ""));
            map.put("vehicleNo3", (v3.getText().toString()).replaceAll("\\s", ""));
            map.put("vehicleNo4", (v4.getText().toString()).replaceAll("\\s", ""));
            map.put("vt1", vt1.getText().toString());
            map.put("vt2", vt2.getText().toString());
            map.put("vt3", vt3.getText().toString());
            map.put("vt4", vt4.getText().toString());
            map.put("city", city.getText().toString());
            map.put("country", country.getText().toString());
            map.put("primaryMobile", m1.getText().toString());
            map.put("alternateNumber1", m2.getText().toString());
            map.put("alternateNumber2", m3.getText().toString());
            map.put("alternateNumber3", m4.getText().toString());
            map.put("status", status.getText().toString());
            map.put("email", email.getText().toString());

            //map.put("profilePicture", "later");


            /*if (emptyImage)
            {
                map.put("profilePicture", getStringImage(bitmap));
            }
            else
            {
                Drawable dr = dp.getDrawable();
                bitmap = (Bitmap) ((GlideBitmapDrawable)dr.getCurrent()).getBitmap();
                map.put("profilePicture", getStringImage(bitmap));
            }*/


            //Log.v("imageBitmap", getStringImage(bitmap));
            map.put("deviceId", "");
            SharedPreferences sp = getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);
            map.put("something", sp.getString(Config.PREF_KEY_SHARE_PRIMARY_MOBILE, "1"));
            map.put("token", sp.getString(Config.PREF_KEY_TOKEN,""));
        }

        @Override
        protected String doInBackground(Void... params) {

            String jsonResponse = QueryUtils.updateUserProfile(map);
            progress.dismiss();

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String jsonResponse) {

            String success = "";
            try {
                success = extractSuccessFromUpdateUser(jsonResponse);
            } catch (Exception e) {

            }


            if (success.equals("1")) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
                builder.setCancelable(false);
                builder.setTitle(getResources().getString(R.string.app_name));
                builder.setMessage(extractPostFromUserUpdate(jsonResponse));
                builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.dismiss();

                        Intent i = new Intent(UpdateProfileActivity.this, MainScreenActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("logoutcheck","");
                        startActivity(i);

                    }
                });

                final AlertDialog alert = builder.create();

                alert.show();
            } else if (success.equals("0")) {
                String error = extractErrorFromUserUpdate(jsonResponse);

                final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
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
            else if (success.equals("-1")) {
                final AlertDialog.Builder build = new AlertDialog.Builder(UpdateProfileActivity.this);
                build.setCancelable(false);
                build.setMessage(getResources().getString(R.string.token_expired));

                build.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                SharedPreferences s = getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = s.edit();

                                editor.putString(Config.PREF_KEY_PRIMARY_MOBILE, "");
                                editor.apply();
                                finishAffinity();
                            }
                        });

                AlertDialog alert = build.create();
                alert.show();
            }
            else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
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

        String extractSuccessFromUpdateUser(String jsonResponse) {

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

        String extractErrorFromUserUpdate(String jsonResponse) {

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

        String extractPostFromUserUpdate(String jsonResponse) {

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

            return null;

        }

    }

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

}

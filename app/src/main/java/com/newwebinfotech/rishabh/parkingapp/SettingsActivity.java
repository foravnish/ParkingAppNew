package com.newwebinfotech.rishabh.parkingapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.newwebinfotech.rishabh.parkingapp.database.DBOperation;
import com.newwebinfotech.rishabh.parkingapp.model.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences sp;
    SwitchCompat s,s2;
    String mNo;
    String switchValue = "";
    boolean switchChange = true;
    DBOperation dbOperation;
    DBOperation.DatabaseHelper dbhelper;
    AdView adView;

    String password="",confirmPassword="", oldPassword="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        adView = (AdView) findViewById(R.id.search_ad_view);

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);

        dbOperation = new DBOperation(this);
        dbhelper = dbOperation.new DatabaseHelper(getApplicationContext());

        s =  (SwitchCompat) findViewById(R.id.switch1);
        s2 = (SwitchCompat) findViewById(R.id.switch3);

        sp = getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);

        mNo = sp.getString(Config.PREF_KEY_PRIMARY_MOBILE,"");

        updateSwitch();

        new CheckAction(SettingsActivity.this).execute();


        s2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    new SetAction(SettingsActivity.this,"ON").execute();

                   // Toast.makeText(SettingsActivity.this, "true", Toast.LENGTH_SHORT).show();
                    s2.setChecked(true);
                }
                else{
                    new SetAction(SettingsActivity.this,"OFF").execute();

                   // Toast.makeText(SettingsActivity.this, "false", Toast.LENGTH_SHORT).show();
                    s2.setChecked(false);
                }
            }
        });

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    switchValue="1";
                }
                else
                {
                    switchValue="0";
                }

                if(switchChange)
                {
                    ConnectivityManager cm = (ConnectivityManager) SettingsActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                    if (isConnected) {
                        Log.v("switchChanged","true");
                        SharePrimaryNumberAsyncTask task = new SharePrimaryNumberAsyncTask();
                        task.execute();
                    } else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
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

                switchChange = true;

            }
        });

        TextView deleteAccount = (TextView) findViewById(R.id.delete_account);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAccountAsyncTask task = new DeleteAccountAsyncTask();
                task.execute();
            }
        });

        LinearLayout changePassword = (LinearLayout) findViewById(R.id.change_password);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangePasswordDialog();
            }
        });

        TextView terms = (TextView) findViewById(R.id.terms);
        TextView aboutUs = (TextView) findViewById(R.id.about_us);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, TermsActivity.class));
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, AboutUsActivity.class));
            }
        });

    }

    private void openChangePasswordDialog() {

        LayoutInflater inflater = LayoutInflater.from(SettingsActivity.this);
        View subView = inflater.inflate(R.layout.change_password_dialog, null);

        final EditText v1 = (EditText) subView.findViewById(R.id.old_password);
        final EditText v2 = (EditText) subView.findViewById(R.id.new_password);
        final EditText v3 = (EditText) subView.findViewById(R.id.new_confirm_password);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(subView);

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                oldPassword = v1.getText().toString();
                password = v2.getText().toString();
                confirmPassword = v3.getText().toString();

                if(oldPassword.isEmpty()||password.isEmpty()||confirmPassword.isEmpty())
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
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
                else if(!password.equals(confirmPassword))
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Password doesn't match confirm password.");
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.show();
                }
                else if(password.length()<6)
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
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
                }
                else
                {
                    ConnectivityManager cm = (ConnectivityManager) SettingsActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                    if (isConnected) {
                        ChangePasswordAsyncTask task = new ChangePasswordAsyncTask();
                        task.execute();
                    }
                    else
                    {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
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

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }

    private void updateSwitch() {

        String no = sp.getString(Config.PREF_KEY_SHARE_PRIMARY_MOBILE, "1");
        Log.v("Noshare",no);

        if(no.equals("1"))
        {
            s.setChecked(true);
        }
        else if(no.equals("0"))
        {
            s.setChecked(false);
        }


    }

    private class SharePrimaryNumberAsyncTask extends AsyncTask<Void, Void, String> {

        HashMap<String, String> map = new HashMap<String, String>();
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {

            progress = new ProgressDialog(SettingsActivity.this);
            progress.setMessage("Updating...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            Log.v("switchValue", switchValue);

            map.put("shareNumber", switchValue);
            map.put("primaryMobile", mNo);
            map.put("token", sp.getString(Config.PREF_KEY_TOKEN,""));


        }

        @Override
        protected String doInBackground(Void... params) {

            String jsonResponse = QueryUtils.updateUserSharePrimaryMobile(map);
            progress.dismiss();

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String jsonResponse) {

            String success = "";
            try {
                success = extractSuccessFromUpdateUser(jsonResponse);
            } catch (Exception e) {
                Log.e("Null Pointer", String.valueOf(e));
                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
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


            if (success.equals("1")) {

                SharedPreferences.Editor editor = sp.edit();

                editor.putString(Config.PREF_KEY_SHARE_PRIMARY_MOBILE, switchValue);
                editor.apply();

                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setCancelable(false);
                builder.setTitle(getResources().getString(R.string.app_name));
                builder.setMessage(extractPostFromUserUpdate(jsonResponse));
                builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

                final AlertDialog alert = builder.create();

                alert.show();
            } else if (success.equals("0")) {
                String error = extractErrorFromUserUpdate(jsonResponse);

                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
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

                switchChange = false;
                updateSwitch();

            }
            else if (success.equals("-1")) {
                final AlertDialog.Builder build = new AlertDialog.Builder(SettingsActivity.this);
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
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

                switchChange = false;
                updateSwitch();
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

            return "";

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

            return "";

        }

    }

    private class ChangePasswordAsyncTask extends AsyncTask<Void, Void, String> {

        HashMap<String, String> map = new HashMap<String, String>();
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {

            progress = new ProgressDialog(SettingsActivity.this);
            progress.setMessage("Updating...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();



            map.put("password", password);
            map.put("confirmPassword", confirmPassword);
            map.put("oldPassword", oldPassword);
            map.put("primaryMobile", mNo);
            map.put("token", sp.getString(Config.PREF_KEY_TOKEN,""));


        }

        @Override
        protected String doInBackground(Void... params) {

            String jsonResponse = QueryUtils.changePassword(map);
            progress.dismiss();

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String jsonResponse) {

            String success = "";
            try {
                success = extractSuccessFromChangePassword(jsonResponse);
            } catch (Exception e) {

            }


            if (success.equals("1")) {

                SharedPreferences.Editor editor = sp.edit();

                editor.putString(Config.PREF_KEY_SHARE_PRIMARY_MOBILE, switchValue);
                editor.apply();

                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setCancelable(false);
                builder.setTitle(getResources().getString(R.string.app_name));
                builder.setMessage(extractPostFromChangePassword(jsonResponse));
                builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

                final AlertDialog alert = builder.create();

                alert.show();
            } else if (success.equals("0")) {
                String error = extractErrorFromChangePassword(jsonResponse);

                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
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
                final AlertDialog.Builder build = new AlertDialog.Builder(SettingsActivity.this);
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
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

        String extractSuccessFromChangePassword(String jsonResponse) {

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

        String extractErrorFromChangePassword(String jsonResponse) {

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

        String extractPostFromChangePassword(String jsonResponse) {

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

    private class DeleteAccountAsyncTask extends AsyncTask<Void, Void, String> {

        HashMap<String, String> map;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(SettingsActivity.this);
            progress.setMessage("Updating data...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            map = new HashMap<String, String>();
            map.put("primaryMobile", sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, ""));
            map.put("token", sp.getString(Config.PREF_KEY_TOKEN,""));

        }

        @Override
        protected String doInBackground(Void... params) {

            String s = QueryUtils.deleteProfile(map);
            progress.dismiss();
            return s;

        }

        @Override
        protected void onPostExecute(String s) {

			Log.v("DeleteAccountjson",s);
		
		
            String success="";
            try {
                success = extractSuccessFromUserDelete(s);
				Log.v("DeleteAccountSuccess",success);
            }
            catch (Exception e)
            {
                Log.e("Null Pointer", String.valueOf(e));
            }


            if (success.equals("1")) {

                dbhelper.deleteChatFromDB();
                dbhelper.deleteRecentChatFromDB();

                SharedPreferences.Editor editor = sp.edit();

                editor.putString(Config.PREF_KEY_PRIMARY_MOBILE, "");
                editor.apply();

                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setCancelable(false);
                builder.setMessage("You account has been successfully deleted.");
                builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(SettingsActivity.this, SplashScreen.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });

                final AlertDialog alert = builder.create();

                alert.show();
            }
            else if(success.equals("0"))
            {
                String error = extractErrorFromUserDelete(s);

                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
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

            }
            else if (success.equals("-1")) {
                final AlertDialog.Builder build = new AlertDialog.Builder(SettingsActivity.this);
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
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

        String extractSuccessFromUserDelete(String jsonResponse) {

            Log.v("khali h", "Aa gaya");

            // If the JSON string is empty or null, then return early.
            if (jsonResponse.isEmpty() || jsonResponse.equals(null)) {
                Log.v("khali h", "khali h bhai sachi123");
                return "";
            }

            try {

                // Create a JSONObject from the JSON response string
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);

                String success = baseJsonResponse.optString("Success","0");
                return success;

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the event JSON results", e);
            }

            return "";

        }

        String extractErrorFromUserDelete(String jsonResponse) {

            try {

                // Create a JSONObject from the JSON response string
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);

                return baseJsonResponse.optString("error", getResources().getString(R.string.alert_something_went_wrong));

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the event JSON results", e);
            }

            return "";

        }

    }

    private class SetAction extends AsyncTask<String, Void, String>{
        Context context;
        ProgressDialog progressDialog;
        String action;
        boolean isValidation;

        public SetAction(Context  context, String action) {
            this.context = context;
            this.action=action;
            progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            params.put("primaryMobile",  sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, ""));
            params.put("action", action);

            Log.d("gfhbfhfghfgj",sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, ""));
            Log.d("gfhbfhfghfgj",action);

            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest("http://parkingeye.in/app/parking_eye/api/locDefault.php", "GET", params);
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Log.e("response", ": " + s);
            Log.d("hgfhfghfgghfgh",s.toString());

//            Util.cancelPgDialog(progressDialog);

            try {

                JSONObject jsonObject=new JSONObject(s);

//                JSONObject jsonObject11=jsonObject.getJSONObject("msg");
//
//                Log.d("dfgdfgddf",jsonObject11.optString("account"));

//                bal_txv.setText("Balance : "+jsonObject11.optString("retailer_avl_bal"));
//                limit_txv.setText("Remaining Limit : "+jsonObject11.optString("waller_balance"));

                if (jsonObject.optString("Success").equals("1")){
                    Log.d("gdfgdfghddfhdf",jsonObject.optString("message"));
                }

                else if (jsonObject.optString("Success").equals("0")){
                    Log.d("gdfgdfghddfhdf",jsonObject.optString("message"));
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }




        }


    }

    private class CheckAction extends AsyncTask<String, Void, String>{
        Context context;
        ProgressDialog progressDialog;
        String action;
        boolean isValidation;

        public CheckAction(Context  context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            params.put("primaryMobile",  sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, ""));
            //params.put("action", action);

            Log.d("gfhbfhfghfgj",sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, ""));
            //Log.d("gfhbfhfghfgj",action);

            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest("http://parkingeye.in/app/parking_eye/api/locDefault.php", "GET", params);
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Log.e("response", ": " + s);
            Log.d("hgfhfghfgghfgh",s.toString());

//            Util.cancelPgDialog(progressDialog);



            try {

                JSONObject jsonObject=new JSONObject(s);

                if (jsonObject.optString("Success").equals("1")){
                    Log.d("gdfgdfghddfhdf",jsonObject.optString("message"));
                    s2.setChecked(true);

                }

                else if (jsonObject.optString("Success").equals("0")){
                    Log.d("gdfgdfghddfhdf",jsonObject.optString("message"));
                    s2.setChecked(false);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }




        }


    }


}

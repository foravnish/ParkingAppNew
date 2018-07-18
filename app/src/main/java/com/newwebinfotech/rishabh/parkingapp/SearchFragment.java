package com.newwebinfotech.rishabh.parkingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.newwebinfotech.rishabh.parkingapp.model.JSONParser;
import com.newwebinfotech.rishabh.parkingapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class SearchFragment extends Fragment {

    private View rootView;
    String s;
    SharedPreferences sp;

    String userName="", userStatus="", userPhNo="", userAltNo1="", userAltNo2="", userAltNo3="", userId="", vehicleType="", userCity="", profilePicture="";
    public TextView textUserStatus, textUserName, textUserPhNo, textUserAltNo1, textUserAltNo2, textUserAltNo3;
    LinearLayout searchResult;
    ProgressBar progressBar;
    ImageButton call1, call2, call3, call4;
    ImageButton msg1, msg2, msg3, msg4;
    LinearLayout l1, l2, l3, l4;
    ImageView dp;
    AdView adView;
    String showPrimaryMobile="";
    Boolean visibleHint = false;
    EditText e;
    ImageButton track;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getContext().getSharedPreferences(Config.PREF_FILE, MODE_PRIVATE);
        visibleHint = true;

    }


    @Override
    public void onResume() {


        super.onResume();

        if (adView != null) {
            adView.resume();
        }

       // searchResult.setVisibility(View.GONE);
        e.setText(Utils.sharedPreferences.getString("edittext",""));



    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        Utils.sharedPreferences = getActivity().getSharedPreferences("My_Prefences", MODE_PRIVATE);
        Utils.editor= getActivity().getSharedPreferences("My_Prefences", MODE_PRIVATE).edit();



        textUserStatus = (TextView) rootView.findViewById(R.id.search_user_status);
        textUserName = (TextView) rootView.findViewById(R.id.search_user_name);
        textUserPhNo = (TextView) rootView.findViewById(R.id.search_user_primary_no);
        textUserAltNo1 = (TextView) rootView.findViewById(R.id.search_user_alternate1_no);
        textUserAltNo2 = (TextView) rootView.findViewById(R.id.search_user_alternate2_no);
        textUserAltNo3 = (TextView) rootView.findViewById(R.id.search_user_alternate3_no);
        dp = (ImageView) rootView.findViewById(R.id.search_imageView);

        call1 = (ImageButton) rootView.findViewById(R.id.search_user_primary_call);
        call2 = (ImageButton) rootView.findViewById(R.id.search_user_alternate1_call);
        call3 = (ImageButton) rootView.findViewById(R.id.search_user_alternate2_call);
        call4 = (ImageButton) rootView.findViewById(R.id.search_user_alternate3_call);

        msg1 = (ImageButton) rootView.findViewById(R.id.search_user_primary_msg);
        msg2 = (ImageButton) rootView.findViewById(R.id.search_user_alternate1_msg);
        msg3 = (ImageButton) rootView.findViewById(R.id.search_user_alternate2_msg);
        msg4 = (ImageButton) rootView.findViewById(R.id.search_user_alternate3_msg);

        l1 = (LinearLayout) rootView.findViewById(R.id.search_user_primary);
        l2 = (LinearLayout) rootView.findViewById(R.id.search_user_alternate1);
        l3 = (LinearLayout) rootView.findViewById(R.id.search_user_alternate2);
        l4 = (LinearLayout) rootView.findViewById(R.id.search_user_alternate3);

        track=(ImageButton)rootView.findViewById(R.id.track);
        adView = (AdView) rootView.findViewById(R.id.search_ad_view);

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);

        searchResult = (LinearLayout)  rootView.findViewById(R.id.search_result_layout);
        //searchResult.setVisibility(View.VISIBLE);

        progressBar = (ProgressBar) rootView.findViewById(R.id.search_progressBar);

        e = (EditText) rootView.findViewById(R.id.search_edittext);

        TextView search = (TextView) rootView.findViewById(R.id.search_button);

        userId=Utils.sharedPreferences.getString("userId","");
        userName = Utils.sharedPreferences.getString("userName","");
        userCity = Utils.sharedPreferences.getString("city","");
        userPhNo = Utils.sharedPreferences.getString("primaryMobile","");
        userAltNo1 = Utils.sharedPreferences.getString("alternateNumber1","");
        userAltNo2 = Utils.sharedPreferences.getString("alternateNumber2","");
        userAltNo3 = Utils.sharedPreferences.getString("alternateNumber3","");
        userStatus = Utils.sharedPreferences.getString("status","");
        profilePicture = Utils.sharedPreferences.getString("profilePicture","");
        showPrimaryMobile = Utils.sharedPreferences.getString("something","");
        vehicleType = Utils.sharedPreferences.getString("vehicleType","");

        textUserStatus.setText(userStatus);
        if (userName.equals("")) {
            searchResult.setVisibility(View.GONE);
        }
        else{
            textUserName.setText(userName + "(" + userCity + ")\n" + Utils.sharedPreferences.getString("edittext","")+ "(" + vehicleType + ")");
        }
        if(showPrimaryMobile.equals("0"))
        {
            call1.setEnabled(false);
            call1.setClickable(false);
            call1.setVisibility(View.INVISIBLE);
            //msg1.setEnabled(false);
            //msg1.setClickable(false);
            msg1.setVisibility(View.VISIBLE);
            textUserPhNo.setText("XX" + userPhNo.substring(2,4) + "XX" + userPhNo.substring(6,8) + "XX");
        }
        else
        {
            call1.setEnabled(true);
            call1.setClickable(true);
            msg1.setEnabled(true);
            msg1.setClickable(true);
            call1.setVisibility(View.VISIBLE);
            msg1.setVisibility(View.VISIBLE);
            textUserPhNo.setText(userPhNo);
        }

        textUserAltNo1.setText(userAltNo1);
        textUserAltNo2.setText(userAltNo2);
        textUserAltNo3.setText(userAltNo3);

        Glide.with(getContext()).load(Config.BASE_URL + profilePicture).placeholder(R.drawable.splash_screen).into(dp);

//        if(userPhNo.isEmpty())
//        {
//            l1.setVisibility(View.GONE);
//        }
//        if(userAltNo1.isEmpty())
//        {
//            l2.setVisibility(View.GONE);
//        }
//        if(userAltNo2.isEmpty())
//        {
//            l3.setVisibility(View.GONE);
//        }
//        if(userAltNo3.isEmpty())
//        {
//            l4.setVisibility(View.GONE);
//        }



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                s = e.getText().toString();

                if(!s.isEmpty())
                {
                    e.clearFocus();
                    if(getActivity().getCurrentFocus()!=null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }

                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString(Config.PREF_KEY_VICHEL, s.toString());
                    editor.apply();

                    SearchAsyncTask task = new SearchAsyncTask();
                    task.execute();
                }

            }
        });

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CheckAction(getActivity(),textUserPhNo.getText().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//                sendNotification();

//                NotificationCompat.Builder builder=new NotificationCompat.Builder(getActivity());
//                builder.setSmallIcon(R.drawable.ic_launcher);
//                builder.setContentTitle("Parking");
//                builder.setContentText("Tracking");
//                builder.setDefaults(Notification.DEFAULT_ALL);
//                builder.setAutoCancel(true);
//                Intent intent=new Intent(getActivity(),TrackersActivity.class);
//                intent.putExtra("data",textUserName.getText().toString());
//                TaskStackBuilder stackBuilder= TaskStackBuilder.create(getActivity());
//                stackBuilder.addParentStack(TrackersActivity.class);
//                stackBuilder.addNextIntent(intent);
//                PendingIntent pendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_CANCEL_CURRENT);
//                builder.setContentIntent(pendingIntent);
//                NotificationManager nm= (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//                nm.notify(0,builder.build());



//                NotificationManager notif=(NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//                Notification notify=new Notification.Builder
//                        (getActivity()).setContentTitle("Track").setContentText("Tracking").
//                        setDefaults(Notification.DEFAULT_ALL).
//                        setContentTitle("Track").setSmallIcon(R.mipmap.ic_launcher).build();
//
//                Intent resultIntent = new Intent(getActivity(), MainActivity.class);
//
//                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
//
//                // Adds the back stack for the Intent
//                stackBuilder.addParentStack(getActivity());
//
//                stackBuilder.addNextIntent(resultIntent);
//
//                notify.flags |= Notification.FLAG_AUTO_CANCEL;
//                notif.notify(0, notify);
//


            }
        });
        call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userPhNo.isEmpty())
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+userPhNo));
                    startActivity(intent);
                }
            }
        });

        call2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userAltNo1.isEmpty())
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+userAltNo1));
                    startActivity(intent);
                }
            }
        });

        call3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userAltNo2.isEmpty())
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+userAltNo2));
                    startActivity(intent);
                }
            }
        });

        call4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userAltNo3.isEmpty())
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+userAltNo3));
                    startActivity(intent);
                }
            }
        });

        msg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userPhNo.isEmpty())
                {

//                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
//                    smsIntent.setData(Uri.parse("sms:"+userPhNo));
//                    smsIntent.putExtra("sms_body","Body of Message");
//                    startActivity(smsIntent);
                    Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                    intent.putExtra("chattingToName", textUserName.getText().toString());
                    intent.putExtra("chattingToPhone", userPhNo);
                    intent.putExtra("chattingToPic", "");
                    intent.putExtra("chattingToEntityType", "one_to_one");
                    startActivity(intent);

                }
            }
        });

        msg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userAltNo1.isEmpty())
                {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setData(Uri.parse("sms:"+userAltNo1));
                    smsIntent.putExtra("sms_body","Body of Message");
                    startActivity(smsIntent);
                }
            }
        });

        msg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userAltNo2.isEmpty())
                {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setData(Uri.parse("sms:"+userAltNo2));
                    smsIntent.putExtra("sms_body","Body of Message");
                    startActivity(smsIntent);
                }
            }
        });

        msg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userAltNo3.isEmpty())
                {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setData(Uri.parse("sms:"+userAltNo3));
                    smsIntent.putExtra("sms_body","Body of Message");
                    startActivity(smsIntent);
                }
            }
        });

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDP();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }


    private void sendNotification() {

        Log.d("fgdbfddhbfgfg",sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, ""));

        new SenndNotification(getActivity(),textUserPhNo.getText().toString()).execute();
        
//        final StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://parkingeye.in/app/parking_eye/api/location.php?user_phoneno="+textUserPhNo.getText().toString()+"&phoneno="+textUserPhNo.getText().toString(), new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String s) {
//                JSONObject jsonObject = null;
//                Log.d("fdsgdsgdf",s+"");
//                Log.d("fdsgdsgdf",textUserPhNo.getText().toString()+"");
//
//
//                try {
//                    jsonObject = new JSONObject(s);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                //Constants.exitdialog(dialog);
//
//                if (jsonObject.optString("success").equals("1")) {
//
//
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Log.d("fdsgdsgdferreo",volleyError+"");
//                //Constants.exitdialog(dialog);
//                //Crouton.makeText(User_Login.this, "" + "Some problem occured pls try again", Style.INFO).show();
//            }
//        }) {
//            @Override
//            protected Maps<String, String> getParams() throws AuthFailureError {
//                Maps<String, String> params = new HashMap<String, String>();
//
//                params.put("user_phoneno", textUserPhNo.getText().toString());
//                params.put("phoneno", textUserPhNo.getText().toString());
//                Log.d("gfdgbfdhdfdhd",textUserPhNo.getText().toString());
//                Log.d("gfdgbfdhdfdhd","dfghfdhfgh");
//                return params;
//
//            }
//        };

//        App.getInstance().addToRequestQueue(stringRequest);





    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(visibleHint)
        {
            if (isVisibleToUser)
            {
//                searchResult.setVisibility(View.GONE);
//                e.setText("");
            }
        }

    }

    private void showDP() {

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View view = factory.inflate(R.layout.display_picture_dialog, null);
        alert.setView(view);

        Drawable d = dp.getDrawable();

        final ImageView i = (ImageView) view.findViewById(R.id.dialog_imageview);
        i.setImageDrawable(d);

        alert.show();

    }



    private class SearchAsyncTask extends AsyncTask<Void, Void, String> {

        HashMap<String, String> map;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            searchResult.setVisibility(View.GONE);
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);

            call1.setEnabled(true);

            map = new HashMap<String, String>();
//            map.put("vehicle", s.replaceAll("\\s",""));
            map.put("vehicle", sp.getString(Config.PREF_KEY_VICHEL,"").replaceAll("\\s",""));
            map.put("token", sp.getString(Config.PREF_KEY_TOKEN,""));
            map.put("primaryMobile", sp.getString(Config.PREF_KEY_PRIMARY_MOBILE,""));

        }

        @Override
        protected String doInBackground(Void... params) {

            String s = QueryUtils.searchVehicleNo(map);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {

            progressBar.setVisibility(View.GONE);

            String success=null;
            try {
                success = extractSuccessFromUserSearch(s);
            }
            catch (Exception e)
            {

            }


            if (success.equals("1")) {
                try {

                    searchResult.setVisibility(View.VISIBLE);

                    JSONObject baseJsonResponse = new JSONObject(s);

                    JSONObject postJsonResponse = baseJsonResponse.optJSONObject("post");

                    Utils.editor.putString("userId", postJsonResponse.optString("userId",""));
                    Utils.editor.putString("userName", postJsonResponse.optString("userName",""));
                    Utils.editor.putString("city", postJsonResponse.optString("city",""));
                    Utils.editor.putString("primaryMobile", postJsonResponse.optString("primaryMobile",""));
                    Utils.editor.putString("alternateNumber1", postJsonResponse.optString("alternateNumber1",""));
                    Utils.editor.putString("alternateNumber2", postJsonResponse.optString("alternateNumber2",""));
                    Utils.editor.putString("alternateNumber3", postJsonResponse.optString("alternateNumber3",""));
                    Utils.editor.putString("status", postJsonResponse.optString("status",""));
                    Utils.editor.putString("profilePicture", postJsonResponse.optString("profilePicture",""));
                    Utils.editor.putString("vehicleType", postJsonResponse.optString("vehicleType",""));
                    Utils.editor.putString("edittext", e.getText().toString());
                    Utils.editor.commit();

                    userId=Utils.sharedPreferences.getString("userId","");
                    userName = Utils.sharedPreferences.getString("userName","");
                    userCity = Utils.sharedPreferences.getString("city","");
                    userPhNo = Utils.sharedPreferences.getString("primaryMobile","");
                    userAltNo1 = Utils.sharedPreferences.getString("alternateNumber1","");
                    userAltNo2 = Utils.sharedPreferences.getString("alternateNumber2","");
                    userAltNo3 = Utils.sharedPreferences.getString("alternateNumber3","");
                    userStatus = Utils.sharedPreferences.getString("status","");
                    profilePicture = Utils.sharedPreferences.getString("profilePicture","");
                    showPrimaryMobile = Utils.sharedPreferences.getString("something","");
                    vehicleType = Utils.sharedPreferences.getString("vehicleType","");



//                    userId = postJsonResponse.optString("userId","");
//                    userName = postJsonResponse.optString("userName","");
//                    userCity = postJsonResponse.optString("city","");
//                    userPhNo = postJsonResponse.optString("primaryMobile","");
//                    userAltNo1 = postJsonResponse.optString("alternateNumber1","");
//                    userAltNo2 = postJsonResponse.optString("alternateNumber2","");
//                    userAltNo3 = postJsonResponse.optString("alternateNumber3","");
//                    userStatus = postJsonResponse.optString("status","");
//                    profilePicture = postJsonResponse.optString("profilePicture","");
//                    showPrimaryMobile = postJsonResponse.optString("something","");
//                    vehicleType = postJsonResponse.optString("vehicleType","");





                    textUserStatus.setText(userStatus);
                    textUserName.setText(userName + "(" + userCity + ")\n" + Utils.sharedPreferences.getString("edittext","")+ "(" + vehicleType + ")");
                    if(showPrimaryMobile.equals("0"))
                    {
                        call1.setEnabled(false);
                        call1.setClickable(false);
                        call1.setVisibility(View.INVISIBLE);
                        //msg1.setEnabled(false);
                        //msg1.setClickable(false);
                        msg1.setVisibility(View.VISIBLE);
                        textUserPhNo.setText("XX" + userPhNo.substring(2,4) + "XX" + userPhNo.substring(6,8) + "XX");
                    }
                    else
                    {
                        call1.setEnabled(true);
                        call1.setClickable(true);
                        msg1.setEnabled(true);
                        msg1.setClickable(true);
                        call1.setVisibility(View.VISIBLE);
                        msg1.setVisibility(View.VISIBLE);
                        textUserPhNo.setText(userPhNo);
                    }

                    textUserAltNo1.setText(userAltNo1);
                    textUserAltNo2.setText(userAltNo2);
                    textUserAltNo3.setText(userAltNo3);

                    Glide.with(getContext()).load(Config.BASE_URL + profilePicture).placeholder(R.drawable.splash_screen).into(dp);

                    if(userPhNo.isEmpty())
                    {
                        l1.setVisibility(View.GONE);
                    }
                    if(userAltNo1.isEmpty())
                    {
                        l2.setVisibility(View.GONE);
                    }
                    if(userAltNo2.isEmpty())
                    {
                        l3.setVisibility(View.GONE);
                    }
                    if(userAltNo3.isEmpty())
                    {
                        l4.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    // If an error is thrown when executing any of the above statements in the "try" block,
                    // catch the exception here, so the app doesn't crash. Print a log message
                    // with the message from the exception.
                    Log.e("QueryUtils", "Problem parsing the event JSON results", e);
                }
            }
            else if(success.equals("0"))
            {
                String error = extractErrorFromUserSearch(s);

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                final AlertDialog.Builder build = new AlertDialog.Builder(getContext());
                build.setCancelable(false);
                build.setMessage(getResources().getString(R.string.token_expired));

                build.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                SharedPreferences s = getContext().getSharedPreferences(Config.PREF_FILE, MODE_PRIVATE);
                                SharedPreferences.Editor editor = s.edit();

                                editor.putString(Config.PREF_KEY_PRIMARY_MOBILE, "");
                                editor.apply();
                                getActivity().finishAffinity();
                            }
                        });

                AlertDialog alert = build.create();
                alert.show();
            }
            else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

       String extractSuccessFromUserSearch(String jsonResponse) {

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

       String extractErrorFromUserSearch(String jsonResponse) {

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

           return null;

       }

    }

    public class SenndNotification extends AsyncTask<String, Void, String> {
        Context context;
        ProgressDialog progressDialog;
        String url;
        boolean isValidation;

        public SenndNotification(Context context,String url) {
            this.context = context;
            this.url=url;
            progressDialog = new ProgressDialog(context);

            this.isValidation = isValidation;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Track Request Sending...");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            params.put("user_phoneno",  sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, ""));
            params.put("phoneno", url);

            Log.d("gfhbfhfghfgjuser",sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, ""));
            Log.d("gfhbfhfghfgj",url);

            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest("http://parkingeye.in/app/parking_eye/api/location.php", "GET", params);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Log.e("response", ": " + s);
            Log.d("dfgdfhgbdfhdgdfdgh123",s.toString());

//            Util.cancelPgDialog(progressDialog);



            try {

                JSONObject jsonObject=new JSONObject(s);

                JSONObject jsonObject11=jsonObject.getJSONObject("msg");

                Log.d("dfgdfgddf",jsonObject11.optString("account"));

//                bal_txv.setText("Balance : "+jsonObject11.optString("retailer_avl_bal"));
//                limit_txv.setText("Remaining Limit : "+jsonObject11.optString("waller_balance"));

                if (jsonObject.optString("status").equals("1")){

                }

                else if (jsonObject.optString("status").equals("0")){

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
        String num;
        boolean isValidation;

        public CheckAction(Context  context,String num) {
            this.context = context;
            this.num=num;
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
            params.put("primaryMobile", num);
            //params.put("action", action);

            Log.d("dfdgdgdfdhfdhfgh",num);
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

//                JSONObject jsonObject11=jsonObject.getJSONObject("msg");
//
//                Log.d("dfgdfgddf",jsonObject11.optString("account"));

//                bal_txv.setText("Balance : "+jsonObject11.optString("retailer_avl_bal"));
//                limit_txv.setText("Remaining Limit : "+jsonObject11.optString("waller_balance"));

                if (jsonObject.optString("Success").equals("1")){
                    Log.d("gdfgdfghddfhdf",jsonObject.optString("message"));

//                    Intent intent = new Intent(getActivity(), Maps.class);
                    Intent intent = new Intent(getActivity(), UserTracking.class);
                    intent.putExtra("latitude", jsonObject.optString("lat"));
                    intent.putExtra("longitude", jsonObject.optString("long"));
                    intent.putExtra("cMobile", jsonObject.optString("phoneNo"));
                    intent.putExtra("time", "");
                    intent.putExtra("name",jsonObject.optString("phoneNo") );
                    intent.putExtra("json", "");
                    intent.putExtra("type","");
                    intent.putExtra("image","");
                    startActivity(intent);

                }

                else if (jsonObject.optString("Success").equals("0")){
                    Log.d("gdfgdfghddfhdf",jsonObject.optString("message"));
                    sendNotification();
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }




        }


    }

}

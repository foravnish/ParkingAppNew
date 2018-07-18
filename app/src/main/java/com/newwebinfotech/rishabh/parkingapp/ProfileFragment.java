package com.newwebinfotech.rishabh.parkingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.newwebinfotech.rishabh.parkingapp.utils.AppUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ProfileFragment extends Fragment {

    String userName="", userStatus="", userPhNo="", userAltNo1="", userAltNo2="", userAltNo3="", userId="", userCity="", userCountry="", profilePicture="", vehicleType1="", vehicleType2="", vehicleType3="", vehicleType4="", userEmail="";
    EditText name, status, city, country, v1, v2, v3, v4, m1, m2, m3, m4, vt1, vt2, vt3, vt4, email;
    TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, tc, t11, t12, t13, t14, t15;
    ImageView dp;
    ConstraintLayout constraintLayout;
    String[] v = {"","","",""};
    String[] vt = {"","","",""};
    private View rootView;
    boolean userData = false;
    ProgressBar progressBar;

    Button edit;
    AdView adView;
    private ProfileAsyncTask task;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(task!=null){
            task.onAttach(context);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(task!=null){
            task.onDetach();
        }
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile_new, container, false);

//        adView = (AdView) rootView.findViewById(R.id.search_ad_view);
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//
//        adView.loadAd(adRequest);

        userData = false;

        constraintLayout = (ConstraintLayout) rootView.findViewById(R.id.profile_constraint_layout);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar_profile);
        name = (EditText) rootView.findViewById(R.id.profile_username);
        city = (EditText) rootView.findViewById(R.id.profile_city);
        country = (EditText) rootView.findViewById(R.id.profile_country);
        status = (EditText) rootView.findViewById(R.id.profile_status);
        v1 = (EditText) rootView.findViewById(R.id.profile_vehicle_no_1);
        v2 = (EditText) rootView.findViewById(R.id.profile_vehicle_no_2);
        v3 = (EditText) rootView.findViewById(R.id.profile_vehicle_no_3);
        v4 = (EditText) rootView.findViewById(R.id.profile_vehicle_no_4);
        m1 = (EditText) rootView.findViewById(R.id.profile_primary_mobile);
        m2 = (EditText) rootView.findViewById(R.id.profile_alternate_mobile1);
        m3 = (EditText) rootView.findViewById(R.id.profile_alternate_mobile2);
        m4 = (EditText) rootView.findViewById(R.id.profile_alternate_mobile3);
        vt1 = (EditText) rootView.findViewById(R.id.profile_vehicle_type_1);
        vt2 = (EditText) rootView.findViewById(R.id.profile_vehicle_type_2);
        vt3 = (EditText) rootView.findViewById(R.id.profile_vehicle_type_3);
        vt4 = (EditText) rootView.findViewById(R.id.profile_vehicle_type_4);
        email = (EditText) rootView.findViewById(R.id.profile_user_email);

        t1 = (TextView) rootView.findViewById(R.id.textView2);
        t2 = (TextView) rootView.findViewById(R.id.textView3);
        t3 = (TextView) rootView.findViewById(R.id.textView4);
        t4 = (TextView) rootView.findViewById(R.id.textView5);
        t5 = (TextView) rootView.findViewById(R.id.textView6);
        t6 = (TextView) rootView.findViewById(R.id.textView7);
        t7 = (TextView) rootView.findViewById(R.id.textView8);
        t8 = (TextView) rootView.findViewById(R.id.textView9);
        t9 = (TextView) rootView.findViewById(R.id.textView10);
        t10 = (TextView) rootView.findViewById(R.id.textView11);
        t11 = (TextView) rootView.findViewById(R.id.textView12);
        t12 = (TextView) rootView.findViewById(R.id.textView13);
        t13 = (TextView) rootView.findViewById(R.id.textView14);
        t14 = (TextView) rootView.findViewById(R.id.textView15);
        t15 = (TextView) rootView.findViewById(R.id.textView16);

        tc = (TextView) rootView.findViewById(R.id.textViewCountry);

        //hideViews(true, constraintLayout);

        constraintLayout.setVisibility(View.GONE);

        edit = (Button) rootView.findViewById(R.id.edit_button);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userData)
                {
                    Intent i = new Intent(getContext(), UpdateProfileActivity.class);
                    i.putExtra("v1", v[0]);
                    i.putExtra("v2", v[1]);
                    i.putExtra("v3", v[2]);
                    i.putExtra("v4", v[3]);
                    i.putExtra("vt1", vt[0]);
                    i.putExtra("vt2", vt[1]);
                    i.putExtra("vt3", vt[2]);
                    i.putExtra("vt4", vt[3]);
                    i.putExtra("name", userName);
                    i.putExtra("city", userCity);
                    i.putExtra("country", userCountry);
                    i.putExtra("m1", userPhNo);
                    i.putExtra("m2", userAltNo1);
                    i.putExtra("m3", userAltNo2);
                    i.putExtra("m4", userAltNo3);
                    i.putExtra("status", userStatus);
                    i.putExtra("image", profilePicture);
                    i.putExtra("email", userEmail);

                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getContext(), "Please wait!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dp = (ImageView) rootView.findViewById(R.id.profile_picture);

        task = new ProfileAsyncTask(getContext());
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        // Inflate the layout for this fragment
        return rootView;

    }

    private class ProfileAsyncTask extends AsyncTask<Void, Void, String> {

        private Context context;
        HashMap<String, String> map;
        private ProgressDialog progress;


        public void onAttach(Context context) {
            this.context=context;
        }

        public void onDetach() {
            this.context=null;
        }

        public ProfileAsyncTask(Context context) {
            this.context=context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getContext());
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(true);
            progress.show();

            progressBar.setVisibility(View.VISIBLE);
            SharedPreferences sp = getContext().getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);

            String no = sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, "");
            map = new HashMap<String, String>();
            map.put("primaryMobile", no);
            map.put("token", sp.getString(Config.PREF_KEY_TOKEN,""));

        }

        @Override
        protected String doInBackground(Void... params) {

            String s = QueryUtils.getProfileInfo(map);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {

            if(context!=null){
                progress.dismiss();

                String success="";
                try {
                    success = extractSuccessFromProfileInfo(s);
                }
                catch (Exception e)
                {
                    Log.e("Null Pointer", String.valueOf(e));
                }


                if (success.equals("1")) {
                    Log.v("ProfileLog","Aa gaya1");
                    try {

                        Log.v("ProfileLog","Aa gaya2");

                        JSONObject baseJsonResponse = new JSONObject(s);

                        JSONArray postJsonResponse = baseJsonResponse.optJSONArray("post");

                        userName = postJsonResponse.optJSONObject(0).optString("userName","");
                        userCity = postJsonResponse.optJSONObject(0).optString("city","");
                        userCountry = postJsonResponse.optJSONObject(0).optString("country","");
                        userPhNo = postJsonResponse.optJSONObject(0).optString("primaryMobile","");
                        userAltNo1 = postJsonResponse.optJSONObject(0).optString("alternateNumber1","");
                        userAltNo2 = postJsonResponse.optJSONObject(0).optString("alternateNumber2","");
                        userAltNo3 = postJsonResponse.optJSONObject(0).optString("alternateNumber3","");
                        userStatus = postJsonResponse.optJSONObject(0).optString("status","");
                        profilePicture = postJsonResponse.optJSONObject(0).optString("profilePicture","");
                        userEmail = postJsonResponse.optJSONObject(0).optString("email","");



                        for(int i=0; i< postJsonResponse.length(); i++)
                        {
                            v[i] = postJsonResponse.optJSONObject(i).optString("vehicleNo","");
                            vt[i] = postJsonResponse.optJSONObject(i).optString("vehicleType","");
                        }

                        userData = true;

                        status.setText(userStatus);
                        name.setText(userName);
                        m1.setText(userPhNo);
                        m2.setText(userAltNo1);
                        m3.setText(userAltNo2);
                        m4.setText(userAltNo3);
                        city.setText(userCity);
                        country.setText(userCountry);
                        v1.setText(v[0]);
                        v2.setText(v[1]);
                        v3.setText(v[2]);
                        v4.setText(v[3]);
                        vt1.setText(vt[0]);
                        vt2.setText(vt[1]);
                        vt3.setText(vt[2]);
                        vt4.setText(vt[3]);
                        email.setText(userEmail);

                        //hideViews(false, constraintLayout);
                        constraintLayout.setVisibility(View.VISIBLE);
                        checkEmptyViews();
                        AppUser.setImage(getContext(),Config.BASE_URL + profilePicture);

                        Glide.with(getContext()).load(Config.BASE_URL + profilePicture).into(dp);

                    } catch (JSONException e) {
                        // If an error is thrown when executing any of the above statements in the "try" block,
                        // catch the exception here, so the app doesn't crash. Print a log message
                        // with the message from the exception.
                        Log.e("QueryUtils", "Problem parsing the event JSON results", e);
                    }
                }
                else if(success.equals("0"))
                {
                    String error = extractErrorFromProfileInfo(s);

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
                                    SharedPreferences s = getContext().getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);
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
                    Log.v("ProfileLog","Aa gaya3");
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

                progressBar.setVisibility(View.GONE);

            }
        }

        private String extractSuccessFromProfileInfo(String jsonResponse) {
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

        String extractErrorFromProfileInfo(String jsonResponse) {

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

    @Override
    public void onResume() {
        super.onResume();



    }

    private void checkEmptyViews() {

        if(status.getText().toString().isEmpty())
        {
            status.setVisibility(View.GONE);
        }
        if(city.getText().toString().isEmpty())
        {
            city.setVisibility(View.GONE);
            t6.setVisibility(View.GONE);
        }
        if(country.getText().toString().isEmpty())
        {
            country.setVisibility(View.GONE);
            tc.setVisibility(View.GONE);
        }
        if(name.getText().toString().isEmpty())
        {
            t1.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
        }

        if(m1.getText().toString().isEmpty())
        {
            t7.setVisibility(View.GONE);
            m1.setVisibility(View.GONE);
        }
        if(m2.getText().toString().isEmpty())
        {
            t8.setVisibility(View.GONE);
            m2.setVisibility(View.GONE);
        }
        if(m3.getText().toString().isEmpty())
        {
            t9.setVisibility(View.GONE);
            m3.setVisibility(View.GONE);
        }
        if(m4.getText().toString().isEmpty())
        {
            t10.setVisibility(View.GONE);
            m4.setVisibility(View.GONE);
        }

        if(v1.getText().toString().isEmpty())
        {
            t2.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
        }
        if(v2.getText().toString().isEmpty())
        {
            t3.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
        }
        if(v3.getText().toString().isEmpty())
        {
            t4.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
        }
        if(v4.getText().toString().isEmpty())
        {
            t5.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
        }

        if(vt1.getText().toString().isEmpty())
        {
            t11.setVisibility(View.GONE);
            vt1.setVisibility(View.GONE);
        }
        if(vt2.getText().toString().isEmpty())
        {
            t12.setVisibility(View.GONE);
            vt2.setVisibility(View.GONE);
        }
        if(vt3.getText().toString().isEmpty())
        {
            t13.setVisibility(View.GONE);
            vt3.setVisibility(View.GONE);
        }
        if(vt4.getText().toString().isEmpty())
        {
            t14.setVisibility(View.GONE);
            vt4.setVisibility(View.GONE);
        }
        if(email.getText().toString().isEmpty())
        {
            t15.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
        }

    }

    private void hideViews(boolean hide, ViewGroup vg){
        for (int i = 0; i < vg.getChildCount(); i++){
            View child = vg.getChildAt(i);
            if (hide)
            {
                child.setVisibility(View.GONE);
            }
            else
            {
                child.setVisibility(View.VISIBLE);
            }

            if (child instanceof ViewGroup){
                hideViews(hide, (ViewGroup)child);
            }
        }
    }

}

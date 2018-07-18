package com.newwebinfotech.rishabh.parkingapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.newwebinfotech.rishabh.parkingapp.model.JSONParser;

import java.util.HashMap;

public class TrackersActivity extends AppCompatActivity {

    TextView textview;
    Button sendLocation,cancel;
    RadioGroup radioGroup;
    String selectedtext;
    int idx;
    int time;
    String time2="60";
    GPSTracker gps;
    String mobileno;
    SharedPreferences sp;
    Button angry_btn1,angry_btn2,angry_btn3,angry_btn4,angry_btn5,angry_btn6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackers2);

       gps = new GPSTracker(TrackersActivity.this);

        sp = getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);
        //sendLocation=(Button)findViewById(R.id.sendLocation);
        textview=(TextView) findViewById(R.id.textview);
        cancel=(Button)findViewById(R.id.cancel) ;
//        sendLocation=(Button)findViewById(R.id.sendLocation) ;

        angry_btn1=(Button)findViewById(R.id.angry_btn1) ;
        angry_btn2=(Button)findViewById(R.id.angry_btn2) ;
        angry_btn3=(Button)findViewById(R.id.angry_btn3) ;
        angry_btn4=(Button)findViewById(R.id.angry_btn4) ;
        angry_btn5=(Button)findViewById(R.id.angry_btn5) ;
        angry_btn6=(Button)findViewById(R.id.angry_btn6) ;


        textview.setText(getIntent().getStringExtra("chattingToPhone")+"");
//        textview.setText("Want to be Tracking You...");
//        Toast.makeText(getApplicationContext(), getIntent().getStringExtra("data")+"", Toast.LENGTH_SHORT).show();

        mobileno=getIntent().getStringExtra("mobile");
        Log.d("fghfghfghfg",mobileno);
//        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);

//        int radioButtonID = radioGroup.getCheckedRadioButtonId();
//        View radioButton = radioGroup.findViewById(radioButtonID);
//        idx= radioGroup.indexOfChild(radioButton);

//        RadioButton r = (RadioButton)  radioGroup.getChildAt(idx);
//        selectedtext= r.getText().toString();

//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                int radioButtonID = radioGroup.getCheckedRadioButtonId();
//                View radioButton = radioGroup.findViewById(radioButtonID);
//                idx = radioGroup.indexOfChild(radioButton);
//
//                RadioButton r = (RadioButton)  radioGroup.getChildAt(idx);
//                selectedtext= r.getText().toString();
//            }
//        });

        angry_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time=60;
                time2= String.valueOf(time);
                new SendLocation(getApplicationContext(),time2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        angry_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time=24*60;
                time2= String.valueOf(time);
                new SendLocation(getApplicationContext(),time2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        angry_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time=24*60*7;
                time2= String.valueOf(time);
                new SendLocation(getApplicationContext(),time2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        angry_btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time=24*60*30;
                time2= String.valueOf(time);
                new SendLocation(getApplicationContext(),time2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        angry_btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time2= "ever";
                new SendLocation(getApplicationContext(),time2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        angry_btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time2= "never";
                new SendLocation(getApplicationContext(),time2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

//        sendLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (idx==0){
//
//                    //Toast.makeText(TrackersActivity.this, "1 hr", Toast.LENGTH_SHORT).show();
//                }
//                else if (idx==1) {
//
//                    //Toast.makeText(TrackersActivity.this, "1 day", Toast.LENGTH_SHORT).show();
//                }
//                else if (idx==2) {
//
//                    //Toast.makeText(TrackersActivity.this, "1 week", Toast.LENGTH_SHORT).show();
//                }
//                else if (idx==3) {
//
//                    //Toast.makeText(TrackersActivity.this, "1 month", Toast.LENGTH_SHORT).show();
//                }
//                else if (idx==4) {
//
//                   // Toast.makeText(TrackersActivity.this, "ever", Toast.LENGTH_SHORT).show();
//                }
//                else if (idx==5) {
//
//                   // Toast.makeText(TrackersActivity.this, "never", Toast.LENGTH_SHORT).show();
//                }
//
//
//
//                new SendLocation(getApplicationContext(),time2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            }
//        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(TrackersActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alertdialogcustom);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView text = (TextView) dialog.findViewById(R.id.msg_txv);
                text.setText("I am not sending tracking request");
                Button ok = (Button) dialog.findViewById(R.id.btn_ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        time2= "never";
                        new SendLocation(getApplicationContext(),time2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        dialog.dismiss();
                         finish();
                    }
                });
                dialog.show();


            }
        });
    }





    public class SendLocation extends AsyncTask<String, Void, String> {
        Context context;
        ProgressDialog progressDialog;
        String url;
        boolean isValidation;
        String time;

        public SendLocation(Context context,String time) {
            this.context = context;
            this.time=time;
            this.isValidation = isValidation;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setMessage("Please Wait...");
//            progressDialog.setCancelable(true);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            params.put("user_phoneno", sp.getString(Config.PREF_KEY_PRIMARY_MOBILE,""));
            params.put("phoneno", mobileno);
            params.put("timeForMin", time);
            params.put("latitude", String.valueOf(gps.getLatitude()));
            params.put("longitude", String.valueOf(gps.getLongitude()));

//            Toast.makeText(TrackersActivity.this, "Location Sent", Toast.LENGTH_SHORT).show();
            Log.d("gfddhfhfghfg",String.valueOf(gps.getLatitude()));
            Log.d("gfddhfhfghfg",String.valueOf(gps.getLatitude()));
            Log.d("gfddhfhfghfg",sp.getString(Config.PREF_KEY_PRIMARY_MOBILE,""));
            Log.d("gfddhfhfghfg",mobileno);


            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest(Config.TRACK_RESPONSE, "GET", params);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            progressDialog.dismiss();
            Log.e("response", ": " + s);
            Log.d("dfgdfhgbdfhdgdfdgh123",s.toString());

//            startActivity(new Intent(TrackersActivity.this,MainScreenActivity.class) );
            Intent intent1=new Intent(TrackersActivity.this, TrackingStarted.class);
            intent1.putExtra("time",time);
            intent1.putExtra("clientMobile",mobileno);
            startActivity(intent1);
            finish();
        }

    }




}

package com.newwebinfotech.rishabh.parkingapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Abhijit-PC on 03-Nov-17.
 */

public class TrackingStarted extends AppCompatActivity{

    private SharedPreferences sp;
    private Button cancel;
    private Button angry_btn1;
    private String mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_started);

        sp = getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);

        startService(new Intent(this, NotifyService.class));

        mTime=getIntent().getStringExtra("time");
        cancel=(Button)findViewById(R.id.cancel) ;

        angry_btn1=(Button)findViewById(R.id.angry_btn1) ;
        if(mTime!=null){
            long timer = Long.parseLong(mTime);
            timer = timer * 1000 * 60;
////
            new CountDownTimer(timer, 1000) {
                public void onTick(long millisUntilFinished) {
                    int seconds = (int) (millisUntilFinished / 1000) % 60;
                    int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                    int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                    int day = (int) ((millisUntilFinished / (1000 * 60 * 60 * 24)) % 30);
                    angry_btn1.setText("Left Time: " + String.format("%dD %d:%d:%d", day, hours, minutes, seconds));
                }

                public void onFinish() {
                    angry_btn1.setText("00:00:00");
                }
            }.start();
        }

        angry_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTracking();
            }
        });
    }

    private void stopTracking() {
        final Dialog dialog = new Dialog(TrackingStarted.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertdialogcustom);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView text = (TextView) dialog.findViewById(R.id.msg_txv);
        text.setText("Are you sure to abort tracking?");
        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                stopService(new Intent(TrackingStarted.this, NotifyService.class));
                Intent intent1=new Intent(TrackingStarted.this, MainScreenActivity.class);
                intent1.putExtra("logoutcheck","");
                startActivity(intent1);
                finish();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if(0==0){
            stopTracking();
        }else {
            super.onBackPressed();
        }
    }
}

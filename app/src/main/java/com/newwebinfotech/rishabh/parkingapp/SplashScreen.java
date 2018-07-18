package com.newwebinfotech.rishabh.parkingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Intent intent2 = new Intent(SplashScreen.this, NotifyHitApi.class);
        SplashScreen.this.startService(intent2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);

                String no = sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, "");
                Log.v("prefmobile", no);

                if (!no.isEmpty()) {
                    Intent intent1=new Intent(SplashScreen.this, MainScreenActivity.class);
                    intent1.putExtra("logoutcheck","");
                    startActivity(intent1);
                    finish();
                } else {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

}

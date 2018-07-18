package com.newwebinfotech.rishabh.parkingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        TextView about = (TextView) findViewById(R.id.about_us_text);
        about.setText(getResources().getString(R.string.about_us));

    }
}

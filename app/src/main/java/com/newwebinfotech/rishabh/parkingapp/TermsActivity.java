package com.newwebinfotech.rishabh.parkingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TermsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        TextView terms = (TextView) findViewById(R.id.terms_text);
        TextView privacy = (TextView) findViewById(R.id.privacy_text);

        terms.setText(getResources().getString(R.string.terms));
        privacy.setText(getResources().getString(R.string.privacy_policy));

    }
}

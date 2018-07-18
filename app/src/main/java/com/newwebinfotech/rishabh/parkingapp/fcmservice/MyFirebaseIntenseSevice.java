package com.newwebinfotech.rishabh.parkingapp.fcmservice;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.newwebinfotech.rishabh.parkingapp.utils.Appsetting;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;



public class MyFirebaseIntenseSevice  extends FirebaseInstanceIdService {

    private static final String REG_TOKEN="REG_TOKEN";

    @Override
    public void onTokenRefresh() {

        String resent_token= FirebaseInstanceId.getInstance().getToken();
        new Appsetting(this).saveString(Appsetting.FCMDEVICEID, resent_token);
        Log.d("FCN TOKEN GET", "Refreshed token: " + resent_token);

        final Intent intent = new Intent("tokenReceiver");

        final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        intent.putExtra("token",resent_token);
        broadcastManager.sendBroadcast(intent);
    }
}

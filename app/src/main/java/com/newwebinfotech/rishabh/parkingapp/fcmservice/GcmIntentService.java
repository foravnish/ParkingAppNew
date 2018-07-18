package com.newwebinfotech.rishabh.parkingapp.fcmservice;

import android.app.Application;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.Getseter;
import com.newwebinfotech.rishabh.parkingapp.ChatRoomActivity;

import com.newwebinfotech.rishabh.parkingapp.Maps;
import com.newwebinfotech.rishabh.parkingapp.R;
import com.newwebinfotech.rishabh.parkingapp.TrackersActivity;
import com.newwebinfotech.rishabh.parkingapp.UserTracking;
import com.newwebinfotech.rishabh.parkingapp.database.DBOperation;
import com.newwebinfotech.rishabh.parkingapp.database.DatabaseHandler;
import com.newwebinfotech.rishabh.parkingapp.utils.App;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GcmIntentService extends IntentService {
	public static  int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	public static final String TAG = "GcmIntentService";
	private Handler handler;
	String chatmessage;
	String lati,longi,mobileno;
	Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	String chattingToName,chattingToPic;
	Bundle extras;
	DBOperation.DatabaseHelper dbHelper;
	DBOperation dbOperation;
	Application application;
//	Context appContext;
	JSONObject json;
	Intent intent;
	DatabaseHandler db;
	List<Getseter> DataList=new ArrayList<Getseter>();
	public GcmIntentService()
	{
		super("GcmIntentService");
	}
	@Override
	public void onCreate() {
		super.onCreate();
		handler = new Handler();
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

	}
	@Override
	protected void onHandleIntent(Intent intent) {

		extras = intent.getExtras();
//		application = (Application)App.getContext();
//		appContext = (App)application;
//		appContext=getApplicationContext();
		String message=extras.getString("message");
		try {
			json = new JSONObject(message);

			if(!json.getString("entity_type").equalsIgnoreCase(" "))
			{
				if(json.getString("data").equalsIgnoreCase("data")){
					if(json.getString("message")!=null) {

						if (!json.getString("entity_type").equalsIgnoreCase("bigbroadcast")) {
							chatmessage = (json.getString("message"));

						}

					}
				}else if(json.getString("data").equalsIgnoreCase("imagefile")){
					if (json.getString("imagefiledata")!=null) {
						chatmessage = "Image";
					}
				}
			}else{
				chatmessage = json.getString("message");

			}
			if (!extras.isEmpty()) {

				if(json.getString("data").equalsIgnoreCase("data"))
				{
					sendNotification(chatmessage);


				}else if(json.getString("data").equalsIgnoreCase("imagefile")){
					sendNotification("Received Image : "+ chatmessage);

				}

				Log.i(TAG, "Received: " + extras.toString());
			}
			MyFirebaseMessagingService.completeWakefulIntent(intent);

		}catch (Exception e) {
			Log.e(TAG, "Exception: " + e.getMessage());
		}
	}
	private void sendNotification(String msg) {
		Log.d("GCM INTENT SERVICE", "Preparing to send notification...: " + msg);

		try{

			String message=extras.getString("message");
			Log.d("bgfcgbhgfhnbfgh",message);



			json = new JSONObject(message);
			String chattingToPhone =json.getString("user_phoneno");;
			String chattingToEntityType = json.getString("entity_type");;

			dbOperation = new DBOperation(getApplicationContext());
			dbHelper = dbOperation.new DatabaseHelper(getApplicationContext());
			db = new DatabaseHandler(getApplicationContext());

			//DataList=db.getAllCatagory();

//		db.addCatagory(new Getseter(json.getString("user_name"), json.getString("latitude"), json.getString("longitude"), json.getString("user_phoneno"), json.getString("message"), json.getString("phoneno"), null,null ));



			if(json.getString("entity_type").equalsIgnoreCase("one_to_one") ){

//				chattingToName = json.getString("message");;
				chattingToName = json.getString("user_name");;

			}

			if(json.getString("entity_type").equalsIgnoreCase("one_to_one") ){

				chattingToPic = "";

			}

			if (chatmessage.contains("Want To Track You")){
				intent = new Intent(this, TrackersActivity.class);
				intent.putExtra("chattingToPhone", chatmessage);
				intent.putExtra("mobile", json.getString("user_phoneno"));
				NOTIFICATION_ID=2;
//			if(!json.getString("entity_type").equalsIgnoreCase("group")){
//
//				intent.putExtra("chattingToName", chattingToName);
//				if(!json.getString("entity_type").equalsIgnoreCase("bigbroadcast")){
//					intent.putExtra("chattingToPhone", chattingToPhone);
//				}
//				else
//				{
//					intent.putExtra("chattingToPhone", chattingToName);
//				}
//				intent.putExtra("chattingToEntityType", chattingToEntityType);
//				intent.putExtra("chattingToPic", chattingToPic);
//			}
			}


			else if (chatmessage.contains("TrackResponse")){
//				intent = new Intent(this, Maps.class);
				intent = new Intent(this, UserTracking.class);
				intent.putExtra("latitude",  json.getString("latitude"));
				intent.putExtra("longitude",  json.getString("longitude"));
				intent.putExtra("cMobile",json.optString("user_phoneno"));
				intent.putExtra("name", json.getString("user_name"));
				intent.putExtra("time", json.getString("chat_Time_for_delete"));
				intent.putExtra("json", "");
				intent.putExtra("type","");
				intent.putExtra("image","");

				NOTIFICATION_ID=3;
//
//			for (int i=0;i<=DataList.size();i++) {
//
//				if (!json.getString("user_name").equals(DataList.get(0).getID().toString())){
				//  db.addCatagory(new Getseter(json.getString("user_name"), json.getString("latitude"), json.getString("longitude"), json.getString("user_phoneno"), json.getString("message"), json.getString("phoneno"), json.getString("chat_Time_for_delete"),null ));

//				}
//			}



//			if(!json.getString("entity_type").equalsIgnoreCase("group")){
//
//				intent.putExtra("chattingToName", chattingToName);
//				if(!json.getString("entity_type").equalsIgnoreCase("bigbroadcast")){
//					intent.putExtra("chattingToPhone", chattingToPhone);
//				}
//				else
//				{
//					intent.putExtra("chattingToPhone", chattingToName);
//				}
//				intent.putExtra("chattingToEntityType", chattingToEntityType);
//				intent.putExtra("chattingToPic", chattingToPic);
//			}

			}
			else {
				intent = new Intent(this, ChatRoomActivity.class);
				if (!json.getString("entity_type").equalsIgnoreCase("group")) {

					intent.putExtra("chattingToName", chattingToName);
					if (!json.getString("entity_type").equalsIgnoreCase("bigbroadcast")) {
						intent.putExtra("chattingToPhone", chattingToPhone);
					} else {
						intent.putExtra("chattingToPhone", chattingToName);
					}
					intent.putExtra("chattingToEntityType", chattingToEntityType);
					intent.putExtra("chattingToPic", chattingToPic);
				}

			}
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this).setSmallIcon(R.mipmap.ic_launcher)
					.setContentTitle("Parking Eye")
					.setContentText(msg)
					.setAutoCancel(true)
					.setSound(soundUri)
					.setTicker("Message from " + chatmessage);
			mBuilder.setContentIntent(contentIntent);
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

			Log.d(TAG, "Notification sent successfully.");
		}catch (Exception e) {
			Log.e(TAG, "Exception: " + e.getMessage());
		}
	}

}
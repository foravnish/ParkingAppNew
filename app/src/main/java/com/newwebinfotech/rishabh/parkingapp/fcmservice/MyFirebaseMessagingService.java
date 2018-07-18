package com.newwebinfotech.rishabh.parkingapp.fcmservice;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.newwebinfotech.rishabh.parkingapp.ChatRoomActivity;
import com.newwebinfotech.rishabh.parkingapp.database.DBOperation;
import com.newwebinfotech.rishabh.parkingapp.model.ChatPeople;
import com.newwebinfotech.rishabh.parkingapp.model.RecentChatList;
import com.newwebinfotech.rishabh.parkingapp.utils.App;
import com.newwebinfotech.rishabh.parkingapp.utils.DownloadTaskFromNotification;
import com.newwebinfotech.rishabh.parkingapp.utils.FileHandler;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyFirebaseMessagingService extends WakefulBroadcastReceiver {
    String entity_type, message, chat_Time_for_delete, phoneno, sender_gcm_reg_id, messages;
    private static final String TAG = "MyFirebaseMsgService";
    DBOperation.DatabaseHelper dbHelper;
    DBOperation dbOperation;
//    Application application;
    ChatPeople curChatObj;
    RecentChatList recentChat;
    //    App app;
    String chattingToName, chatmessage, chattingToPic, sender_phone_no, sender_name, chattingTolat, chattingTolong;
    String SERVER_SIDE_CHAT_TO_NAME;
    String TO_CHAT_DATE = "6/16/2016";
    Intent i = null;
    Context context;
    JSONObject json;
    Bundle data;
    String serverFileLink, appFilePath;
    FileHandler fileHandler;
    String incomingImage = null;

    DBOperation.DatabaseHelper db;

    @Override

    public void onReceive(Context context, Intent intent) {


        String vv = "";
        data = intent.getExtras();
//        application = (Application) App.getContext();

//        app = (App) application;
        dbOperation = new DBOperation(context);
        dbHelper = dbOperation.new DatabaseHelper(context);
        curChatObj = new ChatPeople();
        recentChat = new RecentChatList();


        try {
            // JSONObject data = json.getJSONObject("message");

            message = data.getString("message");
            json = new JSONObject(message);
            //JSONObject data = json.getJSONObject("message");
            entity_type = json.getString("entity_type");
            chat_Time_for_delete = json.getString("chat_Time_for_delete");
            sender_gcm_reg_id = json.getString("sender_gcm_reg_id");
            phoneno = json.getString("phoneno");

            sender_phone_no = json.getString("user_phoneno");
            sender_name = json.getString("user_name");


            if (entity_type != null) {
                if (entity_type.equalsIgnoreCase("one_to_one")) {

                    SERVER_SIDE_CHAT_TO_NAME = json.getString("user_name");

                }

            }


            /***************************TEXT CHAT MESSAGING*****************************/
            if (json.getString("data").equalsIgnoreCase("data")) {
                messages = json.getString("message");

                if (json.getString("message") != null) {
                    if (ChatRoomActivity.ACTIVE_CHAT_TO_NAME == null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                        String chat_Time = dateFormat.format(new Date()).toString();

                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String chat_Time_for_delete = dateFormat1.format(new Date()).toString();

                        SimpleDateFormat simpledateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        Date date = new Date();
                        String recent_chat_time = simpledateFormat.format(date);


                        switch (entity_type) {
                            case "one_to_one":


                                chattingToName = json.getString("user_name");
                                chattingToPic = "";
                                chatmessage = json.getString("message");

                                curChatObj.setPERSON_NAME(chattingToName);
                                curChatObj.setPERSON_CHAT_MESSAGE(chatmessage);
                                curChatObj.setPERSON_CHAT_TO_FROM("Received");
                                curChatObj.setPERSON_PHONE_NO(json.getString("user_phoneno"));
                                curChatObj.setPERSON_EMAIL("parking@gmail.com");
                                curChatObj.setPERSON_CHAT_TYPE("text");
                                curChatObj.setPERSON_ENTITY_TYPE("one_to_one");
                                curChatObj.setCHAT_MESSAGE_TIME(chat_Time);
                                curChatObj.setTO_CHAT_MESSAGE_TIME_FOR_DELETE(chat_Time_for_delete);
                                curChatObj.setGROUP_SENDER_MESSAGE_NAME("");
                                curChatObj.setCHAT_MESSAGE_DELIVER("true");
                                curChatObj.setTO_CHAT_DATE(TO_CHAT_DATE);
                                dbHelper.addPeopleChatListToDB(curChatObj);


                                recentChat.setCHAT_PERSON_NAME(chattingToName);
                                recentChat.setCHAT_PERSON_PIC(chattingToPic.toString());
                                recentChat.setPERSON_LAST_CHAT("Received: " + chatmessage);
                                recentChat.setPERSON_PHONE_NO(json.getString("user_phoneno"));
                                recentChat.setRECENT_PERSON_ENTITY_TYPE("one_to_one");
                                recentChat.setRECENT_CHAT_READ_UNREAD_STATUS("unread");
                                recentChat.setRECENT_CHAT_TIME(recent_chat_time);
                                recentChat.setDELIVER("");
                                dbHelper.addRecentChatListToDB(recentChat);
                                String incomingChatMessage = null;

                                incomingChatMessage = json.getString("message");

                                i = new Intent("RECENT_TEXT_CHAT_MESSAGE_RECEIVED");
                                i.putExtra("message", incomingChatMessage);
                                i.putExtra("sender_phone", json.getString("user_phoneno"));
                                context.sendBroadcast(i);
                                ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
                                startWakefulService(context, (intent.setComponent(comp)));

                                setResultCode(Activity.RESULT_OK);

                                break;


                            default:
                                break;
                        }


                    } else if (!ChatRoomActivity.ACTIVE_CHAT_TO_NAME.equalsIgnoreCase(SERVER_SIDE_CHAT_TO_NAME)) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                        String chat_Time = dateFormat.format(new Date()).toString();

                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("hh:mm a");
                        String chat_Time_for_delete = dateFormat1.format(new Date()).toString();

                        SimpleDateFormat simpledateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        Date date = new Date();
                        String recent_chat_time = simpledateFormat.format(date);

                        switch (entity_type) {
                            case "one_to_one":

                                chattingToName = json.getString("user_name");
                                chattingToPic = "";

                                chatmessage = json.getString("message");

                                if (chattingToName == null) {


                                }
                                curChatObj.setPERSON_NAME(chattingToName);
                                curChatObj.setPERSON_CHAT_MESSAGE(chatmessage);
                                curChatObj.setPERSON_CHAT_TO_FROM("Received");
                                curChatObj.setPERSON_PHONE_NO(json.getString("user_phoneno"));
                                curChatObj.setPERSON_EMAIL("demo@gmail.com");
                                curChatObj.setPERSON_CHAT_TYPE("text");
                                curChatObj.setPERSON_ENTITY_TYPE("one_to_one");
                                curChatObj.setCHAT_MESSAGE_TIME(chat_Time);
                                curChatObj.setTO_CHAT_MESSAGE_TIME_FOR_DELETE(chat_Time_for_delete);
                                curChatObj.setGROUP_SENDER_MESSAGE_NAME("");
                                curChatObj.setCHAT_MESSAGE_DELIVER("true");
                                curChatObj.setTO_CHAT_DATE(TO_CHAT_DATE);
                                dbHelper.addPeopleChatListToDB(curChatObj);


                                recentChat.setCHAT_PERSON_NAME(chattingToName.toString());
                                recentChat.setCHAT_PERSON_PIC(chattingToPic.toString());
                                recentChat.setPERSON_LAST_CHAT("Received: " + chatmessage);
                                recentChat.setPERSON_PHONE_NO(json.getString("user_phoneno").toString());
                                recentChat.setRECENT_PERSON_ENTITY_TYPE("one_to_one");
                                recentChat.setRECENT_CHAT_READ_UNREAD_STATUS("unread");
                                recentChat.setRECENT_CHAT_TIME(recent_chat_time);
                                recentChat.setDELIVER("");
                                dbHelper.addRecentChatListToDB(recentChat);
                                String incomingChatMessage = null;
                                incomingChatMessage = json.getString("message");
                                i = new Intent("RECENT_TEXT_CHAT_MESSAGE_RECEIVED");
                                i.putExtra("message", incomingChatMessage);
                                i.putExtra("sender_phone", json.getString("user_phoneno"));
                                context.sendBroadcast(i);
                                ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
                                startWakefulService(context, (intent.setComponent(comp)));

                                setResultCode(Activity.RESULT_OK);
                                break;
                            default:
                                break;
                        }

                    } else {
                        String incomingChatMessage = null;

                        incomingChatMessage = json.getString("message");
                        i = new Intent("CHAT_MESSAGE_RECEIVED");
                        i.putExtra("message", incomingChatMessage);
                        i.putExtra("sender_phone", json.getString("user_phoneno"));
                        i.putExtra("user_name", json.getString("user_name"));

                        i.putExtra("entitytype", json.getString("entity_type"));


                        context.sendBroadcast(i);
                    }

                }
            } else if (json.getString("data").equalsIgnoreCase("imagefile")) {


                if (json.getString("imagefiledata") != null)

                {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                    String chat_Time = dateFormat.format(new Date()).toString();
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String chat_Time_for_delete = dateFormat1.format(new Date()).toString();
                    long millisecond = new Date().getTime();
                    long mi = millisecond - 60000;
                    Date datehh = new Date(mi);
                    SimpleDateFormat simpledateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    Date date = new Date();
                    String recent_chat_time = simpledateFormat.format(date);
                    if (ChatRoomActivity.ACTIVE_CHAT_TO_NAME == null) {

                        switch (entity_type) {
                            case "one_to_one":

                                chattingToName = json.getString("user_name");
                                chattingToPic = "";

                                serverFileLink = json.getString("imagefiledata");
                                fileHandler = new FileHandler();
                                String dwnldFileName = serverFileLink.substring(serverFileLink.lastIndexOf("/") + 1);
                                appFilePath = fileHandler.createFolder("Parkingeye Images/", dwnldFileName);
                                final DownloadTaskFromNotification downloadTask = new DownloadTaskFromNotification(context
                                        , appFilePath);

                                downloadTask.execute(serverFileLink);
                                File file = new File(appFilePath);
                                String actual_file_path = file.getAbsoluteFile().toString();
                                curChatObj.setPERSON_NAME(chattingToName);
                                curChatObj.setPERSON_CHAT_MESSAGE(actual_file_path);
                                curChatObj.setPERSON_CHAT_TO_FROM("Received");
                                curChatObj.setPERSON_PHONE_NO(json.getString("user_phoneno"));
                                curChatObj.setPERSON_EMAIL("demo@gmail.com");
                                curChatObj.setPERSON_CHAT_TYPE("image");
                                curChatObj.setPERSON_ENTITY_TYPE("one_to_one");
                                curChatObj.setCHAT_MESSAGE_TIME(chat_Time);
                                curChatObj.setTO_CHAT_MESSAGE_TIME_FOR_DELETE(chat_Time_for_delete);
                                curChatObj.setGROUP_SENDER_MESSAGE_NAME("");
                                curChatObj.setCHAT_MESSAGE_DELIVER("true");
                                curChatObj.setTO_CHAT_DATE(TO_CHAT_DATE);
                                dbHelper.addPeopleChatListToDB(curChatObj);


                                recentChat.setCHAT_PERSON_NAME(chattingToName.toString());
                                recentChat.setCHAT_PERSON_PIC(chattingToPic.toString());
                                recentChat.setPERSON_LAST_CHAT("Received: Image");
                                recentChat.setPERSON_PHONE_NO(json.getString("user_phoneno").toString());
                                recentChat.setRECENT_PERSON_ENTITY_TYPE("one_to_one");
                                recentChat.setRECENT_CHAT_READ_UNREAD_STATUS("unread");
                                recentChat.setRECENT_CHAT_TIME(recent_chat_time);
                                recentChat.setDELIVER("");
                                dbHelper.addRecentChatListToDB(recentChat);
                                incomingImage = json.getString("imagefiledata");
                                i = new Intent("RECENT_IMAGE_RECEIVED");
                                i.putExtra("message", incomingImage);
                                i.putExtra("sender_phone", json.getString("user_phoneno"));
                                context.sendBroadcast(i);
                                ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
                                startWakefulService(context, (intent.setComponent(comp)));

                                setResultCode(Activity.RESULT_OK);

                                break;

                            default:
                                break;
                        }

                    } else if (!ChatRoomActivity.ACTIVE_CHAT_TO_NAME.equalsIgnoreCase(SERVER_SIDE_CHAT_TO_NAME)) {


                        switch (entity_type) {
                            case "one_to_one":

                                chattingToName = json.getString("user_name");
                                chattingToPic = "";

                                serverFileLink = json.getString("imagefiledata");
                                fileHandler = new FileHandler();
                                String dwnldFileName = serverFileLink.substring(serverFileLink.lastIndexOf("/") + 1);
                                appFilePath = fileHandler.createFolder("Parkingeye Images/", dwnldFileName);
                                final DownloadTaskFromNotification downloadTask = new DownloadTaskFromNotification(context, appFilePath);
                                downloadTask.execute(serverFileLink);
                                File file = new File(appFilePath);
                                String actual_file_path = file.getAbsoluteFile().toString();
                                curChatObj.setPERSON_NAME(chattingToName);
                                curChatObj.setPERSON_CHAT_MESSAGE(actual_file_path);
                                curChatObj.setPERSON_CHAT_TO_FROM("Received");
                                curChatObj.setPERSON_PHONE_NO(json.getString("user_phoneno"));
                                curChatObj.setPERSON_EMAIL("demo@gmail.com");
                                curChatObj.setPERSON_CHAT_TYPE("image");
                                curChatObj.setPERSON_ENTITY_TYPE("one_to_one");
                                curChatObj.setCHAT_MESSAGE_TIME(chat_Time);
                                curChatObj.setTO_CHAT_MESSAGE_TIME_FOR_DELETE(chat_Time_for_delete);
                                curChatObj.setGROUP_SENDER_MESSAGE_NAME("");
                                curChatObj.setCHAT_MESSAGE_DELIVER("true");
                                curChatObj.setTO_CHAT_DATE(TO_CHAT_DATE);
                                dbHelper.addPeopleChatListToDB(curChatObj);


                                recentChat.setCHAT_PERSON_NAME(chattingToName.toString());
                                recentChat.setCHAT_PERSON_PIC(chattingToPic.toString());
                                recentChat.setPERSON_LAST_CHAT("Received: Image");
                                recentChat.setPERSON_PHONE_NO(json.getString("user_phoneno").toString());
                                recentChat.setRECENT_PERSON_ENTITY_TYPE("one_to_one");

                                recentChat.setRECENT_CHAT_READ_UNREAD_STATUS("unread");
                                recentChat.setRECENT_CHAT_TIME(recent_chat_time);
                                recentChat.setDELIVER("");
                                dbHelper.addRecentChatListToDB(recentChat);
                                incomingImage = json.getString("imagefiledata");
                                i = new Intent("RECENT_IMAGE_RECEIVED");
                                i.putExtra("message", incomingImage);
                                i.putExtra("sender_phone", json.getString("user_phoneno"));
                                context.sendBroadcast(i);
                                ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
                                startWakefulService(context, (intent.setComponent(comp)));

                                setResultCode(Activity.RESULT_OK);

                                break;


                            default:
                                break;
                        }
                    } else {
                        i = new Intent("IMAGE_FILE_DATA_RECEIVED");
                        String a = json.getString("imagefiledata");
                        i.putExtra("image_file_data", a);
                        i.putExtra("sender_phone", json.getString("user_phoneno"));
                        i.putExtra("entitytype", json.getString("entity_type"));
                        i.putExtra("user_name", json.getString("user_name"));
                        context.sendBroadcast(i);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


}

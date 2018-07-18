package com.newwebinfotech.rishabh.parkingapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.newwebinfotech.rishabh.parkingapp.adapter.ChatListAdapter;
import com.newwebinfotech.rishabh.parkingapp.database.DBOperation;
import com.newwebinfotech.rishabh.parkingapp.model.ChatPeople;
import com.newwebinfotech.rishabh.parkingapp.model.RecentChatList;
import com.newwebinfotech.rishabh.parkingapp.utils.Appsetting;
import com.newwebinfotech.rishabh.parkingapp.utils.ConstantStrings;
import com.newwebinfotech.rishabh.parkingapp.utils.FileHandler;
import com.newwebinfotech.rishabh.parkingapp.utils.RequestHandler;
import com.newwebinfotech.rishabh.parkingapp.utils.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class ChatRoomActivity extends AppCompatActivity {
    EditText edtMessage;
    ImageView submitButton;
    String chat_Time_for_delete, chat_Time_for_delete1, chattingToPhoneNo, CHAT_ROOM_DATE, chattingToEntityType, chattingToPic;
    DBOperation dbOperation;
    DBOperation.DatabaseHelper dbhelper;
    Ringtone r ;
    String mCurrentPhotoPath;
    public static String chattingToName;
    ArrayList<ChatPeople> ChatPeoples;
    ListView chatLV;
    ChatListAdapter chatAdapater;
    protected static int RESULT_LOAD_IMAGE = 10,REQUEST_CAMERA = 11;
    List<NameValuePair> nameValuePairs;
    HttpClient httpclient;
    HttpPost httppost;
    public static String ACTIVE_CHAT_TO_NAME;
    Appsetting appsetting;
    String sender_phoneno;
    String sender_entityType;
    String serverComingContactName,serverComingContactPic;
    ImageView imgselect;
    private String userChoosenTask;
    String sourcefilePath,fileExtension,mime,randomString,appFileName;
    String appFilePath,location,URL;
    FileHandler fileHandler;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private GPSTracker gpsTracker;
    private Location mLocation;
    double latitude, longitude;
    private ProgressDialog pDialog;
    private ActionBar actionBar;
    int type=0;
    TextView tvActionBarUserName;
    public static final int MULTIPLE_PERMISSIONS = 14;
    String[] permissions= new String[]{

            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_FINE_LOCATION,

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        setContentView(R.layout.activity_chat_room);
        edtMessage = (EditText) findViewById(R.id.emojicon_edit_text);
        edtMessage.setSelected(true);
        submitButton = (ImageView) findViewById(R.id.submit_btn);
        chatLV = (ListView) findViewById(R.id.listView_chat);
        CHAT_ROOM_DATE = "6/07/2017";
        chattingToName = "8285858965";
        chattingToEntityType = "one_to_one";

        ChatPeoples = new ArrayList<ChatPeople>();
        appsetting = new Appsetting(ChatRoomActivity.this);
        imgselect=(ImageView)findViewById(R.id.addimage) ;
        fileHandler = new FileHandler();


        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);


        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        View customNav = LayoutInflater.from(this).inflate(R.layout.custom_action, null);
        actionBar.setCustomView(customNav);
        Toolbar parent =(Toolbar) customNav.getParent();//first get parent toolbar of current action bar
        parent.setContentInsetsAbsolute(0,0);
        View v = actionBar.getCustomView();
        ActionBar.LayoutParams lp = (ActionBar.LayoutParams) v.getLayoutParams();
        lp.width = ActionBar.LayoutParams.MATCH_PARENT;
        v.setLayoutParams(lp);

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            displayLocationSettingsRequest(ChatRoomActivity.this);

        }else {
            displayLocation();
        }
        Bundle b = getIntent().getExtras();
        chattingToEntityType = b.getString("chattingToEntityType");
        chattingToName = b.getString("chattingToName");
        chattingToPhoneNo = b.getString("chattingToPhone");
        chattingToPic = b.getString("chattingToPic");
        dbOperation = new DBOperation(this);
        dbhelper = dbOperation.new DatabaseHelper(getApplicationContext());

        chatLV.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        chatLV.setStackFromBottom(true);
        chatLV.setSmoothScrollbarEnabled(true);

        tvActionBarUserName = (TextView)findViewById(R.id.chattingToUserName);
        tvActionBarUserName.setText(chattingToName);

        imgselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermissions()) {
                    selectImage();
                }else {
                    checkPermissions();
                }


            }
        });


        chatLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                // TODO Auto-generated method stub
                int checkedCount = chatAdapater.getSelectedCount();
                if(checkedCount==0){
                    SparseBooleanArray selected = chatAdapater.getSelectedIds();



                    ChatPeople chatpeople =new ChatPeople();
                    type = chatLV.getAdapter().getItemViewType(position);

                    chatpeople = chatAdapater.getItem(position);
                    String a=chatpeople.getPERSON_CHAT_MESSAGE();
                    File selectedImagePath =  new File(chatpeople.getPERSON_CHAT_MESSAGE());


                    if(type==1){

//                        Intent i = new Intent();
//                        i.setAction(Intent.ACTION_VIEW);
//
//                        i.setDataAndType(Uri.fromFile(selectedImagePath), "image/*");


                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri uri;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(ChatRoomActivity.this,  "com.newwebinfotech.rishabh.parkingapp.provider", selectedImagePath);

                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                            intent.setDataAndType(uri, "image/*");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }else {
                            uri = Uri.fromFile(selectedImagePath);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setDataAndType(uri, "image/*");

                        }


                        startActivity(intent);
                    }


                }

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected()) {
                    Utils.showToast(ChatRoomActivity.this, "You have no network enabled, please enable mobile or WIFI network.");
                    return;
                }
                ChatPeople curChatObj = new ChatPeople();
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                String chat_Time = dateFormat.format(new Date()).toString();
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                chat_Time_for_delete = dateFormat1.format(new Date()).toString();
                long millisecond = new Date().getTime();
                long mi = millisecond - 60000;
                Date datehh = new Date(mi);
                chat_Time_for_delete1 = dateFormat1.format(datehh).toString();
                SimpleDateFormat simpledateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date date = new Date();

                String recent_chat_time = simpledateFormat.format(date);
                String time = dbhelper.getPeopleChatlastTimeDataFromDB(chattingToPhoneNo.toString());
                StringTokenizer token = new StringTokenizer(chat_Time_for_delete);
                String date1 = token.nextToken();
                String time1 = token.nextToken();

                if (time == null) {

//                    curChatObj = addToChat(chattingToName,date1.toString(), "grp_notification_server","grp_event_notify","one_to_one",chattingToPhoneNo,chat_Time,chat_Time_for_delete1,"","true",CHAT_ROOM_DATE);
//                    dbhelper.addPeopleChatListToDB(curChatObj);
                } else if (Utils.Checkdate(time, chat_Time_for_delete)) {

                } else {
//                    curChatObj = addToChat(chattingToName,date1.toString(), "grp_notification_server","grp_event_notify","one_to_one",chattingToPhoneNo,chat_Time,chat_Time_for_delete1,"","true",CHAT_ROOM_DATE);
//                    dbhelper.addPeopleChatListToDB(curChatObj);
                }
                switch (chattingToEntityType) {

                    case "one_to_one":

                        if (!edtMessage.getText().toString().matches("")) {
                            if (isConnected()) {
                                curChatObj = addToChat(chattingToName,  edtMessage.getText().toString(), "Sent", "text", "one_to_one", chattingToPhoneNo, chat_Time, chat_Time_for_delete, "", "true", CHAT_ROOM_DATE);
                                dbhelper.addPeopleChatListToDB(curChatObj);
                                populateChatMessages();
                                RecentChatList recentChat = new RecentChatList();
                                sendMessage("one_to_one");
                                recentChat.setCHAT_PERSON_NAME(chattingToName.toString());
                                if (chattingToPic == null) {
                                    recentChat.setCHAT_PERSON_PIC("");
                                } else {
                                    recentChat.setCHAT_PERSON_PIC("");
                                }
                                recentChat.setPERSON_LAST_CHAT("Sent: " +  edtMessage.getText().toString());
                                recentChat.setPERSON_PHONE_NO(chattingToPhoneNo.toString());
                                recentChat.setRECENT_PERSON_ENTITY_TYPE("one_to_one");
                                recentChat.setRECENT_CHAT_READ_UNREAD_STATUS("read");
                                recentChat.setRECENT_CHAT_TIME(recent_chat_time);
                                recentChat.setDELIVER("true");
                                dbhelper.addRecentChatListToDB(recentChat);

                                clearMessageTextBox();
                            } else {
                                Toast.makeText(ChatRoomActivity.this, ConstantStrings.CONNECTION_ERROR, Toast.LENGTH_LONG).show();
                            }
                        }
                        break;


                    default:
                        break;
                }


            }
        });


    }


    public static ChatPeople addToChat(String personName, String chatMessage, String toOrFrom, String type, String entityType, String chattingToPhoneNo, String chatMessageTime, String chatMessageTimeDelete, String senderName, String deliver, String toChatDate) {
        ChatPeople curChatObj = new ChatPeople();
        curChatObj.setPERSON_NAME(personName);
        curChatObj.setPERSON_CHAT_MESSAGE(chatMessage);
        curChatObj.setPERSON_CHAT_TO_FROM(toOrFrom);
        curChatObj.setPERSON_PHONE_NO(chattingToPhoneNo);
        curChatObj.setPERSON_EMAIL("demo@gmail.com");
        curChatObj.setPERSON_CHAT_TYPE(type);
        curChatObj.setPERSON_ENTITY_TYPE(entityType);
        curChatObj.setCHAT_MESSAGE_TIME(chatMessageTime);
        curChatObj.setTO_CHAT_MESSAGE_TIME_FOR_DELETE(chatMessageTimeDelete);
        curChatObj.setGROUP_SENDER_MESSAGE_NAME(senderName);
        curChatObj.setCHAT_MESSAGE_DELIVER(deliver);
        curChatObj.setTO_CHAT_DATE(toChatDate);
        return curChatObj;

    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public void populateChatMessages() {
        System.gc();
        getData();
        if (ChatPeoples.size() > 0) {
            chatAdapater = new ChatListAdapter(this, ChatPeoples);
            chatLV.setAdapter(chatAdapater);
            //chatAdapater.notifyDataSetChanged();
        } else {
            chatLV.setAdapter(null);
        }

    }

    void getData() {

        ChatPeoples.clear();

        Cursor cursor = dbhelper.getPeopleChatDataFromDB(chattingToPhoneNo);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                ChatPeople people = addToChat(cursor.getString(0), cursor.getString(1), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(2), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));
                ChatPeoples.add(people);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }


    void clearMessageTextBox() {

        //edtMessage.clearFocus();
        edtMessage.setText("");


    }


    public void sendMessage(final String entitytype) {
        final String messageToSend = edtMessage.getText().toString().trim();

        switch (entitytype) {
            case "one_to_one":
                final String phoneNoArr = chattingToPhoneNo.toString();

                if (messageToSend.length() > 0) {
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                httpclient = new DefaultHttpClient();
                                httppost = new HttpPost(Config.BASE_URL + "send_message.php");
                                nameValuePairs = new ArrayList<NameValuePair>(1);

                                //nameValuePairs.add(new BasicNameValuePair("message", messageToSend));
                                nameValuePairs.add(new BasicNameValuePair("message", messageToSend));
                                nameValuePairs.add(new BasicNameValuePair("sender_gcm_reg_id", appsetting.getString(Appsetting.FCMDEVICEID)));
                                nameValuePairs.add(new BasicNameValuePair("phoneno", phoneNoArr.toString()));
                                nameValuePairs.add(new BasicNameValuePair("apiKey", "AIzaSyDzOWTU7FDj-c3H8A2VGdb7NCazzoV4vwU"));
                                nameValuePairs.add(new BasicNameValuePair("entity_type", entitytype));
                                nameValuePairs.add(new BasicNameValuePair("chat_Time_for_delete", chat_Time_for_delete));
                                nameValuePairs.add(new BasicNameValuePair("user_phoneno", appsetting.getString(Appsetting.MYPRIMARYPHONENO)));
                                nameValuePairs.add(new BasicNameValuePair("user_name", appsetting.getString(Appsetting.MYNAME)));

                                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                                final String response = httpclient.execute(httppost, responseHandler);

                                if (response.trim().isEmpty()) {

                                }

                            } catch (Exception e) {

                            }
                        }
                    };

                    thread.start();

                }

                break;


            default:
                break;
        }


    }


    @Override
    public void onDestroy() {

        super.onDestroy();

        ACTIVE_CHAT_TO_NAME = null;
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        ACTIVE_CHAT_TO_NAME = null;
    }

    @Override
    public void onResume() {
        super.onResume();


        ACTIVE_CHAT_TO_NAME = chattingToName;

        registerReceiver(broadcastReceiver, new IntentFilter("CHAT_MESSAGE_RECEIVED"));
        registerReceiver(broadcastReceiver, new IntentFilter("IMAGE_FILE_DATA_RECEIVED"));
        registerReceiver(broadcastReceiver, new IntentFilter("AUDIO_FILE_DATA_RECEIVED"));
        registerReceiver(broadcastReceiver, new IntentFilter("VIDEO_FILE_DATA_RECEIVED"));
        registerReceiver(broadcastReceiver, new IntentFilter("OTHER_FILE_DATA_RECEIVED"));
        populateChatMessages();
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent){
            Bundle b = intent.getExtras();
            Log.d("dfgdgddfggdf",intent.getExtras().toString());
            String message = b.getString("message");
            String bold_message = b.getString("BoldMessage");
            sender_phoneno = b.getString("sender_phone");
            sender_entityType = b.getString("entitytype");

            tvActionBarUserName.setText(chattingToName);
            if (!sender_entityType.equalsIgnoreCase("group")) {

                serverComingContactName = b.getString("user_name");
                serverComingContactPic = "";


            } else {


            }


            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            String chat_Time = dateFormat.format(new Date()).toString();

            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            chat_Time_for_delete = dateFormat1.format(new Date()).toString();
            long millisecond = new Date().getTime();
            long mi = millisecond - 60000;
            Date datehh = new Date(mi);
            chat_Time_for_delete1 = dateFormat1.format(datehh).toString();
            SimpleDateFormat simpledateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();
            String recent_chat_time = simpledateFormat.format(date);
            if (intent.hasExtra("message")) {
                if (ACTIVE_CHAT_TO_NAME.equalsIgnoreCase(chattingToName)) {
                    try {
                        Uri notification = Uri.parse("android.resource://com.newwebinfotech.rishabh.parkingapp/raw/sounds");
                        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        r.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (!sender_entityType.equalsIgnoreCase("group")) {
                    if (sender_entityType.equalsIgnoreCase("one_to_one")) {
                        ChatPeople curChatObj = addToChat(serverComingContactName, message, "Received", "text", "one_to_one", sender_phoneno, chat_Time, chat_Time_for_delete, "", "true", CHAT_ROOM_DATE);
                        dbhelper.addPeopleChatListToDB(curChatObj);


                        RecentChatList recentChatForBroadcast = new RecentChatList();
                        recentChatForBroadcast.setCHAT_PERSON_NAME(serverComingContactName.toString());
                        recentChatForBroadcast.setCHAT_PERSON_PIC("");
                        recentChatForBroadcast.setPERSON_LAST_CHAT("Received: " + message);
                        recentChatForBroadcast.setPERSON_PHONE_NO(sender_phoneno.toString());
                        recentChatForBroadcast.setRECENT_PERSON_ENTITY_TYPE("one_to_one");

                        recentChatForBroadcast.setRECENT_CHAT_READ_UNREAD_STATUS("read");
                        recentChatForBroadcast.setRECENT_CHAT_TIME(recent_chat_time);
                        recentChatForBroadcast.setDELIVER("");
                        dbhelper.addRecentChatListToDB(recentChatForBroadcast);


                    }
                    if (ACTIVE_CHAT_TO_NAME.equalsIgnoreCase(serverComingContactName) && ( sender_entityType.equalsIgnoreCase("one_to_one"))) {
                        populateChatMessages();
                    }  else {

                    }
                }
            }

            //------------------------ IMAGE FILE RECEIVING.-----------------------//
            else if (intent.hasExtra("image_file_data")) {
                if (ACTIVE_CHAT_TO_NAME.equalsIgnoreCase(chattingToName)) {
                    try {
                        Uri notification = Uri.parse("android.resource://com.newwebinfotech.rishabh.parkingapp/raw/sounds");
                        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        r.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (b.getString("image_file_data") != null) {

                    String file_link = b.getString("image_file_data");

                    Log.i("User-Name", "Image File Received in Activity " + file_link);


                    String dwnldFileName = file_link.substring(file_link.lastIndexOf("/") + 1);
                    appFilePath = fileHandler.createFolder("Parkingeye Images/", dwnldFileName);
                    new ImageLoadTask().execute(file_link, appFilePath);
                }
            }

            else {
                Toast.makeText(getApplicationContext(), "no data...", Toast.LENGTH_LONG).show();
            }
        }
    };


    @Override
    public void onBackPressed()
    {

        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent i = new Intent(ChatRoomActivity.this,MainScreenActivity.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TASK|i.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("logoutcheck","");
        startActivity(i);
        finish();

    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo","Choose from Gallery","Add Current location",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoomActivity.this);
        builder.setTitle("Attachment");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";

                    cameraIntent();

                }
                else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask ="Choose from Gallery";
                    Intent i=new Intent(Intent.ACTION_PICK   , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);

                } else if (items[item].equals("Add Current location")) {
                    userChoosenTask ="Add Current location";
                    edtMessage.setText(location);

                }
                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String chat_Time = dateFormat.format(new Date()).toString();

        // FETCHING TIME FOR MESSAGE DELETE ON THE BASIS OF TIMESTAMP.
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        chat_Time_for_delete = dateFormat1.format(new Date()).toString();
        long millisecond = new Date().getTime();
        long mi=millisecond-60000;
        Date datehh=new Date(mi);
        SimpleDateFormat simpledateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String recent_chat_time =  simpledateFormat.format(date);


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE && null != data) {
                Bundle extras = data.getExtras();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                //clears any previous errors
                if (networkInfo == null || !networkInfo.isConnected()) {
                    //  btnConfirm.setEnabled(true);
                    Utils.showToast(ChatRoomActivity.this, "You have no network enabled, please enable mobile or WIFI network.");
                    return;
                }
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                sourcefilePath = cursor.getString(columnIndex);
                mime = getMimeType(sourcefilePath);
                fileExtension = sourcefilePath.substring(sourcefilePath.lastIndexOf("."));
                cursor.close();
                if (selectedImage != null) {

                    String getRandomString = generatefourDigitPIN() + "_" + generateSixDigitPIN();
                    appFileName = "IQ_IMG_" + getRandomString + fileExtension;
                    appFilePath = fileHandler.createFolder("parking Images/Sent/", appFileName);

                    fileHandler.copyFile(sourcefilePath, appFilePath);

                    File file = new File(appFilePath);
                    String actual_file_path = file.getAbsoluteFile().toString();
                    ChatPeople curChatObj = addToChat(chattingToName, sourcefilePath, "Sent", "image", chattingToEntityType, chattingToPhoneNo, chat_Time, chat_Time_for_delete, "", "true", CHAT_ROOM_DATE);

                    dbhelper.addPeopleChatListToDB(curChatObj);
                    populateChatMessages();

                    chatAdapater = new ChatListAdapter(this, ChatPeoples);
                    chatLV.setAdapter(chatAdapater);

                    RecentChatList recentChat = new RecentChatList();
                    recentChat.setCHAT_PERSON_NAME(chattingToName.toString());
                    if (chattingToPic == null) {
                        recentChat.setCHAT_PERSON_PIC("");
                    } else {
                        recentChat.setCHAT_PERSON_PIC(chattingToPic.toString());
                    }
                    recentChat.setPERSON_LAST_CHAT("Sent: Image");
                    recentChat.setPERSON_PHONE_NO(chattingToPhoneNo.toString());
                    recentChat.setRECENT_PERSON_ENTITY_TYPE(chattingToEntityType.toString());
                    recentChat.setRECENT_CHAT_READ_UNREAD_STATUS("read");
                    recentChat.setRECENT_CHAT_TIME(recent_chat_time);
                    recentChat.setDELIVER("true");
                    dbhelper.addRecentChatListToDB(recentChat);

                }

                PostImageFile postImage1 = new PostImageFile();
                postImage1.execute(appFilePath);

            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made

                        displayLocation();

                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        displayLocationSettingsRequest(ChatRoomActivity.this);

                        break;
                    default:
                        break;
                }
                break;
        }

    }
    private void displayLocation() {

        gpsTracker = new GPSTracker(getApplicationContext());

        mLocation = gpsTracker.getLocation();
        if(mLocation!=null) {

            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();

            location="http://maps.google.com/?q="+latitude+","+longitude;

        } else {


        }
    }

    public static String getMimeType(String url)
    {

        String type = null;
        if(url.lastIndexOf(".")!=-1){
            String extn = url.substring(url.lastIndexOf(".")+1).toLowerCase() ;//MimeTypeMap.getFileExtensionFromUrl(url);
            if (extn != null) {
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                type = mime.getMimeTypeFromExtension(extn);
            }
        }
        return type;
    }
    public String generatefourDigitPIN()
    {

        int randomPIN = (int)(Math.random()*9000)+1000;
        randomString = String.valueOf(randomPIN);
        return randomString;
    }


    public String generateSixDigitPIN()
    {
        int randomPIN = (int)(Math.random()*900000)+100000;
        randomString = String.valueOf(randomPIN);
        return randomString;
    }


    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {

                            status.startResolutionForResult(ChatRoomActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    private void onCaptureImageResult(Intent data) {

        try {
            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fo;

            sourcefilePath = destination.toString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            String chat_Time = dateFormat.format(new Date()).toString();
            SimpleDateFormat simpledateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();
            String recent_chat_time =  simpledateFormat.format(date);


            if(data==null){
                sourcefilePath= mCurrentPhotoPath;
            }else {
                //Uri selectedImage=data.getData();
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
                sourcefilePath = getRealPathFromURI(tempUri);
                // Toast.makeText(ChatRoomActivity.this,"picture not click. try again.",Toast.LENGTH_SHORT).show();
            }


            mime = getMimeType(sourcefilePath);
//        fileExtension =  sourcefilePath.substring(sourcefilePath.lastIndexOf("."));
//
//        Uri selectedImage=data.getData();
//        String[] filePathColumn={MediaStore.Images.Media.DATA};
//        Cursor cursor=getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//        cursor.moveToFirst();
//        int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
//        sourcefilePath=cursor.getString(columnIndex);
//        mime = getMimeType(sourcefilePath);
//        fileExtension =  sourcefilePath.substring(sourcefilePath.lastIndexOf("."));
//        cursor.close();


            if (sourcefilePath != null)
            {

                String getRandomString = generatefourDigitPIN()+ "_" + generateSixDigitPIN();
                appFileName = "IQ_IMG_" + getRandomString + fileExtension;
                appFilePath = fileHandler.createFolder("parking Images/Sent/",appFileName);

                fileHandler.copyFile(sourcefilePath, appFilePath);

                File file = new File(appFilePath);
                String actual_file_path = file.getAbsoluteFile().toString();

                ChatPeople curChatObj = addToChat(chattingToName, sourcefilePath, "Sent","image",chattingToEntityType,chattingToPhoneNo,chat_Time,chat_Time_for_delete,"","true",CHAT_ROOM_DATE);

                dbhelper.addPeopleChatListToDB(curChatObj);
                populateChatMessages();

                chatAdapater = new ChatListAdapter(this, ChatPeoples);//,photo,mime);
                chatLV.setAdapter(chatAdapater);
                RecentChatList recentChat = new RecentChatList();
                recentChat.setCHAT_PERSON_NAME(chattingToName.toString());
                if(chattingToPic==null){
                    recentChat.setCHAT_PERSON_PIC("");
                }else{
                    recentChat.setCHAT_PERSON_PIC(chattingToPic.toString());
                }
                recentChat.setPERSON_LAST_CHAT("Sent: Image");
                recentChat.setPERSON_PHONE_NO(chattingToPhoneNo.toString());
                recentChat.setRECENT_PERSON_ENTITY_TYPE(chattingToEntityType.toString());
                recentChat.setRECENT_CHAT_READ_UNREAD_STATUS("read");
                recentChat.setRECENT_CHAT_TIME(recent_chat_time);
                recentChat.setDELIVER("true");
                dbhelper.addRecentChatListToDB(recentChat);

            }

            PostImageFile postImage1 = new PostImageFile();
            postImage1.execute(appFilePath);



            destination.createNewFile();
            fo = new FileOutputStream(destination);
            // fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.newwebinfotech.rishabh.parkingapp.provider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            }
        }
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public class PostImageFile extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // Showing progress dialog
            pDialog = new ProgressDialog(ChatRoomActivity.this);
            pDialog.setMessage("Sending Image, Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub

            FileHandler fh = new FileHandler();
            String response = fh.doFileUpload(arg0[0],"TAG_IMAGE");
            return response;
        }

        @Override
        protected void onPostExecute(String serverResponse) {
            super.onPostExecute(serverResponse);

            if(serverResponse.trim().equalsIgnoreCase("success")){
                PostImageFileDetail postImageFileDetail = new PostImageFileDetail();
                postImageFileDetail.execute(appFileName);
            }


            else{
                Toast.makeText(ChatRoomActivity.this, "Cannot sent. Try again!..", Toast.LENGTH_LONG).show();
                if (pDialog.isShowing())
                    pDialog.dismiss();
                if(!isConnected()){
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                    dbhelper.deleteSelectedChatsByIds(chat_Time_for_delete);
                    populateChatMessages();
                    Toast.makeText(ChatRoomActivity.this, "Connection is lost. please try again!..", Toast.LENGTH_LONG).show();
                    return;


                }
                dbhelper.deleteSelectedChatsByIds(chat_Time_for_delete);
                populateChatMessages();

            }


        }
    }

    // SENDING IMAGE TO SERVER.
    public class PostImageFileDetail extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... response) {
            // TODO Auto-generated method stub

            HttpEntity httpEntity = null;
            String strResponse = null;
            URL = Config.BASE_URL + "send_image_file.php";
            String fileName = generatefourDigitPIN() + "_" + generateSixDigitPIN();
            String ImageFileName = response[0].toString();

            try {
                if(chattingToEntityType.equalsIgnoreCase("one_to_one")){


                    final String phoneNoArr = chattingToPhoneNo.toString();
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("image_file_content", ImageFileName));
                    nameValuePairs.add(new BasicNameValuePair("file_type", mime));
                    nameValuePairs.add(new BasicNameValuePair("sender_gcm_reg_id", appsetting.getString(Appsetting.FCMDEVICEID)));
                    nameValuePairs.add(new BasicNameValuePair("phoneno", phoneNoArr.toString()));
                    nameValuePairs.add(new BasicNameValuePair("entity_type", chattingToEntityType.toString()));
                    nameValuePairs.add(new BasicNameValuePair("chat_Time_for_delete", chat_Time_for_delete));
                    nameValuePairs.add(new BasicNameValuePair("user_phoneno", appsetting.getString(Appsetting.MYPRIMARYPHONENO)));
                    nameValuePairs.add(new BasicNameValuePair("user_name", appsetting.getString(Appsetting.MYNAME)));

                    httpEntity = new UrlEncodedFormEntity(nameValuePairs);
                    RequestHandler rh = new RequestHandler();
                    strResponse = rh.makePostServiceCall(URL,httpEntity);
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());

            }
            return strResponse;
        }

        @Override
        protected void onPostExecute(String serverResponse) {
            super.onPostExecute(serverResponse);
            if ((pDialog != null) && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            Toast.makeText(ChatRoomActivity.this,"Picture Sent.",Toast.LENGTH_LONG).show();

            fileHandler.deleteOldFileFromParking(appFilePath);
        }
    }

    public class ImageLoadTask extends AsyncTask<String, String, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                //mIcon11 = BitmapFactory.decodeStream(in);

                byte[] buffer = new byte[1024];
                File file = new File(appFilePath);
                OutputStream output = new FileOutputStream (file);
                try
                {
                    int bytesRead = 0;
                    while ((bytesRead = in.read(buffer, 0, buffer.length)) >= 0)
                    {
                        output.write(buffer, 0, bytesRead);
                    }
                }
                finally
                {
                    output.close();
                    buffer=null;
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            // SENDING  APP FOLDER PATH OF IMAGE FOR SETTING OF BITMAP ON UI.
            File file = new File(appFilePath);
            String actual_file_path = file.getAbsoluteFile().toString();

            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            String chat_Time = dateFormat.format(new Date()).toString();

            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            chat_Time_for_delete = dateFormat1.format(new Date()).toString();
            long millisecond = new Date().getTime();
            long mi=millisecond-60000;
            Date datehh=new Date(mi);
            chat_Time_for_delete1 = dateFormat1.format(datehh).toString();
            SimpleDateFormat simpledateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();
            String recent_chat_time =  simpledateFormat.format(date);

            if (!chattingToEntityType.equalsIgnoreCase("group")){
                String time=dbhelper.getPeopleChatlastTimeDataFromDB(chattingToPhoneNo.toString());
                StringTokenizer token = new StringTokenizer(chat_Time_for_delete);
                String date1 = token.nextToken();
                String time1 = token.nextToken();

                // SAVING IMAGE DATA ON TO DATABASE.
                if (chattingToEntityType.equalsIgnoreCase("one_to_one")){
                    ChatPeople curChatObj = addToChat(chattingToName, actual_file_path, "Received","image","one_to_one",chattingToPhoneNo,chat_Time,chat_Time_for_delete,"","true",CHAT_ROOM_DATE);
                    //addToDB(curChatObj); // adding to db
                    dbhelper.addPeopleChatListToDB(curChatObj);

                    RecentChatList recentChatForBroadcast = new RecentChatList();
                    recentChatForBroadcast.setCHAT_PERSON_NAME(chattingToName.toString());
                    if(chattingToPic==null){
                        recentChatForBroadcast.setCHAT_PERSON_PIC("");
                    }else{
                        recentChatForBroadcast.setCHAT_PERSON_PIC(chattingToPic.toString());
                    }
                    recentChatForBroadcast.setPERSON_LAST_CHAT("Received: Image");
                    recentChatForBroadcast.setPERSON_PHONE_NO(chattingToPhoneNo.toString());
                    recentChatForBroadcast.setRECENT_PERSON_ENTITY_TYPE("one_to_one");
                    recentChatForBroadcast.setRECENT_CHAT_READ_UNREAD_STATUS("read");
                    recentChatForBroadcast.setRECENT_CHAT_TIME(recent_chat_time);
                    recentChatForBroadcast.setDELIVER("");
                    dbhelper.addRecentChatListToDB(recentChatForBroadcast);
                }
                chatAdapater = new ChatListAdapter(ChatRoomActivity.this, ChatPeoples);
                chatLV.setAdapter(chatAdapater);

                if(ACTIVE_CHAT_TO_NAME.equalsIgnoreCase(serverComingContactName) && ( sender_entityType.equalsIgnoreCase("one_to_one"))){
                    populateChatMessages();
                }

                else{
                }

                Toast.makeText(ChatRoomActivity.this, "Picture Received",Toast.LENGTH_LONG).show();
            }
        }

    }

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(ChatRoomActivity.this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(ChatRoomActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permissions granted.
                } else {

                }
                return;
            }
        }

    }

}

package com.newwebinfotech.rishabh.parkingapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.newwebinfotech.rishabh.parkingapp.database.DBOperation;
import com.newwebinfotech.rishabh.parkingapp.model.RecentChatList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by user on 9/1/2017.
 */

class CustomListAdapter2 extends BaseAdapter {
    Context context;

	List<RecentChatList> items;
	RecentChatList recent;
	public static TextView txtChats;
	boolean isREAD;
	String shortTimeStr;
	String userPic,userPhone,personPhoneNO;
	DBOperation dbOperation;
	DBOperation.DatabaseHelper dbhelper;
	private SparseBooleanArray mSelectedItemsIds;
    public CustomListAdapter2(Context context, ArrayList<RecentChatList> items) {
        this.context = context;
        this.items = items;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listitemview_layout2,null);

        }

        final ImageView imgIcon = (ImageView) convertView.findViewById(R.id.profilePic);
        TextView txtUName = (TextView) convertView.findViewById(R.id.userName);
//        txtChats = (TextView) convertView.findViewById(R.id.userCurrentStatus);
//        TextView txtTime = (TextView)convertView.findViewById(R.id.usertime);
//        ImageView txtstatustxtstatus = (ImageView)convertView.findViewById(R.id.userStatus);
        imgIcon.setTag(position);
        recent = items.get(position);
        imgIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                int taggedPosition = (int)v.getTag();
                recent = items.get(taggedPosition);
                String chattingToPic = recent.getCHAT_PERSON_PIC().toString();
                Bitmap contactPic = BitmapFactory.decodeFile(chattingToPic);
                final Dialog nagDialog = new Dialog(context);
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nagDialog.setCancelable(true);
                nagDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                nagDialog.setContentView(R.layout.full_image);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(nagDialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                nagDialog.getWindow().setAttributes(lp);
                ImageView ivPreview = (ImageView)nagDialog.findViewById(R.id.iv_preview_image);
                ivPreview.setImageBitmap(contactPic);
                nagDialog.show();
            }
        });

        if(userPhone!=null){
            if(items.get(position).getPERSON_PHONE_NO().toString().equalsIgnoreCase(userPhone)){

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Bitmap photo = BitmapFactory.decodeFile(userPic);
                        imgIcon.setImageBitmap(photo);
                    }
                },3000);


            }else{
                Bitmap photo = BitmapFactory.decodeFile(recent.getCHAT_PERSON_PIC());
                ExifInterface ei = null;
                try {
                    ei = new ExifInterface(recent.getCHAT_PERSON_PIC());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
//				int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//				switch(orientation) {
//				case ExifInterface.ORIENTATION_ROTATE_90:
//					photo = RotateBitmap(photo, -270);
//					break;
//				case ExifInterface.ORIENTATION_ROTATE_180:
//					photo = RotateBitmap(photo, 180);
//					break;
//				case ExifInterface.ORIENTATION_ROTATE_270:
//					photo = RotateBitmap(photo, 270);
//					break;
//				}
//				photo = RotateBitmap(photo, 270);
                imgIcon.setImageBitmap(photo);
            }

        }else{
            Bitmap photo = BitmapFactory.decodeFile(recent.getCHAT_PERSON_PIC());
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(recent.getCHAT_PERSON_PIC());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//			switch(orientation) {
//			case ExifInterface.ORIENTATION_ROTATE_90:
//				photo = RotateBitmap(photo, -270);
//				break;
//			case ExifInterface.ORIENTATION_ROTATE_180:
//				photo = RotateBitmap(photo, 180);
//				break;
//			case ExifInterface.ORIENTATION_ROTATE_270:
//				photo = RotateBitmap(photo, 270);
//				break;
//			}
//			photo = RotateBitmap(photo, 270);
            imgIcon.setImageBitmap(photo);

        }


        txtUName.setText(recent.getCHAT_PERSON_NAME());

//        txtChats.setText(recent.getPERSON_LAST_CHAT());
        String str=recent.getRECENT_CHAT_TIME();

        if (str==null)
        {

        }
        else {

            StringTokenizer token = new StringTokenizer(str);
            String date1 = token.nextToken();
            String time1 = token.nextToken();
//            txtTime.setText(time1);
        }


//        if(recent.getRECENT_CHAT_READ_UNREAD_STATUS().equalsIgnoreCase("read")){
//            txtChats.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
//        }else{
//            txtChats.setTextColor(Color.parseColor("#FF4000"));
//        }

        Bitmap	myBitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.bubble_in);
        Bitmap	myBitmap1 = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.bubble_out);
//        if (recent.getRECENT_PERSON_ENTITY_TYPE().equalsIgnoreCase("one_to_one"))
//        {
//
//            if (recent.getDELIVER().equalsIgnoreCase("true"))
//            {
//                txtstatus.setVisibility(View.VISIBLE);
//                //	txtstatus.setImageBitmap(myBitmap1);
//            } else if (recent.getDELIVER().equalsIgnoreCase("false"))
//            {
//                txtstatus.setVisibility(View.VISIBLE);
//                //	txtstatus.setImageBitmap(myBitmap);
//            }
//            else
//
//            {
//                txtstatus.setVisibility(View.INVISIBLE);
//            }
//        }
//        else {
//            txtstatus.setVisibility(View.INVISIBLE);
//        }

        return convertView;
    }
}

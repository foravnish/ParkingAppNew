package com.newwebinfotech.rishabh.parkingapp.adapter;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newwebinfotech.rishabh.parkingapp.R;
import com.newwebinfotech.rishabh.parkingapp.database.DBOperation;
import com.newwebinfotech.rishabh.parkingapp.model.ChatPeople;
import com.newwebinfotech.rishabh.parkingapp.utils.FileHandler;

import java.io.IOException;
import java.util.ArrayList;


public class ChatListAdapter extends ArrayAdapter<ChatPeople> {

    private final Activity context;
    private final ArrayList<ChatPeople> list;
    private SparseBooleanArray mSelectedItemsIds;
    DBOperation dbOperation;
    DBOperation.DatabaseHelper dbhelper;
    public ViewHolder holder;
    FileHandler fileHandler;

    public ChatListAdapter(Activity context, ArrayList<ChatPeople> list) {
        super(context, R.layout.list_layout, list);
        this.context = context;
        this.list = list;
        fileHandler = new FileHandler();
        mSelectedItemsIds = new SparseBooleanArray();
    }


    public static class ViewHolder {
        protected LinearLayout chat_row_lin;
        protected LinearLayout llTextChat;
        protected LinearLayout llTextChatR;
        protected TextView text;
        protected TextView sent_or_received;
        protected TextView tvMessageSenderName;
        protected ImageView setimage;
        protected LinearLayout llStatus;
        protected RelativeLayout rlImageChat;
        public ImageView imViewChatImage;

    }

    @Override
    public int getItemViewType(int position) {

        if (list.get(position).getPERSON_CHAT_TYPE().toString().equalsIgnoreCase("text")) {
            return 0;
        } else if (list.get(position).getPERSON_CHAT_TYPE().toString().equalsIgnoreCase("image")) {
            return 1;
        } else {
            return 2;
        }

    }

    @Override
    public int getViewTypeCount() {

        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();

            switch (type) {
                case 0: {
                    convertView = inflator.inflate(R.layout.chat_list_row, null);

                    break;
                }
                case 1: {
                    convertView = inflator.inflate(R.layout.chat_list_row_image, null);

                    break;
                }


            }
            holder = new ViewHolder();

            holder.sent_or_received = (TextView) convertView.findViewById(R.id.sent_or_received);
            holder.tvMessageSenderName = (TextView) convertView.findViewById(R.id.tvMessageSenderName);
            holder.setimage = (ImageView) convertView.findViewById(R.id.img);
            holder.chat_row_lin = (LinearLayout) convertView.findViewById(R.id.chat_row_lin);
            if (type == 0) {
                holder.llTextChat = (LinearLayout) convertView.findViewById(R.id.llTextChat);
                holder.text = (TextView) convertView.findViewById(R.id.person_chat);
            } else if (type == 1) {
                holder.rlImageChat = (RelativeLayout) convertView.findViewById(R.id.relLayoutImage);
                holder.imViewChatImage = (ImageView) convertView.findViewById(R.id.person_chat_image);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (list != null) {

            final ChatPeople h = list.get(position);
            if (type == 0) {
                if (h.getPERSON_CHAT_MESSAGE().contains("<b>") || h.getPERSON_CHAT_MESSAGE().contains("<i>") || h.getPERSON_CHAT_MESSAGE().contains("<u>")) {
                    holder.text.setText(Html.fromHtml(h.getPERSON_CHAT_MESSAGE().toString()));
                } else {
                    holder.text.setText(h.getPERSON_CHAT_MESSAGE().toString());
                }

                holder.sent_or_received.setText(h.getCHAT_MESSAGE_TIME().toString());
                Bitmap myBitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.bubble_out);
                Bitmap myBitmap1 = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.bubble_in);
                if (h.getPERSON_ENTITY_TYPE().equalsIgnoreCase("one_to_one")) {
                    if (h.getPERSON_CHAT_TO_FROM().equalsIgnoreCase("Sent")) {
                        holder.setimage.setVisibility(View.VISIBLE);
                        if (h.getCHAT_MESSAGE_DELIVER().equalsIgnoreCase("false")) {
                            //holder.setimage.setImageBitmap(myBitmap);
                        } else if (h.getCHAT_MESSAGE_DELIVER().equalsIgnoreCase("true")) {
                            //holder.setimage.setImageBitmap(myBitmap1);
                        }

                    } else if (h.getPERSON_CHAT_TO_FROM().equalsIgnoreCase("Received")) {
                        holder.setimage.setVisibility(View.INVISIBLE);
                    }

                } else {
                    holder.setimage.setVisibility(View.INVISIBLE);
                }

            } else if (type == 1) {
                Bitmap myBitmap = BitmapFactory.decodeFile(h.getPERSON_CHAT_MESSAGE().toString());
                if (myBitmap != null) {
                    myBitmap = Bitmap.createScaledBitmap(myBitmap, 250, 300, true);
                } else {
                    myBitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.img_filenotfound);
                }
                ExifInterface ei = null;
                try {
                    ei = new ExifInterface(h.getPERSON_CHAT_MESSAGE().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        myBitmap = RotateBitmap(myBitmap, 270);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        myBitmap = RotateBitmap(myBitmap, 180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        myBitmap = RotateBitmap(myBitmap, 90);
                        break;
                }
                holder.imViewChatImage.setImageBitmap(myBitmap);
                holder.sent_or_received.setText(h.getCHAT_MESSAGE_TIME().toString());
                Bitmap myBitmap_image = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.bubble_out);
                Bitmap myBitmap1 = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.bubble_in);
                if (h.getPERSON_ENTITY_TYPE().equalsIgnoreCase("one_to_one")) {
                    if (h.getPERSON_CHAT_TO_FROM().equalsIgnoreCase("Sent")) {
                        holder.setimage.setVisibility(View.VISIBLE);
                        if (h.getCHAT_MESSAGE_DELIVER().equalsIgnoreCase("false")) {
                            //	holder.setimage.setImageBitmap(myBitmap_image);
                        } else if (h.getCHAT_MESSAGE_DELIVER().equalsIgnoreCase("true")) {
                            //holder.setimage.setImageBitmap(myBitmap1);
                        }
                    } else if (h.getPERSON_CHAT_TO_FROM().equalsIgnoreCase("Received")) {

                        holder.setimage.setVisibility(View.INVISIBLE);
                    }

                } else {
                    holder.setimage.setVisibility(View.INVISIBLE);
                }

            }


            if (h.getPERSON_CHAT_TO_FROM().equalsIgnoreCase("SENT")) {
                if (type == 0) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(70, 3, 10, 0);
                  //holder.llTextChat.setPadding(10,10,10,10);
                    holder.llTextChat.setLayoutParams(layoutParams);
                    holder.chat_row_lin.setGravity(Gravity.RIGHT);
                    holder.llTextChat.setBackgroundResource(R.drawable.bubble_out);
                } else if (type == 1) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(70, 3, 10, 0);
                    holder.rlImageChat.setLayoutParams(layoutParams);
                    //holder.llTextChat.setPadding(10,10,10,10);
                    holder.chat_row_lin.setGravity(Gravity.RIGHT);
                    holder.rlImageChat.setBackgroundResource(R.drawable.bubble_out);
                }
            } else if (h.getPERSON_CHAT_TO_FROM().equalsIgnoreCase("grp_notification_server")) {
                holder.llStatus.setBackgroundColor(Color.parseColor("#EEEEEE"));

            } else {
                if (type == 0) {

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(10, 3, 70, 0);
                    //holder.llTextChat.setPadding(10,10,10,10);
                    holder.chat_row_lin.setGravity(Gravity.LEFT);
                    holder.llTextChat.setLayoutParams(layoutParams);
                    holder.llTextChat.setBackgroundResource(R.drawable.bubble_in);
                } else if (type == 1) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(10, 3, 70, 0);
                    holder.rlImageChat.setLayoutParams(layoutParams);
                    //holder.llTextChat.setPadding(10,10,10,10);
                    holder.chat_row_lin.setGravity(Gravity.LEFT);
                    holder.rlImageChat.setBackgroundResource(R.drawable.bubble_in);
                }
            }
        }

        return convertView;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        //notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value) {
            mSelectedItemsIds.put(position, value);

        } else
            mSelectedItemsIds.delete(position);

    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


}

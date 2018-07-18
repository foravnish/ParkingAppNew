package com.newwebinfotech.rishabh.parkingapp.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.newwebinfotech.rishabh.parkingapp.R;


public class PopupDialog extends DialogFragment {
    private TextView tvDescription;
    private Dialog dialog;
    private String mTitle;
    private String mMessage;
    private DialogListener mListener;
    private DialogTaskListener mTaskListener;
    private int task;

    public interface DialogListener {
        void onOkClick(DialogFragment dialog);

        void onCancelClick(DialogFragment dialog);
    }

    public interface DialogTaskListener {
        void onDialogOkClick(DialogFragment dialog, int task);

        void onDialogCancelClick(DialogFragment dialog, int task);
    }

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static PopupDialog newInstance(String title, String message, DialogListener listener) {
        PopupDialog fragment = new PopupDialog();
        fragment.mTitle = title;
        fragment.mMessage = message;
        fragment.mListener = listener;
        return fragment;
    }

    public static PopupDialog newInstance(String title, String message,int task, DialogTaskListener listener) {
        PopupDialog fragment = new PopupDialog();
        fragment.mTitle = title;
        fragment.mMessage = message;
        fragment.task = task;
        fragment.mTaskListener = listener;
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.alert_dialog, null);
        dialogBuilder.setView(v);
        ImageView ivAbout = (ImageView) v.findViewById(R.id.iv_alert_dialog_about);
        Button button1 = (Button) v.findViewById(R.id.btn_alert_dialog_button1);
        Button button2 = (Button) v.findViewById(R.id.btn_alert_dialog_button2);
        TextView tvTitle = (TextView) v.findViewById(R.id.tv_alert_dialog_title);
        tvDescription = (TextView) v.findViewById(R.id.tv_alert_dialog_detail);

        tvTitle.setText(mTitle);
        tvDescription.setText(mMessage);
        button1.setText(getString(R.string.ok));
        button2.setText(getString(R.string.cancel));

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
                if (mListener != null) {
                    mListener.onOkClick(PopupDialog.this);
                }
                if (mTaskListener != null) {
                    mTaskListener.onDialogOkClick(PopupDialog.this,task);
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
                if (mListener != null) {
                    mListener.onCancelClick(PopupDialog.this);
                }
                if (mTaskListener != null) {
                    mTaskListener.onDialogCancelClick(PopupDialog.this,task);
                }
            }
        });

        dialog = dialogBuilder.create();
        dialog.show();

        return dialog;
    }
}

package com.newwebinfotech.rishabh.parkingapp.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.newwebinfotech.rishabh.parkingapp.Config;
import com.newwebinfotech.rishabh.parkingapp.R;
import com.newwebinfotech.rishabh.parkingapp.model.ApiErrorModel;


public class ApiCallError extends DialogFragment {
    private static final String DEFAULT_REPORTING_VALUE = "internet";
    public static final String INTERNET_ERROR = "internet";
    public static final String SERVER_ERROR = "server";
    private String errorMessage;
    private TextView tvDescription;
    private Dialog dialog;
    private String mTitle;
    private String mMessage;
    private ErrorListener mListener;
    private ErrorTaskListener mTaskListener;
    private ErrorModelListener mModelListener;
    private ErrorTaskWithModelListener mTaskModelListener;
    private int task;
    private String errorType;
    private ApiErrorModel apiModel;
    private String[] params;
    private ErrorParamListener mParamListener;
    private ErrorTaskWithParamListener mTaskParamListener;
    private Boolean isMarkAttendance;

    public interface ErrorListener {
        void onErrorRetryClick(DialogFragment dialog);
        void onErrorCancelClick(DialogFragment dialog);
    }

    public interface ErrorTaskListener {
        void onErrorRetryClick(DialogFragment dialog, int task);
        void onErrorCancelClick(DialogFragment dialog, int task);
    }

    public interface ErrorModelListener {
        void onErrorRetryClick(DialogFragment dialog, ApiErrorModel eModel);
        void onErrorCancelClick(DialogFragment dialog, ApiErrorModel eModel);
    }

    public interface ErrorTaskWithModelListener {
        void onErrorRetryClick(DialogFragment dialog, int task, ApiErrorModel eModel);
        void onErrorCancelClick(DialogFragment dialog, int task, ApiErrorModel eModel);
    }

    public interface ErrorParamListener {
        void onErrorRetryClick(DialogFragment dialog, String[] param);
        void onErrorCancelClick(DialogFragment dialog, String[] param);
    }

    public interface ErrorTaskWithParamListener {
        void onErrorRetryClick(DialogFragment dialog, int task, String[] param);
        void onErrorCancelClick(DialogFragment dialog, int task, String[] param);
    }


    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static ApiCallError newInstance(String title, String message, String errorMessage, ErrorListener listener) {
        ApiCallError fragment = new ApiCallError();
        fragment.mTitle = title;
        fragment.mMessage = message;
        fragment.mListener = listener;
        fragment.errorMessage = errorMessage;
        fragment.errorType = DEFAULT_REPORTING_VALUE;
        fragment.isMarkAttendance = false;
        return fragment;
    }

    public static ApiCallError newInstance(String errorMessage, ErrorListener listener) {
        ApiCallError fragment = new ApiCallError();
        fragment.mListener = listener;
        fragment.errorMessage = errorMessage;
        fragment.errorType = DEFAULT_REPORTING_VALUE;
        fragment.isMarkAttendance = false;
        return fragment;
    }

    public static ApiCallError newInstance(String errorType, String errorMessage, ErrorListener listener) {
        ApiCallError fragment = new ApiCallError();
        fragment.mListener = listener;
        fragment.errorMessage = errorMessage;
        fragment.errorType = errorType;
        fragment.isMarkAttendance = false;
        return fragment;
    }

    public static ApiCallError newInstance(String errorMessage, int task, ErrorTaskListener listener) {
        ApiCallError fragment = new ApiCallError();
        fragment.mTaskListener = listener;
        fragment.task = task;
        fragment.errorMessage = errorMessage;
        fragment.errorType = DEFAULT_REPORTING_VALUE;
        fragment.isMarkAttendance = false;
        return fragment;
    }

    public static ApiCallError newInstance(String errorType, String errorMessage, int task, ErrorTaskListener listener) {
        ApiCallError fragment = new ApiCallError();
        fragment.mTaskListener = listener;
        fragment.task = task;
        fragment.errorMessage = errorMessage;
        fragment.errorType = errorType;
        fragment.isMarkAttendance = false;
        return fragment;
    }


    public static ApiCallError newInstance(String errorMessage, ApiErrorModel apiModel, ErrorModelListener listener) {
        ApiCallError fragment = new ApiCallError();
        fragment.mModelListener = listener;
        fragment.apiModel = apiModel;
        fragment.errorMessage = errorMessage;
        fragment.errorType = DEFAULT_REPORTING_VALUE;
        fragment.isMarkAttendance = false;
        return fragment;
    }

    public static ApiCallError newInstance(String errorMessage, String[] params, ErrorParamListener listener) {
        ApiCallError fragment = new ApiCallError();
        fragment.mParamListener = listener;
        fragment.params = params;
        fragment.errorMessage = errorMessage;
        fragment.errorType = DEFAULT_REPORTING_VALUE;
        fragment.isMarkAttendance = false;
        return fragment;
    }

    public static ApiCallError newInstance(String errorType, String errorMessage, ApiErrorModel apiModel, ErrorModelListener listener) {
        ApiCallError fragment = new ApiCallError();
        fragment.mModelListener = listener;
        fragment.apiModel = apiModel;
        fragment.errorMessage = errorMessage;
        fragment.errorType = errorType;
        fragment.isMarkAttendance = false;
        return fragment;
    }

    public static ApiCallError newInstance(String errorType, String errorMessage, String[] params, ErrorParamListener listener) {
        ApiCallError fragment = new ApiCallError();
        fragment.mParamListener = listener;
        fragment.params = params;
        fragment.errorMessage = errorMessage;
        fragment.errorType = errorType;
        fragment.isMarkAttendance = false;
        return fragment;
    }

    public static ApiCallError newInstance(String errorMessage, int task, ApiErrorModel apiModel, ErrorTaskWithModelListener listener) {
        ApiCallError fragment = new ApiCallError();
        fragment.mTaskModelListener = listener;
        fragment.task = task;
        fragment.apiModel = apiModel;
        fragment.errorMessage = errorMessage;
        fragment.errorType = DEFAULT_REPORTING_VALUE;
        fragment.isMarkAttendance = false;
        return fragment;
    }

    public static ApiCallError newInstance(String errorMessage, int task, String[] params, ErrorTaskWithParamListener listener) {
        ApiCallError fragment = new ApiCallError();
        fragment.mTaskParamListener = listener;
        fragment.task = task;
        fragment.params = params;
        fragment.errorMessage = errorMessage;
        fragment.errorType = DEFAULT_REPORTING_VALUE;
        fragment.isMarkAttendance = false;
        return fragment;
    }

    public static ApiCallError newInstance( String errorType,String errorMessage, int task, ApiErrorModel apiModel, ErrorTaskWithModelListener listener) {
        ApiCallError fragment = new ApiCallError();
        fragment.mTaskModelListener = listener;
        fragment.task = task;
        fragment.apiModel = apiModel;
        fragment.errorMessage = errorMessage;
        fragment.errorType = errorType;
        fragment.isMarkAttendance = false;
        return fragment;
    }

    public static ApiCallError newInstance( String errorType,String errorMessage, int task, String[] params, ErrorTaskWithParamListener listener) {
        ApiCallError fragment = new ApiCallError();
        fragment.mTaskParamListener = listener;
        fragment.task = task;
        fragment.params = params;
        fragment.errorMessage = errorMessage;
        fragment.errorType = errorType;
        fragment.isMarkAttendance = false;
        return fragment;
    }


    public static ApiCallError newInstance(Boolean isMarkAttendance,String errorType,String errorMessage, int task, String[] params, ErrorTaskWithParamListener listener) {
        ApiCallError fragment = new ApiCallError();
        fragment.mTaskParamListener = listener;
        fragment.task = task;
        fragment.isMarkAttendance = isMarkAttendance;
        fragment.params = params;
        fragment.errorMessage = errorMessage;
        fragment.errorType = errorType;
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
        if (mTitle == null) {
            if (errorType.equals(SERVER_ERROR)) {
                mTitle = getString(R.string.server_error_reporting_tag);
                mMessage = getString(R.string.server_error_reporting_msg);
            } else {
                mTitle = getString(R.string.server_time_out_tag);
                mMessage = getString(R.string.server_time_out_msg);
            }
        }
        tvTitle.setText(mTitle);
        tvDescription.setText(mMessage);
        if (errorType.equals(SERVER_ERROR)) {
            button1.setText(getString(R.string.report2));
        } else {
            button1.setText(getString(R.string.retry));
        }
        button2.setText(getString(R.string.cancel));
        ivAbout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (errorMessage != null) {
                    tvDescription.setText(errorMessage);
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
                if (errorType.equals(SERVER_ERROR)) {
                    new ErrorReportingTask().execute(errorMessage);
                } else {
                    if (mListener != null) {
                        mListener.onErrorRetryClick(ApiCallError.this);
                    }
                    if (mTaskListener != null) {
                        mTaskListener.onErrorRetryClick(ApiCallError.this, task);
                    }
                    if (mModelListener != null) {
                        mModelListener.onErrorRetryClick(ApiCallError.this, apiModel);
                    }
                    if (mTaskModelListener != null) {
                        mTaskModelListener.onErrorRetryClick(ApiCallError.this, task, apiModel);
                    }
                    if (mParamListener != null) {
                        mParamListener.onErrorRetryClick(ApiCallError.this, params);
                    }
                    if (mTaskParamListener != null) {
                        mTaskParamListener.onErrorRetryClick(ApiCallError.this, task, params);
                    }

                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
                if (mListener != null) {
                    mListener.onErrorCancelClick(ApiCallError.this);
                }
                if (mTaskListener != null) {
                    mTaskListener.onErrorCancelClick(ApiCallError.this, task);
                }
                if (mModelListener != null) {
                    mModelListener.onErrorCancelClick(ApiCallError.this, apiModel);
                }
                if (mTaskModelListener != null) {
                    mTaskModelListener.onErrorCancelClick(ApiCallError.this, task, apiModel);
                }

            }
        });

        dialog = dialogBuilder.create();
        dialog.show();

        return dialog;
    }


    private class ErrorReportingTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... report) {
            String response = ApiCall.POST(Config.REPORT_SERVER_ERROR, RequestBuilder.ErrorReport(report[0]));
            return null;
        }
    }
}

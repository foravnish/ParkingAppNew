package com.newwebinfotech.rishabh.parkingapp.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

// GPSTracker 256
// Maps 386
// NotifyHitApi 110

public class App extends Application
{
	private static Context mContext;
	private static Activity mact;
	public static final String TAG = App.class.getSimpleName();

	private RequestQueue mRequestQueue;

	private static App mInstance;

	public void onCreate()
	{
		super.onCreate();
		this.mContext = getApplicationContext();
		mInstance = this;

	    ApplicationLifecycleHandler handler = new ApplicationLifecycleHandler(mContext);
		registerActivityLifecycleCallbacks(handler);
		registerComponentCallbacks(handler);
	}

	public static Context getContext()
	{
		return mContext;
	}

	public static synchronized App getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}


	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
}

package com.newwebinfotech.rishabh.parkingapp.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;


/**
 * Created by Dk on 6/29/2016.
 */
public class ApplicationLifecycleHandler implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2
{

    private static final String TAG = ApplicationLifecycleHandler.class.getSimpleName();
    private static boolean isInBackground = false;
    Context mcontext;
    public ApplicationLifecycleHandler(Context context)
    {
        this.mcontext=context;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle)
    {


    }

    @Override
    public void onActivityStarted(Activity activity)
    {
    }


    @Override
    public void onActivityResumed(Activity activity) {

        if(isInBackground)
        {


        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
    }

    @Override
    public void onLowMemory() {
    }

    @Override
    public void onTrimMemory(int i)
    {

    }
}
package com.moonma.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.moonma.common.UIView;

import android.content.Intent;

public class Common {

    static public Context appContext() {
        return MyApplication.getAppContext();
    }

    static public Activity getMainActivity() {
        MyApplication app = (MyApplication) MyApplication.getAppContext();
        return app.getMainActivity();
    }


    static public String getRunningActivityName() {
        // <uses-permission android:name="android.permission.GET_TASKS" />
        ActivityManager activityManager = (ActivityManager) appContext().getSystemService(Context.ACTIVITY_SERVICE);
        // Activity ac = null;
        String str = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return str;
    }


    static public String stringFromResId(int res) {
        return appContext().getResources().getString(res);
    }


}

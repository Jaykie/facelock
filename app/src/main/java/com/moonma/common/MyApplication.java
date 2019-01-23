package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.moonma.FaceSDK.FaceDB;

/**
 * Created by gqj3375 on 2017/4/28.
 */

public class MyApplication extends android.app.Application {
    private final String TAG = this.getClass().toString();
//    public FaceDB mFaceDB;
//    Uri mImage;
    private static Context context;
    private Activity mainActivity;


    private class AppActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.d(TAG, "onActivityStarted :" + activity);
            mainActivity = activity;
        }

        @Override
        public void onActivityResumed(Activity activity) {

            mainActivity = activity;
        }

        @Override
        public void onActivityPaused(Activity activity) {
            mainActivity = null;
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

    static private MyApplication _main;

    public static MyApplication main() {
        return _main;
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public Activity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Activity ac) {
        mainActivity = ac;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        _main = this;

        registerActivityLifecycleCallbacks(new AppActivityLifecycleCallbacks());

//		mFaceDB = new FaceDB(this.getExternalCacheDir().getPath());
//		mImage = null;
    }


}

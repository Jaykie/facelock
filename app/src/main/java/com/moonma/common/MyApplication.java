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
    public 	FaceDB mFaceDB;
	Uri mImage;
	private static Context context;
	private Activity mainActivity;


	private class AppActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

		@Override
		public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		}

		@Override
		public void onActivityStarted(Activity activity) {
			Log.d(TAG, "onActivityStarted :" + activity);
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


	public static Context getAppContext() {
		return MyApplication.context;
	}

	   public Activity getMainActivity() {
		return  mainActivity;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		MyApplication.context = getApplicationContext();

		registerActivityLifecycleCallbacks(new AppActivityLifecycleCallbacks());

//		mFaceDB = new FaceDB(this.getExternalCacheDir().getPath());
//		mImage = null;
	}

	public void setCaptureImage(Uri uri) {
		mImage = uri;
	}

	public Uri getCaptureImage() {
		return mImage;
	}

	/**
	 * @param path
	 * @return
	 */
	public static Bitmap decodeImage(String path) {
		Bitmap res;
		try {
			ExifInterface exif = new ExifInterface(path);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

			BitmapFactory.Options op = new BitmapFactory.Options();
			op.inSampleSize = 1;
			op.inJustDecodeBounds = false;
			//op.inMutable = true;
			res = BitmapFactory.decodeFile(path, op);
			//rotate and scale.
			Matrix matrix = new Matrix();

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				matrix.postRotate(90);
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				matrix.postRotate(180);
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				matrix.postRotate(270);
			}

			Bitmap temp = Bitmap.createBitmap(res, 0, 0, res.getWidth(), res.getHeight(), matrix, true);
			Log.d("com.arcsoft", "check target Image:" + temp.getWidth() + "X" + temp.getHeight());

			if (!temp.equals(res)) {
				res.recycle();
			}
			return temp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

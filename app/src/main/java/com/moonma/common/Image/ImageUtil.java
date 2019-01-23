package com.moonma.common;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;

import java.io.File;

public class ImageUtil {
    static private final String TAG = "ImageUtil";

    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_IMAGE = 2;
    public static final int REQUEST_CODE_OP = 3;
    /**
     * @param path
     * @return
     */
    public static Bitmap DecodeImage(String path) {
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

    static public void OpenSystemImageLib() {
        Activity ac = Common.getMainActivity();
        Intent getImageByalbum = new Intent(Intent.ACTION_GET_CONTENT);
        getImageByalbum.addCategory(Intent.CATEGORY_OPENABLE);
        getImageByalbum.setType("image/jpeg");
        ac.startActivityForResult(getImageByalbum, REQUEST_CODE_IMAGE);
    }

    static public void OpenSystemCameraApp() {
        Activity ac = Common.getMainActivity();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues(1);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = ac.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //((Application)(MainActivity.this.getApplicationContext())).setCaptureImage(uri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        ac.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    //https://blog.csdn.net/ak4100/article/details/52933923
    //调用相机
    public void takephoto(){
        Activity ac = Common.getMainActivity();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
        ac.startActivityForResult(intent, REQUEST_CODE_CAMERA);

    }

    //调用相册
    public void OpenGallery() {
        Activity ac = Common.getMainActivity();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        ac.startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }
}

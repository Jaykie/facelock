package com.moonma.facelock;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.moonma.common.Common;
import com.moonma.common.ImageUtil;
import com.moonma.common.MyApplication;
import com.moonma.common.MainActivityBase;
import com.moonma.common.Source;
import com.moonma.common.TabBarItemInfo;
import com.moonma.common.TabBarViewController;
import com.moonma.facelock.RegisterViewController;
import com.moonma.facelock.DetectViewController;
import com.moonma.facelock.SettingViewController;

import com.moonma.FaceSDK.FaceDBCommon;
import com.moonma.FaceSDK.FaceSDKCommon;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MainActivityBase {
    private final String TAG = this.getClass().toString();
    private static final int REQUEST_CODE_OP = 3;


    public static int PERMISSION_REQ = 0x123456;
    private String[] mPermission = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private List<String> mRequestPermission = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //

       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        FaceDBCommon.main().createSDK(Source.FACE_ARC);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            for (String one : mPermission) {
                if (PackageManager.PERMISSION_GRANTED != this.checkPermission(one, Process.myPid(), Process.myUid())) {
                    mRequestPermission.add(one);
                }
            }
            if (!mRequestPermission.isEmpty()) {
                this.requestPermissions(mRequestPermission.toArray(new String[mRequestPermission.size()]), PERMISSION_REQ);
                return;
            }
        }


        startDetector(0);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 版本兼容
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return;
        }
        if (requestCode == PERMISSION_REQ) {
            for (int i = 0; i < grantResults.length; i++) {
                for (String one : mPermission) {
                    if (permissions[i].equals(one) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        mRequestPermission.remove(one);
                    }
                }
            }
            startDetector(0);
        }
    }


    private void startDetector(int camera) {

        LayoutInflater inflater = LayoutInflater.from(this);

//        Context context = Common.getMainActivity();
//
//       // ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
////        ConstraintLayout  content1 = new ConstraintLayout(context);
////        content1.setLayoutParams(lp1);
//
//        ConstraintLayout content1 = (ConstraintLayout) inflater.inflate(R.layout.layout_uiview, getRootContentView(), false);
//
//        getRootContentView().addView(content1);
//
//
//        ViewGroup  content= (ViewGroup) inflater.inflate(R.layout.layout_tabbar,content1, false);
//
//
//        ViewGroup.LayoutParams lp =content.getLayoutParams();
//      //  content.setLayoutParams((lp));
//       content1.addView(content);
//
//
//      //  tab.view.content.addView(content);


        TabBarViewController tab = TabBarViewController.main();

        tab.resIdLayoutTabBar = R.layout.layout_tabbar;
        tab.resIdLayoutTabItem = R.layout.layout_tabbaritem;
        tab.resIdTabItemBtn = R.id.btn_tabbaritem;
        tab.resIdTabItemText = R.id.text_tabbaritem;

        this.setRootViewController(tab);
        {
            TabBarItemInfo info = new TabBarItemInfo();
            info.title = Common.stringFromResId(R.string.register);
            info.controller = RegisterViewController.main();
            tab.addItem(info);
        }

        {
            TabBarItemInfo info = new TabBarItemInfo();
            info.title = Common.stringFromResId(R.string.detect);
            info.controller = DetectViewController.main();
            tab.addItem(info);
        }

        {
            TabBarItemInfo info = new TabBarItemInfo();
            info.title = Common.stringFromResId(R.string.setting);
            info.controller = SettingViewController.main();
            tab.addItem(info);
        }

        tab.selectItem(0);

//            startRegister();
//            return;

//         Intent it = new Intent(this, FaceRegisterActivity.class);//RegisterActivity FaceDetectActivity   class FaceRegisterActivity
//
//        it.putExtra("Camera", camera);
//        startActivityForResult(it, REQUEST_CODE_OP);


    }

    private void startRegister() {
//        new AlertDialog.Builder(this)
//                .setTitle("请选择注册方式")
//                .setIcon(android.R.drawable.ic_dialog_info)
//                .setItems(new String[]{"打开图片", "拍摄照片"}, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 1:
//                                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                                ContentValues values = new ContentValues(1);
//                                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//                                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//                                //  ((MyApplication) (MainActivity.this.getApplicationContext())).setCaptureImage(uri);
//                                FaceSDKCommon.main().setCaptureImage(uri);
//                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                                startActivityForResult(intent, REQUEST_CODE_IMAGE_CAMERA);
//                                break;
//                            case 0:
//                                Intent getImageByalbum = new Intent(Intent.ACTION_GET_CONTENT);
//                                getImageByalbum.addCategory(Intent.CATEGORY_OPENABLE);
//                                getImageByalbum.setType("image/jpeg");
//                                startActivityForResult(getImageByalbum, REQUEST_CODE_IMAGE_OP);
//                                break;
//                            default:
//                                ;
//                        }
//                    }
//                })
//                .show();
    }

    private void startRegister(Bitmap bmp, String file) {
//        Intent it = new Intent(this, RegisterActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("imagePath", file);
//        it.putExtras(bundle);
//        startActivityForResult(it, REQUEST_CODE_OP);
        RegisterViewController.main().ui.doRegister(bmp);
    }

    /**
     * @param uri
     * @return
     */
    private String getPath(Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(this, contentUri, null, null);
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(this, contentUri, selection, selectionArgs);
                }
            }
        }
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = this.getContentResolver().query(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor.getString(actual_image_column_index);
        String end = img_path.substring(img_path.length() - 4);
        if (0 != end.compareToIgnoreCase(".jpg") && 0 != end.compareToIgnoreCase(".png")) {
            return null;
        }
        return img_path;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImageUtil.REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            Uri mPath = data.getData();
            String file = getPath(mPath);
            Bitmap bmp = ImageUtil.DecodeImage(file);
            if (bmp == null || bmp.getWidth() <= 0 || bmp.getHeight() <= 0) {
                Log.e(TAG, "error");
            } else {
                Log.i(TAG, "bmp [" + bmp.getWidth() + "," + bmp.getHeight());
            }
            startRegister(bmp, file);
        } else if (requestCode == REQUEST_CODE_OP) {
            Log.i(TAG, "RESULT =" + resultCode);
            if (data == null) {
                return;
            }
            Bundle bundle = data.getExtras();
            String path = bundle.getString("imagePath");
            Log.i(TAG, "path=" + path);
        } else if (requestCode == ImageUtil.REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            // Uri mPath = ((MyApplication) (this.getApplicationContext())).getCaptureImage();
            Uri mPath = FaceSDKCommon.main().getCaptureImage();
            String file = getPath(mPath);
            Bitmap bmp = ImageUtil.DecodeImage(file);
            startRegister(bmp, file);
        }
    }
}

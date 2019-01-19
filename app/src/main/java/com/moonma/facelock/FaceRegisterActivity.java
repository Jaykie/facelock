package com.moonma.facelock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.guo.android_extend.GLES2Render;
import com.guo.android_extend.tools.CameraHelper;
import com.guo.android_extend.widget.CameraFrameData;
import com.guo.android_extend.widget.CameraGLSurfaceView;
import com.guo.android_extend.widget.CameraSurfaceView;
import com.guo.android_extend.widget.ExtImageView;
import com.moonma.common.Source;


import com.moonma.FaceSDK.FaceSDKBase;
import com.moonma.FaceSDK.FaceSDKCommon;
import com.moonma.FaceSDK.FaceDB;

import java.util.List;

public class FaceRegisterActivity extends AppCompatActivity implements
        CameraSurfaceView.OnCameraListener,
        View.OnTouchListener,
        Camera.AutoFocusCallback, View.OnClickListener,
        com.moonma.FaceSDK.IFaceSDKBaseListener
{

    private final String TAG = this.getClass().getSimpleName();

    private int mWidth, mHeight, mFormat;
    private CameraSurfaceView mSurfaceView;
    private CameraGLSurfaceView mGLSurfaceView;
    private Camera mCamera;
    int mCameraID;
    int mCameraRotate;
    int mCameraMirror;


    //FACESDK
   FaceSDKCommon faceSDKCommon;

    private ImageButton btnCamSelect;
    private ImageButton btnRegister;
    private ImageButton btnCancel;
    private ImageButton btnDelAll;

    private EditText mEditText;
    private ExtImageView mExtImageView;
    private static final int REQUEST_CODE_OP = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_register);


        mCameraID = getIntent().getIntExtra("Camera", 0) == 0 ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT;
        mCameraRotate = getIntent().getIntExtra("Camera", 0) == 0 ? 90 : 270;
        mCameraMirror = getIntent().getIntExtra("Camera", 0) == 0 ? GLES2Render.MIRROR_NONE : GLES2Render.MIRROR_X;
        mWidth = 1280;
        mHeight = 960;
        mFormat = ImageFormat.NV21;
       // mHandler = new Handler();

        mGLSurfaceView = (CameraGLSurfaceView) findViewById(R.id.glsurfaceView);
        mGLSurfaceView.setOnTouchListener(this);
        mSurfaceView = (CameraSurfaceView) findViewById(R.id.surfaceView);
        mSurfaceView.setOnCameraListener(this);
        mSurfaceView.setupGLSurafceView(mGLSurfaceView, true, mCameraMirror, mCameraRotate);
        mSurfaceView.debug_print_fps(true, false);




        faceSDKCommon = new FaceSDKCommon();
        faceSDKCommon.setMode(FaceSDKBase.MODE_PREVIEW);
        faceSDKCommon.createSDK(Source.FACE_ARC);
        faceSDKCommon.setListener(this);


        btnCamSelect = (ImageButton) findViewById(R.id.BtnCameraSelect);
        btnCamSelect.setOnClickListener(this);
        btnRegister = (ImageButton) findViewById(R.id.BtnRegister);
        btnRegister.setOnClickListener(this);

        btnCancel = (ImageButton) findViewById(R.id.BtnCancel);
        btnCancel.setOnClickListener(this);

        btnDelAll = (ImageButton) findViewById(R.id.BtnDelAll);
        btnDelAll.setOnClickListener(this);

    }


    void  OnFaceRegister()
    {
        faceSDKCommon.setMode(FaceSDKBase.MODE_REGISTR);
    }
    private void startDetector(int camera) {

//            startRegister();
//            return;

        Intent it = new Intent(this, FaceDetectActivity.class);//RegisterActivity FaceDetectActivity   class FaceRegisterActivity

        it.putExtra("Camera", camera);
        startActivityForResult(it, REQUEST_CODE_OP);



    }
    void doRegister(Bitmap bmp)
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.dialog_register, null);

        mEditText = (EditText) layout.findViewById(R.id.editview);
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        mExtImageView = (ExtImageView) layout.findViewById(R.id.extimageview);
        mExtImageView.setImageBitmap(bmp);

        mExtImageView.setRotation(mCameraRotate);
        mExtImageView.setScaleY(-mCameraMirror);
        final  Bitmap bmpFace = bmp;

        new AlertDialog.Builder(this)
                .setTitle("请输入注册名字")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(layout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                        faceSDKCommon.registerFace(mEditText.getText().toString(),bmpFace);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public Camera setupCamera() {
        // TODO Auto-generated method stub
        mCamera = Camera.open(mCameraID);
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(mWidth, mHeight);
            parameters.setPreviewFormat(mFormat);

            for( Camera.Size size : parameters.getSupportedPreviewSizes()) {
                Log.d(TAG, "SIZE:" + size.width + "x" + size.height);
            }
            for( Integer format : parameters.getSupportedPreviewFormats()) {
                Log.d(TAG, "FORMAT:" + format);
            }

            List<int[]> fps = parameters.getSupportedPreviewFpsRange();
            for(int[] count : fps) {
                Log.d(TAG, "T:");
                for (int data : count) {
                    Log.d(TAG, "V=" + data);
                }
            }
            //parameters.setPreviewFpsRange(15000, 30000);
            //parameters.setExposureCompensation(parameters.getMaxExposureCompensation());
            //parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
            //parameters.setAntibanding(Camera.Parameters.ANTIBANDING_AUTO);
            //parmeters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            //parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
            //parameters.setColorEffect(Camera.Parameters.EFFECT_NONE);
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mCamera != null) {
            mWidth = mCamera.getParameters().getPreviewSize().width;
            mHeight = mCamera.getParameters().getPreviewSize().height;
        }
        return mCamera;
    }

    @Override
    public void setupChanged(int format, int width, int height) {

    }

    @Override
    public boolean startPreviewImmediately() {
        return true;
    }



    @Override
    public Object onPreview(byte[] data, int width, int height, int format, long timestamp) {
//        Rect[] rects = new Rect[result.size()];
//        return  rects;
        return  faceSDKCommon.onPreview(data,width,height,format,timestamp);

    }

    @Override
    public void onBeforeRender(CameraFrameData data) {

    }

    @Override
    public void onAfterRender(CameraFrameData data) {
        mGLSurfaceView.getGLES2Render().draw_rect((Rect[])data.getParams(), Color.GREEN, 2);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        CameraHelper.touchFocus(mCamera, event, v, this);
        return false;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success) {
            Log.d(TAG, "Camera Focus SUCCESS!");
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.BtnCameraSelect) {
            if (mCameraID == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
                mCameraRotate = 270;
                mCameraMirror = GLES2Render.MIRROR_X;
            } else {
                mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
                mCameraRotate = 90;
                mCameraMirror = GLES2Render.MIRROR_NONE;
            }
            mSurfaceView.resetCamera();
            mGLSurfaceView.setRenderConfig(mCameraRotate, mCameraMirror);
            mGLSurfaceView.getGLES2Render().setViewDisplay(mCameraMirror, mCameraRotate);
        }
        if (view.getId() == R.id.BtnRegister) {
            OnFaceRegister();
        }
        if (view.getId() == R.id.BtnCancel) {
            startDetector(0);
        }
        if (view.getId() == R.id.BtnDelAll) {
            faceSDKCommon.deleteAllFace();
        }

    }
    @Override
    public void FaceDidDetect(String name, float score , Bitmap bmp){

//        mHandler.removeCallbacks(hide);
//        final String mNameShow = name;
//        final float max_score= score;
//        final Bitmap bmp_show = bmp;
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
    }
    @Override
    public void FaceDidFail(Bitmap bmp)
    {
        final String mNameShow = "未识别";
        final Bitmap bmp_show = bmp;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void FaceDidRegister(Bitmap bmp)
    {
        final Bitmap bmp_show = bmp;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doRegister(bmp_show);
            }
        });

    }
}

package com.moonma.facelock;

//import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.moonma.FaceSDK.FaceSDKBase;
import com.moonma.FaceSDK.FaceSDKCommon;
import com.moonma.FaceSDK.FaceDB;

import com.guo.android_extend.GLES2Render;
import com.guo.android_extend.java.AbsLoop;
import com.guo.android_extend.java.ExtByteArrayOutputStream;
import com.guo.android_extend.tools.CameraHelper;
import com.guo.android_extend.widget.CameraFrameData;
import com.guo.android_extend.widget.CameraGLSurfaceView;
import com.guo.android_extend.widget.CameraSurfaceView;
import com.moonma.common.Source;
import com.moonma.FaceSDK.IFaceSDKBaseListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//implements OnCameraListener, View.OnTouchListener, Camera.AutoFocusCallback, View.OnClickListener
public class FaceDetectActivity extends AppCompatActivity implements
        CameraSurfaceView.OnCameraListener,
        View.OnTouchListener,
        Camera.AutoFocusCallback, View.OnClickListener,
         IFaceSDKBaseListener
{

    private final String TAG = this.getClass().getSimpleName();

    private int mWidth, mHeight, mFormat;
    private CameraSurfaceView mSurfaceView;
    private CameraGLSurfaceView mGLSurfaceView;
    private Camera mCamera;


    int mCameraID;
    int mCameraRotate;
    int mCameraMirror;
    byte[] mImageNV21 = null;

    Handler mHandler;
    boolean isPostted = false;


    private TextView mTextView;
    private TextView mTextView1;
    private ImageView mImageView;
    private ImageButton mImageButton;
    private ImageButton btnRegister;
    private ImageButton btnDetect;

    List<Rect> result = new ArrayList<>();
    //FACESDK
    FaceSDKCommon faceSDKCommon;

    Runnable hide = new Runnable() {
        @Override
        public void run() {
            mTextView.setAlpha(0.5f);
            mImageView.setImageAlpha(128);
            isPostted = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detect);


        mCameraID = getIntent().getIntExtra("Camera", 0) == 0 ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT;
        mCameraRotate = getIntent().getIntExtra("Camera", 0) == 0 ? 90 : 270;
        mCameraMirror = getIntent().getIntExtra("Camera", 0) == 0 ? GLES2Render.MIRROR_NONE : GLES2Render.MIRROR_X;
        mWidth = 1280;
        mHeight = 960;
        mFormat = ImageFormat.NV21;
        mHandler = new Handler();

        mGLSurfaceView = (CameraGLSurfaceView) findViewById(R.id.glsurfaceView);
        mGLSurfaceView.setOnTouchListener(this);
        mSurfaceView = (CameraSurfaceView) findViewById(R.id.surfaceView);
        mSurfaceView.setOnCameraListener(this);
        mSurfaceView.setupGLSurafceView(mGLSurfaceView, true, mCameraMirror, mCameraRotate);
        mSurfaceView.debug_print_fps(true, false);

        //snap
        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setText("");
        mTextView1 = (TextView) findViewById(R.id.textView1);
        mTextView1.setText("");

        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageButton = (ImageButton) findViewById(R.id.imageButton);
        mImageButton.setOnClickListener(this);

        btnRegister = (ImageButton) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        btnDetect = (ImageButton) findViewById(R.id.btnDetect);
        btnDetect.setOnClickListener(this);


        faceSDKCommon = new FaceSDKCommon();
        faceSDKCommon.setMode(FaceSDKBase.MODE_PREVIEW);
        faceSDKCommon.createSDK(Source.FACE_ARC);
        faceSDKCommon.setListener(this);
    }

    void  OnFaceRegister()
    {
        faceSDKCommon.setMode(FaceSDKBase.MODE_REGISTR);
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
        if (view.getId() == R.id.imageButton) {
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
        if (view.getId() == R.id.btnRegister) {
            OnFaceRegister();
        }
        if (view.getId() == R.id.btnDetect) {
            faceSDKCommon.setMode(FaceSDKBase.MODE_DETECT);
        }


    }
    @Override
    public void FaceDidDetect(String name, float score , Bitmap bmp){

                    mHandler.removeCallbacks(hide);
        final String mNameShow = name;
        final float max_score= score;
        final Bitmap bmp_show = bmp;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTextView.setAlpha(1.0f);
                            mTextView.setText(mNameShow);
                            mTextView.setTextColor(Color.RED);
                            mTextView1.setVisibility(View.VISIBLE);
                            mTextView1.setText("置信度：" + (float)((int)(max_score * 1000)) / 1000.0);
                            mTextView1.setTextColor(Color.RED);
                            mImageView.setRotation(mCameraRotate);
                            mImageView.setScaleY(-mCameraMirror);
                            mImageView.setImageAlpha(255);
                            mImageView.setImageBitmap(bmp_show);
                        }
                    });
    }
    @Override
    public void FaceDidFail(Bitmap bmp)
    {
        final String mNameShow = "未识别";
        final Bitmap bmp_show = bmp;
                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextView.setAlpha(1.0f);
                            mTextView1.setVisibility(View.VISIBLE);
                          //  mTextView1.setText( gender + "," + age);
                            mTextView1.setTextColor(Color.RED);
                            mTextView.setText(mNameShow);
                            mTextView.setTextColor(Color.RED);
                            mImageView.setImageAlpha(255);
                            mImageView.setRotation(mCameraRotate);
                            mImageView.setScaleY(-mCameraMirror);
                            mImageView.setImageBitmap(bmp_show);
                        }
                    });
    }
}

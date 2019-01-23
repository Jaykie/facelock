package com.moonma.facelock;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.guo.android_extend.GLES2Render;
import com.guo.android_extend.tools.CameraHelper;
import com.guo.android_extend.widget.CameraFrameData;
import com.guo.android_extend.widget.CameraGLSurfaceView;
import com.guo.android_extend.widget.CameraSurfaceView;
import com.moonma.common.Common;
import com.moonma.common.Device;
import com.moonma.common.Source;
import com.moonma.common.UIView;

import com.moonma.FaceSDK.FaceSDKBase;
import com.moonma.FaceSDK.IFaceSDKBaseListener;
import com.moonma.FaceSDK.FaceSDKCommon;
import com.moonma.FaceSDK.FaceDB;
import com.moonma.FaceSDK.FaceDBCommon;
import com.moonma.FaceSDK.IFaceDBBaseListener;

import java.util.List;

public class UICamera extends UIView
        implements
        CameraSurfaceView.OnCameraListener,
        View.OnTouchListener,
        Camera.AutoFocusCallback, View.OnClickListener,
        IFaceSDKBaseListener {

    private final String TAG = this.getClass().getSimpleName();

    private int mWidth, mHeight, mFormat;
    private CameraSurfaceView mSurfaceView;
    private CameraGLSurfaceView mGLSurfaceView;
    private Camera mCamera;

    public int mCameraID;
    public int mCameraRotate;
    public int mCameraMirror;

    //ui
    ImageButton btnCamSelect;

    //FACESDK
    public FaceSDKCommon faceSDKCommon;
    public OnUICameraListener mListener;

    public interface OnUICameraListener {
        public void CameraDidRegisterFace(UICamera ui, Bitmap bmp);
        public void CameraDidDetect(String name, float score , Bitmap bmp);
        public void CameraDetectFail(Bitmap bmp);
    }

    public UICamera(int layoutId, UIView parent) {
        super(layoutId, parent);

        Activity ac = Common.getMainActivity();

        mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;//ac.getIntent().getIntExtra("Camera", 0) == 0 ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT;
        //ac.getIntent().getIntExtra("Camera", 0) == 0 ? 90 : 270;

        mWidth = 1280;
        mHeight = 960;

        updateCameraSize();

        mCameraMirror = GLES2Render.MIRROR_NONE;//ac.getIntent().getIntExtra("Camera", 0) == 0 ? GLES2Render.MIRROR_NONE : GLES2Render.MIRROR_X;

        mFormat = ImageFormat.NV21;
        // mHandler = new Handler();

        mGLSurfaceView = (CameraGLSurfaceView) content.findViewById(R.id.glsurfaceView);
        mGLSurfaceView.setOnTouchListener(this);
        mSurfaceView = (CameraSurfaceView) content.findViewById(R.id.surfaceView);
        mSurfaceView.setOnCameraListener(this);
        mSurfaceView.setupGLSurafceView(mGLSurfaceView, true, mCameraMirror, mCameraRotate);
        mSurfaceView.debug_print_fps(true, false);

        if(!Device.isEmulator()){
            faceSDKCommon = new FaceSDKCommon();
            faceSDKCommon.setMode(FaceSDKBase.MODE_PREVIEW);
            faceSDKCommon.createSDK(Source.FACE_ARC);
            faceSDKCommon.setListener(this);
        }




        btnCamSelect = (ImageButton) findViewById(R.id.BtnCameraSelect);
        btnCamSelect.setOnClickListener(this);

    }

    public void setUICameraListener(OnUICameraListener listener) {
        mListener = listener;
    }

    public void setMode(int mode) {
        if (faceSDKCommon != null) {
            faceSDKCommon.setMode(mode);
        }
    }

    public void updateCameraSize( ) {
        if (mCameraID == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mCameraRotate = 270;
            mCameraMirror = GLES2Render.MIRROR_X;
            if(Device.isLandscape())
            {
                mCameraRotate = 180;
            }
        } else {
            mCameraRotate = 90;
            mCameraMirror = GLES2Render.MIRROR_NONE;

            if(Device.isLandscape())
            {
                mCameraRotate = 0;
            }
        }
    }

    @Override
    public Camera setupCamera() {
        // TODO Auto-generated method stub
        mCamera = Camera.open(mCameraID);
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(mWidth, mHeight);
            parameters.setPreviewFormat(mFormat);

            for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
                Log.d(TAG, "SIZE:" + size.width + "x" + size.height);
            }
            for (Integer format : parameters.getSupportedPreviewFormats()) {
                Log.d(TAG, "FORMAT:" + format);
            }

            List<int[]> fps = parameters.getSupportedPreviewFpsRange();
            for (int[] count : fps) {
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

        if (faceSDKCommon != null) {
            return faceSDKCommon.onPreview(data, width, height, format, timestamp);
        }
        Rect[] rects = new Rect[0];
        return rects;
    }

    @Override
    public void onBeforeRender(CameraFrameData data) {

    }

    @Override
    public void onAfterRender(CameraFrameData data) {
        mGLSurfaceView.getGLES2Render().draw_rect((Rect[]) data.getParams(), Color.GREEN, 2);
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
            } else {
                mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
            }

            updateCameraSize();
            mSurfaceView.resetCamera();
            mGLSurfaceView.setRenderConfig(mCameraRotate, mCameraMirror);
            mGLSurfaceView.getGLES2Render().setViewDisplay(mCameraMirror, mCameraRotate);
        }


    }

    @Override
    public void FaceDidDetect(String name, float score, Bitmap bmp) {
        if (mListener != null) {
            mListener.CameraDidDetect(name,score,bmp);
        }
    }

    @Override
    public void FaceDidFail(Bitmap bmp) {
        if (mListener != null) {
            mListener.CameraDetectFail(bmp);
        }
    }

    @Override
    public void FaceDidRegister(Bitmap bmp) {
        final Bitmap bmp_show = bmp;
        final UICamera ui = this;
        Common.getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // doRegister(bmp_show);
                if (mListener != null) {
                    mListener.CameraDidRegisterFace(ui, bmp_show);
                }
            }
        });

    }


}

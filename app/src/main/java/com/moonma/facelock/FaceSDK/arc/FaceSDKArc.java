package com.moonma.FaceSDK;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.arcsoft.ageestimation.ASAE_FSDKAge;
import com.arcsoft.ageestimation.ASAE_FSDKEngine;
import com.arcsoft.ageestimation.ASAE_FSDKError;
import com.arcsoft.ageestimation.ASAE_FSDKFace;
import com.arcsoft.ageestimation.ASAE_FSDKVersion;
import com.arcsoft.facedetection.AFD_FSDKEngine;
import com.arcsoft.facedetection.AFD_FSDKError;
import com.arcsoft.facedetection.AFD_FSDKFace;
import com.arcsoft.facedetection.AFD_FSDKVersion;
import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKMatching;
import com.arcsoft.facerecognition.AFR_FSDKVersion;
import com.arcsoft.facetracking.AFT_FSDKEngine;
import com.arcsoft.facetracking.AFT_FSDKError;
import com.arcsoft.facetracking.AFT_FSDKFace;
import com.arcsoft.facetracking.AFT_FSDKVersion;
import com.arcsoft.genderestimation.ASGE_FSDKEngine;
import com.arcsoft.genderestimation.ASGE_FSDKError;
import com.arcsoft.genderestimation.ASGE_FSDKFace;
import com.arcsoft.genderestimation.ASGE_FSDKGender;
import com.arcsoft.genderestimation.ASGE_FSDKVersion;
import com.guo.android_extend.image.ImageConverter;
import com.guo.android_extend.java.AbsLoop;
import com.guo.android_extend.java.ExtByteArrayOutputStream;
import com.moonma.FaceSDK.IFaceSDKBaseListener;
import com.moonma.common.Common;
import com.moonma.common.MyApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.moonma.facelock.FaceDetectActivity;
import com.moonma.FaceSDK.FaceSDKBase;
import com.moonma.FaceSDK.FaceDB;

public class FaceSDKArc extends FaceSDKBase {
    private final String TAG = this.getClass().getSimpleName();

    AFT_FSDKVersion version = new AFT_FSDKVersion();
    AFT_FSDKEngine engine = new AFT_FSDKEngine();
    ASAE_FSDKVersion mAgeVersion = new ASAE_FSDKVersion();
    ASAE_FSDKEngine mAgeEngine = new ASAE_FSDKEngine();
    ASGE_FSDKVersion mGenderVersion = new ASGE_FSDKVersion();
    ASGE_FSDKEngine mGenderEngine = new ASGE_FSDKEngine();
    List<AFT_FSDKFace> result = new ArrayList<>();
    List<ASAE_FSDKAge> ages = new ArrayList<>();
    List<ASGE_FSDKGender> genders = new ArrayList<>();
    FRAbsLoop mFRAbsLoop = null;
    AFT_FSDKFace mAFT_FSDKFace = null;

    byte[] mImageNV21 = null;
    int mCameraID;
    int mCameraRotate;
    int mCameraMirror;
    private int mWidth, mHeight, mFormat;

    private AFR_FSDKFace mAFR_FSDKFace;


    class FRAbsLoop extends AbsLoop {
        AFR_FSDKVersion version = new AFR_FSDKVersion();
        AFR_FSDKEngine engine = new AFR_FSDKEngine();
        AFR_FSDKFace result = new AFR_FSDKFace();
        List<FaceDB.FaceRegist> mResgist = FaceDB.main().mRegister;
        List<ASAE_FSDKFace> face1 = new ArrayList<>();
        List<ASGE_FSDKFace> face2 = new ArrayList<>();


        @Override
        public void setup() {
            AFR_FSDKError error = engine.AFR_FSDK_InitialEngine(com.moonma.FaceSDK.FaceDB.appid, com.moonma.FaceSDK.FaceDB.fr_key);
            Log.d(TAG, "AFR_FSDK_InitialEngine = " + error.getCode());
            error = engine.AFR_FSDK_GetVersion(version);
            Log.d(TAG, "FR=" + version.toString() + "," + error.getCode()); //(210, 178 - 478, 446), degree = 1　780, 2208 - 1942, 3370

            //@moon
            FaceDB.main().setFaceResult(result);
        }

        @Override
        public void loop() {

            if (FaceSDKBase.faceMode == FaceSDKBase.MODE_PREVIEW) {
                mImageNV21 = null;
                return;
            }
            if (mImageNV21 != null) {
                final int rotate = mCameraRotate;

                long time = System.currentTimeMillis();
                AFR_FSDKError error = engine.AFR_FSDK_ExtractFRFeature(mImageNV21, mWidth, mHeight, AFR_FSDKEngine.CP_PAF_NV21, mAFT_FSDKFace.getRect(), mAFT_FSDKFace.getDegree(), result);
                Log.d(TAG, "AFR_FSDK_ExtractFRFeature cost :" + (System.currentTimeMillis() - time) + "ms");
                Log.d(TAG, "Face=" + result.getFeatureData()[0] + "," + result.getFeatureData()[1] + "," + result.getFeatureData()[2] + "," + error.getCode());
                AFR_FSDKMatching score = new AFR_FSDKMatching();
                float max = 0.0f;
                String name = null;
                for (FaceDB.FaceRegist fr : mResgist) {
                    for (AFR_FSDKFace face : fr.mFaceList.values()) {
                        error = engine.AFR_FSDK_FacePairMatching(result, face, score);
                        Log.d(TAG, "Score:" + score.getScore() + ", AFR_FSDK_FacePairMatching=" + error.getCode());
                        if (max < score.getScore()) {
                            max = score.getScore();
                            name = fr.mName;
                        }
                    }
                }

                //age & gender
                face1.clear();
                face2.clear();
                face1.add(new ASAE_FSDKFace(mAFT_FSDKFace.getRect(), mAFT_FSDKFace.getDegree()));
                face2.add(new ASGE_FSDKFace(mAFT_FSDKFace.getRect(), mAFT_FSDKFace.getDegree()));
                ASAE_FSDKError error1 = mAgeEngine.ASAE_FSDK_AgeEstimation_Image(mImageNV21, mWidth, mHeight, AFT_FSDKEngine.CP_PAF_NV21, face1, ages);
                ASGE_FSDKError error2 = mGenderEngine.ASGE_FSDK_GenderEstimation_Image(mImageNV21, mWidth, mHeight, AFT_FSDKEngine.CP_PAF_NV21, face2, genders);
                Log.d(TAG, "ASAE_FSDK_AgeEstimation_Image:" + error1.getCode() + ",ASGE_FSDK_GenderEstimation_Image:" + error2.getCode());
                // Log.d(TAG, "age:" + ages.get(0).getAge() + ",gender:" + genders.get(0).getGender());
                //  final String age = ages.get(0).getAge() == 0 ? "年龄未知" : ages.get(0).getAge() + "岁";
                //  final String gender = genders.get(0).getGender() == -1 ? "性别未知" : (genders.get(0).getGender() == 0 ? "男" : "女");

                //crop
                byte[] data = mImageNV21;
                YuvImage yuv = new YuvImage(data, ImageFormat.NV21, mWidth, mHeight, null);
                ExtByteArrayOutputStream ops = new ExtByteArrayOutputStream();
                yuv.compressToJpeg(mAFT_FSDKFace.getRect(), 80, ops);
                final Bitmap bmp = BitmapFactory.decodeByteArray(ops.getByteArray(), 0, ops.getByteArray().length);
                try {
                    ops.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                if (FaceSDKBase.faceMode == FaceSDKBase.MODE_REGISTR) {
                    MyApplication app = (MyApplication) MyApplication.getAppContext();
                    //   app.mFaceDB.addFace("moon1234", result, bmp);
                    if (iListener != null) {
                        iListener.FaceDidRegister(bmp);
                    }
                    mImageNV21 = null;
                    FaceSDKBase.faceMode = FaceSDKBase.MODE_PREVIEW;
                    return;
                }
                if (max > 0.6f) {
                    //fr success.
                    final float max_score = max;
                    Log.d(TAG, "fit Score:" + max + ", NAME:" + name);
                    final String mNameShow = name;
//                    mHandler.removeCallbacks(hide);
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mTextView.setAlpha(1.0f);
//                            mTextView.setText(mNameShow);
//                            mTextView.setTextColor(Color.RED);
//                            mTextView1.setVisibility(View.VISIBLE);
//                            mTextView1.setText("置信度：" + (float)((int)(max_score * 1000)) / 1000.0);
//                            mTextView1.setTextColor(Color.RED);
//                            mImageView.setRotation(rotate);
//                            mImageView.setScaleY(-mCameraMirror);
//                            mImageView.setImageAlpha(255);
//                            mImageView.setImageBitmap(bmp);
//                        }
//                    });

                    if (iListener != null) {
                        iListener.FaceDidDetect(mNameShow, max_score, bmp);
                    }
                } else {
                    final String mNameShow = "未识别";
//                    DetecterActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mTextView.setAlpha(1.0f);
//                            mTextView1.setVisibility(View.VISIBLE);
//                            mTextView1.setText( gender + "," + age);
//                            mTextView1.setTextColor(Color.RED);
//                            mTextView.setText(mNameShow);
//                            mTextView.setTextColor(Color.RED);
//                            mImageView.setImageAlpha(255);
//                            mImageView.setRotation(rotate);
//                            mImageView.setScaleY(-mCameraMirror);
//                            mImageView.setImageBitmap(bmp);
//                        }
//                    });
                    if (iListener != null) {
                        iListener.FaceDidFail(bmp);
                    }
                }
                mImageNV21 = null;
            }

        }

        @Override
        public void over() {
            AFR_FSDKError error = engine.AFR_FSDK_UninitialEngine();
            Log.d(TAG, "AFR_FSDK_UninitialEngine : " + error.getCode());
        }
    }

    public void init() {
        mWidth = 1280;
        mHeight = 960;
        mFormat = ImageFormat.NV21;


        AFT_FSDKError err = engine.AFT_FSDK_InitialFaceEngine(com.moonma.FaceSDK.FaceDB.appid, com.moonma.FaceSDK.FaceDB.ft_key, AFT_FSDKEngine.AFT_OPF_0_HIGHER_EXT, 16, 5);
        Log.d(TAG, "AFT_FSDK_InitialFaceEngine =" + err.getCode());
        err = engine.AFT_FSDK_GetVersion(version);
        Log.d(TAG, "AFT_FSDK_GetVersion:" + version.toString() + "," + err.getCode());

        ASAE_FSDKError error = mAgeEngine.ASAE_FSDK_InitAgeEngine(com.moonma.FaceSDK.FaceDB.appid, com.moonma.FaceSDK.FaceDB.age_key);
        Log.d(TAG, "ASAE_FSDK_InitAgeEngine =" + error.getCode());
        error = mAgeEngine.ASAE_FSDK_GetVersion(mAgeVersion);
        Log.d(TAG, "ASAE_FSDK_GetVersion:" + mAgeVersion.toString() + "," + error.getCode());

        ASGE_FSDKError error1 = mGenderEngine.ASGE_FSDK_InitgGenderEngine(com.moonma.FaceSDK.FaceDB.appid, com.moonma.FaceSDK.FaceDB.gender_key);
        Log.d(TAG, "ASGE_FSDK_InitgGenderEngine =" + error1.getCode());
        error1 = mGenderEngine.ASGE_FSDK_GetVersion(mGenderVersion);
        Log.d(TAG, "ASGE_FSDK_GetVersion:" + mGenderVersion.toString() + "," + error1.getCode());

        mFRAbsLoop = new FRAbsLoop();
        mFRAbsLoop.start();
    }


    public Object onPreview(byte[] data, int width, int height, int format, long timestamp) {
        AFT_FSDKError err = engine.AFT_FSDK_FaceFeatureDetect(data, width, height, AFT_FSDKEngine.CP_PAF_NV21, result);
        Log.d(TAG, "AFT_FSDK_FaceFeatureDetect =" + err.getCode());
        Log.d(TAG, "Face=" + result.size());
        for (AFT_FSDKFace face : result) {
            Log.d(TAG, "Face:" + face.toString());
        }
        if (mImageNV21 == null) {
            if (!result.isEmpty()) {
                mAFT_FSDKFace = result.get(0).clone();
                mImageNV21 = data.clone();
            } else {
//                if (!isPostted) {
//                    mHandler.removeCallbacks(hide);
//                    mHandler.postDelayed(hide, 2000);
//                    isPostted = true;
//                }
            }
        }
        //copy rects
        Rect[] rects = new Rect[result.size()];
        for (int i = 0; i < result.size(); i++) {
            rects[i] = new Rect(result.get(i).getRect());
        }
        //clear result.
        result.clear();
        //return the rects for render.
        return rects;
    }

   public int  CheckRegisterFace(Bitmap bmp)
    {
        int ret = 0;
        byte[] data = new byte[bmp.getWidth() * bmp.getHeight() * 3 / 2];
        try {
            ImageConverter convert = new ImageConverter();
            convert.initial(bmp.getWidth(), bmp.getHeight(), ImageConverter.CP_PAF_NV21);
            if (convert.convert(bmp, data)) {
                Log.d(TAG, "convert ok!");
            }
            convert.destroy();
        } catch (Exception e) {
            e.printStackTrace();
           // Message reg = Message.obtain();
           // reg.what = MSG_CODE;
           // reg.arg1 = MSG_EVENT_IMG_ERROR;
            ret =MSG_EVENT_IMG_ERROR;
         //   reg.obj = e.getMessage();
          //  mUIHandler.sendMessage(reg);
        }

        AFD_FSDKEngine engine = new AFD_FSDKEngine();
        AFD_FSDKVersion version = new AFD_FSDKVersion();
        List<AFD_FSDKFace> result = new ArrayList<AFD_FSDKFace>();
        AFD_FSDKError err = engine.AFD_FSDK_InitialFaceEngine(FaceDB.appid, FaceDB.fd_key, AFD_FSDKEngine.AFD_OPF_0_HIGHER_EXT, 16, 5);
        Log.d(TAG, "AFD_FSDK_InitialFaceEngine = " + err.getCode());
        if (err.getCode() != AFD_FSDKError.MOK) {
           // Message reg = Message.obtain();
           // reg.what = MSG_CODE;
          //  reg.arg1 = MSG_EVENT_FD_ERROR;
           // reg.arg2 = err.getCode();
           // mUIHandler.sendMessage(reg);
            ret =MSG_EVENT_FD_ERROR;
        }
        err = engine.AFD_FSDK_GetVersion(version);
        Log.d(TAG, "AFD_FSDK_GetVersion =" + version.toString() + ", " + err.getCode());
        err  = engine.AFD_FSDK_StillImageFaceDetection(data, bmp.getWidth(), bmp.getHeight(), AFD_FSDKEngine.CP_PAF_NV21, result);
        Log.d(TAG, "AFD_FSDK_StillImageFaceDetection =" + err.getCode() + "<" + result.size());
        /*
        while (mSurfaceHolder != null) {
            Canvas canvas = mSurfaceHolder.lockCanvas();
            if (canvas != null) {
                Paint mPaint = new Paint();
                boolean fit_horizontal = canvas.getWidth() / (float)src.width() < canvas.getHeight() / (float)src.height() ? true : false;
                float scale = 1.0f;
                if (fit_horizontal) {
                    scale = canvas.getWidth() / (float)src.width();
                    dst.left = 0;
                    dst.top = (canvas.getHeight() - (int)(src.height() * scale)) / 2;
                    dst.right = dst.left + canvas.getWidth();
                    dst.bottom = dst.top + (int)(src.height() * scale);
                } else {
                    scale = canvas.getHeight() / (float)src.height();
                    dst.left = (canvas.getWidth() - (int)(src.width() * scale)) / 2;
                    dst.top = 0;
                    dst.right = dst.left + (int)(src.width() * scale);
                    dst.bottom = dst.top + canvas.getHeight();
                }
                canvas.drawBitmap(mBitmap, src, dst, mPaint);
                canvas.save();
                canvas.scale((float) dst.width() / (float) src.width(), (float) dst.height() / (float) src.height());
                canvas.translate(dst.left / scale, dst.top / scale);
                for (AFD_FSDKFace face : result) {
                    mPaint.setColor(Color.RED);
                    mPaint.setStrokeWidth(10.0f);
                    mPaint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(face.getRect(), mPaint);
                }
                canvas.restore();
                mSurfaceHolder.unlockCanvasAndPost(canvas);
                break;
            }
        }
*/
        if (!result.isEmpty()) {
            AFR_FSDKVersion version1 = new AFR_FSDKVersion();
            AFR_FSDKEngine engine1 = new AFR_FSDKEngine();
            AFR_FSDKFace result1 = new AFR_FSDKFace();
            AFR_FSDKError error1 = engine1.AFR_FSDK_InitialEngine(FaceDB.appid, FaceDB.fr_key);
            Log.d("com.arcsoft", "AFR_FSDK_InitialEngine = " + error1.getCode());
            if (error1.getCode() != AFD_FSDKError.MOK) {
             //   Message reg = Message.obtain();
              //  reg.what = MSG_CODE;
              //  reg.arg1 = MSG_EVENT_FR_ERROR;
             //   reg.arg2 = error1.getCode();
             //   mUIHandler.sendMessage(reg);

                ret =MSG_EVENT_FR_ERROR;
            }
            error1 = engine1.AFR_FSDK_GetVersion(version1);
            Log.d("com.arcsoft", "FR=" + version.toString() + "," + error1.getCode()); //(210, 178 - 478, 446), degree = 1　780, 2208 - 1942, 3370
            error1 = engine1.AFR_FSDK_ExtractFRFeature(data, bmp.getWidth(), bmp.getHeight(), AFR_FSDKEngine.CP_PAF_NV21, new Rect(result.get(0).getRect()), result.get(0).getDegree(), result1);
            Log.d("com.arcsoft", "Face=" + result1.getFeatureData()[0] + "," + result1.getFeatureData()[1] + "," + result1.getFeatureData()[2] + "," + error1.getCode());
            if(error1.getCode() == error1.MOK) {
                mAFR_FSDKFace = result1.clone();

                //@moon
                FaceDB.main().setFaceResult(mAFR_FSDKFace);

                int width = result.get(0).getRect().width();
                int height = result.get(0).getRect().height();
                Bitmap face_bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                Canvas face_canvas = new Canvas(face_bitmap);
                face_canvas.drawBitmap(bmp, result.get(0).getRect(), new Rect(0, 0, width, height), null);
//                Message reg = Message.obtain();
//                reg.what = MSG_CODE;
//                reg.arg1 = MSG_EVENT_REG;
//                reg.obj = face_bitmap;
//                mUIHandler.sendMessage(reg);
                ret =MSG_EVENT_REG;
            } else {
//                Message reg = Message.obtain();
//                reg.what = MSG_CODE;
//                reg.arg1 = MSG_EVENT_NO_FEATURE;
//                mUIHandler.sendMessage(reg);
                ret =MSG_EVENT_NO_FEATURE;
            }
            error1 = engine1.AFR_FSDK_UninitialEngine();
            Log.d("com.arcsoft", "AFR_FSDK_UninitialEngine : " + error1.getCode());
        } else {
//            Message reg = Message.obtain();
//            reg.what = MSG_CODE;
//            reg.arg1 = MSG_EVENT_NO_FACE;
//            mUIHandler.sendMessage(reg);
            ret =MSG_EVENT_NO_FACE;
        }
        err = engine.AFD_FSDK_UninitialFaceEngine();
        Log.d(TAG, "AFD_FSDK_UninitialFaceEngine =" + err.getCode());
        return  ret;
    }

}
package com.moonma.FaceSDK;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.arcsoft.ageestimation.ASAE_FSDKAge;
import com.arcsoft.ageestimation.ASAE_FSDKEngine;
import com.arcsoft.ageestimation.ASAE_FSDKError;
import com.arcsoft.ageestimation.ASAE_FSDKFace;
import com.arcsoft.ageestimation.ASAE_FSDKVersion;
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
import com.guo.android_extend.java.AbsLoop;
import com.guo.android_extend.java.ExtByteArrayOutputStream;
import com.moonma.FaceSDK.IFaceSDKBaseListener;
import com.moonma.common.Common;
import com.moonma.common.MyApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.moonma.facelock.FaceDetectActivity;
import com.moonma.FaceSDK.FaceDBBase;
import com.moonma.FaceSDK.FaceDB;

public class FaceDBArc extends FaceDBBase {
    private final String TAG = this.getClass().getSimpleName();

    //facedb
    FaceDB mFaceDB;

    public void init() {

        Context app = Common.appContext();
        mFaceDB = FaceDB.main();
        mFaceDB.initPath(app.getExternalCacheDir().getPath());
        //  mImage = null;

        mFaceDB.loadFaces();
    }

    public void registerFace(String name, Bitmap bmp) {
        if (mFaceDB.faceResult != null) {
            mFaceDB.addFace(name, mFaceDB.faceResult, bmp);
        }

    }

    public void deleteAllFace() {

        mFaceDB.deleteAll();
    }


}
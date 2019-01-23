package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moonma.facelock.R;

import com.moonma.common.UIView;
import com.moonma.facelock.UICamera;

import com.moonma.FaceSDK.FaceSDKBase;
import com.moonma.FaceSDK.IFaceSDKBaseListener;
import com.moonma.FaceSDK.FaceSDKCommon;
import com.moonma.FaceSDK.FaceDB;
import com.moonma.FaceSDK.FaceDBCommon;
import com.moonma.FaceSDK.IFaceDBBaseListener;
/**
 * TODO: document your custom view class.
 */
public class UIDetect extends UIView implements View.OnClickListener ,UICamera.OnUICameraListener
{
    public UICamera uiCamera;

    private TextView textTitle;
    private TextView textDetail;
    private ImageView imageFace;
    //FACESDK
   FaceSDKCommon faceSDKCommon;

    public UIDetect(int layoutId,UIView parent) {
        super(layoutId,parent);


        //snap
        textTitle = (TextView) findViewById(R.id.text_title);
        //textTitle.setText("");
        textDetail = (TextView) findViewById(R.id.text_detail);
        //textDetail.setText("");
        imageFace = (ImageView) findViewById(R.id.imageFace);

    }

    @Override
    public void CameraDidDetect(String name, float score , Bitmap bmp){

       // mHandler.removeCallbacks(hide);
        final String mNameShow = name;
        final float max_score= score;
        final Bitmap bmp_show = bmp;
        Activity ac = Common.getMainActivity();
        ac.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textTitle.setAlpha(1.0f);
                textTitle.setText(mNameShow);
                textTitle.setTextColor(Color.RED);
                textDetail.setVisibility(View.VISIBLE);
                textDetail.setText("置信度：" + (float)((int)(max_score * 1000)) / 1000.0);
                textDetail.setTextColor(Color.RED);
                imageFace.setRotation(uiCamera.mCameraRotate);
                imageFace.setScaleY(-uiCamera.mCameraMirror);
                imageFace.setImageAlpha(255);
                imageFace.setImageBitmap(bmp_show);
            }
        });
    }
    @Override
    public void CameraDetectFail(Bitmap bmp)
    {
        final String mNameShow = "未识别";
        final Bitmap bmp_show = bmp;
        Activity ac = Common.getMainActivity();
        ac.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textTitle.setAlpha(1.0f);
                textTitle.setText(mNameShow);
                textTitle.setTextColor(Color.RED);

                textDetail.setVisibility(View.VISIBLE);
                //  mTextView1.setText( gender + "," + age);
                textDetail.setTextColor(Color.RED);

                imageFace.setImageAlpha(255);
                imageFace.setRotation(uiCamera.mCameraRotate);
                imageFace.setScaleY(-uiCamera.mCameraMirror);
                imageFace.setImageBitmap(bmp_show);
            }
        });
    }



    @Override
    public void CameraDidRegisterFace(UICamera ui, Bitmap bmp) {

    }

    @Override
    public void onClick(View view) {

//        if (view.getId() == R.id.BtnRegister) {
//
//        }



    }
}

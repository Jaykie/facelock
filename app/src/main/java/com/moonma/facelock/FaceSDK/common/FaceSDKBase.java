package com.moonma.FaceSDK;
import android.graphics.Bitmap;
import android.graphics.Rect;

import com.moonma.FaceSDK.IFaceSDKBaseListener;

public class FaceSDKBase{
    public static final int MODE_PREVIEW= 0;
    public static final int MODE_REGISTR = 1;
    public static final int MODE_DETECT = 2;
    public static  int faceMode = MODE_REGISTR;
    public IFaceSDKBaseListener iListener;
    public void init()
    {

    }
    public void setListener(IFaceSDKBaseListener listener)
    {
        iListener = listener;
    }
    public Object onPreview(byte[] data, int width, int height, int format, long timestamp)
    {
       Rect[] rects = new Rect[0];
        return  rects;
    }
    public  void registerFace(String name,Bitmap bmp)
    {
    }
    public  void deleteAllFace()
    {

    }
}


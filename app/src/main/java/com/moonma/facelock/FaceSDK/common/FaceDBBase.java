package com.moonma.FaceSDK;
import android.graphics.Bitmap;
import android.graphics.Rect;

import com.moonma.FaceSDK.IFaceDBBaseListener;

public class FaceDBBase{

    public IFaceDBBaseListener iListener;
    public void init()
    {

    }

    public void setListener(IFaceDBBaseListener listener)
    {
        iListener = listener;
    }

    public  void registerFace(String name,Bitmap bmp)
    {
    }
    public  void deleteAllFace()
    {

    }
}


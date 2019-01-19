package com.moonma.FaceSDK;

import android.graphics.Bitmap;

public interface IFaceSDKBaseListener {
    public void FaceDidDetect(String name, float score , Bitmap bmp);
    public void FaceDidFail(Bitmap bmp);
    public void FaceDidRegister(Bitmap bmp);
}
package com.moonma.FaceSDK;
import com.moonma.common.Source;
import com.moonma.FaceSDK.IFaceSDKBaseListener;
import com.moonma.FaceSDK.FaceSDKArc;
import com.moonma.FaceSDK.FaceSDKOpenAiLab;
import com.moonma.FaceSDK.FaceSDKBase;

import android.graphics.Bitmap;
import android.util.Log;

public class FaceSDKCommon  implements IFaceSDKBaseListener {

    FaceSDKBase faceSDK;
     IFaceSDKBaseListener iListener;
    public void setMode(int mode)
    {
        FaceSDKBase.faceMode = mode;
    }
    public void createSDK(String source) {

        if (source.equals(Source.FACE_ARC)) {
            faceSDK = new FaceSDKArc();
        } else if (source.equals(Source.FACE_OPENAILAB)) {
            faceSDK = new FaceSDKOpenAiLab();
        }
        if (faceSDK != null) {
            faceSDK.init();
            faceSDK.setListener(this);
        }
    }

    public  void registerFace(String name,Bitmap bmp)
    {
        if(faceSDK!=null){
            faceSDK.registerFace(name,bmp);
        }
    }

    public  void deleteAllFace()
    {
        if(faceSDK!=null){
            faceSDK.deleteAllFace();
        }
    }

    public void setListener(IFaceSDKBaseListener listener)
    {
        iListener = listener;
    }
    public Object onPreview(byte[] data, int width, int height, int format, long timestamp) {
            return  faceSDK.onPreview(data,width,height,format,timestamp);
    }
    @Override
    public void FaceDidDetect(String name, float score , Bitmap bmp) {
        if(iListener!=null){
            iListener.FaceDidDetect(name,score,bmp);
        }
    }
    @Override
    public void FaceDidFail(Bitmap bmp)
    {
        if(iListener!=null){
            iListener.FaceDidFail(bmp);
        }
    }

    @Override
    public void FaceDidRegister(Bitmap bmp)
    {
        if(iListener!=null){
            iListener.FaceDidRegister(bmp);
        }
    }
}

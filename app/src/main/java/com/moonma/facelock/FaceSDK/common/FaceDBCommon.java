package com.moonma.FaceSDK;
import com.moonma.common.Common;
import com.moonma.common.Source;
import com.moonma.FaceSDK.IFaceDBBaseListener;
import com.moonma.FaceSDK.FaceDBArc;
import com.moonma.FaceSDK.FaceSDKOpenAiLab;
import com.moonma.FaceSDK.FaceDBBase;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

public class FaceDBCommon  implements IFaceDBBaseListener {

    FaceDBBase faceDB;
    IFaceDBBaseListener iListener;

    Uri mImage = null;

    static private FaceDBCommon _main;
    public static FaceDBCommon main() {
        if (_main == null) {
            _main = new FaceDBCommon();
        }
        return _main;
    }

    public void createSDK(String source) {

        if (source.equals(Source.FACE_ARC)) {
         //   faceDB = new FaceDBArc();
        } else if (source.equals(Source.FACE_OPENAILAB)) {

        }
        if (faceDB != null) {
            faceDB.init();
            faceDB.setListener(this);
        }
    }

    public  void registerFace(String name,Bitmap bmp)
    {
        if(faceDB!=null){
            faceDB.registerFace(name,bmp);
        }
    }

    public  void deleteAllFace()
    {
        if(faceDB!=null){
            faceDB.deleteAllFace();
        }
    }

    public void setListener(IFaceDBBaseListener listener)
    {
        iListener = listener;
    }

}

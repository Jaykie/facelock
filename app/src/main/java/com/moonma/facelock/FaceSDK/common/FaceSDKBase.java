package com.moonma.FaceSDK;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.moonma.FaceSDK.IFaceSDKBaseListener;

public class FaceSDKBase {
    public static final int MODE_PREVIEW = 0;
    public static final int MODE_REGISTR = 1;
    public static final int MODE_DETECT = 2;

    public final static int MSG_CODE = 0x1000;
    public final static int MSG_EVENT_REG = 0x1001;
    public final static int MSG_EVENT_NO_FACE = 0x1002;
    public final static int MSG_EVENT_NO_FEATURE = 0x1003;
    public final static int MSG_EVENT_FD_ERROR = 0x1004;
    public final static int MSG_EVENT_FR_ERROR = 0x1005;
    public final static int MSG_EVENT_IMG_ERROR = 0x1006;

    public static int faceMode = MODE_REGISTR;
    public IFaceSDKBaseListener iListener;

    public void init() {

    }

    public void setListener(IFaceSDKBaseListener listener) {
        iListener = listener;
    }

    public Object onPreview(byte[] data, int width, int height, int format, long timestamp) {
        Rect[] rects = new Rect[0];
        return rects;
    }

    public int CheckRegisterFace(Bitmap bmp) {
        return 0;
    }
}


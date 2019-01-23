package com.moonma.facelock;

import android.content.Context;
import android.view.View;

import com.moonma.common.UIDetect;
import com.moonma.common.UISetting;
import com.moonma.common.UIView;
import com.moonma.common.UIViewController;
import com.moonma.FaceSDK.FaceSDKBase;

public class SettingViewController extends UIViewController {
    UISetting ui;


    static private SettingViewController _main;

    public static SettingViewController main() {
        if (_main == null) {
            _main = new SettingViewController();
        }
        return _main;
    }


    public void ViewDidLoad() {
        super.ViewDidLoad();
        createContent();
    }

    public void createContent() {


        int retId = R.layout.layout_detect_view_controller;
//
        ui = new UISetting(retId, view);
        view.addView(ui);
    }
}

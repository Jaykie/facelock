package com.moonma.facelock;

import android.content.Context;
import android.view.View;

import com.moonma.common.UIDetect;
import com.moonma.common.UIView;
import com.moonma.common.UIViewController;

public class DetectViewController extends UIViewController
{
    UIDetect ui;
    static private DetectViewController _main;

    public static DetectViewController main() {
        if(_main==null){
            _main = new DetectViewController();
        }
        return _main;
    }


    public  void ViewDidLoad()
    {
        super.ViewDidLoad();
        createContent();
    }

    public void createContent()
    {
        int retId = R.layout.layout_detect_view_controller;
//
        ui = new UIDetect(retId);
        view.addView(ui);

    }
}

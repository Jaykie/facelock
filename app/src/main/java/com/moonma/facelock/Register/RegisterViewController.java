package com.moonma.facelock;

import android.content.Context;
import android.view.View;

import com.moonma.common.UIRegister;
import com.moonma.common.UIView;
import com.moonma.common.UIViewController;

public class RegisterViewController extends UIViewController
{

    UIRegister ui;
    UICamera uiCamera;

    static private RegisterViewController _main;

    public static RegisterViewController main() {
        if(_main==null){
            _main = new RegisterViewController();
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
        int retId = R.layout.layout_register_view_controller;

//        string strPrefab = "Common/Prefab/TabBar/UITabBar";
//        GameObject obj = (GameObject)Resources.Load(strPrefab);
//        uiTabBarPrefab = obj.GetComponent<UITabBar>();
//


        uiCamera = new UICamera(R.layout.layout_camera);
        view.addView(uiCamera);


        ui = new UIRegister(retId);
        view.addView(ui);
    }
}

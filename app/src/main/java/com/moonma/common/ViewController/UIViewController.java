package com.moonma.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.moonma.common.UIView;

public class UIViewController {
    public UIView view;

    public UIViewController() {

    }


    public void ViewDidLoad() {
        // Debug.Log("UIViewController:ViewDidLoad");

    }

    public void ViewDidUnLoad() {

    }

    public void LayOutView() {

    }

    void CreateView() {
        if (view == null) {
         view = new UIView();
       // view = new UIView(R.layout.layout_uiview,null);
            ViewDidLoad();
        }


    }

    public void DestroyView() {
        if(view!=null){
            view.removeSelfFromParent(view.content);
            view = null;
        }
        ViewDidUnLoad();

    }

    public void SetViewParent(ViewGroup parent) {
        if (view == null) {
            CreateView();
        }
        parent.addView(view.content);
    }

}
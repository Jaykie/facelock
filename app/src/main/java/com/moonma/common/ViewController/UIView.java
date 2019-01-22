package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintLayout.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moonma.common.UIViewController;

public class UIView {
    public ViewGroup content;
    public UIViewController controller;

    public UIView() {
        // Context context = Common.appContext();
        //必须用MainActivity，用appContext的话ui layout 显示会出问题
        Context context = Common.getMainActivity();

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        content = new ConstraintLayout(context);
        content.setLayoutParams(lp);
    }

    public UIView(int layoutId, UIView parent ) {
        //  super(context);
        // Context context = Common.appContext();
        //必须用MainActivity，用appContext的话ui layout 显示会出问题
        Activity ac = Common.getMainActivity();
        LayoutInflater inflater = LayoutInflater.from(ac);

        ViewGroup rootview = ac.findViewById(android.R.id.content);
        if (parent != null) {
            rootview = parent.content;
        }
        content = (ViewGroup) inflater.inflate(layoutId, rootview, false);
        // this.addView(v);
    }

    public void setController(UIViewController con) {
        controller = con;

    }

    public void removeSelfFromParent(View child) {
        if (child != null) {
            ViewGroup parent = (ViewGroup) child.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                parent.removeView(child);
            }
        }
    }


    public void addView(UIView child) {

        content.addView(child.content);
    }
}

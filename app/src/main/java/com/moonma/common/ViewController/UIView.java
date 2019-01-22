package com.moonma.common;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintLayout.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UIView {
    public ViewGroup content;

    public UIView() {
        Context context = Common.appContext();

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        content = new ConstraintLayout(context);
        content.setLayoutParams(lp);
//        ViewGroup.LayoutParams layoutParams =content.getLayoutParams();
//        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
    }

    public UIView(int layoutId) {
        //  super(context);
        Context context = Common.appContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        content = (ViewGroup) inflater.inflate(layoutId, null, false);
        // this.addView(v);
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

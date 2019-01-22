package com.moonma.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moonma.common.UIView;
import com.moonma.common.UIViewController;

import com.moonma.common.TabBarItemInfo;
import com.moonma.common.TabBarViewController;

/**
 * TODO: document your custom view class.
 */
public class UITabBarItem extends UIView implements View.OnClickListener {
    //    public Button btnItem;
    public TextView textTitle;
    public ImageButton btnItem;
    public OnClickTabBarItemListener mOnClickListener;
    public int index;
    int resId;


    public interface OnClickTabBarItemListener {
        void onClickTabBarItem(UITabBarItem item);
    }

    public UITabBarItem(int layoutId,UIView parent) {
        super(layoutId, parent);
        textTitle = content.findViewById(TabBarViewController.main().resIdTabItemText);
        btnItem = content.findViewById(TabBarViewController.main().resIdTabItemBtn);
        textTitle.setOnClickListener(this);
        btnItem.setOnClickListener(this);
        content.setOnClickListener(this);
    }

    public void UpdateItem(TabBarItemInfo info) {
        // textTitle.setText(info.resIdTitle);
        CharSequence str = (CharSequence) info.title;
        textTitle.setText(str);
    }

    public void setOnClickListener(OnClickTabBarItemListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (mOnClickListener != null) {
            mOnClickListener.onClickTabBarItem(this);
        }
    }
}

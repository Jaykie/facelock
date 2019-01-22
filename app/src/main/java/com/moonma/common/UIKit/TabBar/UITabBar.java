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
import android.widget.LinearLayout;

import com.moonma.common.TabBarViewController;
import com.moonma.common.UIView;

import com.moonma.common.UITabBarItem;
import com.moonma.common.TabBarItemInfo;

/**
 * TODO: document your custom view class.
 */
public class UITabBar extends UIView implements UITabBarItem.OnClickTabBarItemListener {
    UITabBarItem uiTabBarItem;
    public UITabBarItem.OnClickTabBarItemListener mOnClickListener;
    public UITabBar(int layoutId,UIView parent) {
        super(layoutId, parent);
    }


    public void CreateTabItem() {
        uiTabBarItem = new UITabBarItem(TabBarViewController.main().resIdLayoutTabItem,this);
        addView(uiTabBarItem);
        uiTabBarItem.setOnClickListener(this);
//        uiTabBarItem.transform.parent = objLayoutItem.transform;
//        uiTabBarItem.callbackClick = OnUITabBarItemClick;

    }

    public void AddItem(TabBarItemInfo info, int idx) {
        CreateTabItem();
        uiTabBarItem.index = idx;
        uiTabBarItem.UpdateItem(info);
    }

    public void setOnClickListener(UITabBarItem.OnClickTabBarItemListener listener) {
        mOnClickListener = listener;
    }
    @Override
    public void onClickTabBarItem(UITabBarItem item) {
        if (mOnClickListener != null) {
            mOnClickListener.onClickTabBarItem(item);
        }
    }
}

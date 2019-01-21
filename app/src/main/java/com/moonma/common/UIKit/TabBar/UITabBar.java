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
import com.moonma.common.UIView;

import com.moonma.common.UITabBarItem;
import com.moonma.common.TabBarItemInfo;
import com.moonma.facelock.R;

/**
 * TODO: document your custom view class.
 */
public class UITabBar extends UIView
{
    UITabBarItem uiTabBarItem;
    public UITabBar(int layoutId) {
        super(layoutId);
    }


    public void CreateTabItem()
    {
       uiTabBarItem = new  UITabBarItem(R.layout.layout_tabbaritem);
        addView(uiTabBarItem);
//        uiTabBarItem.transform.parent = objLayoutItem.transform;
//        uiTabBarItem.callbackClick = OnUITabBarItemClick;

    }
    public void AddItem(TabBarItemInfo info,int idx)
    {
        CreateTabItem();
        uiTabBarItem.index = idx;
        uiTabBarItem.UpdateItem(info);
    }
}

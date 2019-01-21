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
import com.moonma.facelock.R;

/**
 * TODO: document your custom view class.
 */
public class UITabBarItem extends UIView {
    //    public Button btnItem;
    public TextView textTitle;
    public ImageButton btnItem;

    public int index;

    public UITabBarItem(int layoutId) {
        super(layoutId);
        textTitle = content.findViewById(R.id.tabbaritem);

    }

    public void UpdateItem(TabBarItemInfo info) {
       // textTitle.setText(info.resIdTitle);
        CharSequence str = (CharSequence)info.title;
        textTitle.setText(str);
    }
}

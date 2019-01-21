
package com.moonma.common;

import android.content.Context;
import android.view.View;

import com.moonma.common.UIView;
import com.moonma.common.UIViewController;
import com.moonma.common.UITabBar;
import com.moonma.common.TabBarItemInfo;

import com.moonma.facelock.R;

import java.util.ArrayList;
import java.util.List;

public class TabBarViewController extends UIViewController {
    static private  TabBarViewController _main;

    UITabBar uiTabBar;
    List<TabBarItemInfo> listItem=new ArrayList<TabBarItemInfo>();
    int selectIndex = -1;
    UIViewController rootController;

    public static TabBarViewController main() {
        if(_main==null){
            _main = new TabBarViewController();
        }
        return _main;
    }


    public  void ViewDidLoad()
    {
        super.ViewDidLoad();
        CreateTabBar();
    }

    public void CreateContent()
    {
//        string classname = "Content";
//        objContent = new GameObject(classname);
//        RectTransform rctran = objContent.AddComponent<RectTransform>();
//        objContent.transform.parent = objController.transform;
//        // rctran.sizeDelta = sizeCanvas;
//
//        rctran.anchorMin = new Vector2(0, 0);
//        rctran.anchorMax = new Vector2(1, 1);
//
//        rctran.offsetMin = new Vector2(0, 0);
//        rctran.offsetMax = new Vector2(0, 0);
    }
    public void CreateTabBar()
    {
        CreateContent();
        int retId = R.layout.layout_tabbar;

//        string strPrefab = "Common/Prefab/TabBar/UITabBar";
//        GameObject obj = (GameObject)Resources.Load(strPrefab);
//        uiTabBarPrefab = obj.GetComponent<UITabBar>();
//
   uiTabBar = new UITabBar(retId);
   view.addView(uiTabBar);



//        uiTabBar.transform.parent = objController.transform;
//        uiTabBar.callbackClick = OnUITabBarClick;
//        ViewControllerManager.ClonePrefabRectTransform(uiTabBarPrefab.gameObject, uiTabBar.gameObject);
    }
    // Use this for initialization
    public void addItem(TabBarItemInfo info)
    {
        if (listItem == null)
        {
            listItem = new ArrayList<TabBarItemInfo>();
        }
        listItem.add(info);
        uiTabBar.AddItem(info, listItem.size() - 1);
    }

    public TabBarItemInfo getItem(int idx)
    {
        if (listItem == null)
        {
            return null;
        }
        if ((idx < 0) || (idx >= listItem.size()))
        {
            return null;
        }

        TabBarItemInfo info = listItem.get(idx);
        return info;
    }

    public void DestroyController()
    {
        if (view == null)
        {
            return;
        }

        TabBarItemInfo info = getItem(selectIndex);
        if (info == null)
        {
            //Debug.Log("DestroyController null,selectIndex=" + selectIndex);
            return;
        }

        info.controller.DestroyView();


    }

    public void selectItem(int idx)
    {
        if (selectIndex == idx)
        {
           // Debug.Log("tabbar click the same item selectIndex=" + idx);
            return;
        }
        TabBarItemInfo info = getItem(idx);
        if (info == null)
        {
           // Debug.Log("SelectItem null,idx=" + idx);
            return;
        }

        DestroyController();

        selectIndex = idx;
        //info.controller.CreateView(sizeCanvas);
        info.controller.SetViewParent(view.content);
        rootController = info.controller;

        view.content.bringChildToFront(uiTabBar.content);
    }
}

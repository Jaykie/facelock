package com.moonma.common;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import com.moonma.common.MyApplication;
import com.moonma.common.UIView;
import com.moonma.common.UIViewController;

import java.util.ArrayList;
import java.util.List;

public class MainActivityBase extends AppCompatActivity {
    private final String TAG = this.getClass().toString();

    public UIViewController rootViewController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.main().setMainActivity(this);
    }

    public  ViewGroup getRootContentView()
    {
        ViewGroup contentView = findViewById(android.R.id.content);
        //ViewGroup rootView =(ViewGroup)contentView.getChildAt(0);
        return  contentView;
    }

    public void setRootViewController(UIViewController controller)
    {
        float x = 0, y = 0;
        if (rootViewController != null)
        {
         rootViewController.DestroyView();
        }
        rootViewController = controller;
       rootViewController.SetViewParent(getRootContentView());


    }

}

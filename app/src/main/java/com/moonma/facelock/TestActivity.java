package com.moonma.facelock;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.moonma.common.Common;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   //   setContentView(R.layout.activity_main);
     //  setContentView(R.layout.layout_uiview);


        ViewGroup contentView = findViewById(android.R.id.content);
        ViewGroup rootView =(ViewGroup)contentView.getChildAt(0);

        Context context = Common.appContext();
        LayoutInflater inflater = LayoutInflater.from(this);
        ConstraintLayout  content = (ConstraintLayout) inflater.inflate(R.layout.layout_uiview, null, false);
        ViewGroup.LayoutParams lp = content.getLayoutParams();
        if(lp!=null){
           setContentView(content,lp);
        }else {
           setContentView(content);
        }

    }
}

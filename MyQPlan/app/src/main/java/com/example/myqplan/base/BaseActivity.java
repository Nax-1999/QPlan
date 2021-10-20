package com.example.myqplan.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        getContent();
        initView();
        initData();
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void getContent();


}

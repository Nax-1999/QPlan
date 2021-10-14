package com.example.myqplan.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContent();
        initView();
        initData();
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void getContent();


}

package com.example.myqplan.utils;

import android.os.Handler;
import android.os.Looper;

public class MainHandlerHelper {

    private final Handler handler = new Handler(Looper.getMainLooper());

    private static MainHandlerHelper instance;

    private MainHandlerHelper() {

    }

    public static synchronized MainHandlerHelper getInstance() {
        if (instance == null)
            instance = new MainHandlerHelper();
        return instance;
    }

    public final  void runOnUiThread(Runnable r) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            handler.post(r);
        } else {
            r.run();
        }
    }

    public final boolean postDelayed(Runnable r, long delayMillis) {
        return handler.postDelayed(r, delayMillis);
    }

}

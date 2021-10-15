package com.example.myqplan.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.myqplan.MyApplication;

public class SpUtils {

    private static SharedPreferences sp = null;
    private static SharedPreferences.Editor editor = null;

    public static void addToSp(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public static String getFromSp(String key) {
        return sp.getString(key, "");
    }

    public static void setSp(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sp.edit();
    }


}

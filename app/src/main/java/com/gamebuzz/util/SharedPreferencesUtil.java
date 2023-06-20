package com.gamebuzz.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.content.ContextCompat;

public class SharedPreferencesUtil {

    private final Application application;

    public SharedPreferencesUtil(Application application) { this.application = application; }

    public void writeBooleanData(String sharedPreferencesFileName, String key, boolean value) {
        SharedPreferences sharedPreferences = application.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean readBooleanData(String sharePreferencesFileName, String key) {
        SharedPreferences sharedPreferences = application.getSharedPreferences(sharePreferencesFileName, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public void deleteAll(String sharedPreferencesFileName) {
        SharedPreferences sharedPref = application.getSharedPreferences(sharedPreferencesFileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }

}

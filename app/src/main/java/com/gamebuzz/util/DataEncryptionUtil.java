package com.gamebuzz.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class DataEncryptionUtil {

    private final Application application;

    public DataEncryptionUtil(Application application) {
        this.application = application;
    }

    public void writeSecretDaatWithEncryptedSharedPreferences (String sharedPreferencesFileName, String key, String value) throws GeneralSecurityException, IOException {
        MasterKey mainKey = new MasterKey.Builder(application)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();

        SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                application,
                sharedPreferencesFileName,
                mainKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public String readSecretDataWithEncryptedSharePreferences(String sharedPreferencesFileName, String key) throws GeneralSecurityException, IOException {
        MasterKey mainKey = new MasterKey.Builder(application)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                application,
                sharedPreferencesFileName,
                mainKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

        return sharedPreferences.getString(key, null);
    }

    public void deleteAll (String encryptedSharedPreferencesFileName) {
        SharedPreferences sharedPreferences = application.getSharedPreferences(encryptedSharedPreferencesFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
    }

}

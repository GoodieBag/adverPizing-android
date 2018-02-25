package com.goodiebag.adverPizing.utils;

import android.content.SharedPreferences;

import com.goodiebag.adverPizing.AppController;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Kai on 25/02/18.
 */

public class SharedPreferenceHelper {

    public static final String VIBRATIONS = "Vibrations";
    public static final String NOTIFICATION_SOUND = " NotificationSound";

    private static SharedPreferences getSharedPreference(){
        return AppController.getAppContext().getSharedPreferences("PREF", MODE_PRIVATE);
    }
    //String
    public static void saveStringSharedPreference(String key, String value){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static String getStringSharedPreference(String key, String defaultValue){
        SharedPreferences sharedPreferences = getSharedPreference();
        return sharedPreferences.getString(key,defaultValue);
    }

    //boolean
    public static void saveBooleanSharedPreference(String key, boolean value){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static boolean getBooleanSharedPreference(String key, boolean defaultValue){
        SharedPreferences sharedPreferences = getSharedPreference();
        return sharedPreferences.getBoolean(key,defaultValue);
    }

    //int

    public static void saveIntSharedPreference(String key, int value){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public static int getIntSharedPreference(String key, int defaultValue){
        SharedPreferences sharedPreferences = getSharedPreference();
        return sharedPreferences.getInt(key,defaultValue);
    }
}

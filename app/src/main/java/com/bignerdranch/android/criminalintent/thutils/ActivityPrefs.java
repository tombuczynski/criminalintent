package com.bignerdranch.android.criminalintent.thutils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

/**
 * Created by Tom Buczynski on 21.10.2019.
 */
public class ActivityPrefs {
    public static String getString(@NonNull Activity activity, String key, String defValue) {
        return getPreferences(activity).getString(key, defValue);
    }

    public static void putString(@NonNull Activity activity, String key, String value) {
        SharedPreferences.Editor editor = getPreferences(activity).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static boolean getBoolean(@NonNull Activity activity, String key, boolean defValue) {
        return getPreferences(activity).getBoolean(key, defValue);
    }

    public static void putBoolean(@NonNull Activity activity, String key, boolean value) {
        SharedPreferences.Editor editor = getPreferences(activity).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static int getInt(@NonNull Activity activity, String key, int defValue) {
        return getPreferences(activity).getInt(key, defValue);
    }

    public static void putInt(@NonNull Activity activity, String key, int value) {
        SharedPreferences.Editor editor = getPreferences(activity).edit();
        editor.putInt(key, value);
        editor.apply();
    }


    private static SharedPreferences getPreferences(@NonNull Activity activity) {
        return activity.getPreferences(Context.MODE_PRIVATE);
    }
}

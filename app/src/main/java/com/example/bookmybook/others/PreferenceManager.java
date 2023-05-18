package com.example.bookmybook.others;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor spEditor;
    Context context;
    private static final String FIRST_LAUNCH = "firstLaunch";
    int MODE = 0;
    private static final String PREFERENCE = "Javapapers";

    public PreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE, MODE);
        spEditor = sharedPreferences.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        spEditor.putBoolean(FIRST_LAUNCH, isFirstTime);
        spEditor.commit();
    }

    public boolean FirstLaunch() {
        return sharedPreferences.getBoolean(FIRST_LAUNCH, true);
    }

    public void setUserId(Context context, String value) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        spEditor = sharedPreferences.edit();
        spEditor.putString("userId", value);
        spEditor.commit();
    }

    public String getUserId(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getString("userId", "");
    }

    public void setUserFireBaseId(Context context, String value) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        spEditor = sharedPreferences.edit();
        spEditor.putString("userFireBaseId", value);
        spEditor.commit();
    }

    public String getUserFireBaseId(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getString("userFireBaseId", "");
    }

}

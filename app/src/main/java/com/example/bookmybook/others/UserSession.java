package com.example.bookmybook.others;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.bookmybook.activity.SignInActivity;

public class UserSession {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    public static final String PREFER_NAME = "isFirstRun";
    public static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public UserSession(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createUserLoginSession(){
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.commit();
    }

    public boolean checkLogin(){
        if(!this.isUserLoggedIn()){

            Intent i = new Intent(_context, SignInActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);

            return true;
        }
        return false;
    }



    public boolean isUserLoggedIn(){

        return pref.getBoolean(IS_USER_LOGIN, false);
    }

}

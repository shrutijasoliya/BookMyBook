package com.example.bookmybook.activity;

import static com.example.bookmybook.others.UserSession.IS_USER_LOGIN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.example.bookmybook.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkFirstRun();
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }

    private void checkFirstRun() {

        SharedPreferences sh = getSharedPreferences("isFirstRun", 0);

        Log.e("54545", "checkFirstRun: " + sh.getBoolean(IS_USER_LOGIN, false));

        if (sh.getBoolean(IS_USER_LOGIN, false)) {
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashScreenActivity.this, WelcomeActivity.class));
        }
    }
}
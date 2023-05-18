package com.example.bookmybook.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmybook.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private WebView webViewPrivacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        webViewPrivacyPolicy = findViewById(R.id.webViewPrivacyPolicy);

        webViewPrivacyPolicy.loadUrl("https://sites.google.com/view/bookmybookweb/home");
        webViewPrivacyPolicy.getSettings().setJavaScriptEnabled(true);
        webViewPrivacyPolicy.setWebViewClient(new WebViewClient());
    }

}
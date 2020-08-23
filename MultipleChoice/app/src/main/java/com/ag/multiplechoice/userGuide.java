package com.ag.multiplechoice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class userGuide extends AppCompatActivity {

    private WebView userGuide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);

        userGuide = findViewById(R.id.user_guide);
        userGuide.setWebViewClient(new WebViewClient());
        userGuide.loadUrl("file:///android_asset/userGuide.html");

        WebSettings webSetting = userGuide.getSettings();
        webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptEnabled(true);


    }
}
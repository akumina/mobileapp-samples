package com.mobile.akumina.sample.test.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.akumina.android.auth.akuminalib.data.AppSettings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobile.akumina.sample.test.R;
import com.mobile.akumina.sample.test.utils.URLS;

public class WebActivity extends AppCompatActivity {

    private WebView myWebView;

    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        token = AppSettings.getToken(getApplicationContext());
        myWebView = findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl(URLS.WEB_APP_URL + token);

        FloatingActionButton homeFloatingActionButton = findViewById(R.id.fab_home);
        homeFloatingActionButton.setOnClickListener(view -> reloadWebViewOnHomePress());
    }

    private void reloadWebViewOnHomePress() {
        if (myWebView != null) {
            myWebView.loadUrl(URLS.WEB_APP_URL + token);
        }
    }
}
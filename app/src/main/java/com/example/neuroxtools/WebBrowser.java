package com.example.neuroxtools;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import androidx.activity.OnBackPressedCallback; // Modern Back Callback Import
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class WebBrowser extends AppCompatActivity {

    private WebView wv;
    private EditText et;
    private ProgressBar pb;
    private MaterialButton btnBrowserHome, btnBrowserBack, btnBrowserForward, btnBrowserRefresh;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_browser);
        wv = findViewById(R.id.coreWebView);
        et= findViewById(R.id.etUrlInput);
        pb= findViewById(R.id.browserProgressBar);
        btnBrowserHome = findViewById(R.id.btnBrowserHome);
        btnBrowserBack = findViewById(R.id.btnBrowserBack);
        btnBrowserForward = findViewById(R.id.btnBrowserForward);
        btnBrowserRefresh = findViewById(R.id.btnBrowserRefresh);

        if (wv != null) {
            WebSettings webSettings = wv.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
            webSettings.setDomStorageEnabled(true);
            webSettings.setDatabaseEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
        }

        if (wv != null) {
            wv.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    if (pb!= null) {
                        pb.setVisibility(ProgressBar.VISIBLE);
                    }
                    if (et != null) {
                        et.setText(url);
                    }
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (pb!= null) {
                        pb.setVisibility(ProgressBar.GONE);
                    }
                }
            });
            wv.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (pb!= null) {
                        pb.setProgress(newProgress);
                    }
                }
            });
        }
        if (et != null) {
            et.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                    loadWebAddress(et.getText().toString().trim());
                    return true;
                }
                return false;
            });
        }
        if (btnBrowserHome != null) {
            btnBrowserHome.setOnClickListener(v -> {
                Intent intent = new Intent(WebBrowser.this, Application.class);
                startActivity(intent);
                finish();
            });
        }
        if (btnBrowserBack != null) {
            btnBrowserBack.setOnClickListener(v -> {
                if (wv != null && wv.canGoBack()) {
                    wv.goBack();
                }
            });
        }

        if (btnBrowserForward != null) {
            btnBrowserForward.setOnClickListener(v -> {
                if (wv != null && wv.canGoForward()) {
                    wv.goForward();
                }
            });
        }

        if (btnBrowserRefresh != null) {
            btnBrowserRefresh.setOnClickListener(v -> {
                if (wv != null) {
                    wv.reload();
                }
            });
        }
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (wv != null && wv.canGoBack()) {
                    wv.goBack();
                } else {
                    Intent intent = new Intent(WebBrowser.this, Application.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        loadWebAddress("https://www.google.com");
    }

    private void loadWebAddress(String address) {
        if (address.isEmpty() || wv == null) return;
        if (!address.startsWith("http://") && !address.startsWith("https://")) {
            if (address.contains(".") && !address.contains(" ")) {
                address = "https://" + address;
            } else {
                address = "https://www.google.com/search?q=" + address;
            }
        }
        wv.loadUrl(address);
    }
}
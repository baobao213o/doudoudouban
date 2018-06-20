package com.xxx.library.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.xxx.library.BuildConfig;
import com.xxx.library.R;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.views.ProgressWebView;

public class WebViewActivity extends BaseActivity {

    public static final String TAG_URL = "TAG_URL";
    public static final String TAG_TITLE = "TAG_TITLE";
    private ProgressWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_webview);
        if (savedInstanceState == null) {

            Toolbar toolbar_common_webview = findViewById(R.id.toolbar_common_webview);
            String title = getIntent().getStringExtra(TAG_TITLE);
            toolbar_common_webview.setTitle(TextUtils.isEmpty(title) ? BuildConfig.application_id : title);
            setSupportActionBar(toolbar_common_webview);

            webView = findViewById(R.id.webview_common);
            String url = getIntent().getStringExtra(TAG_URL);

            webView.loadUrl(url);
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}

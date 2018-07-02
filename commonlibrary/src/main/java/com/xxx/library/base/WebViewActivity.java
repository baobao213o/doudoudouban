package com.xxx.library.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.TextView;

import com.xxx.library.BuildConfig;
import com.xxx.library.R;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.views.ElasticDragDismissFrameLayout;

public class WebViewActivity extends BaseActivity {

    public static final String TAG_URL = "TAG_URL";
    public static final String TAG_TITLE = "TAG_TITLE";
    public static final String TAG_COLOR = "TAG_COLOR";
    private WebView webView;
    private ElasticDragDismissFrameLayout draggableFrame;
    private ElasticDragDismissFrameLayout.SystemChromeFader chromeFader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_webview);
        if (savedInstanceState == null) {

            TextView tv_common_webview_tb_inner = findViewById(R.id.tv_common_webview_tb_inner);
            Toolbar toolbar_common_webview = findViewById(R.id.toolbar_common_webview);
            String title = getIntent().getStringExtra(TAG_TITLE);
            int toolbarColor = getIntent().getIntExtra(TAG_COLOR, getResources().getColor(R.color.colorPrimary));
            tv_common_webview_tb_inner.setText(TextUtils.isEmpty(title) ? BuildConfig.application_id : title);
            toolbar_common_webview.setBackgroundColor(toolbarColor);
            setSupportActionBar(toolbar_common_webview);

            getWindow().setStatusBarColor(toolbarColor);

            webView = findViewById(R.id.webview_common);
            webView.setBackgroundColor(0);

            String url = getIntent().getStringExtra(TAG_URL);

            draggableFrame = findViewById(R.id.eddf_common_container);
            chromeFader = new ElasticDragDismissFrameLayout.SystemChromeFader(this) {
                @Override
                public void onDragDismissed() {
                    finishAfterTransition();
                }
            };

            webView.loadUrl(url);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (draggableFrame != null) {
            draggableFrame.addListener(chromeFader);
        }
    }

    @Override
    protected void onPause() {
        if (draggableFrame != null) {
            draggableFrame.removeListener(chromeFader);
        }
        super.onPause();
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

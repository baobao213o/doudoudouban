package com.xxx.library.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.xxx.library.R;
import com.xxx.library.utils.DeviceUtil;

public class ProgressWebView extends WebView {

    private ProgressBar progressBar;//进度条
    private Context context;

    public ProgressWebView(Context context) {
        this(context, null);
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        //初始化进度条
        progressBar = (ProgressBar) View.inflate(context, R.layout.common_progress_webview, null);
        //把进度条加到Webview中
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DeviceUtil.getScreenWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(progressBar, params);
        //初始化设置
        setWebChromeClient(new MyWebCromeClient());
        setWebViewClient(new MyWebviewClient());

        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        //设置缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //Dom Storage
        webSettings.setDomStorageEnabled(true);
//        //Application Cache
//        getSettings().setAppCacheEnabled(true);

    }

    private class MyWebCromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    private class MyWebviewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            String scheme = uri.getScheme();
            if (TextUtils.isEmpty(scheme)) return true;
            if (scheme.equals("http") || scheme.equals("https")) {
                //处理http协议
                if (Uri.parse(url).getHost().contains("douban.com")) {
                    // 内部网址，不拦截，用自己的webview加载
                    return false;
                } else {
                    //跳转外部浏览器
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                    return true;
                }
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }


}

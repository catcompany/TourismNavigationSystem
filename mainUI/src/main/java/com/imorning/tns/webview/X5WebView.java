package com.imorning.tns.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialog;

import com.imorning.tns.R;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import me.wangyuwei.slackloadingview.SlackLoadingView;


@SuppressLint("SetJavaScriptEnabled")
public class X5WebView extends WebView {

    //加载动画框
    private AppCompatDialog loadingDialog;

    private final WebChromeClient chromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView webView, int progress) {
            super.onProgressChanged(webView, progress);
            if (progress > 50)
                hideLoadingDialog();
        }


    };
    private Context context;
    private final WebViewClient client = new WebViewClient() {
        /**
         * 防止加载网页时调起系统浏览器
         */
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showLoadingDialog();
        }
    };

    public X5WebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initWebViewSettings(context);

    }

    public X5WebView(Context context) {
        super(context);
        initWebViewSettings(context);
    }

    private void initWebViewSettings(Context context) {
        this.context = context;
        this.setWebViewClient(client);
        this.setWebChromeClient(chromeClient);
        this.getView().setClickable(true);
        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
    }

    /**
     * 显示加载动画
     */
    private synchronized void showLoadingDialog() {
        if (loadingDialog == null)
            loadingDialog = new AppCompatDialog(context);
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.web_loading_layout, null);
        ((SlackLoadingView) findViewById(R.id.web_loading_view)).start();
        loadingDialog.setContentView(view);
        loadingDialog.setCancelable(false);
        loadingDialog.create();
        loadingDialog.show();
    }

    /**
     * 隐藏加载动画
     */
    private synchronized void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}

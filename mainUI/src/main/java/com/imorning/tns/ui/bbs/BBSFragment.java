package com.imorning.tns.ui.bbs;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.imorning.tns.R;
import com.imorning.tns.ui.SlackLoadingView;
import com.mylhyl.circledialog.CircleDialog;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import static android.app.Activity.RESULT_OK;

public class BBSFragment extends Fragment {
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;
    private static final String TAG = "BBSFragment";
    private final static String bbsUrl = "https://bbs.catcompany.cn/";
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private ValueCallback<Uri> uploadMessage;

    private WebView webView;
    private OnBackPressedCallback callback;
    //加载动画框
    private AppCompatDialog loadingDialog;
    private final WebViewClient client = new WebViewClient() {
        /**
         * 防止加载网页时调起系统浏览器
         */
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            //要返回true否则内核会继续处理
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showLoadingDialog();
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
            hideLoadingDialog();
        }

    };

    public static BBSFragment newInstance() {
        return new BBSFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        new ViewModelProvider(this).get(BBSViewModel.class);
        View rootView = inflater.inflate(R.layout.bbs_fragment, container, false);
        webView = rootView.findViewById(R.id.webview_bbs);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new ViewModelProvider(this).get(BBSViewModel.class);
        initWebViewSettings();
        webView.loadUrl(bbsUrl);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSettings() {
        webView.setWebViewClient(client);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
                uploadMessageAboveL = valueCallback;
                BBSFragment.this.openFileChooser();
                return true;
            }

            /**
             * 加载进度发生改变时触发
             * @param webView ...
             * @param i 进度，值从0~100
             */
            @Override
            public void onProgressChanged(WebView view, int i) {
                super.onProgressChanged(view, i);
                if (i > 50) hideLoadingDialog();
            }

            /**
             * 当webview接收到标题的时候触发
             * @param webView ..
             * @param title 标题
             */
            @Override
            public void onReceivedTitle(WebView webView, String title) {
                super.onReceivedTitle(webView, title);

            }
        });
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setAllowFileAccessFromFileURLs(true);
        webSetting.setAllowUniversalAccessFromFileURLs(true);
        webSetting.setSupportZoom(false);
        webSetting.setGeolocationEnabled(true);
        webSetting.setSavePassword(true);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // settings 的设计
    }

    /**
     * 选择文件上传
     */
    private void openFileChooser() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "文件选择"), FILE_CHOOSER_RESULT_CODE);
    }

    /**
     * 回调处理
     *
     * @param requestCode ..
     * @param resultCode  ..
     * @param data        数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }


    /**
     * 显示加载动画
     */
    public synchronized void showLoadingDialog() {
        if (loadingDialog == null)
            loadingDialog = new AppCompatDialog(requireContext());
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.web_loading_layout, null);
        ((SlackLoadingView) view.findViewById(R.id.web_loading_view)).start();
        loadingDialog.setContentView(view);
        loadingDialog.setCancelable(false);
        loadingDialog.create();
        loadingDialog.show();
    }

    /**
     * 隐藏加载动画
     */
    public synchronized void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                webViewBack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }


    private void webViewBack() {
        if (webView.canGoBack() && callback != null) {
            webView.goBack();
            return;
        } else {
            if (callback != null) {
                callback.setEnabled(false);
            }
        }
        new CircleDialog.Builder()
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .setTitle("提示")
                .setText("确定退出？")
                .setNegative("取消", null)
                .setPositive("确定", v -> requireActivity().getOnBackPressedDispatcher().onBackPressed())
                .show(requireActivity().getSupportFragmentManager());
    }
}
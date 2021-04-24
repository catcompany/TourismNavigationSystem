package com.imorning.tns.ui.bbs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.imorning.tns.R;
import com.imorning.tns.webview.X5WebView;

public class BBSFragment extends Fragment {
    private static final String TAG = "BBSFragment";
    private final static String bbsUrl = "https://bbs.catcompany.cn/";
    private X5WebView webView;
    private OnBackPressedCallback callback;

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
        webView.loadUrl(bbsUrl);
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
        requireActivity().getOnBackPressedDispatcher().onBackPressed();
    }
}
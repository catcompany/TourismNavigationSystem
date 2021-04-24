package com.imorning.tns.ui.scense;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.imorning.tns.R;
import com.imorning.tns.webview.X5WebView;

public class ScenicSpotFragment extends Fragment {
    private View rootView;
    private X5WebView x5WebView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        x5WebView.loadUrl("https://maplab.amap.com/share/mapv/c92ba6344f8eef39d12afe73d4e8a7cd");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_scenic_spot, container, false);
        x5WebView = rootView.findViewById(R.id.scenic_webview);
        return rootView;
    }
}
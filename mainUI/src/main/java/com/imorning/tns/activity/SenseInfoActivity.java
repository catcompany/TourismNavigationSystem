package com.imorning.tns.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.imorning.tns.R;

public class SenseInfoActivity extends AppCompatActivity {
    public static final String SCENE_INFO_DATA = "data";
    private static final String TAG = "SenseInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sense_info);
        Log.d(TAG, "onCreate: " + getIntent().getExtras().get(SCENE_INFO_DATA));
    }
}
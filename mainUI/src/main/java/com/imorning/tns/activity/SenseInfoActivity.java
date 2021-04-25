package com.imorning.tns.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.imorning.tns.R;

public class SenseInfoActivity extends AppCompatActivity {
    public static final String SCENE_NAME = "data";
    public static final String CITY = "city";
    private static final String TAG = "SenseInfoActivity";

    private String sceneName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sense_info);
        sceneName = getIntent().getExtras().get(SCENE_NAME).toString();
    }
}
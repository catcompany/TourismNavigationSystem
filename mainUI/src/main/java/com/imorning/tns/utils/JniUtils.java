package com.imorning.tns.utils;

import android.content.Context;

public class JniUtils {
    static {
        System.loadLibrary("main");
    }
    public native String getAppSHA1(Context contextObj);
}

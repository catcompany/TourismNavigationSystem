package com.imorning.senseinfohelper.utils;

public class JniUtils {
    static {
        System.loadLibrary("codeutils");
    }

    protected static native String getApiId(String key);

    protected static native String getKey(String apiId);
}

package com.imorning.tns;

import android.app.Application;

import com.hjq.permissions.XXPermissions;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;

import java.util.HashMap;

public class App extends Application {
    private static final String TAG = "AppApplicationInfo";

    @Override
    public void onCreate() {
        super.onCreate();
        // 当前项目是否已经适配了分区存储的特性
        XXPermissions.setScopedStorage(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                initX5SDK();
            }
        }).start();
        //Log.e(TAG, ApkUtils.getCertificateSHA1Fingerprint(this));
    }

    /**
     * 初始化X5内核
     */
    private void initX5SDK() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);

    }
}

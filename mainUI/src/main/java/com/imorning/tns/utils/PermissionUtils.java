package com.imorning.tns.utils;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

/**
 * 申请权限
 */
public class PermissionUtils {
    private final AppCompatActivity appCompatActivity;

    public final static String[] permissions = new String[]{
            Permission.WRITE_EXTERNAL_STORAGE,
            Permission.READ_PHONE_STATE,
            Permission.ACCESS_COARSE_LOCATION,
            Permission.ACCESS_FINE_LOCATION
    };

    public PermissionUtils(AppCompatActivity activity) {
        this.appCompatActivity = activity;
    }

    public void requestAllPermission() {
        XXPermissions.with(appCompatActivity)
                .permission(permissions)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (!all) {
                            toast("获取部分权限成功，但部分权限未正常授予");
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            toast("拒绝授权，请手动授予权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(appCompatActivity, permissions);
                        }
                    }
                });

    }

    private void toast(String msg) {
        Toast.makeText(appCompatActivity.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}

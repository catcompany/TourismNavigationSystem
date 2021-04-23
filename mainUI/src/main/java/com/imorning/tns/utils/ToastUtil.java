/**
 *
 */
package com.imorning.tns.utils;

import android.content.Context;
import android.widget.Toast;


public class ToastUtil {

    public static void show(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    public static void show(Context context, int info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    public static void showerror(Context context, int rCode) {
        show(context, "查询失败：" + rCode);
    }
}

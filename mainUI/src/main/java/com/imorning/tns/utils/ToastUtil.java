/**
 *
 */
package com.imorning.tns.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author iMorning
 *
 * {@link Toast} 使用方法的集合
 */
public class ToastUtil {
    /**
     * 显示信息
     * @param context 上下文
     * @param info 信息
     */
    public static void show(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    public static void show(Context context, int info) {
        show(context, context.getString(info));
    }

    public static void showerror(Context context, int rCode) {
        show(context, "查" +
                "询失败：" + rCode);
    }
}

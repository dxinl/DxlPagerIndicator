package com.mx.dengxinliang.library;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by dxinliang on 9/5/15.
 */
public class Utils {
    public static boolean isBlank(String str) {
        return str == null || str.equals("");
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static int getScreenWidth(Context context) {
        return getScreenMetrics(context).widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return getScreenMetrics(context).heightPixels;
    }

    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }
}

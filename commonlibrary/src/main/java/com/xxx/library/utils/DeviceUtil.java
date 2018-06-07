package com.xxx.library.utils;

import android.util.DisplayMetrics;

import com.xxx.library.BaseApplication;

/**
 * Created by gaoruochen on 18-6-4.
 */

public class DeviceUtil {

    public static int getScreenWidth() {
        DisplayMetrics dm;
        dm = BaseApplication.getInstance().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        DisplayMetrics dm;
        dm = BaseApplication.getInstance().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int dip2px(float dpValue) {
        final float scale = BaseApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}

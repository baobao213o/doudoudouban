package com.xxx.library.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xxx.library.BaseApplication;


public class NetUtil {

    public static boolean isNetworkAvalible() {
        // 获得网络状态管理器
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 建立网络数组
            NetworkInfo[] net_infos = connectivityManager.getAllNetworkInfo();

            if (net_infos != null) {
                for (NetworkInfo net_info:net_infos) {
                    // 判断获得的网络状态是否是处于连接状态
                    if (net_info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

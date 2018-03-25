package com.xxx.library.appdelegate;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;

/**
 * 调用所有组件application生命周期方法
 */

public class Delegate implements IAppLife {

    private static final String APP_DELEGATE = "APP_DELEGATE";

    private static ArrayList<IAppLife> lifes;

    private static Delegate instance;

    public static Delegate getInstance() {
        if (instance == null) {
            instance = new Delegate();
        }
        return instance;
    }

    private Delegate() {
        lifes = new ArrayList<>();
    }


    @Override
    public void attachBaseContext(Context base) {
        getAppDelegate(base);
        for (IAppLife life : lifes) {
            life.attachBaseContext(base);
        }
    }

    @Override
    public void onCreate(Application application) {
        for (IAppLife life : lifes) {
            life.onCreate(application);
        }
    }

    @Override
    public void onTerminate(Application application) {
        for (IAppLife life : lifes) {
            life.onTerminate(application);
        }
    }

    private void getAppDelegate(Context context) {
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                for (String key : appInfo.metaData.keySet()) {
                    if (APP_DELEGATE.equals(appInfo.metaData.getString(key))) {
                        //遍历manifest配置
                        Class app = Class.forName(key);
                        if (IAppLife.class.isAssignableFrom(app)) {
                            IAppLife delegate = (IAppLife) app.newInstance();
                            lifes.add(delegate);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


package com.xxx.library;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.squareup.leakcanary.LeakCanary;
import com.xxx.library.appdelegate.Delegate;

import io.realm.Realm;

import static com.xxx.library.BuildConfig.DEBUG;


public class BaseApplication extends Application {

    private static BaseApplication instance;

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        instance = this;
        Delegate.getInstance().attachBaseContext(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
        LeakCanary.install(this);
        Realm.init(this);
        Delegate.getInstance().onCreate(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Delegate.getInstance().onTerminate(this);
    }

}

package com.xxx.library;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.xxx.library.appdelegate.Delegate;
import com.xxx.library.crash.CrashHandler;

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
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }
        ARouter.init(this);
        LeakCanary.install(this);
        Realm.init(this);
        CrashHandler.getInstance().init();
        Fresco.initialize(this);
        Delegate.getInstance().onCreate(this);
        registerActivityListener();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Delegate.getInstance().onTerminate(this);
    }


    private void registerActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                AppManager.getInstance().addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                AppManager.getInstance().removeActivity(activity);
            }
        });
    }
}

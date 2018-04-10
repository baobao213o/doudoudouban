package com.xxx.library;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;

import com.alibaba.android.arouter.launcher.ARouter;
import com.squareup.leakcanary.LeakCanary;
import com.xxx.library.appdelegate.Delegate;
import com.xxx.library.crash.CrashHandler;

import java.util.LinkedList;

import io.realm.Realm;

import static com.xxx.library.BuildConfig.DEBUG;

public class BaseApplication extends Application {

    private static BaseApplication instance;

    private static LinkedList<Activity> activityList = new LinkedList<>();


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
        CrashHandler.getInstance().init();
        Delegate.getInstance().onCreate(this);
        registerActivityListener();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Delegate.getInstance().onTerminate(this);
    }

    public static void exit() {
        for (int i = 0; i < activityList.size(); i++) {
            activityList.get(i).finish();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    Process.killProcess(Process.myPid());
                }
            }
        }).start();

    }

    public static Activity getCurrentActivity() {
        return activityList.getLast();

    }

    private void registerActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activityList.add(activity);
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
                if (activityList.size() == 0) {
                    return;
                }
                activityList.remove(activity);
            }
        });
    }
}

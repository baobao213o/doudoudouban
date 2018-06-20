package com.xxx.library;

import android.app.Activity;
import android.os.Process;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class AppManager {


    private static volatile AppManager instance;

    private static LinkedList<Activity> activityList;

    public static AppManager getInstance() {
        if (instance == null) {
            synchronized (AppManager.class) {
                if (instance == null) {
                    instance = new AppManager();
                }
            }
        }
        return instance;
    }

    private AppManager() {
        if (activityList == null) {
            activityList = new LinkedList<>();
        }
    }

    public void exit() {
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
                } finally {
                    Process.killProcess(Process.myPid());
                }
            }
        }).start();

    }

    public Activity getCurrentActivity() {
        Activity activity;
        try {
            activity = activityList.getLast();
            return activity;
        } catch (NoSuchElementException e) {
            return null;
        }

    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (activityList.size() == 0) {
            return;
        }
        activityList.remove(activity);
    }


}

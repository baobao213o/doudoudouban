package com.xxx.group;


import android.app.Application;
import android.content.Context;

import com.xxx.library.appdelegate.IAppLife;

public class GroupApplication implements IAppLife{
    @Override
    public void attachBaseContext(Context base) {
    }

    @Override
    public void onCreate(Application application) {

    }

    @Override
    public void onTerminate(Application application) {

    }
}

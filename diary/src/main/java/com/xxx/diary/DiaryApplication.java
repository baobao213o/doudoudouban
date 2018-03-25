package com.xxx.diary;


import android.app.Application;
import android.content.Context;

import com.xxx.library.appdelegate.IAppLife;

public class DiaryApplication implements IAppLife{
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

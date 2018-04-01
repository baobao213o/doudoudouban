package com.xxx.library.views;

import android.widget.Toast;

import com.xxx.library.BaseApplication;

public class ToastHelper {

    public static void showToast(String content) {

        Toast toast = Toast.makeText(BaseApplication.getInstance(), content, Toast.LENGTH_SHORT);
//        View view = LayoutInflater.from(BaseApplication.getInstance()).inflate(R.layout.common_view_toast, null);
//        toast.setView(view);
        toast.show();

    }

}


package com.xxx.library.views;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xxx.library.BaseApplication;
import com.xxx.library.R;

public class ToastHelper {

    public static void showToast(String content) {
        Toast toast = new Toast(BaseApplication.getInstance());
        View layout = View.inflate(BaseApplication.getInstance(), R.layout.common_view_toast, null);
        TextView textView = layout.findViewById(R.id.common_view_toast_content);
        textView.setText(content);
        toast.setView(layout);
        toast.show();
    }

}


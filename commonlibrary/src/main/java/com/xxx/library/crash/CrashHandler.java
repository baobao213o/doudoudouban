package com.xxx.library.crash;

import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xxx.library.BaseApplication;
import com.xxx.library.utils.IOUtil;
import com.xxx.library.views.ToastHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by gaoruochen on 18-4-9.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler sInstance = null;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // 保存手机信息和异常信息
    private Map<String, String> mMessage = new HashMap<>();

    public static CrashHandler getInstance() {
        if (sInstance == null) {
            synchronized (CrashHandler.class) {
                if (sInstance == null) {
                    synchronized (CrashHandler.class) {
                        sInstance = new CrashHandler();
                    }
                }
            }
        }
        return sInstance;
    }

    private CrashHandler() {
        //主线程异常
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    Looper.loop();
                } catch (Throwable e) {
                    ToastHelper.showToast("mainThread exception");
                    saveExceptions(e);
                }
            }
        });
    }


    private void saveExceptions(final Throwable e) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            new RxPermissions(BaseApplication.getCurrentActivity())
                    .requestEachCombined(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Permission>() {
                        @Override
                        public void accept(Permission permission) throws Exception {
                            if (permission.granted) {
                                try {
                                    collectErrorMessages();
                                    saveErrorMessages(e);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                } finally {
                                    BaseApplication.exit();
                                }
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // At least one denied permission without ask never again
                                ToastHelper.showToast("please agree storage permission to collect crash");
                                BaseApplication.exit();
                            } else {
                                // At least one denied permission with ask never again
                                // Need to go to the settings
                                ToastHelper.showToast("please go to the settings to agree storage permission");
                                BaseApplication.exit();
                            }
                        }
                    });
        }
    }

    /**
     * 捕获子线程异常
     */
    public void init() {
        // 获取默认异常处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 将此类设为默认异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e == null) {
            // 未经过人为处理,则调用系统默认处理异常,弹出系统强制关闭的对话框
            if (mDefaultHandler != null) {
                mDefaultHandler.uncaughtException(t, null);
            }
        } else {
            // 已经人为处理,系统自己退出
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    ToastHelper.showToast("thread exception");
                    Looper.loop();
                }
            }.start();
            saveExceptions(e);
        }
    }

    /**
     * 1.收集错误信息
     */
    private void collectErrorMessages() throws PackageManager.NameNotFoundException {
        PackageManager pm = BaseApplication.getInstance().getPackageManager();
        PackageInfo pi = pm.getPackageInfo(BaseApplication.getInstance().getPackageName(), PackageManager.GET_ACTIVITIES);
        if (pi != null) {
            String versionName = TextUtils.isEmpty(pi.versionName) ? "null" : pi.versionName;
            String versionCode = "" + pi.versionCode;
            mMessage.put("versionName", versionName);
            mMessage.put("versionCode", versionCode);
        }
        // 通过反射拿到错误信息
        Field[] fields = Build.class.getFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    mMessage.put(field.getName(), field.get(null).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 2.保存错误信息
     *
     * @param e Throwable
     */
    private void saveErrorMessages(Throwable e) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : mMessage.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        e.printStackTrace(pw);
        Throwable cause = e.getCause();
        // 循环取出Cause
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = e.getCause();
        }
        pw.close();
        String result = writer.toString();
        sb.append(result);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
        String fileName = "crash-" + time + "-" + System.currentTimeMillis() + ".txt";
        String path = IOUtil.externalPath + "/crash/";
        IOUtil.createFolder(path);
        IOUtil.write(sb.toString().getBytes(), new FileOutputStream(path + fileName));
    }
}
package com.xxx.library.crash;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xxx.library.AppManager;
import com.xxx.library.BaseApplication;
import com.xxx.library.R;
import com.xxx.library.utils.CommonLogger;
import com.xxx.library.utils.IOUtil;
import com.xxx.library.views.ToastHelper;
import com.xxx.library.views.dialog.DialogFragmentHelper;
import com.xxx.library.views.dialog.IDialogResultListener;

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
        CommonLogger.e(e);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                FragmentActivity activity = (FragmentActivity) AppManager.getInstance().getCurrentActivity();
                //activity未加载好之前申请权限有问题
                if (activity.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                    requestPermission(activity, e);
                } else {
                    activity.finish();
                    AppManager.getInstance().removeActivity(activity);
                    activity = (FragmentActivity) AppManager.getInstance().getCurrentActivity();
                    if (activity != null) {
                        requestPermission(activity, e);
                    } else {
                        AppManager.getInstance().exit();
                    }
                }
            } else {
                try {
                    collectErrorMessages();
                    saveErrorMessages(e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    AppManager.getInstance().exit();
                }
            }
        } else {
            AppManager.getInstance().exit();
        }
    }

    private void requestPermission(final FragmentActivity activity, final Throwable e) {
        activity.runOnUiThread(new Runnable() {
            @SuppressLint("CheckResult")
            @Override
            public void run() {
                new RxPermissions(activity)
                        .requestEachCombined(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) {
                                if (permission.granted) {
                                    try {
                                        collectErrorMessages();
                                        saveErrorMessages(e);
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    } finally {
                                        AppManager.getInstance().exit();
                                    }
                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    // At least one denied permission without ask never again
                                    DialogFragmentHelper.showConfirmDialog(activity.getSupportFragmentManager()
                                            , "please agree storage permission to collect crash"
                                            , activity.getString(R.string.common_dialog_exit)
                                            , null
                                            , new IDialogResultListener<Integer>() {
                                                @Override
                                                public void onDataResult(Integer which) {
                                                    AppManager.getInstance().exit();
                                                }
                                            }, false, null);
                                } else {
                                    // At least one denied permission with ask never again
                                    // Need to go to the settings
                                    DialogFragmentHelper.showConfirmDialog(activity.getSupportFragmentManager()
                                            , "please go to the settings to agree storage permission"
                                            , activity.getString(R.string.common_dialog_exit)
                                            , null
                                            , new IDialogResultListener<Integer>() {
                                                @Override
                                                public void onDataResult(Integer which) {
                                                    AppManager.getInstance().exit();
                                                }
                                            }, false, null);
                                }
                            }
                        });
            }
        });
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
            cause = cause.getCause();
        }
        pw.close();
        String result = writer.toString();
        sb.append(result);
        String time = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.CHINA).format(new Date());
        String fileName = "crash-" + time + "-" + System.currentTimeMillis() + ".txt";
        String path = IOUtil.externalPath + "/crash/";
        IOUtil.createFolder(path);
        IOUtil.write(sb.toString().getBytes(), new FileOutputStream(path + fileName));
    }
}

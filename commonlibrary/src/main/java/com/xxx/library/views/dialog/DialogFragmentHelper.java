package com.xxx.library.views.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.xxx.library.BaseApplication;
import com.xxx.library.R;

public class DialogFragmentHelper {

    private static final String DIALOG_POSITIVE = BaseApplication.getInstance().getString(R.string.common_dialog_confirm);
    private static final String DIALOG_NEGATIVE = BaseApplication.getInstance().getString(R.string.common_dialog_cancel);

    private static final String TAG_HEAD = DialogFragmentHelper.class.getSimpleName();

    /**
     * 加载中的弹出窗
     */
    public static final String PROGRESS_TAG = TAG_HEAD + ":progress";

    public static CommonDialogFragment createProgrssDialog(final String message, boolean cancelable) {
        return CommonDialogFragment.newInstance(new CommonDialogFragment.OnCallDialog() {
            @Override
            public Dialog getDialog(Context context) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_loading, null);
                builder.setView(view);
                TextView tv_common_dialog = view.findViewById(R.id.tv_common_loading_dialog_msg);
                tv_common_dialog.setText(message);
                return builder.create();
            }
        }, cancelable, PROGRESS_TAG);
    }

    /**
     * 简单提示弹出窗
     */
    public static final String TIPS_TAG = TAG_HEAD + ":tips";

    public static CommonDialogFragment createMessageDialog(final String message, boolean cancelable) {

        return CommonDialogFragment.newInstance(new CommonDialogFragment.OnCallDialog() {
            @Override
            public Dialog getDialog(Context context) {
                AlertDialog dialog = new AlertDialog.Builder(context).create();
                View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_tip, null);
                dialog.setView(view, 0, 0, 0, 0);
                TextView tv_common_dialog_tip = view.findViewById(R.id.tv_common_dialog_tip);
                tv_common_dialog_tip.setText(message);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                return dialog;
            }
        }, cancelable, TIPS_TAG);
    }


    /**
     * 确定取消框
     */
    private static final int CONFIRM_THEME = R.style.Base_AlertDialog;
    public static final String CONfIRM_TAG = TAG_HEAD + ":confirm";

    public static void showConfirmDialog(FragmentManager fragmentManager, final String message, final String confirm, final String cancel, final IDialogResultListener<Integer> listener
            , boolean cancelable, CommonDialogFragment.OnDialogCancelListener cancelListener) {
        CommonDialogFragment dialogFragment = CommonDialogFragment.newInstance(new CommonDialogFragment.OnCallDialog() {
            @Override
            public Dialog getDialog(Context context) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, CONFIRM_THEME);
                builder.setMessage(message);
                builder.setPositiveButton(TextUtils.isEmpty(confirm) ? DIALOG_POSITIVE : confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onDataResult(which);
                        }
                    }
                });
                if (cancel != null) {
                    builder.setNegativeButton(TextUtils.isEmpty(cancel) ? DIALOG_NEGATIVE : cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (listener != null) {
                                listener.onDataResult(which);
                            }
                        }
                    });
                }
                return builder.create();
            }
        }, cancelable, cancelListener, CONfIRM_TAG);
        dialogFragment.show(fragmentManager, CONfIRM_TAG);

    }

}


















package com.xxx.library.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;



public class CommonDialogFragment extends DialogFragment {

    /**
     * 监听弹出窗是否被取消
     */
    private OnDialogCancelListener mCancelListener;

    /**
     * 回调获得需要显示的 dialog
     */
    private OnCallDialog mOnCallDialog;

    private String style;

    public interface OnDialogCancelListener {
        void onCancel();
    }

    public interface OnCallDialog {
        Dialog getDialog(Context context);
    }

    public static CommonDialogFragment newInstance(OnCallDialog callDialog, boolean cancelable, String style) {
        return newInstance(callDialog, cancelable, null, style);
    }

    public static CommonDialogFragment newInstance(OnCallDialog callDialog, boolean cancelable, OnDialogCancelListener cancelListener, String style) {
        CommonDialogFragment instance = new CommonDialogFragment();
        instance.setCancelable(cancelable);
        instance.mCancelListener = cancelListener;
        instance.mOnCallDialog = callDialog;
        instance.style = style;
        return instance;
    }

    public void setCancelListener(OnDialogCancelListener mCancelListener) {
        this.mCancelListener = mCancelListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (null == mOnCallDialog) {
            super.onCreate(savedInstanceState);
        }
        return mOnCallDialog.getDialog(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
//            if (style.equals(DialogFragmentHelper.TIPS_TAG)) {
                Window window = getDialog().getWindow();
                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                window.setLayout((int) (dm.widthPixels * 0.6), ViewGroup.LayoutParams.WRAP_CONTENT);
//            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mCancelListener != null) {
            mCancelListener.onCancel();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCancelListener = null;
    }

}

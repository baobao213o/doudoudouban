package com.xxx.library.base;


import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.xxx.library.R;
import com.xxx.library.account.AccountHelper;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.mvp.view.IView;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.utils.dialog.CommonDialogFragment;
import com.xxx.library.utils.dialog.DialogFragmentHelper;
import com.xxx.library.utils.dialog.IDialogResultListener;

import io.reactivex.disposables.Disposable;


public abstract class BaseActivity<Entity, P extends BasePresenter> extends AppCompatActivity implements IView<Entity> {

    protected P presenter;
    private CommonDialogFragment progessDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        presenter = createPresenter();

    }

    protected abstract P createPresenter();

    @Override
    public void showLoading(final Disposable disposable, boolean cancel) {
        progessDialog = (CommonDialogFragment) getSupportFragmentManager().findFragmentByTag(DialogFragmentHelper.PROGRESS_TAG);
        if (progessDialog == null) {
            progessDialog = DialogFragmentHelper.createProgrssDialog("加载中...", cancel);
        }
        progessDialog.setCancelListener(new CommonDialogFragment.OnDialogCancelListener() {
            @Override
            public void onCancel() {
                presenter.cancelRequest(disposable);
            }
        });
        if (progessDialog.getDialog() == null || !progessDialog.getDialog().isShowing()) {
            progessDialog.show(getSupportFragmentManager(), DialogFragmentHelper.PROGRESS_TAG);
        }
    }


    @Override
    public void hideLoading() {
        if (progessDialog != null) {
            progessDialog.dismiss();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onSuccess(Entity data) {

    }

    @Override
    public void onFailure(ExceptionHandle.ResponseThrowable responseThrowable) {

    }

    @Override
    public void showErrorResult(String errorMsg) {
        //始终显示最新信息
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(DialogFragmentHelper.TIPS_TAG);
        if (null != fragment) {
            ft.remove(fragment);
        }
        CommonDialogFragment errorDialog = DialogFragmentHelper.createMessageDialog(errorMsg, true);
        ft.add(errorDialog, DialogFragmentHelper.TIPS_TAG);
        ft.commit();
    }

    @Override
    public void showAuthError(String errorMsg) {
        DialogFragmentHelper.showConfirmDialog(getSupportFragmentManager()
                ,errorMsg,getString(R.string.common_dialog_login)
                ,getString(R.string.common_dialog_cancel)
                ,new IDialogResultListener<Integer>() {
            @Override
            public void onDataResult(Integer which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    AccountHelper.getInstance().addAccount(BaseActivity.this, null);
                }
            }
        }, true, null);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.destroy();
        }
    }

    @Override
    protected void onDestroy() {
        if (progessDialog != null) {
            progessDialog.dismiss();
            progessDialog = null;
        }
        super.onDestroy();
    }
}

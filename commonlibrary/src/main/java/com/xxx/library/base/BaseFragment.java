package com.xxx.library.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.xxx.library.account.AccountHelper;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.mvp.view.IView;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.utils.dialog.CommonDialogFragment;
import com.xxx.library.utils.dialog.DialogFragmentHelper;
import com.xxx.library.utils.dialog.IDialogResultListener;

import io.reactivex.disposables.Disposable;

/**
 * Created by gaoruochen on 18-3-20.
 */

public abstract class BaseFragment <B, P extends BasePresenter> extends Fragment implements IView<B> {


    protected P presenter;
    private CommonDialogFragment progessDialog;


    protected abstract P createPresenter();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = createPresenter();
    }

    @Override
    public void showLoading(final Disposable disposable, boolean cancel) {
        progessDialog = (CommonDialogFragment) getChildFragmentManager().findFragmentByTag(DialogFragmentHelper.PROGRESS_TAG);
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
            progessDialog.show(getChildFragmentManager(), DialogFragmentHelper.PROGRESS_TAG);
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
        return getActivity();
    }

    @Override
    public void onSuccess(B data) {

    }

    @Override
    public void onFailure(ExceptionHandle.ResponeThrowable responeThrowable) {

    }

    @Override
    public void showErrorResult(String errorMsg) {
        //始终显示最新信息
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment fragment = getChildFragmentManager().findFragmentByTag(DialogFragmentHelper.TIPS_TAG);
        if (null != fragment) {
            ft.remove(fragment);
        }
        CommonDialogFragment errorDialog = DialogFragmentHelper.createMessageDialog(errorMsg, true);
        ft.add(errorDialog, DialogFragmentHelper.TIPS_TAG);
        ft.commit();
    }

    @Override
    public void showAuthError(String errorMsg) {
        DialogFragmentHelper.showConfirmDialog(getChildFragmentManager(), errorMsg, new IDialogResultListener<Integer>() {
            @Override
            public void onDataResult(Integer which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    AccountHelper.getInstance().addAccount(getActivity(), null);
                }
            }
        }, true, null);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.destroy();
        }
    }

    @Override
    public void onDestroyView() {
        if (progessDialog != null) {
            progessDialog.dismiss();
            progessDialog = null;
        }
        super.onDestroyView();
    }

}

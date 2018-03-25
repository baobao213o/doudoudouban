package com.xxx.library.network.exception;


import com.xxx.library.BaseApplication;
import com.xxx.library.R;
import com.xxx.library.account.AccountHelper;
import com.xxx.library.entity.ErrorResponse;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.utils.NetUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class HandleNetExceptionObserver<T> implements Observer<T> {

    private BasePresenter presenter;
    private boolean showLoadingDialog = true;
    private boolean cancelDialog = true;
    private boolean showErrorDialog = true;
    private boolean ifNeedNetworkAvalible = true;   //可能有缓存 忽略网络检测

    protected HandleNetExceptionObserver(BasePresenter presenter) {
        this.presenter = presenter;
    }

    public HandleNetExceptionObserver<T> isShowLoading(boolean showLoadingDialog) {
        this.showLoadingDialog = showLoadingDialog;
        return this;
    }

    public HandleNetExceptionObserver<T> setLoadingCancel(boolean cancelDialog) {
        this.cancelDialog = cancelDialog;
        return this;
    }

    public HandleNetExceptionObserver<T> isShowError(boolean showErrorDialog) {
        this.showErrorDialog = showErrorDialog;
        return this;
    }

    public HandleNetExceptionObserver<T> ifNeedNetworkAvalible(boolean ifNeedNetworkAvalible) {
        this.ifNeedNetworkAvalible = ifNeedNetworkAvalible;
        return this;
    }


    @Override
    public void onSubscribe(final Disposable d) {
        if (presenter == null) {
            return;
        }
        presenter.addDisposable(d);
        if (ifNeedNetworkAvalible && !NetUtil.isNetworkAvalible()) {
            presenter.getView().showErrorResult(BaseApplication.getInstance().getString(R.string.common_network_error));
            d.dispose();
            return;
        }
        if (showLoadingDialog) {
            presenter.getView().showLoading(d, cancelDialog);
        }
    }

    public abstract void onError(ExceptionHandle.ResponeThrowable responeThrowable);

    @Override
    public void onError(Throwable e) {
        if (showLoadingDialog && presenter != null) {
            presenter.getView().hideLoading();
        }
        if (e instanceof Exception) {
            //访问获得对应的Exception
            ExceptionHandle.ResponeThrowable responeThrowable = new ExceptionHandle().handleException(e);

            ErrorResponse error = responeThrowable.getResponseError();
            switch (error.code) {
                case ErrorResponse.ACCESS_TOKEN_IS_MISSING:
                case ErrorResponse.INVALID_ACCESS_TOKEN:
                case ErrorResponse.ACCESS_TOKEN_HAS_EXPIRED:
                    AccountHelper.getInstance().setAccountExpired(true);
                    showAuthError(responeThrowable);
                    break;
                default:
                    showErrorDialog(responeThrowable);
                    break;
            }
            onError(responeThrowable);


        } else {
            //将Throwable 和 未知错误的status code返回
            ExceptionHandle.ResponeThrowable responeThrowable = new ExceptionHandle().new ResponeThrowable(e, ExceptionHandle.ERROR.UNKNOW);
            showErrorDialog(responeThrowable);
            onError(responeThrowable);
        }
    }

    @Override
    public void onComplete() {
        if (showLoadingDialog && presenter != null) {
            presenter.getView().hideLoading();
        }
    }

    private void showErrorDialog(ExceptionHandle.ResponeThrowable responeThrowable) {
        if (showErrorDialog && presenter != null) {
            presenter.getView().showErrorResult(responeThrowable.message);
        }
    }

    private void showAuthError(ExceptionHandle.ResponeThrowable responeThrowable) {
        if (showErrorDialog && presenter != null) {
            presenter.getView().showAuthError(responeThrowable.message);
        }
    }

}

package com.xxx.library.network.exception;


import android.text.TextUtils;

import com.xxx.library.account.AccountHelper;
import com.xxx.library.entity.ErrorResponse;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.utils.NetUtil;

import java.net.ConnectException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class HandleNetExceptionObserver<T> implements Observer<T> {

    private BasePresenter presenter;
    private boolean showLoadingDialog = true;
    private boolean cancelDialog = true;
    private boolean showErrorDialog = true;
    private boolean ifNeedNetworkAvalible = true;   //可能有缓存 忽略网络检测
    private int requestCode = -1;   //请求码

    private final int ACTION_SHOW_LOADING = 0;
    private final int ACTION_HIDE_LOADING = 1;
    private final int ACTION_SHOW_ERRORMSG = 2;
    private final int ACTION_SHOW_AUTHERROR = 3;
    private final int ACTION_ON_FAILURE = 4;

    protected HandleNetExceptionObserver(BasePresenter presenter) {
        this.presenter = presenter;
    }

    protected HandleNetExceptionObserver(BasePresenter presenter, int requestCode) {
        this.presenter = presenter;
        this.requestCode = requestCode;
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
            onError(new ConnectException());
            d.dispose();
            return;
        }
        if (showLoadingDialog) {
            safeShowView(ACTION_SHOW_LOADING, null, d);
        }
    }


    @Override
    public void onError(Throwable e) {
        if (showLoadingDialog && presenter != null) {
            safeShowView(ACTION_HIDE_LOADING);
        }
        if (e instanceof Exception) {
            //访问获得对应的Exception
            ExceptionHandle.ResponseThrowable responseThrowable = new ExceptionHandle().handleException(e);
            ErrorResponse error = responseThrowable.getResponseError();
            switch (error.code) {
                case ErrorResponse.USER_LOCKED:
                case ErrorResponse.ACCESS_TOKEN_IS_MISSING:
                case ErrorResponse.INVALID_ACCESS_TOKEN:
                case ErrorResponse.ACCESS_TOKEN_HAS_EXPIRED:
                    AccountHelper.getInstance().removeAllAccount();
                    showAuthError(responseThrowable);
                    break;
                default:
                    break;
            }
            onError(responseThrowable);

        } else {
            //将Throwable 和 未知错误的status code返回
            ExceptionHandle.ResponseThrowable responseThrowable = new ExceptionHandle().new ResponseThrowable(e, ExceptionHandle.ERROR.UNKNOW);
            onError(responseThrowable);
        }
    }

    public void onError(ExceptionHandle.ResponseThrowable responseThrowable) {
        if (presenter != null) {
            if ((ifNeedNetworkAvalible && responseThrowable.code == ExceptionHandle.ERROR.NETWORD_ERROR) || showErrorDialog) {
                safeShowView(ACTION_SHOW_ERRORMSG, responseThrowable);
            }
            safeShowView(ACTION_ON_FAILURE, responseThrowable);
        }
    }

    @Override
    public void onComplete() {
        if (showLoadingDialog && presenter != null) {
            safeShowView(ACTION_HIDE_LOADING);
        }
    }

    private void showAuthError(ExceptionHandle.ResponseThrowable responseThrowable) {
        if (presenter != null) {
            safeShowView(ACTION_SHOW_AUTHERROR, responseThrowable);
        }
    }

    private void safeShowView(int action) {
        this.safeShowView(action, null);
    }

    private void safeShowView(int action, ExceptionHandle.ResponseThrowable responseThrowable) {
        this.safeShowView(action, responseThrowable, null);
    }

    private void safeShowView(int action, ExceptionHandle.ResponseThrowable responseThrowable, Disposable d) {
        if (presenter.isDestroy()) {
            return;
        }
        if (presenter.getView() == null) {
            return;
        }
        switch (action) {
            case ACTION_SHOW_LOADING:
                presenter.getView().showLoading(d, cancelDialog);
                break;
            case ACTION_HIDE_LOADING:
                presenter.getView().hideLoading();
                break;
            case ACTION_SHOW_ERRORMSG:
                String errorMsg = responseThrowable.message;
                if (TextUtils.isEmpty(errorMsg)) {
                    errorMsg = responseThrowable.getResponseError().msg;
                }
                presenter.getView().showErrorResult(errorMsg);
                break;
            case ACTION_SHOW_AUTHERROR:
                presenter.getView().showAuthError(responseThrowable.message);
                break;
            case ACTION_ON_FAILURE:
                presenter.getView().onFailure(responseThrowable, requestCode);
                break;
            default:
                break;
        }

    }

}

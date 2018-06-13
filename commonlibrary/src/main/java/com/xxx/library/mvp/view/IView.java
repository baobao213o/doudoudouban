package com.xxx.library.mvp.view;


import android.content.Context;

import com.xxx.library.network.exception.ExceptionHandle;

import io.reactivex.disposables.Disposable;

public interface IView {

    void onSuccess(Object data);

    void onFailure(ExceptionHandle.ResponseThrowable responseThrowable,int requestCode);

    void showLoading(Disposable disposable,boolean cancel);

    void hideLoading();

    void showErrorResult(String errorMsg);

    void showAuthError(String errorMsg);

    Context getContext();
}

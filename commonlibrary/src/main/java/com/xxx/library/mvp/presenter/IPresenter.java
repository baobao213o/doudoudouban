package com.xxx.library.mvp.presenter;


import io.reactivex.disposables.Disposable;

public interface IPresenter {

    void addDisposable(Disposable disposable);

    void clearDisposable();

    void cancelRequest(Disposable disposable);

    boolean isDestroy();

    void detachView();

}

package com.xxx.library.mvp.presenter;


import io.reactivex.disposables.Disposable;

public interface IPresenter {

    void addDisposable(Disposable disposable);

    void destroy();

    void cancelRequest(Disposable disposable);

}

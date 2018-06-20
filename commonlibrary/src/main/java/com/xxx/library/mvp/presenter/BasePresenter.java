package com.xxx.library.mvp.presenter;


import android.support.annotation.Nullable;

import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.mvp.view.IView;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<V extends IView, M extends BaseModel> implements IPresenter {


    private WeakReference<V> weakReferenceView;
    protected V mView;
    protected M mModel;
    private boolean isDestroy = false;

    private CompositeDisposable compositeDisposable;

    public BasePresenter(V mView, M mModel) {
        weakReferenceView = new WeakReference<>(mView);
        this.mView = weakReferenceView.get();
        this.mModel = mModel;
    }

    @Nullable
    public V getView() {
        return mView;
    }

    @Override
    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    public void clearDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
            compositeDisposable = null;
        }
        isDestroy = true;
    }

    @Override
    public boolean isDestroy() {
        return isDestroy;
    }

    @Override
    public void cancelRequest(Disposable d) {
        if (d != null && !d.isDisposed()) {
            d.dispose();
        }
    }

    @Override
    public void detachView() {
        weakReferenceView.clear();
        weakReferenceView = null;
        mView = null;
    }

}

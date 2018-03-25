package com.xxx.library.mvp.presenter;


import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.mvp.view.IView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<V extends IView, M extends BaseModel> implements IPresenter {

    protected V mView;
    protected M mModel;

    private CompositeDisposable compositeDisposable;

    public BasePresenter(V mView, M mModel) {
        this.mView = mView;
        this.mModel = mModel;
    }

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
    public void destroy() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
            compositeDisposable = null;
//            mView = null;
        }
    }

    @Override
    public void cancelRequest(Disposable d) {
        if (d != null && !d.isDisposed()) {
            d.dispose();
        }
    }
}

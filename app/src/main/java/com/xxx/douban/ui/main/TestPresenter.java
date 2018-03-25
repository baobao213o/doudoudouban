package com.xxx.douban.ui.main;


import com.xxx.douban.api.TestApi;
import com.xxx.library.entity.User;
import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.mvp.view.IView;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.network.exception.HandleNetExceptionObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

class TestPresenter extends BasePresenter<IView<User>,BaseModel> {

    TestPresenter(IView<User> mView, BaseModel model) {
        super(mView,model);
    }


    void getUsr() {
        mModel.getDataFromRemote(TestApi.class).getUserInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new HandleNetExceptionObserver<User>(this) {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {

            }

            @Override
            public void onNext(User userInfo) {
                mView.onSuccess(userInfo);
            }
        }.isShowLoading(false));
    }


}

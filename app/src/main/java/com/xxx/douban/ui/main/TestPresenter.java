package com.xxx.douban.ui.main;


import com.xxx.douban.api.TestApi;
import com.xxx.douban.entity.BookInfo;
import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.network.exception.HandleNetExceptionObserver;
import com.xxx.library.user.Contract;
import com.xxx.library.user.UserPresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

class TestPresenter extends UserPresenter<Contract.View, BaseModel> {

    TestPresenter(Contract.View mView, BaseModel model) {
        super(mView, model);
    }


    void getBookInfo() {
        mModel.postDataFromRemote(TestApi.class).getBookInfo("123123").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new HandleNetExceptionObserver<BookInfo>(this) {
            @Override
            public void onError(ExceptionHandle.ResponseThrowable responseThrowable) {

            }

            @Override
            public void onNext(BookInfo bookInfo) {
                mView.onSuccess(bookInfo);
            }
        }.isShowLoading(false));
    }


}

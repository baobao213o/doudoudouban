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

class TestPresenter extends UserPresenter<BookInfo,Contract.View<BookInfo>, BaseModel> {

    TestPresenter(Contract.View<BookInfo> mView, BaseModel model) {
        super(mView, model);
    }


    void getBookInfo() {
        mModel.getDataFromRemote(TestApi.class).getBookInfo("123123").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new HandleNetExceptionObserver<BookInfo>(this) {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {

            }

            @Override
            public void onNext(BookInfo bookInfo) {
                mView.onSuccess(bookInfo);
            }
        }.isShowLoading(false));
    }


}

package com.xxx.my.ui.user;

import com.xxx.library.entity.User;
import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.mvp.view.IView;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.network.exception.HandleNetExceptionObserver;
import com.xxx.my.api.UserApi;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gaoruochen on 18-3-20.
 */

public class UserPresenter extends BasePresenter<IView<User>,BaseModel> {

    UserPresenter(IView<User> mView, BaseModel model) {
        super(mView,model);
    }


    void getUsr() {
        mModel.getDataFromRemote(UserApi.class).getUserInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new HandleNetExceptionObserver<User>(this) {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                mView.onFailure(responeThrowable);
            }

            @Override
            public void onNext(User userInfo) {
                mView.onSuccess(userInfo);
            }
        }.isShowLoading(false).isShowError(false));
    }


}

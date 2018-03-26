package com.xxx.library.user;

import com.xxx.library.entity.User;
import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.network.exception.HandleNetExceptionObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gaoruochen on 18-3-20.
 */

public class UserPresenter<Entity,V extends Contract.View<Entity>,M extends BaseModel> extends BasePresenter<V, M> {

    public UserPresenter(V mView, M model) {
        super(mView, model);
    }

    public void getUserFromRemote() {
        mModel.getDataFromRemote(UserApi.class).getUserInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new HandleNetExceptionObserver<User>(this) {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                mView.onUserUpdateFailure(responeThrowable);
            }

            @Override
            public void onNext(User userInfo) {
                UserManager.getInstance().setUser(userInfo);
                mView.onUserUpdateSuccess(userInfo);
            }
        }.isShowLoading(false).isShowError(false));
    }


}

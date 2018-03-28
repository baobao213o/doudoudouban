package com.xxx.douban.ui.auth;

import com.xxx.library.Constant;
import com.xxx.library.account.AuthenticationApi;
import com.xxx.library.entity.AuthenticationResponse;
import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.mvp.view.IView;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.network.exception.HandleNetExceptionObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gaoruochen on 18-3-11.
 */

class AuthenticationPresenter extends BasePresenter<IView<AuthenticationResponse>, BaseModel> {


    AuthenticationPresenter(IView<AuthenticationResponse> mView, BaseModel mModel) {
        super(mView, mModel);
    }

    void authentication(String user, String pwd) {
        String client_id = Constant.Authentication.KEY;
        String client_secret = Constant.Authentication.SECRET;
        String redirect_uri = Constant.Authentication.REDIRECT_URI;
        String grant_type = Constant.Authentication.GrantType.GRANT_TYPE_PASSWORD;
        mModel.getDataFromRemote(AuthenticationApi.class).authenticate(client_id, client_secret, redirect_uri, grant_type, user, pwd).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new HandleNetExceptionObserver<AuthenticationResponse>(this) {
            @Override
            public void onError(ExceptionHandle.ResponseThrowable responseThrowable) {
                mView.onFailure(responseThrowable);
            }

            @Override
            public void onNext(AuthenticationResponse response) {
                mView.onSuccess(response);
            }
        }.isShowError(false));
    }
}

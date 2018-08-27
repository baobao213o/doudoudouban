package com.xxx.library.account.ui;

import android.annotation.SuppressLint;

import com.xxx.library.Constant;
import com.xxx.library.account.AuthStatus;
import com.xxx.library.account.AuthenticationApi;
import com.xxx.library.entity.AuthenticationResponse;
import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.network.exception.HandleNetExceptionObserver;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by gaoruochen on 18-3-11.
 */

class AuthenticationPresenter extends BasePresenter<AuthenticationContract.View> {


    AuthenticationPresenter(AuthenticationContract.View mView, BaseModel mModel) {
        super(mView, mModel);
    }

    void authentication(String user, String pwd) {
        String grant_type = Constant.Authentication.GrantType.GRANT_TYPE_PASSWORD;
        mModel.postDataFromRemote(AuthenticationApi.class).authenticate(grant_type, user, pwd).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new HandleNetExceptionObserver<AuthenticationResponse>(this) {
            @Override
            public void onNext(AuthenticationResponse response) {
                mView.onSuccess(response);
            }
        }.isShowError(false).ifNeedNetworkAvalible(false));

    }


    @SuppressLint("CheckResult")
    void saveAuthenticationResponse(final AuthStatus auth) {

        mModel.modifyDataFromLocal(new Function<Realm, AuthStatus>() {
            @Override
            public AuthStatus apply(Realm realm) throws Exception {
                return realm.copyToRealmOrUpdate(auth);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<AuthStatus>() {
            @Override
            public void accept(AuthStatus authStatus) throws Exception {
                mView.saveAuthStatusSuccess();
            }
        });
    }

    void getAuthenticationResponse() {
        mModel.getDataFromLocal(AuthStatus.class).subscribe(new Consumer<List<AuthStatus>>() {
            @Override
            public void accept(List<AuthStatus> authStatuses) {
                mView.showAuthStatus(authStatuses);
            }
        }).dispose();
    }

}

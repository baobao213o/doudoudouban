package com.xxx.library.user;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xxx.library.base.BaseActivity;
import com.xxx.library.entity.ErrorResponse;
import com.xxx.library.entity.User;
import com.xxx.library.network.exception.ExceptionHandle;

/**
 * Created by 50640 on 2018/3/26.
 */

public abstract class BaseUserActivity<Entity, P extends UserPresenter> extends BaseActivity<Entity, P> implements UserManager.IUser, Contract.View<Entity> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBaseUserActivityCreate(savedInstanceState);
        UserManager.getInstance().register(this, this);
        if(!shouldInitUserInfo()){
            return;
        }
        User user = UserManager.getInstance().getUser(savedInstanceState);
        if (user == null) {
            presenter.getUserFromRemote();
        } else {
            onUserUpdateSuccess(user);
        }
    }


    public abstract void onBaseUserActivityCreate(Bundle savedInstanceState);


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserManager.getInstance().unregiser(this);
    }


    @Override
    public void getUserFromRemote() {
        presenter.getUserFromRemote();
    }

    @Override
    public void onUserUpdateSuccess(User user) {
        UserManager.getInstance().setUser(user);
    }

    @Override
    public void onUserUpdateFailure(ExceptionHandle.ResponseThrowable responseThrowable) {
        ErrorResponse error = responseThrowable.getResponseError();
        switch (error.code) {
            case ErrorResponse.NEED_PERMISSION:
                showAuthError(responseThrowable.message);
                break;
        }
    }

    public boolean shouldInitUserInfo(){
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        UserManager.getInstance().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
}

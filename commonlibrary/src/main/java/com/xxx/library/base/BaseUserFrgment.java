package com.xxx.library.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.xxx.library.account.AccountHelper;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.rxjava.RxBusManager;
import com.xxx.library.utils.CommonLogger;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by gaoruochen on 18-3-23.
 */

public abstract class BaseUserFrgment<Entity, P extends BasePresenter> extends BaseFragment<Entity, P> {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Disposable disposable = RxBusManager.getInstance().registerEvent(Integer.class, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                switch (integer) {
                    case AccountHelper.RXBUS_LOGIN:
                        updateUserInfo();
                        break;
                    case AccountHelper.RXBUS_LOGOUT:
                        removeUserInfo();
                        break;
                    default:
                        break;
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                CommonLogger.e(throwable.getMessage());
            }
        });
        RxBusManager.getInstance().addSubscription(this, disposable);
    }


    public abstract void updateUserInfo();

    public abstract void removeUserInfo();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBusManager.getInstance().unSubscrible(this);
    }
}

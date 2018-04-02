package com.xxx.library.user;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.xxx.library.BaseApplication;
import com.xxx.library.account.AccountHelper;
import com.xxx.library.entity.User;
import com.xxx.library.rxjava.RxBusManager;
import com.xxx.library.utils.CommonLogger;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by 50640 on 2018/3/26.
 */


public class UserManager {


    private final static String USER_SAVE = "user_save";

    interface IUser {


        void getUserFromRemote();

        void clearUserStatus();

    }

    private static User user;

    private static UserManager instance = new UserManager();

    public static UserManager getInstance() {
        return instance;
    }

    void register(Context context, final IUser listener) {

        Disposable disposable = RxBusManager.getInstance().registerEvent(Integer.class, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                switch (integer) {
                    case AccountHelper.RXBUS_UPDATE_USER_STATUS:
                        listener.getUserFromRemote();
                        break;
                    case AccountHelper.RXBUS_CLEAR_USER_STATUS:
                        listener.clearUserStatus();
                        break;
                    default:
                        break;
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                CommonLogger.e(throwable.getMessage());
            }
        });
        RxBusManager.getInstance().addSubscription(context, disposable);
    }

    void unregiser(Context context) {
        RxBusManager.getInstance().unSubscrible(context);
    }


    void setUser(User userInfo) {
        user = userInfo;
    }

    public User getUser() {
        return getUser(null);
    }

    void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(USER_SAVE, user);
    }

    public User getUser(Bundle outState) {
        if (outState != null) {
            user = outState.getParcelable(USER_SAVE);
        }
        return user;
    }


}

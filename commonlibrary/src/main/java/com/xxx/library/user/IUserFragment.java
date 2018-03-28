package com.xxx.library.user;

import com.xxx.library.entity.User;
import com.xxx.library.network.exception.ExceptionHandle;

/**
 * Created by 50640 on 2018/3/26.
 */

public interface IUserFragment {

    void onUserUpdateFailure(ExceptionHandle.ResponseThrowable responseThrowable);

    void onUserUpdateSuccess(User user);

    void clearUserStatus();

}

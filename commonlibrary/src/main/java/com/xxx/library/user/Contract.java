package com.xxx.library.user;

import com.xxx.library.entity.User;
import com.xxx.library.mvp.view.IView;
import com.xxx.library.network.exception.ExceptionHandle;

/**
 * Created by 50640 on 2018/3/26.
 */

public interface Contract {

    interface View<Entity> extends IView<Entity> {

        void onUserUpdateSuccess(User user);

        void onUserUpdateFailure(ExceptionHandle.ResponseThrowable responseThrowable);
    }


}

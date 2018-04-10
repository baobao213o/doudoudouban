package com.xxx.douban.ui.auth;

import com.xxx.douban.entity.AuthStatus;
import com.xxx.library.mvp.view.IView;

import java.util.List;


/**
 * Created by gaoruochen on 18-4-8.
 */

public class AuthenticationContract {

    interface View<Entity> extends IView<Entity> {

        void showAuthStatus(List<AuthStatus> authStatus);

        void saveAuthStatusSuccess();
    }

}

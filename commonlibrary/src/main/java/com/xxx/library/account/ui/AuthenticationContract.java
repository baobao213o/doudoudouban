package com.xxx.library.account.ui;

import com.xxx.library.account.AuthStatus;
import com.xxx.library.mvp.view.IView;

import java.util.List;


/**
 * Created by gaoruochen on 18-4-8.
 */

class AuthenticationContract {

    interface View extends IView {

        void showAuthStatus(List<AuthStatus> authStatus);

        void saveAuthStatusSuccess();
    }

}

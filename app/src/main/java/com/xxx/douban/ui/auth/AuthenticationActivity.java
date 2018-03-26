package com.xxx.douban.ui.auth;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xxx.douban.R;
import com.xxx.library.Constant;
import com.xxx.library.account.AccountHelper;
import com.xxx.library.account.AppCompatAccountAuthenticatorActivity;
import com.xxx.library.entity.AuthenticationResponse;
import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.mvp.view.IView;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.rxjava.RxBusManager;
import com.xxx.library.utils.FormUtil;

import static com.xxx.library.Constant.AuthenticationAccount.AUTH_MODE_CONFIRM;
import static com.xxx.library.Constant.AuthenticationAccount.AUTH_MODE_NEW;
import static com.xxx.library.Constant.AuthenticationAccount.AUTH_MODE_UPDATE;
import static com.xxx.library.Constant.AuthenticationAccount.EXTRA_AUTH_MODE;

/**
 *
 */
@Route(path = "/main/auth/AuthenticationActivity")
public class AuthenticationActivity extends AppCompatAccountAuthenticatorActivity<AuthenticationResponse, AuthenticationPresenter> implements IView<AuthenticationResponse> {

    private AutoCompleteTextView etUser;
    private EditText etPwd;
    private TextInputLayout tilUser;
    private TextInputLayout tilPwd;
    private int authMode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_auth);

        authMode = getIntent().getIntExtra(EXTRA_AUTH_MODE, -1);

        etUser = findViewById(R.id.tv_main_auth_user);
        etPwd = findViewById(R.id.et_main_auth_pwd);
        tilUser = findViewById(R.id.til_main_auth_user);
        tilPwd = findViewById(R.id.til_main_auth_pwd);

        findViewById(R.id.tv_main_auth_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authentication();
            }
        });

        etPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    authentication();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected AuthenticationPresenter createPresenter() {
        return new AuthenticationPresenter(this, new BaseModel());
    }

    private void authentication() {
        String usr = etUser.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        if (!FormUtil.checkEmail(usr) && !FormUtil.isValidPhoneNumber(usr)) {
            tilUser.setError(getString(R.string.main_auth_verify_user));
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            tilPwd.setError(getString(R.string.main_auth_verify_pwd));
            return;
        }
        tilUser.setError("");
        tilPwd.setError("");
        presenter.authentication(usr, pwd);
    }

    @Override
    public void onSuccess(AuthenticationResponse data) {
        super.onSuccess(data);
        String usr = etUser.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();

        Account account = new Account(usr, Constant.AuthenticationAccount.ACCOUNT_TYPE);

        switch (authMode) {
            case AUTH_MODE_NEW:
                AccountHelper.getInstance().addAccountExplicitly(account, pwd);
                break;
            case AUTH_MODE_UPDATE:
            case AUTH_MODE_CONFIRM:
                AccountHelper.getInstance().setUserPwd(pwd);
                break;
            default:
                throw new IllegalArgumentException();
        }


        AccountHelper.getInstance().setUserName(data.userName);
        AccountHelper.getInstance().setUserId(data.userId);
        AccountHelper.getInstance().setAuthToken(data.accessToken);
        AccountHelper.getInstance().setRefreshToken(data.refreshToken);

        RxBusManager.getInstance().post(AccountHelper.RXBUS_UPDATE_USER_STATUS);

        Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, data.userName);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constant.AuthenticationAccount.ACCOUNT_TYPE);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    @Override
    public void onFailure(ExceptionHandle.ResponeThrowable responeThrowable) {
        super.onFailure(responeThrowable);
        tilPwd.setError(responeThrowable.message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        etPwd = null;
    }
}


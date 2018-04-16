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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xxx.douban.R;
import com.xxx.douban.entity.AuthStatus;
import com.xxx.library.Constant;
import com.xxx.library.account.AccountHelper;
import com.xxx.library.account.AppCompatAccountAuthenticatorActivity;
import com.xxx.library.entity.AuthenticationResponse;
import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.rxjava.RxBusManager;
import com.xxx.library.utils.FormUtil;

import java.util.ArrayList;
import java.util.List;

import static com.xxx.library.Constant.AuthenticationAccount.AUTH_MODE_CONFIRM;
import static com.xxx.library.Constant.AuthenticationAccount.AUTH_MODE_NEW;
import static com.xxx.library.Constant.AuthenticationAccount.AUTH_MODE_UPDATE;
import static com.xxx.library.Constant.AuthenticationAccount.EXTRA_AUTH_MODE;

/**
 *
 */
@Route(path = "/main/auth/AuthenticationActivity")
public class AuthenticationActivity extends AppCompatAccountAuthenticatorActivity<AuthenticationPresenter> implements AuthenticationContract.View {

    private AutoCompleteTextView etUser;
    private EditText etPwd;
    private TextInputLayout tilUser;
    private TextInputLayout tilPwd;
    private int authMode = -1;
    private AuthStatus status;
    private AuthenticationResponse data;

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
                if (id == EditorInfo.IME_ACTION_SEND) {
                    authentication();
                    return true;
                }
                return false;
            }
        });

        presenter.getAuthenticationResponse();
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
        status = new AuthStatus();
        status.username = usr;
        status.password = pwd;
        presenter.authentication(usr, pwd);
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
        this.data = (AuthenticationResponse) data;
        presenter.saveAuthenticationResponse(status);
    }

    @Override
    public void onFailure(ExceptionHandle.ResponseThrowable responseThrowable) {
        super.onFailure(responseThrowable);
        tilPwd.setError(responseThrowable.message);
    }

    @Override
    public void showAuthStatus(final List<AuthStatus> authStatus) {
        if (authStatus == null || authStatus.size() == 0) {
            return;
        }
        ArrayList<String> strings = new ArrayList<>();
        for (AuthStatus temp : authStatus) {
            strings.add(temp.username);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strings);
        etUser.setAdapter(adapter);
        etUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etPwd.setText(authStatus.get(position).password);
            }
        });
    }

    @Override
    public void saveAuthStatusSuccess() {

        AccountHelper.getInstance().removeAllAccount();
        Account account = new Account(status.username, Constant.AuthenticationAccount.ACCOUNT_TYPE);
        switch (authMode) {
            case AUTH_MODE_NEW:
                AccountHelper.getInstance().addAccountExplicitly(account, status.password);
                break;
            case AUTH_MODE_UPDATE:
            case AUTH_MODE_CONFIRM:
                AccountHelper.getInstance().setUserPwd(status.password);
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
}


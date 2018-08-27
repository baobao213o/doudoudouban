package com.xxx.library.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.xxx.library.Constant;
import com.xxx.library.account.ui.AuthenticationActivity;
import com.xxx.library.entity.AuthenticationResponse;
import com.xxx.library.network.RetrofitManager;
import com.xxx.library.utils.CommonLogger;

import retrofit2.Call;

import static com.xxx.library.Constant.AuthenticationAccount.AUTH_MODE_NEW;
import static com.xxx.library.Constant.AuthenticationAccount.EXTRA_AUTH_MODE;


/**
 * Created by gaoruochen on 18-3-12.
 */

public class Authenticator extends AbstractAccountAuthenticator {
    private Context mContext;

    Authenticator(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Postcard postcard;
        Class destination;
        if (AccountHelper.getInstance().getActiveAccount() != null) {
            postcard = ARouter.getInstance().build(Constant.ARouter.AROUTER_MAIN_MAIN);
            LogisticsCenter.completion(postcard);
            destination = postcard.getDestination();
        } else {
            destination = AuthenticationActivity.class;
        }
        Intent intent = new Intent(mContext, destination);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(EXTRA_AUTH_MODE, AUTH_MODE_NEW);
        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;

    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, final Account account, final String authTokenType, Bundle options) throws NetworkErrorException {
        Bundle bundle;
        if (!authTokenType.equals(Constant.AuthenticationAccount.ACCOUNT_AUTH_TOKEN)) {
            bundle = new Bundle();
            bundle.putInt(AccountManager.KEY_ERROR_CODE, AccountManager.ERROR_CODE_BAD_ARGUMENTS);
            bundle.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authToken" + authTokenType);
            return bundle;
        }

        String authToken = AccountHelper.getInstance().peekAuthToken();
        String grant_type = Constant.Authentication.GrantType.GRANT_TYPE_REFRESH;
        if (TextUtils.isEmpty(authToken)) {
            String refreshToken = AccountHelper.getInstance().getRefreshToken();
            if (!TextUtils.isEmpty(refreshToken)) {
                try {
                    Call<AuthenticationResponse> call = RetrofitManager.getInstance().getService(AuthenticationApi.class).authenticate(grant_type, refreshToken);
                    AuthenticationResponse authenticationResponse = call.execute().body();
                    if (authenticationResponse != null) {
                        authToken = authenticationResponse.accessToken;
                        AccountHelper.getInstance().setUserName(authenticationResponse.userName);
                        AccountHelper.getInstance().setUserId(authenticationResponse.userId);
                        AccountHelper.getInstance().setRefreshToken(authenticationResponse.refreshToken);
                    }
                } catch (Exception e) {
                    CommonLogger.e(e.toString());
                }
            }
        }

        if (TextUtils.isEmpty(authToken)) {
            bundle = new Bundle();
            bundle.putInt(AccountManager.KEY_ERROR_CODE, AccountManager.ERROR_CODE_INVALID_RESPONSE);
            bundle.putString(AccountManager.KEY_ERROR_MESSAGE, "authToken is still null");
            return bundle;
        }
        bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, Constant.AuthenticationAccount.ACCOUNT_TYPE);
        bundle.putString(AccountManager.KEY_AUTHTOKEN, authToken);
        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}

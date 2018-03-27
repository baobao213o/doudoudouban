package com.xxx.library.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.xxx.library.BaseApplication;
import com.xxx.library.entity.User;

import java.io.IOException;

import static com.xxx.library.Constant.AuthenticationAccount.ACCOUNT_AUTH_TOKEN;
import static com.xxx.library.Constant.AuthenticationAccount.ACCOUNT_REFRESH_TOKEN;
import static com.xxx.library.Constant.AuthenticationAccount.ACCOUNT_TYPE;
import static com.xxx.library.Constant.AuthenticationAccount.ACCOUNT_USER_ID;
import static com.xxx.library.Constant.AuthenticationAccount.ACCOUNT_USER_NAME;
import static com.xxx.library.Constant.AuthenticationAccount.INVALID_USER_ID;

/**
 * Created by gaoruochen on 18-3-12.
 */

public class AccountHelper {

    private static volatile AccountHelper instance;

    private static AccountManager accountManager;

    private static final String ACCOUNT_ACTIVE_ISEXPIRED = "account_active_isexpired";

    private static final String ACCOUNT_USER = "account_user";

    public static final int RXBUS_UPDATE_USER_STATUS = 1;

    public static final int RXBUS_CLEAR_USER_STATUS = 2;

    public static AccountHelper getInstance() {
        if (instance == null) {
            synchronized (AccountHelper.class) {
                if (instance == null) {
                    instance = new AccountHelper();
                }
            }
        }
        return instance;
    }

    private AccountHelper() {
        if (accountManager == null) {
            accountManager = AccountManager.get(BaseApplication.getInstance());
        }
    }

    public void addAccountExplicitly(Account account, String password) {
        accountManager.addAccountExplicitly(account, password, null);
    }

    public void setAuthToken(String authToken) {
        if (isAccountExpired()) {
            setAccountExpired(false);
        }
        accountManager.setAuthToken(getActiveAccount(), ACCOUNT_AUTH_TOKEN, authToken);
    }

    public String peekAuthToken() {
        return accountManager.peekAuthToken(getActiveAccount(), ACCOUNT_AUTH_TOKEN);
    }

    public void setRefreshToken(String token) {
        if (isAccountExpired()) {
            setAccountExpired(false);
        }
        setString(getActiveAccount(), ACCOUNT_REFRESH_TOKEN, token);
    }

    public String getRefreshToken() {
        return getString(getActiveAccount(), ACCOUNT_REFRESH_TOKEN, "");
    }

    public void setUserName(String name) {
        if (isAccountExpired()) {
            setAccountExpired(false);
        }
        setString(getActiveAccount(), ACCOUNT_USER_NAME, name);
    }


    public String getUserName() {
        return getString(getActiveAccount(), ACCOUNT_USER_NAME, "");
    }

    public void setUserId(long id) {
        if (isAccountExpired()) {
            setAccountExpired(false);
        }
        setLong(getActiveAccount(), ACCOUNT_USER_ID, id);
    }

    public long getUserId() {
        return getLong(getActiveAccount(), ACCOUNT_USER_ID, INVALID_USER_ID);
    }

    public void setUserPwd(String password) {
        accountManager.setPassword(getActiveAccount(), password);
    }

    public String getUserPwd() {
        return accountManager.getPassword(getActiveAccount());
    }

    private boolean isAccountExpired() {
        return getAccounts().length > 0 && getBoolean(getAccounts()[0], ACCOUNT_ACTIVE_ISEXPIRED, false);
    }

    public void setAccountExpired(boolean value) {
        if (getAccounts().length <= 0) {
            return ;
        }
        setBoolean(getAccounts()[0], ACCOUNT_ACTIVE_ISEXPIRED, value);
    }


    public void invalidateAuthToken(String token) {
        accountManager.invalidateAuthToken(ACCOUNT_TYPE, token);
    }

    public void addAccount(Activity activity, AccountManagerCallback<Bundle> callback) {
        accountManager.addAccount(ACCOUNT_TYPE, ACCOUNT_AUTH_TOKEN, null, null, activity, callback, null);
    }

    public void setUser(User user) {
        if (!isAccountExpired()) {
            String userStr = new Gson().toJson(user);
            setString(getActiveAccount(),ACCOUNT_USER, userStr);
        }
    }

    public User getUser() {
        if (!isAccountExpired()) {
            String userStr = getString(getActiveAccount(),ACCOUNT_USER, "");
            return new Gson().fromJson(userStr, User.class);
        }
        return null;
    }


    public boolean checkActiveAccount(final Activity activity) {
        if (getActiveAccount() == null) {
            addAccount(activity, new AccountManagerCallback<Bundle>() {
                @Override
                public void run(AccountManagerFuture<Bundle> future) {
                    activity.finish();
                    try {
                        Bundle result = future.getResult();
                        if (result.containsKey(AccountManager.KEY_ACCOUNT_NAME) && result.containsKey(AccountManager.KEY_ACCOUNT_TYPE)) {
                            activity.startActivity(activity.getIntent());
                        }
                    } catch (AuthenticatorException | IOException | OperationCanceledException e) {
                        e.printStackTrace();
                    }
                }
            });
            return false;
        }
        return true;
    }


    public Account getActiveAccount() {
        Account[] accounts = getAccounts();
        if (accounts.length == 0) {
            return null;
        }
        return isAccountExpired() ? null : accounts[0];
    }


    public String getAuthToken() throws Exception {
        AccountManagerFuture<Bundle> future = accountManager.getAuthToken(getActiveAccount(), ACCOUNT_AUTH_TOKEN, null, true, null, null);
        Bundle result;
        try {
            result = future.getResult();
        } catch (Exception e) {
            throw new IOException("Error when retrieving auth token", e);
        }
        String authToken = null;
        if (future.isDone() && !future.isCancelled()) {
            if (result.containsKey(AccountManager.KEY_INTENT)) {
                Intent intent = result.getParcelable(AccountManager.KEY_INTENT);
                throw new IOException("Got Intent when retrieving auth token: " + intent);
            }
            authToken = result.getString(AccountManager.KEY_AUTHTOKEN);
        }
        if (authToken == null) {
            throw new IOException("Got null auth token for type: " + ACCOUNT_AUTH_TOKEN);
        }
        return authToken;
    }


    private Account[] getAccounts() {
        return accountManager.getAccountsByType(ACCOUNT_TYPE);
    }


    private String getString(Account account, String key, String defaultValue) {
        String value = accountManager.getUserData(account, key);
        return value != null ? value : defaultValue;
    }

    private void setString(Account account, String key, String value) {
        accountManager.setUserData(account, key, value);
    }


    private long getLong(Account account, String key, long defaultValue) {
        String stringValue = getString(account, key, null);
        if (stringValue == null) {
            return defaultValue;
        } else {
            try {
                return Long.parseLong(stringValue);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return defaultValue;
            }
        }
    }

    private void setLong(Account account, String key, long value) {
        accountManager.setUserData(account, key, Long.toString(value));
    }

    private boolean getBoolean(Account account, String key, boolean defaultValue) {
        String stringValue = getString(account, key, null);
        if (stringValue == null) {
            return defaultValue;
        } else {
            try {
                return Boolean.parseBoolean(stringValue);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return defaultValue;
            }
        }
    }

    private void setBoolean(Account account, String key, boolean value) {
        accountManager.setUserData(account, key, Boolean.toString(value));
    }

}

package com.xxx.library.network.interceptor;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xxx.library.Constant;
import com.xxx.library.account.AccountHelper;
import com.xxx.library.entity.AuthenticationResponse;
import com.xxx.library.entity.ErrorResponse;
import com.xxx.library.utils.CommonLogger;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by gaoruochen on 18-3-16.
 */

public class AuthenticateInterceptor implements Interceptor {

    private static String token;
    private static final int MaxNumRetries = 3;

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response response = chain.proceed(makeBearerAuthorizationRequest(chain));
        if (!response.isSuccessful()) {
            ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);
            try {
                ErrorResponse error = new Gson().fromJson(responseBody.string(), ErrorResponse.class);
                responseBody.close();
                switch (error.code) {
                    case ErrorResponse.ACCESS_TOKEN_IS_MISSING:
                    case ErrorResponse.INVALID_ACCESS_TOKEN:
                    case ErrorResponse.ACCESS_TOKEN_HAS_EXPIRED:
                        //清空token缓存 否则不会调用authenticator getAuthToken()
                        if (!TextUtils.isEmpty(token)) {
                            AccountHelper.getInstance().invalidateAuthToken(token);
                        }
                        //通过refreshtoken重新获取token
                        token = AccountHelper.getInstance().getAuthToken();
                        break;
                    default:
                        return response;
                }
                //更新token 重发此请求
                int tryCount = 0;
                while (!response.isSuccessful() && tryCount < MaxNumRetries) {
                    CommonLogger.d("intercept", "Request is not successful - " + tryCount);
                    tryCount++;
                    response = chain.proceed(makeBearerAuthorizationRequest(chain));
                }

            } catch (Exception e) {
                e.printStackTrace();
                return response;
            }
        }
        //授权成功刷新token
        if (response.request().url().toString().equals(Constant.Authentication.URL) && response.isSuccessful()) {
            ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);
            AuthenticationResponse authenticationResponse = new Gson().fromJson(responseBody.string(), AuthenticationResponse.class);
            token = authenticationResponse.accessToken;
        }

        return response;
    }

    private Request makeBearerAuthorizationRequest(Chain chain) {
        Request request = chain.request();
        if (request.url().toString().equals(Constant.Authentication.URL)) {
            String client_id = Constant.Authentication.KEY;
            String client_secret = Constant.Authentication.SECRET;
            String redirect_uri = Constant.Authentication.REDIRECT_URI;
            HttpUrl httpUrl = request.url()
                    .newBuilder()
                    .addQueryParameter("client_id", client_id)
                    .addQueryParameter("client_secret", client_secret)
                    .addQueryParameter("redirect_uri", redirect_uri)
                    .build();
            request = request.newBuilder().url(httpUrl).build();
        }
        String prefix = "Bearer ";
        if (TextUtils.isEmpty(token)) {
            try {
                token = AccountHelper.getInstance().peekAuthToken();
            } catch (IllegalArgumentException e) {
                return request;
            }
        }
        request = request.newBuilder()
                .header(Constant.Authentication.Header.AUTHORIZATION, prefix + token)
                .build();
        return request;
    }

}

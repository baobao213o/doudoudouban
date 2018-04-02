package com.xxx.library.account;


import com.xxx.library.Constant;
import com.xxx.library.entity.AuthenticationResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthenticationApi {

    @POST(Constant.Authentication.URL)
    @FormUrlEncoded
    Observable<AuthenticationResponse> authenticate(
            @Field("grant_type") String grantType,
            @Field("username") String username, @Field("password") String password);

    @POST(Constant.Authentication.URL)
    @FormUrlEncoded
    Call<AuthenticationResponse> authenticate(
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refreshToken);
}

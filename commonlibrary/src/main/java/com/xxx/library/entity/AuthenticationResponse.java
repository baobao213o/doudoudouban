/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.xxx.library.entity;

import com.google.gson.annotations.SerializedName;

public class AuthenticationResponse {

    @SerializedName("access_token")
    public String accessToken;

    @SerializedName("douban_user_name")
    public String userName;

    @SerializedName("douban_user_id")
    public long userId;

    @SerializedName("expires_in")
    public long accessTokenExpiresIn;

    @SerializedName("refresh_token")
    public String refreshToken;
}

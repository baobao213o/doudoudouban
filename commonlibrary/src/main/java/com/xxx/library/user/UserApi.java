package com.xxx.library.user;


import com.xxx.library.Constant;
import com.xxx.library.entity.User;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface UserApi {

    @GET(Constant.BASE_URL + "v2/lifestream/user/~me")
    Observable<User> getUserInfo();

}

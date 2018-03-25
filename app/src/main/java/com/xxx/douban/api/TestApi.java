package com.xxx.douban.api;


import com.xxx.douban.entity.BookInfo;
import com.xxx.library.Constant;
import com.xxx.library.entity.User;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TestApi {
    @GET(Constant.BASE_URL + "v2/book/{id}")
    Observable<BookInfo> getBookInfo(@Path("id") String id);

    @GET(Constant.BASE_URL + "v2/user/~me")
    Observable<User> getUserInfo();

}

package com.xxx.syy.api;

import com.xxx.library.Constant;
import com.xxx.syy.entity.MovieInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by gaoruochen on 18-4-13.
 */

public interface SyyApi {

    interface MovieApi{
        @GET(Constant.BASE_URL + "v2/movie/top250")
        Observable<MovieInfo> getMovieTop250(@Query("start") String start, @Query("count") String count);
    }


}

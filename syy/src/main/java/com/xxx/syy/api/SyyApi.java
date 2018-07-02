package com.xxx.syy.api;

import com.xxx.library.Constant;
import com.xxx.syy.entity.CelebrityInfo;
import com.xxx.syy.entity.MovieDetailInfo;
import com.xxx.syy.entity.SearchMovieInfo;
import com.xxx.syy.entity.Top250MovieInfo;
import com.xxx.syy.entity.USBoxMovieInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by gaoruochen on 18-4-13.
 */

public interface SyyApi {

    interface MovieApi {
        @GET(Constant.BASE_URL + "v2/movie/top250")
        Observable<Top250MovieInfo> getMovieTop250(@Query("start") int start, @Query("count") int count);

        @GET(Constant.BASE_URL + "v2/movie/us_box")
        Observable<USBoxMovieInfo> getMovieUSbox();

        @GET(Constant.BASE_URL + "v2/movie/subject/{id}")
        Observable<MovieDetailInfo> getMovieDetail(@Path("id") String id);

        @GET(Constant.BASE_URL + "v2/movie/celebrity/{id}")
        Observable<CelebrityInfo> getCelebrityInfo(@Path("id") String id);
    }

    interface SearchApi {
        @GET(Constant.BASE_URL + "v2/movie/search")
        Observable<SearchMovieInfo> searchMovie(@Query("q") String q);
    }


}

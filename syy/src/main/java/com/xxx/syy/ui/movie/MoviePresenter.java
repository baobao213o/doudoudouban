package com.xxx.syy.ui.movie;

import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.network.exception.HandleNetExceptionObserver;
import com.xxx.syy.api.SyyApi;
import com.xxx.syy.entity.Top250MovieInfo;
import com.xxx.syy.entity.USBoxMovieInfo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gaoruochen on 18-4-13.
 */

class MoviePresenter extends BasePresenter<MovieContract.View> {

    MoviePresenter(MovieContract.View mView, BaseModel mModel) {
        super(mView, mModel);
    }


    void getTop250(int requestCode, int start, int count) {
        mModel.postDataFromRemote(SyyApi.MovieApi.class).getMovieTop250(start, count).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new HandleNetExceptionObserver<Top250MovieInfo>(this, requestCode) {
            @Override
            public void onNext(Top250MovieInfo info) {
                mView.showTop250Movies(info);
            }
        }.isShowLoading(false));
    }

    void getUSbox(int requestCode) {
        mModel.postDataFromRemote(SyyApi.MovieApi.class).getMovieUSbox().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new HandleNetExceptionObserver<USBoxMovieInfo>(this, requestCode) {
            @Override
            public void onNext(USBoxMovieInfo info) {
                mView.showUSboxMovies(info);
            }
        }.isShowLoading(false));
    }

}

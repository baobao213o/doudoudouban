package com.xxx.syy.ui.movie;

import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.network.exception.HandleNetExceptionObserver;
import com.xxx.syy.api.SyyApi;
import com.xxx.syy.entity.MovieInfo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gaoruochen on 18-4-13.
 */

class MoviePresenter extends BasePresenter<MovieContract.View, BaseModel>{

    MoviePresenter(MovieContract.View mView, BaseModel mModel) {
        super(mView, mModel);
    }


    void getTop250() {
        mModel.postDataFromRemote(SyyApi.MovieApi.class).getMovieTop250("0","10").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new HandleNetExceptionObserver<MovieInfo>(this) {
            @Override
            public void onError(ExceptionHandle.ResponseThrowable responseThrowable) {

            }

            @Override
            public void onNext(MovieInfo info) {
                mView.showTop250Movies(info);
            }
        }.isShowLoading(false));
    }

}

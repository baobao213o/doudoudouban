package com.xxx.syy.ui.movie.detail;

import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.mvp.view.IView;
import com.xxx.library.network.exception.HandleNetExceptionObserver;
import com.xxx.syy.api.SyyApi;
import com.xxx.syy.entity.MovieDetailInfo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gaoruochen on 18-6-2.
 */

class MovieDetailPresenter extends BasePresenter<IView> {

    MovieDetailPresenter(IView mView, BaseModel mModel) {
        super(mView, mModel);
    }

    protected void getMovieDetail(String id) {
        mModel.postDataFromRemote(SyyApi.MovieApi.class).getMovieDetail(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new HandleNetExceptionObserver<MovieDetailInfo>(this) {
            @Override
            public void onNext(MovieDetailInfo info) {
                mView.onSuccess(info);
            }
        }.isShowLoading(false));
    }

}

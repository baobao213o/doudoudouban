package com.xxx.syy.ui.movie.celebrity;

import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.mvp.view.IView;
import com.xxx.library.network.exception.HandleNetExceptionObserver;
import com.xxx.syy.api.SyyApi;
import com.xxx.syy.entity.CelebrityInfo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieCelebrityPresenter extends BasePresenter<IView> {


    MovieCelebrityPresenter(IView mView) {
        super(mView);
    }

    protected void getCelebrityInfo(String id) {
        mModel.postDataFromRemote(SyyApi.MovieApi.class).getCelebrityInfo(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new HandleNetExceptionObserver<CelebrityInfo>(this) {
            @Override
            public void onNext(CelebrityInfo info) {
                mView.onSuccess(info);
            }
        }.isShowLoading(false));
    }

}

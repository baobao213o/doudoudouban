package com.xxx.syy.ui.search;

import android.annotation.SuppressLint;

import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.network.exception.HandleNetExceptionObserver;
import com.xxx.syy.api.SyyApi;
import com.xxx.syy.entity.History;
import com.xxx.syy.entity.SearchMovieInfo;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by gaoruochen on 18-4-13.
 */

class SearchPresenter extends BasePresenter<SearchContract.View> {

    SearchPresenter(SearchContract.View mView) {
        super(mView);
    }

    void searchMovies(String q, int requestCode) {
        mModel.postDataFromRemote(SyyApi.SearchApi.class).searchMovie(q).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new HandleNetExceptionObserver<SearchMovieInfo>(this, requestCode) {
            @Override
            public void onNext(SearchMovieInfo info) {
                mView.showSearchMovies(info);
            }
        }.isShowLoading(false));
    }

    void getHistory() {
        mModel.getDataFromLocal(History.class).subscribe(new Consumer<List<History>>() {
            @Override
            public void accept(List<History> histories) {
                mView.showHistory(histories);
            }
        }).dispose();
    }

    @SuppressLint("CheckResult")
    void saveHistory(final History history) {

        mModel.modifyDataFromLocal(new Function<Realm, History>() {
            @Override
            public History apply(Realm realm) {
                return realm.copyToRealmOrUpdate(history);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<History>() {
            @Override
            public void accept(History history) {

            }
        });
    }

    boolean clearHistory() {
        return mModel.deleteDataFromLocal(History.class, -1);
    }

}

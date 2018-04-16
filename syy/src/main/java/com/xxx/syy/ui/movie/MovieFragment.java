package com.xxx.syy.ui.movie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xxx.library.base.BaseFragment;
import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.syy.R;
import com.xxx.syy.entity.MovieInfo;

/**
 * Created by gaoruochen on 18-4-12.
 */

public class MovieFragment extends BaseFragment<MoviePresenter> implements MovieContract.View {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.syy_fragment_movie, container, false);
        presenter.getTop250();
        return view;
    }

    @Override
    protected MoviePresenter createPresenter() {
        return new MoviePresenter(this, new BaseModel());
    }


    @Override
    public void onFailure(ExceptionHandle.ResponseThrowable responseThrowable) {
        super.onFailure(responseThrowable);
    }


    @Override
    public void showTop250Movies(MovieInfo movieInfo) {
        System.out.println(movieInfo.subjects.get(0).alt);
    }


}

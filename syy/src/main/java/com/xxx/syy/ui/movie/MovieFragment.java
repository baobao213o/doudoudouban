package com.xxx.syy.ui.movie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.xxx.library.base.BaseFragment;
import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.views.recyclerview.HeaderAndFooterWrapper;
import com.xxx.syy.R;
import com.xxx.syy.entity.Subjects;
import com.xxx.syy.entity.Top250MovieInfo;
import com.xxx.syy.entity.USBoxMovieInfo;

import java.util.ArrayList;

/**
 * Created by gaoruochen on 18-4-12.
 */

public class MovieFragment extends BaseFragment<MoviePresenter> implements MovieContract.View {

    private SwipeRefreshLayout srl_syy_movie;
    private RecyclerView rv_syy_movie;
    private MovieAdapter adapter;
    private ArrayList<Subjects> list = new ArrayList<>();
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private AppCompatSpinner sp_syy_head_movie;
    private boolean manualRefresh = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.syy_fragment_movie, container, false);
        rv_syy_movie = view.findViewById(R.id.rv_syy_movie);
        srl_syy_movie = view.findViewById(R.id.srl_syy_movie);

        initRecyclerview();

        srl_syy_movie.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                manualRefresh = true;
                presenter.getTop250();
            }
        });
        srl_syy_movie.post(new Runnable() {
            @Override
            public void run() {
                sp_syy_head_movie.setSelection(0);
            }
        });
        return view;
    }

    private void initRecyclerview() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_syy_movie.setLayoutManager(layoutManager);
        adapter = new MovieAdapter(getContext(), list);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        View header = LayoutInflater.from(getContext()).inflate(R.layout.syy_head_movie, null);
        sp_syy_head_movie = header.findViewById(R.id.sp_syy_head_movie);
        sp_syy_head_movie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (srl_syy_movie.isRefreshing() && !manualRefresh) {
                    manualRefresh = false;
                    return;
                }
                srl_syy_movie.setRefreshing(true);
                switch (position) {
                    case 0:
                        presenter.getTop250();
                        break;
                    case 1:
                        presenter.getUSbox();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mHeaderAndFooterWrapper.addHeaderView(header);
        rv_syy_movie.setAdapter(mHeaderAndFooterWrapper);
    }


    @Override
    protected MoviePresenter createPresenter() {
        return new MoviePresenter(this, new BaseModel());
    }


    @Override
    public void onFailure(ExceptionHandle.ResponseThrowable responseThrowable) {
        super.onFailure(responseThrowable);
        srl_syy_movie.setRefreshing(false);
    }


    @Override
    public void showTop250Movies(Top250MovieInfo top250MovieInfo) {
        srl_syy_movie.setRefreshing(false);
        adapter.setList((ArrayList<Subjects>) top250MovieInfo.subjects);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    @Override
    public void showUSboxMovies(USBoxMovieInfo movieInfo) {
        srl_syy_movie.setRefreshing(false);
        ArrayList<Subjects> subjects = new ArrayList<>();
        for (USBoxMovieInfo.SubjectsBean bean : movieInfo.subjects) {
            subjects.add(bean.subject);
        }
        adapter.setList(subjects);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }
}

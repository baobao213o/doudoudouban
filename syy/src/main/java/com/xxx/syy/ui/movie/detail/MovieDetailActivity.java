package com.xxx.syy.ui.movie.detail;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wang.avi.AVLoadingIndicatorView;
import com.xxx.library.base.BaseActivity;
import com.xxx.library.glide.GlideApp;
import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.mvp.view.IView;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.views.SpannableTextView;
import com.xxx.library.views.ToastHelper;
import com.xxx.syy.R;
import com.xxx.syy.entity.MovieDetailInfo;
import com.xxx.syy.entity.Subjects;

public class MovieDetailActivity extends BaseActivity<MovieDetailPresenter> implements IView {

    private FloatingActionButton fab_syy_movie_detail_share;
    private ImageView iv_syy_movie_detail_avator;
    private TextView tv_syy_movie_detail_info1, tv_syy_movie_detail_info2, tv_syy_movie_detail_info3, tv_syy_movie_detail_info4;
    private TextView tv_syy_movie_detail_score, tv_syy_movie_detail_scorenum, tv_syy_movie_detail_noscore;
    private RatingBar rating_syy_movie_detail_score;
    private AVLoadingIndicatorView view_loading;
    private View fl_loading;
    private SwipeRefreshLayout srl_syy_movie_detail;
    private SpannableTextView stv_syy_movie_detail_intro;

    private static final int Loading_success = 0;
    private static final int Loading_fail = 1;
    private static final int Loading_loading = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.syy_activity_movie_detail);
        if (savedInstanceState == null) {
            fab_syy_movie_detail_share = findViewById(R.id.fab_syy_movie_detail_share);
            fab_syy_movie_detail_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastHelper.showToast("share");
                }
            });
            final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.ctoolbar_syy_movie_detail_title);
            String title = getIntent().getStringExtra("title");
            collapsingToolbarLayout.setTitle(title);

            iv_syy_movie_detail_avator = findViewById(R.id.iv_syy_movie_detail_avator);

            String url = getIntent().getStringExtra("url");

            GlideApp.with(this).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                        public void onGenerated(@NonNull Palette palette) {
                            Palette.Swatch vibrant = palette.getMutedSwatch();//有活力的
                            if (vibrant != null) {
                                collapsingToolbarLayout.setContentScrimColor(vibrant.getRgb());
                            }
                        }
                    });
                    return false;
                }
            }).into(iv_syy_movie_detail_avator);


            tv_syy_movie_detail_info1 = findViewById(R.id.tv_syy_movie_detail_info1);
            tv_syy_movie_detail_info2 = findViewById(R.id.tv_syy_movie_detail_info2);
            tv_syy_movie_detail_info3 = findViewById(R.id.tv_syy_movie_detail_info3);
            tv_syy_movie_detail_info4 = findViewById(R.id.tv_syy_movie_detail_info4);

            tv_syy_movie_detail_score = findViewById(R.id.tv_syy_movie_detail_score);
            tv_syy_movie_detail_scorenum = findViewById(R.id.tv_syy_movie_detail_scorenum);
            rating_syy_movie_detail_score = findViewById(R.id.rating_syy_movie_detail_score);
            tv_syy_movie_detail_noscore = findViewById(R.id.tv_syy_movie_detail_noscore);

            stv_syy_movie_detail_intro = findViewById(R.id.stv_syy_movie_detail_intro);

            view_loading = findViewById(R.id.view_loading);
            fl_loading = findViewById(R.id.fl_loading);

            srl_syy_movie_detail = findViewById(R.id.srl_syy_movie_detail);
            srl_syy_movie_detail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadingShade(Loading_loading);
                    presenter.getMovieDetail(getIntent().getStringExtra("id"));
                }
            });

            AppBarLayout appbar_syy_movie_detail = findViewById(R.id.appbar_syy_movie_detail);
            appbar_syy_movie_detail.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (verticalOffset >= 0) {
                        srl_syy_movie_detail.setEnabled(true);
                    } else {
                        srl_syy_movie_detail.setEnabled(false);
                    }
                }
            });
            view_loading.show();
            loadingShade(Loading_loading);
            presenter.getMovieDetail(getIntent().getStringExtra("id"));

        }
    }

    private void loadingShade(int type) {
        switch (type) {
            case Loading_success:
                srl_syy_movie_detail.setRefreshing(false);
                fl_loading.setVisibility(View.GONE);
                break;
            case Loading_fail:
                srl_syy_movie_detail.setRefreshing(false);
                fl_loading.setVisibility(View.VISIBLE);
                view_loading.setVisibility(View.GONE);
                break;
            case Loading_loading:
                fl_loading.setVisibility(View.VISIBLE);
                view_loading.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    protected MovieDetailPresenter createPresenter() {
        return new MovieDetailPresenter(this, new BaseModel());
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
        loadingShade(Loading_success);
        MovieDetailInfo info = ((MovieDetailInfo) data);
        StringBuilder str = new StringBuilder(info.year);
        for (String genre : info.genres) {
            str.append("/").append(genre);
        }
        tv_syy_movie_detail_info1.setText(str);
        tv_syy_movie_detail_info2.setText(String.format(getString(R.string.syy_movie_detail_oldname), info.original_title));
        if (!info.aka.isEmpty()) {
            str = new StringBuilder();
            for (String aka : info.aka) {
                str.append(aka).append("/");
            }
            tv_syy_movie_detail_info3.setText(String.format(getString(R.string.syy_movie_detail_nickname), str.substring(0, str.length() - 1)));
        } else {
            tv_syy_movie_detail_info3.setVisibility(View.GONE);
        }
        if (!info.countries.isEmpty()) {
            str = new StringBuilder();
            for (String country : info.countries) {
                str.append(country).append("/");
            }
            tv_syy_movie_detail_info4.setText(String.format(getString(R.string.syy_movie_detail_area), str.substring(0, str.length() - 1)));
        } else {
            tv_syy_movie_detail_info4.setVisibility(View.GONE);
        }

        Subjects.RatingBean ratingBean = info.rating;
        float rate = ratingBean.average / ratingBean.max * 5;

        rating_syy_movie_detail_score.setRating(rate);
        tv_syy_movie_detail_score.setText(ratingBean.average + "");
        rating_syy_movie_detail_score.setVisibility((int) rate == 0 ? View.GONE : View.VISIBLE);
        tv_syy_movie_detail_noscore.setVisibility((int) rate == 0 ? View.VISIBLE : View.GONE);
        tv_syy_movie_detail_scorenum.setText(String.format(getString(R.string.common_people), info.ratings_count));

        stv_syy_movie_detail_intro.limitTextViewString(info.summary,140);
    }

    @Override
    public void onFailure(ExceptionHandle.ResponseThrowable responseThrowable) {
        super.onFailure(responseThrowable);
        loadingShade(Loading_fail);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (view_loading != null) {
            view_loading.hide();
        }
    }
}

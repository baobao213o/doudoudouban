package com.xxx.syy.ui.movie.detail;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.xxx.library.base.BaseActivity;
import com.xxx.library.base.WebViewActivity;
import com.xxx.library.fresco.FrescoBitmapCallback;
import com.xxx.library.fresco.FrescoLoadUtil;
import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.mvp.view.IView;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.utils.ViewUtils;
import com.xxx.library.views.FocusableFloatingActionButton;
import com.xxx.library.views.LoadingLayoutHelper;
import com.xxx.library.views.SpannableTextView;
import com.xxx.syy.R;
import com.xxx.syy.entity.Character;
import com.xxx.syy.entity.MovieDetailInfo;
import com.xxx.syy.entity.Subjects;

import java.util.ArrayList;

import static com.xxx.library.views.LoadingLayoutHelper.Loading_fail;
import static com.xxx.library.views.LoadingLayoutHelper.Loading_loading;
import static com.xxx.library.views.LoadingLayoutHelper.Loading_success;

public class MovieDetailActivity extends BaseActivity<MovieDetailPresenter> implements IView {

    private FocusableFloatingActionButton fab_syy_movie_detail_share;
    private ImageView iv_syy_movie_detail_avator;
    private TextView tv_syy_movie_detail_info1, tv_syy_movie_detail_info2, tv_syy_movie_detail_info3, tv_syy_movie_detail_info4;
    private TextView tv_syy_movie_detail_score, tv_syy_movie_detail_scorenum, tv_syy_movie_detail_noscore, tv_syy_movie_detail_link;
    private RatingBar rating_syy_movie_detail_score;
    private SwipeRefreshLayout srl_syy_movie_detail;
    private SpannableTextView stv_syy_movie_detail_intro;
    private ConstraintLayout content_syy_movie_detail;
    private RecyclerView rv_syy_movie_detail_director, rv_syy_movie_detail_casts;


    private MovieCharatersAdapter directorAdapter, castsAdapter;
    private ArrayList<Character> directors = new ArrayList<>();
    private ArrayList<Character> casts = new ArrayList<>();
    private MovieDetailInfo info;

    private int toolbarColor;

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
            final Drawable drawable = getDrawable(R.drawable.common_avd_share);
            fab_syy_movie_detail_share = findViewById(R.id.fab_syy_movie_detail_share);
            fab_syy_movie_detail_share.setImageDrawable(drawable);
            fab_syy_movie_detail_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (drawable == null) return;
                    ((AnimatedVectorDrawable) drawable).start();
                }
            });

            initToolbar();
            initRecyclerView();

            tv_syy_movie_detail_info1 = findViewById(R.id.tv_syy_movie_detail_info1);
            tv_syy_movie_detail_info2 = findViewById(R.id.tv_syy_movie_detail_info2);
            tv_syy_movie_detail_info3 = findViewById(R.id.tv_syy_movie_detail_info3);
            tv_syy_movie_detail_info4 = findViewById(R.id.tv_syy_movie_detail_info4);

            tv_syy_movie_detail_score = findViewById(R.id.tv_syy_movie_detail_score);
            tv_syy_movie_detail_scorenum = findViewById(R.id.tv_syy_movie_detail_scorenum);
            rating_syy_movie_detail_score = findViewById(R.id.rating_syy_movie_detail_score);
            tv_syy_movie_detail_noscore = findViewById(R.id.tv_syy_movie_detail_noscore);

            stv_syy_movie_detail_intro = findViewById(R.id.stv_syy_movie_detail_intro);

            content_syy_movie_detail = findViewById(R.id.content_syy_movie_detail);
            tv_syy_movie_detail_link = findViewById(R.id.tv_syy_movie_detail_link);
            LoadingData();
        }
    }


    private void initToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.ctoolbar_syy_movie_detail_title);
        String title = getIntent().getStringExtra("title");
        collapsingToolbarLayout.setTitle(title);

        iv_syy_movie_detail_avator = findViewById(R.id.iv_syy_movie_detail_avator);
        iv_syy_movie_detail_avator.setBackgroundColor(getResources().getColor(R.color.red_3));

        String url = getIntent().getStringExtra("url");

        FrescoLoadUtil.getInstance().loadImageBitmap(url, new FrescoBitmapCallback<Bitmap>() {
            @Override
            public void onSuccess(Uri uri, Bitmap result) {
                Palette.from(result).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(@NonNull Palette palette) {
                        Palette.Swatch vibrant = palette.getMutedSwatch();//有活力的
                        if (vibrant != null) {
                            toolbarColor = vibrant.getRgb();
                            collapsingToolbarLayout.setContentScrimColor(toolbarColor);
                            iv_syy_movie_detail_avator.setBackgroundColor(vibrant.getBodyTextColor());
                            if (castsAdapter != null) {
                                castsAdapter.setColor(toolbarColor);
                            }
                            if (directorAdapter != null) {
                                directorAdapter.setColor(toolbarColor);
                            }
                        }
                    }
                });
                iv_syy_movie_detail_avator.setImageBitmap(result);
            }

            @Override
            public void onFailure(Uri uri, Throwable throwable) {

            }

            @Override
            public void onCancel(Uri uri) {

            }
        });
    }

    private void initRecyclerView() {
        rv_syy_movie_detail_director = findViewById(R.id.rv_syy_movie_detail_director);
        rv_syy_movie_detail_director.setAdapter(directorAdapter = new MovieCharatersAdapter(this, directors));

        rv_syy_movie_detail_casts = findViewById(R.id.rv_syy_movie_detail_casts);
        rv_syy_movie_detail_casts.setAdapter(castsAdapter = new MovieCharatersAdapter(this, casts));

        NestedScrollView srcoll_syy_movie_detail = findViewById(R.id.srcoll_syy_movie_detail);

        final ObjectAnimator disappear = ObjectAnimator.ofPropertyValuesHolder(
                fab_syy_movie_detail_share,
                PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 0f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0f));
        disappear.setInterpolator(new LinearInterpolator());
        disappear.setDuration(300);


        srcoll_syy_movie_detail.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (LoadingLayoutHelper.isViewAdded(content_syy_movie_detail)) {
                    return;
                }
                if (fab_syy_movie_detail_share.getScaleX() == 0) {
                    if (!ViewUtils.viewsIntersect(rv_syy_movie_detail_director, fab_syy_movie_detail_share)
                            && !ViewUtils.viewsIntersect(rv_syy_movie_detail_casts, fab_syy_movie_detail_share)) {
                        if (!disappear.isRunning()) {
                            disappear.reverse();
                            fab_syy_movie_detail_share.isFocus = true;
                            return;
                        }
                    }
                }
                if (fab_syy_movie_detail_share.getScaleX() != 0) {
                    if (ViewUtils.viewsIntersect(rv_syy_movie_detail_director, fab_syy_movie_detail_share)
                            || ViewUtils.viewsIntersect(rv_syy_movie_detail_casts, fab_syy_movie_detail_share)) {
                        if (!disappear.isRunning()) {
                            disappear.start();
                            fab_syy_movie_detail_share.isFocus = false;
                        }
                    }

                }

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
                if (LoadingLayoutHelper.isViewAdded(content_syy_movie_detail)) {
                    return;
                }
                if (fab_syy_movie_detail_share.getScaleX() == 0) {
                    if (!ViewUtils.viewsIntersect(rv_syy_movie_detail_director, fab_syy_movie_detail_share)
                            && !ViewUtils.viewsIntersect(rv_syy_movie_detail_casts, fab_syy_movie_detail_share)) {
                        if (!disappear.isRunning()) {
                            disappear.reverse();
                            fab_syy_movie_detail_share.isFocus = true;
                            return;
                        }
                    }
                }
                if (fab_syy_movie_detail_share.getScaleX() != 0) {
                    if (ViewUtils.viewsIntersect(rv_syy_movie_detail_director, fab_syy_movie_detail_share)
                            || ViewUtils.viewsIntersect(rv_syy_movie_detail_casts, fab_syy_movie_detail_share)) {
                        if (!disappear.isRunning()) {
                            disappear.start();
                            fab_syy_movie_detail_share.isFocus = false;
                        }
                    }

                }
            }
        });

    }

    private void LoadingData() {
        srl_syy_movie_detail = findViewById(R.id.srl_syy_movie_detail);
        srl_syy_movie_detail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingShade(Loading_loading);
                presenter.getMovieDetail(getIntent().getStringExtra("id"));
            }
        });
        loadingShade(Loading_loading);
        presenter.getMovieDetail(getIntent().getStringExtra("id"));
    }

    private void loadingShade(int type) {
        switch (type) {
            case Loading_success:
                srl_syy_movie_detail.setRefreshing(false);
                LoadingLayoutHelper.removeLoadingView(content_syy_movie_detail);
                break;
            case Loading_fail:
                srl_syy_movie_detail.setRefreshing(false);
                LoadingLayoutHelper.addFailureView(content_syy_movie_detail, null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.getMovieDetail(getIntent().getStringExtra("id"));
                    }
                });
                break;
            case Loading_loading:
                LoadingLayoutHelper.addLoadingView(content_syy_movie_detail);
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
        info = ((MovieDetailInfo) data);
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
        tv_syy_movie_detail_score.setText(String.valueOf(ratingBean.average));
        rating_syy_movie_detail_score.setVisibility((int) rate == 0 ? View.GONE : View.VISIBLE);
        tv_syy_movie_detail_noscore.setVisibility((int) rate == 0 ? View.VISIBLE : View.GONE);
        tv_syy_movie_detail_scorenum.setText(String.format(getString(R.string.common_people), info.ratings_count));

        stv_syy_movie_detail_intro.limitTextViewString(info.summary, 140);
        directorAdapter.setList((ArrayList<Character>) info.directors);
        castsAdapter.setList((ArrayList<Character>) info.casts);
        tv_syy_movie_detail_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MovieDetailActivity.this, WebViewActivity.class);
                it.putExtra(WebViewActivity.TAG_TITLE, getString(R.string.syy_movie_link));
                it.putExtra(WebViewActivity.TAG_URL, info.mobile_url);
                it.putExtra(WebViewActivity.TAG_COLOR, toolbarColor);
                startActivity(it);
            }
        });
    }

    @Override
    public void onFailure(ExceptionHandle.ResponseThrowable responseThrowable, int requestCode) {
        super.onFailure(responseThrowable, requestCode);
        loadingShade(Loading_fail);
    }

}

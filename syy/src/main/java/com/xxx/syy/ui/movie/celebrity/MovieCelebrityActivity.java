package com.xxx.syy.ui.movie.celebrity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xxx.library.base.BaseActivity;
import com.xxx.library.base.WebViewActivity;
import com.xxx.library.mvp.view.IView;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.utils.DeviceUtil;
import com.xxx.library.views.ElasticDragDismissFrameLayout;
import com.xxx.library.views.LoadingLayoutHelper;
import com.xxx.syy.R;
import com.xxx.syy.entity.CelebrityInfo;
import com.xxx.syy.entity.Subjects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MovieCelebrityActivity extends BaseActivity<MovieCelebrityPresenter> implements IView {

    private ElasticDragDismissFrameLayout draggableFrame;
    private ElasticDragDismissFrameLayout.SystemChromeFader chromeFader;
    private TextView tv_syy_movie_celebrity_name_en, tv_syy_movie_celebrity_addr, tv_syy_movie_celebrity_roles;
    private SimpleDraweeView simpleDraweeView;
    private ViewGroup ll_syy_movie_celebrity_anim_container;
    private MovieCelebrityAdapter adapter;

    private int toolbarColor;
    private ArrayList<Subjects> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSharedElementEnterTransition(DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER, ScalingUtils.ScaleType.CENTER));
        setContentView(R.layout.syy_activity_movie_celebrity);
        if (savedInstanceState == null) {
            draggableFrame = findViewById(R.id.eddf_syy_movie_celebrity_container);
            chromeFader = new ElasticDragDismissFrameLayout.SystemChromeFader(this) {
                @Override
                public void onDragDismissed() {
                    finishAfterTransition();
                }
            };
            toolbarColor = getIntent().getIntExtra("color", getResources().getColor(R.color.colorPrimary));
            getWindow().setStatusBarColor(toolbarColor);
            Toolbar toolbar = findViewById(R.id.toolbar_syy_movie_celebrity);
            toolbar.setBackgroundColor(toolbarColor);
            setSupportActionBar(toolbar);

            String url = getIntent().getStringExtra("url");
            simpleDraweeView = findViewById(R.id.iv_syy_movie_celebrity_avator);
            simpleDraweeView.setImageURI(url);

            String name = getIntent().getStringExtra("name");
            TextView tv_syy_movie_celebrity_name = findViewById(R.id.tv_syy_movie_celebrity_name);
            tv_syy_movie_celebrity_name.setText(name);

            tv_syy_movie_celebrity_name_en = findViewById(R.id.tv_syy_movie_celebrity_name_en);
            tv_syy_movie_celebrity_addr = findViewById(R.id.tv_syy_movie_celebrity_addr);
            tv_syy_movie_celebrity_roles = findViewById(R.id.tv_syy_movie_celebrity_roles);

            ll_syy_movie_celebrity_anim_container = findViewById(R.id.ll_syy_movie_celebrity_anim_container);
            RecyclerView rv_syy_movie_celebrity = findViewById(R.id.rv_syy_movie_celebrity);
            rv_syy_movie_celebrity.setAdapter(adapter = new MovieCelebrityAdapter(this, list));
            rv_syy_movie_celebrity.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            presenter.getCelebrityInfo(getIntent().getStringExtra("id"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (draggableFrame != null) {
            draggableFrame.addListener(chromeFader);
        }
    }

    @Override
    protected void onPause() {
        if (draggableFrame != null) {
            draggableFrame.removeListener(chromeFader);
        }
        super.onPause();
    }

    @Override
    protected MovieCelebrityPresenter createPresenter() {
        return new MovieCelebrityPresenter(this);
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
        ll_syy_movie_celebrity_anim_container.setVisibility(View.VISIBLE);
        LoadingLayoutHelper.removeLoadingView(ll_syy_movie_celebrity_anim_container);
        int h = DeviceUtil.getScreenHeight();
        ObjectAnimator animator = ObjectAnimator.ofFloat(ll_syy_movie_celebrity_anim_container, "translationY", h, 0);
        animator.setDuration(500);
        animator.start();

        final CelebrityInfo info = (CelebrityInfo) data;
        tv_syy_movie_celebrity_name_en.setText(info.name_en);
        if (!TextUtils.isEmpty(info.mobile_url)) {
            simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(MovieCelebrityActivity.this, WebViewActivity.class);
                    it.putExtra(WebViewActivity.TAG_TITLE, getString(R.string.syy_movie_character));
                    it.putExtra(WebViewActivity.TAG_URL, info.mobile_url);
                    it.putExtra(WebViewActivity.TAG_COLOR, toolbarColor);
                    startActivity(it);
                }
            });
        }
        tv_syy_movie_celebrity_addr.setText(info.born_place);
        Set<String> roles = new HashSet<>();
        for (CelebrityInfo.WorksBean worksBean : info.works) {
            roles.addAll(worksBean.roles);
        }
        if (!roles.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String role : roles) {
                sb.append(role).append("/");
            }
            tv_syy_movie_celebrity_roles.setText(sb.substring(0, sb.length() - 1));
        }
        if (!info.works.isEmpty()) {
            list = new ArrayList<>();
            for (CelebrityInfo.WorksBean worksBean : info.works) {
                list.add(worksBean.subject);
            }
            adapter.setList(list);
        }

    }

    @Override
    public void onFailure(ExceptionHandle.ResponseThrowable responseThrowable, int requestCode) {
        super.onFailure(responseThrowable, requestCode);
        LoadingLayoutHelper.addFailureView(ll_syy_movie_celebrity_anim_container, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getCelebrityInfo(getIntent().getStringExtra("id"));
            }
        });
    }
}

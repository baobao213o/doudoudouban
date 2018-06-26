package com.xxx.syy.ui.movie;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.SharedElementCallback;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListenerAdapter;
import com.xxx.library.base.BaseFragment;
import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.syy.R;
import com.xxx.syy.entity.Subjects;
import com.xxx.syy.entity.Top250MovieInfo;
import com.xxx.syy.entity.USBoxMovieInfo;

import java.util.ArrayList;
import java.util.List;

import lumenghz.com.pullrefresh.PullToRefreshView;

/**
 * Created by gaoruochen on 18-4-12.
 */

public class MovieFragment extends BaseFragment<MoviePresenter> implements MovieContract.View {

    private PullToRefreshView srl_syy_movie;
    private RecyclerView rv_syy_movie;
    private MovieAdapter adapter;
    private BoomMenuButton menuButton;
    private ArrayList<Subjects> list = new ArrayList<>();
    private int type;
    private static final int TOP250 = 0;
    private static final int USBOX = 1;
    private boolean isBoomCLick = false;
    private int start = 0;
    private final static int pageSize = 10;
    private boolean isRefresh = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.syy_fragment_movie, container, false);
        initMenu(view);
        initRecyclerview(view);
        return view;
    }

    private void initMenu(View view) {

        menuButton = view.findViewById(R.id.menu_syy_movie);
        final String[] array = getResources().getStringArray(R.array.syy_movie_type);
        int[] colorRes = {R.drawable.common_image_bat, R.drawable.common_image_bear, R.drawable.common_image_bee,
                R.drawable.common_image_butterfly, R.drawable.common_image_cat, R.drawable.common_image_deer,
                R.drawable.common_image_dolphin, R.drawable.common_image_eagle, R.drawable.common_image_elephant};
        try {
            for (int i = 0; i < menuButton.getPiecePlaceEnum().pieceNumber(); i++) {
                menuButton.addBuilder(new HamButton.Builder()
                        .normalImageRes(colorRes[(int) (Math.random() * colorRes.length)])
                        .normalText(array[i])
                        .subNormalText(array[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        menuButton.setOnBoomListener(new OnBoomListenerAdapter() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                try {
                    switch (array[index]) {
                        case "Top250":
                            if (type == TOP250) {
                                throw new Exception();
                            }
                            start = 0;
                            type = TOP250;
                            break;
                        case "北美票房榜":
                            if (type == USBOX) {
                                throw new Exception();
                            }
                            type = USBOX;
                            break;
                        default:
                            return;
                    }
                    isRefresh = true;
                    isBoomCLick = true;
                    srl_syy_movie.setRefreshing(true);
                    requestData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        final ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(
                menuButton,
                PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f, 1f));
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(300);
        animator.start();

        if (getActivity() != null) {
            getActivity().setExitSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onSharedElementStart(List<String> sharedElementNames, List<View>
                        sharedElements, List<View> sharedElementSnapshots) {
                    animator.start();
                }
            });
        }

    }

    private void initRecyclerview(View view) {
        rv_syy_movie = view.findViewById(R.id.rv_syy_movie);
        srl_syy_movie = view.findViewById(R.id.srl_syy_movie);
        rv_syy_movie.setAdapter(adapter = new MovieAdapter(getActivity(), list, menuButton));

        srl_syy_movie.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                start = 0;
                requestData();
            }
        });

        srl_syy_movie.post(new Runnable() {
            @Override
            public void run() {
                srl_syy_movie.setRefreshing(true);
                requestData();
            }
        });
        rv_syy_movie.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition == totalItemCount - 1
                        && visibleItemCount > 0) {
                    isRefresh = false;
                    start += pageSize;
                    requestData();
                }
            }
        });

    }

    private void requestData() {
        switch (type) {
            case TOP250:
                presenter.getTop250(TOP250, start, pageSize);
                break;
            case USBOX:
                presenter.getUSbox(USBOX);
                break;
            default:
                break;
        }
    }

    @Override
    protected MoviePresenter createPresenter() {
        return new MoviePresenter(this, new BaseModel());
    }


    @Override
    public void onFailure(ExceptionHandle.ResponseThrowable responseThrowable, int requestCode) {
        super.onFailure(responseThrowable, requestCode);
        srl_syy_movie.setRefreshing(false);
        switch (requestCode) {
            case TOP250:
                type = USBOX;
                break;
            case USBOX:
                type = TOP250;
                break;
            default:
                break;
        }
    }


    @Override
    public void showTop250Movies(Top250MovieInfo top250MovieInfo) {
        srl_syy_movie.setRefreshing(false);
        ArrayList<Subjects> datas = adapter.getList();
        if (isRefresh) {
            datas.clear();
        }
        datas.addAll(top250MovieInfo.subjects);
        adapter.setList(datas);
        if (isBoomCLick) {
            rv_syy_movie.scrollToPosition(0);
            isBoomCLick = false;
        }
    }

    @Override
    public void showUSboxMovies(USBoxMovieInfo movieInfo) {
        srl_syy_movie.setRefreshing(false);
        ArrayList<Subjects> subjects = new ArrayList<>();
        for (USBoxMovieInfo.SubjectsBean bean : movieInfo.subjects) {
            subjects.add(bean.subject);
        }
        adapter.setList(subjects);
        if (isBoomCLick) {
            rv_syy_movie.scrollToPosition(0);
            isBoomCLick = false;
        }
    }
}

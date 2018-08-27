package com.xxx.syy.ui.container;

import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.xxx.library.base.BaseFragment;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.utils.DeviceUtil;
import com.xxx.syy.R;
import com.xxx.syy.ui.book.BookFragment;
import com.xxx.syy.ui.movie.MovieFragment;
import com.xxx.syy.ui.music.MusicFragment;
import com.xxx.syy.ui.search.SearchActivity;

import java.util.ArrayList;

/**
 * Created by gaoruochen on 18-4-12.
 */

public class SyyFragment extends BaseFragment {

    private ArrayList<Fragment> list = new ArrayList<>();
    private boolean isGone = false;

    private SyyAdapter adapter;

    private ObjectAnimator animator;

    private final static String FRAG_TAGS = "FRAG_TAGS";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.syy_fragment, container, false);
        initView(view, savedInstanceState);
        return view;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    private void initView(View view, Bundle savedInstanceState) {
        final ViewPager viewPager = view.findViewById(R.id.vp_syy);
        final BottomNavigationView bottomNavigationView = view.findViewById(R.id.bnv_syy);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.syy_item_movie) {
                    viewPager.setCurrentItem(0, false);
                    return true;//返回true,否则tab按钮不变色,未被选中
                } else if (itemId == R.id.syy_item_book) {
                    viewPager.setCurrentItem(1, false);
                    return true;
                } else if (itemId == R.id.syy_item_music) {
                    viewPager.setCurrentItem(2, false);
                    return true;
                }
                return false;

            }
        });

        animator = ObjectAnimator.ofFloat(bottomNavigationView, "translationY", 0, DeviceUtil.dip2px(54));
        animator.setDuration(300);

        if (savedInstanceState != null) {
            ArrayList<String> tags = savedInstanceState.getStringArrayList(FRAG_TAGS);
            if (tags != null && !tags.isEmpty()) {
                for (String tag : tags) {
                    Fragment fragment = getChildFragmentManager().findFragmentByTag(tag);
                    list.add(fragment);
                    setMovieFragmentAnim(fragment);
                }
            }

        } else {
            MovieFragment movieFragment = new MovieFragment();
            BookFragment bookFragment = new BookFragment();
            MusicFragment musicFragment = new MusicFragment();
            setMovieFragmentAnim(movieFragment);
            list.add(movieFragment);
            list.add(bookFragment);
            list.add(musicFragment);
        }


        adapter = new SyyAdapter(getChildFragmentManager(), list);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(list.size());
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (isGone && !animator.isRunning()) {
                    animator.reverse();
                    isGone = false;
                }
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.syy_item_movie);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.syy_item_book);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.syy_item_music);
                        break;
                }
            }
        });

    }


    private void setMovieFragmentAnim(Fragment fragment) {
        if (fragment instanceof MovieFragment) {
            ((MovieFragment) fragment).setListener(new MovieFragment.OnScrollOrientation() {
                @Override
                public void up() {
                    if (isGone && !animator.isRunning()) {
                        animator.reverse();
                        isGone = false;
                    }
                }

                @Override
                public void down() {
                    if (!isGone && !animator.isRunning()) {
                        animator.start();
                        isGone = true;
                    }
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putStringArrayList(FRAG_TAGS, adapter.mTags);
        super.onSaveInstanceState(outState);
    }

    public void onSearchClick(View v) {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                Pair.create(v, getString(R.string.syy_transition_search_img)));
        startActivity(intent, options.toBundle());
    }
}

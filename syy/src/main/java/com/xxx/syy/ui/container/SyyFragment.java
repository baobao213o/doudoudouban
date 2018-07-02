package com.xxx.syy.ui.container;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xxx.library.base.BaseFragment;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.syy.R;
import com.xxx.syy.ui.book.BookFragment;
import com.xxx.syy.ui.movie.MovieFragment;
import com.xxx.syy.ui.music.MusicFragment;
import com.xxx.syy.ui.search.SearchActivity;

import java.util.LinkedHashMap;

/**
 * Created by gaoruochen on 18-4-12.
 */

public class SyyFragment extends BaseFragment {

    private ViewPager viewPager;

    private LinkedHashMap<Fragment, String> map = new LinkedHashMap<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.syy_fragment, container, false);
        initView(view);
        return view;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    private void initView(View view) {
        TabLayout tab = view.findViewById(R.id.tab_syy);
        viewPager = view.findViewById(R.id.vp_syy);
        tab.setupWithViewPager(viewPager);
        MovieFragment movieFragment = new MovieFragment();
        map.put(movieFragment, getString(R.string.syy_movie));
        BookFragment bookFragment = new BookFragment();
        map.put(bookFragment, getString(R.string.syy_book));
        MusicFragment musicFragment = new MusicFragment();
        map.put(musicFragment, getString(R.string.syy_music));
        FragmentPagerAdapter adapter = new SyyAdapter(getFragmentManager(), map);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(map.size());
    }

    public void onSearchClick(View v) {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                Pair.create(v, getString(R.string.syy_transition_search_img)));
        startActivity(intent, options.toBundle());
    }
}

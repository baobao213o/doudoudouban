package com.xxx.syy.ui.container;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xxx.library.base.BaseFragment;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.syy.R;
import com.xxx.syy.ui.book.BookFragment;
import com.xxx.syy.ui.movie.MovieFragment;
import com.xxx.syy.ui.music.MusicFragment;

import java.util.LinkedHashMap;

/**
 * Created by gaoruochen on 18-4-12.
 */

public class SyyFragment extends BaseFragment {

    private LinkedHashMap<Fragment, String> map = new LinkedHashMap<>();

    private MusicFragment musicFragment;
    private BookFragment bookFragment;
    private MovieFragment movieFragment;

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
        ViewPager viewPager = view.findViewById(R.id.vp_syy);
        tab.setupWithViewPager(viewPager);
        movieFragment = new MovieFragment();
        map.put(movieFragment, getString(R.string.syy_container_movie));
        bookFragment = new BookFragment();
        map.put(bookFragment, getString(R.string.syy_container_book));
        musicFragment = new MusicFragment();
        map.put(musicFragment, getString(R.string.syy_container_music));
        FragmentPagerAdapter adapter = new SyyAdapter(getFragmentManager(), map);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(map.size());
    }
}

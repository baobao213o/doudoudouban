package com.xxx.syy.ui.music;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xxx.library.base.BaseFragment;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.syy.R;

/**
 * Created by gaoruochen on 18-4-12.
 */

public class MusicFragment extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.syy_fragment_music, container, false);
        return view;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}

package com.xxx.broadcast.ui.container;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xxx.broadcast.R;
import com.xxx.library.base.BaseFragment;
import com.xxx.library.mvp.presenter.BasePresenter;

/**
 * Created by gaoruochen on 18-4-12.
 */

public class BroadcastFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.broadcast_fragment, container, false);
        return view;
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

}

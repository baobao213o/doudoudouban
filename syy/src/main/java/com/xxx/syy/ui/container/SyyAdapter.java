package com.xxx.syy.ui.container;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by gaoruochen on 18-4-13.
 */

public class SyyAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> list;
    public ArrayList<String> mTags;

    SyyAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        String tag = fragment.getTag();
        if (mTags == null) {
            mTags = new ArrayList<>();
        }
        if (!mTags.contains(tag)) {
            mTags.add(tag);
        }
        return fragment;
    }

}

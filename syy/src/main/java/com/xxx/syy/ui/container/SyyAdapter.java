package com.xxx.syy.ui.container;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by gaoruochen on 18-4-13.
 */

public class SyyAdapter extends FragmentPagerAdapter {

    private LinkedHashMap<Fragment, String> map;
    private ArrayList<Fragment> list = new ArrayList<>();


    SyyAdapter(FragmentManager fm, LinkedHashMap<Fragment, String> map) {
        super(fm);
        this.map = map;
        list.addAll(map.keySet());
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return map.get(list.get(position));
    }
}

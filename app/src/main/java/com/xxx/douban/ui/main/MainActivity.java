package com.xxx.douban.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xxx.douban.R;
import com.xxx.douban.entity.BookInfo;
import com.xxx.library.entity.User;
import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.mvp.view.IView;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.user.BaseUserActivity;
import com.xxx.library.user.IUserFragment;
import com.xxx.library.utils.BottomNavigationViewHelper;
import com.xxx.library.utils.ColorUtil;
import com.xxx.library.utils.FragmentUtil;
import com.xxx.my.ui.user.UserFragment;

@Route(path = "/main/main/MainActivity")
public class MainActivity extends BaseUserActivity<BookInfo, TestPresenter> implements IView<BookInfo>,UserFragment.IDrawerClose {


    private IUserFragment leftFragment;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private BottomNavigationView bottom_navigation;


    @Override
    public void onBaseUserActivityCreate(Bundle savedInstanceState) {

//        if (!AccountHelper.getInstance().checkActiveAccount(this)) {
//            return;
//        }
        setContentView(R.layout.main_activity_main);
        drawer = findViewById(R.id.drawer_main);
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        bottom_navigation = findViewById(R.id.navigation_main);
        BottomNavigationViewHelper.disableShiftMode(bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottom_navigation.setSelectedItemId(R.id.main_bottom_navi_diary);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final int startColor = getResources().getColor(R.color.colorPrimary);
            final int endColor = getResources().getColor(R.color.red_0);
            drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    getWindow().setStatusBarColor(ColorUtil.getCurrentColor(slideOffset, startColor, endColor));
                }
            });
        }
        leftFragment = FragmentUtil.findById(this, R.id.frgment_main_navigation);
        ((UserFragment)leftFragment).setListener(this);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.main_bottom_navi_diary:
                    toolbar.setTitle(getString(R.string.main_bottom_navi_diary));
                    return true;
                case R.id.main_bottom_navi_syy:
                    toolbar.setTitle(getString(R.string.main_bottom_navi_syy));
                    return true;
                case R.id.main_bottom_navi_broadcast:
                    toolbar.setTitle(getString(R.string.main_bottom_navi_broadcast));
                    return true;
                case R.id.main_bottom_navi_group:
                    toolbar.setTitle(getString(R.string.main_bottom_navi_group));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected TestPresenter createPresenter() {
        return new TestPresenter(this, new BaseModel());
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(isTaskRoot());
        }
    }


    @Override
    public void onUserUpdateSuccess(final User user) {
        super.onUserUpdateSuccess(user);
        leftFragment.onUserUpdateSuccess(user);
    }

    @Override
    public void onUserUpdateFailure(ExceptionHandle.ResponseThrowable responseThrowable) {
        super.onUserUpdateFailure(responseThrowable);
        leftFragment.onUserUpdateFailure(responseThrowable);
    }

    @Override
    public void clearUserStatus() {
        leftFragment.clearUserStatus();
    }

    @Override
    public void closeDrawer() {
        drawer.closeDrawer(Gravity.START);
    }
}

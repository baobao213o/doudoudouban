package com.xxx.douban.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xxx.broadcast.ui.container.BroadcastFragment;
import com.xxx.diary.ui.container.DiaryFragment;
import com.xxx.douban.R;
import com.xxx.douban.entity.BookInfo;
import com.xxx.group.ui.container.GroupFragment;
import com.xxx.library.entity.User;
import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.mvp.view.IView;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.rxjava.RxBusManager;
import com.xxx.library.user.BaseUserActivity;
import com.xxx.library.user.IUserFragment;
import com.xxx.library.utils.BottomNavigationViewHelper;
import com.xxx.library.utils.ColorUtil;
import com.xxx.library.views.ToastHelper;
import com.xxx.my.ui.user.UserFragment;
import com.xxx.syy.ui.container.SyyFragment;

@Route(path = "/main/main/MainActivity")
public class MainActivity extends BaseUserActivity<BookInfo, TestPresenter> implements IView<BookInfo>, UserFragment.IDrawerClose {


    private IUserFragment leftFragment;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private BottomNavigationView bottom_navigation;

    private int naviSelectType;

    private DiaryFragment diaryFragment;
    private SyyFragment syyFragment;
    private BroadcastFragment broadcastFragment;
    private GroupFragment groupFragment;

    private static final String FRAG_TAG_DIARY = "diary";
    private static final String FRAG_TAG_SYY = "syy";
    private static final String FRAG_TAG_BROADCAST = "broadcast";
    private static final String FRAG_TAG_GROUP = "group";

    private int index = R.id.main_menu_bottom_navi_diary;

    @Override
    public void onBaseUserActivityCreate(Bundle savedInstanceState) {

//        if (!AccountHelper.getInstance().checkActiveAccount(this)) {
//            return;
//        }
        setContentView(R.layout.main_activity_main);
        drawer = findViewById(R.id.drawer_main);
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.main_toolbar_seach) {
                    ToastHelper.showToast("1111");
                }
                return true;
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        final int startColor = getResources().getColor(R.color.colorPrimary);
        final int endColor = getResources().getColor(R.color.red_0);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(ColorUtil.getCurrentColor(slideOffset, startColor, endColor));
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //FIXME
                if (naviSelectType != -1) {
                    RxBusManager.getInstance().post(naviSelectType);
                }
                naviSelectType = -1;
            }
        });

        bottom_navigation = findViewById(R.id.navigation_main);
        BottomNavigationViewHelper.disableShiftMode(bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottom_navigation.setSelectedItemId(R.id.main_menu_bottom_navi_diary);

        leftFragment = (IUserFragment) getSupportFragmentManager().findFragmentById(R.id.frgment_main_navigation);
        ((UserFragment) leftFragment).setListener(this);
        if (savedInstanceState != null) {
            diaryFragment = (DiaryFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_DIARY);
            syyFragment = (SyyFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_SYY);
            broadcastFragment = (BroadcastFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_BROADCAST);
            groupFragment = (GroupFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_GROUP);
        }
        showFragment();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            invalidateOptionsMenu();
            index = item.getItemId();
            showFragment();
            switch (item.getItemId()) {
                case R.id.main_menu_bottom_navi_diary:
                    toolbar.setTitle(getString(R.string.main_bottom_navi_diary));
                    break;
                case R.id.main_menu_bottom_navi_syy:
                    toolbar.setTitle(getString(R.string.main_bottom_navi_syy));
                    break;
                case R.id.main_menu_bottom_navi_broadcast:
                    toolbar.setTitle(getString(R.string.main_bottom_navi_broadcast));
                    break;
                case R.id.main_menu_bottom_navi_group:
                    toolbar.setTitle(getString(R.string.main_bottom_navi_group));
                    break;
            }
            return true;
        }
    };

    private void showFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(fragmentTransaction);
        switch (index) {
            case R.id.main_menu_bottom_navi_diary:
                if (diaryFragment == null) {
                    diaryFragment = new DiaryFragment();
                    fragmentTransaction.add(R.id.container_main,diaryFragment, FRAG_TAG_DIARY);
                } else {
                    fragmentTransaction.show(diaryFragment);
                }
                break;
            case R.id.main_menu_bottom_navi_syy:
                if (syyFragment == null) {
                    syyFragment = new SyyFragment();
                    fragmentTransaction.add(R.id.container_main,syyFragment, FRAG_TAG_SYY);
                } else {
                    fragmentTransaction.show(syyFragment);
                }
                break;
            case R.id.main_menu_bottom_navi_broadcast:
                if (broadcastFragment == null) {
                    broadcastFragment = new BroadcastFragment();
                    fragmentTransaction.add(R.id.container_main,broadcastFragment, FRAG_TAG_BROADCAST);
                } else {
                    fragmentTransaction.show(broadcastFragment);
                }
                break;
            case R.id.main_menu_bottom_navi_group:
                if (groupFragment == null) {
                    groupFragment = new GroupFragment();
                    fragmentTransaction.add(R.id.container_main,groupFragment, FRAG_TAG_GROUP);
                } else {
                    fragmentTransaction.show(groupFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }


    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (index != R.id.main_menu_bottom_navi_diary && diaryFragment != null) {
            fragmentTransaction.hide(diaryFragment);
        }
        if (index != R.id.main_menu_bottom_navi_syy && syyFragment != null) {
            fragmentTransaction.hide(syyFragment);
        }
        if (index != R.id.main_menu_bottom_navi_broadcast && broadcastFragment != null) {
            fragmentTransaction.hide(broadcastFragment);
        }
        if (index != R.id.main_menu_bottom_navi_group && groupFragment != null) {
            fragmentTransaction.hide(groupFragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (bottom_navigation.getSelectedItemId()) {
            case R.id.main_menu_bottom_navi_diary:
                menu.findItem(R.id.main_toolbar_seach).setVisible(false);
                break;
            case R.id.main_menu_bottom_navi_syy:
                menu.findItem(R.id.main_toolbar_seach).setVisible(true);
                break;
            case R.id.main_menu_bottom_navi_broadcast:
                menu.findItem(R.id.main_toolbar_seach).setVisible(false);
                break;
            case R.id.main_menu_bottom_navi_group:
                menu.findItem(R.id.main_toolbar_seach).setVisible(false);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

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
    public void closeDrawer(int type) {
        drawer.closeDrawer(Gravity.START);
        naviSelectType = type;
    }
}

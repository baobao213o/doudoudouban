package com.xxx.douban.ui.main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xxx.broadcast.ui.container.BroadcastFragment;
import com.xxx.diary.ui.container.DiaryFragment;
import com.xxx.douban.R;
import com.xxx.group.ui.container.GroupFragment;
import com.xxx.library.Constant;
import com.xxx.library.entity.User;
import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.mvp.view.IView;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.rxjava.RxBusManager;
import com.xxx.library.user.BaseUserActivity;
import com.xxx.library.user.IUserFragment;
import com.xxx.library.utils.ColorUtils;
import com.xxx.library.utils.DeviceUtil;
import com.xxx.library.views.ToastHelper;
import com.xxx.my.ui.user.UserFragment;
import com.xxx.syy.ui.container.SyyFragment;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

@Route(path = Constant.ARouter.AROUTER_MAIN_MAIN)
public class MainActivity extends BaseUserActivity<TestPresenter> implements IView, UserFragment.IDrawerClose, OnMenuItemClickListener {


    private IUserFragment leftFragment;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private int naviSelectType;

    private DiaryFragment diaryFragment;
    private SyyFragment syyFragment;
    private BroadcastFragment broadcastFragment;
    private GroupFragment groupFragment;

    private static final String FRAG_TAG_DIARY = "diary";
    private static final String FRAG_TAG_SYY = "syy";
    private static final String FRAG_TAG_BROADCAST = "broadcast";
    private static final String FRAG_TAG_GROUP = "group";

    private int index = FRAG_DIARY;
    private static final int FRAG_DIARY = 1;
    private static final int FRAG_SYY = 2;
    private static final int FRAG_BROADCAST = 3;
    private static final int FRAG_GROUP = 4;


    private static final String SELECTED_ITEM = "arg_selected_item";

    private ContextMenuDialogFragment mMenuDialogFragment;

    @Override
    public void onBaseUserActivityCreate(Bundle savedInstanceState) {

//        if (!AccountHelper.getInstance().checkActiveAccount(this)) {
//            return;
//        }
        setContentView(R.layout.main_activity_main);
        drawer = findViewById(R.id.drawer_main);
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        final int startColor = getResources().getColor(R.color.colorPrimary);
        final int endColor = getResources().getColor(R.color.dark_30_percent);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                getWindow().setStatusBarColor(ColorUtils.getCurrentColor(slideOffset, startColor, endColor));
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
        initMenuFragment();
        if (savedInstanceState != null) {
            index = savedInstanceState.getInt(SELECTED_ITEM, index);
            diaryFragment = (DiaryFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_DIARY);
            syyFragment = (SyyFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_SYY);
            broadcastFragment = (BroadcastFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_BROADCAST);
            groupFragment = (GroupFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_GROUP);
        }
        showFragment();

        leftFragment = (IUserFragment) getSupportFragmentManager().findFragmentById(R.id.frgment_main_navigation);
        ((UserFragment) leftFragment).setListener(this);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, index);
        super.onSaveInstanceState(outState);
    }

    private void showFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.common_anim_fade_in, R.anim.common_anim_fade_out);
        hideFragment(fragmentTransaction);
        switch (index) {
            case FRAG_DIARY:
                if (diaryFragment == null) {
                    diaryFragment = new DiaryFragment();
                    fragmentTransaction.add(R.id.container_main, diaryFragment, FRAG_TAG_DIARY);
                } else {
                    fragmentTransaction.show(diaryFragment);
                }
                toolbar.setTitle(getString(R.string.main_bottom_navi_diary));
                break;
            case FRAG_SYY:
                if (syyFragment == null) {
                    syyFragment = new SyyFragment();
                    fragmentTransaction.add(R.id.container_main, syyFragment, FRAG_TAG_SYY);
                } else {
                    fragmentTransaction.show(syyFragment);
                }
                toolbar.setTitle(getString(R.string.main_bottom_navi_syy));
                break;
            case FRAG_BROADCAST:
                if (broadcastFragment == null) {
                    broadcastFragment = new BroadcastFragment();
                    fragmentTransaction.add(R.id.container_main, broadcastFragment, FRAG_TAG_BROADCAST);
                } else {
                    fragmentTransaction.show(broadcastFragment);
                }
                toolbar.setTitle(getString(R.string.main_bottom_navi_broadcast));
                break;
            case FRAG_GROUP:
                if (groupFragment == null) {
                    groupFragment = new GroupFragment();
                    fragmentTransaction.add(R.id.container_main, groupFragment, FRAG_TAG_GROUP);
                } else {
                    fragmentTransaction.show(groupFragment);
                }
                toolbar.setTitle(getString(R.string.main_bottom_navi_group));
                break;
        }
        fragmentTransaction.commit();
    }


    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (index != FRAG_DIARY && diaryFragment != null) {
            fragmentTransaction.hide(diaryFragment);
        }
        if (index != FRAG_SYY && syyFragment != null) {
            fragmentTransaction.hide(syyFragment);
        }
        if (index != FRAG_BROADCAST && broadcastFragment != null) {
            fragmentTransaction.hide(broadcastFragment);
        }
        if (index != FRAG_GROUP && groupFragment != null) {
            fragmentTransaction.hide(groupFragment);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_toolbar_seach:
                ToastHelper.showToast("search");
                break;
            case R.id.context_menu:
                if (getSupportFragmentManager().findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(getSupportFragmentManager(), ContextMenuDialogFragment.TAG);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar, menu);
        return true;
    }


    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize(DeviceUtil.dip2px(60));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
    }

    private List<MenuObject> getMenuObjects() {
        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.main_svg_bottom_close);

        MenuObject diary = new MenuObject(getString(R.string.main_bottom_navi_diary));
        diary.setResource(R.drawable.main_svg_bottom_diary);

        MenuObject syy = new MenuObject(getString(R.string.main_bottom_navi_syy));
        syy.setResource(R.drawable.main_svg_bottom_syy);

        MenuObject broadcast = new MenuObject(getString(R.string.main_bottom_navi_broadcast));
        broadcast.setResource(R.drawable.main_svg_bottom_broadcast);

        MenuObject group = new MenuObject(getString(R.string.main_bottom_navi_group));
        group.setResource(R.drawable.main_svg_bottom_group);

        menuObjects.add(close);
        menuObjects.add(diary);
        menuObjects.add(syy);
        menuObjects.add(broadcast);
        menuObjects.add(group);
        return menuObjects;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (index) {
            case FRAG_DIARY:
                menu.findItem(R.id.main_toolbar_seach).setVisible(false);
                break;
            case FRAG_SYY:
                menu.findItem(R.id.main_toolbar_seach).setVisible(true);
                break;
            case FRAG_BROADCAST:
                menu.findItem(R.id.main_toolbar_seach).setVisible(false);
                break;
            case FRAG_GROUP:
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
            return;
        }
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
            return;
        }
        moveTaskToBack(isTaskRoot());
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

    @Override
    public void onMenuItemClick(View view, int i) {
        invalidateOptionsMenu();
        if (i == 0) {
            return;
        }
        index = i;
        showFragment();
    }

}

package com.xxx.douban.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xxx.douban.R;
import com.xxx.library.base.BaseActivity;
import com.xxx.library.entity.User;
import com.xxx.library.mvp.model.BaseModel;
import com.xxx.library.mvp.view.IView;
import com.xxx.library.utils.ColorUtil;
import com.xxx.library.utils.FragmentUtil;
import com.xxx.my.ui.user.UserFragment;

@Route(path = "/main/main/MainActivity")
public class MainActivity extends BaseActivity<User, TestPresenter> implements IView<User> {


    private UserFragment leftFragment;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (!AccountHelper.getInstance().checkActiveAccount(this)) {
//            return;
//        }
        setContentView(R.layout.main_activity_main);
        drawer = findViewById(R.id.drawer_main);
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
}

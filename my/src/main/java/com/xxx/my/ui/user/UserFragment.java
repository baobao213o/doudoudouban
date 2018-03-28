package com.xxx.my.ui.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxx.library.account.AccountHelper;
import com.xxx.library.base.BaseFragment;
import com.xxx.library.entity.User;
import com.xxx.library.glide.GlideApp;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.rxjava.RxBusManager;
import com.xxx.library.user.IUserFragment;
import com.xxx.my.R;

/**
 * Created by gaoruochen on 18-3-20.
 */

public class UserFragment extends BaseFragment implements IUserFragment {


    public interface IDrawerClose {
        void closeDrawer();
    }

    private IDrawerClose listener;

    private ImageView userHead;
    private TextView userName;
    private TextView userId;
    private TextView follow;
    private TextView followed;
    private NavigationView navigation;

    private View loginView;
    private View unloginView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment_user, container, false);
        initNavigationView(view);
        initUnLoginView(inflater);
        initLoginView(inflater);
        clearUserStatus();
        return view;
    }

    public void setListener(IDrawerClose listener) {
        this.listener = listener;
    }

    private void initNavigationView(View view) {
        navigation = view.findViewById(R.id.navigation_my_user);
        navigation.setItemIconTintList(null);
        NavigationMenuView navigationMenuView = (NavigationMenuView) navigation.getChildAt(0);
        if (navigationMenuView != null) {
            navigationMenuView.setVerticalScrollBarEnabled(false);
        }

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_message) {
                    RxBusManager.getInstance().post(AccountHelper.RXBUS_UPDATE_USER_STATUS);
                    System.out.println("message");
                } else if (id == R.id.navigation_book) {
                    System.out.println("book");
                } else if (id == R.id.navigation_movie) {
                    System.out.println("movie");
                } else if (id == R.id.navigation_music) {
                    System.out.println("music");
                } else if (id == R.id.navigation_diary) {
                    System.out.println("diary");
                } else if (id == R.id.navigation_exit) {
                    AccountHelper.getInstance().removeAllAccount();
                    RxBusManager.getInstance().post(AccountHelper.RXBUS_CLEAR_USER_STATUS);
                }
                if (listener != null) {
                    listener.closeDrawer();
                }
                return true;
            }
        });
    }

    private void initUnLoginView(LayoutInflater inflater) {
        unloginView = inflater.inflate(R.layout.my_navi_logout_head_layout, navigation, false);
        unloginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountHelper.getInstance().addAccount(getActivity(), null);
            }
        });
    }

    private void initLoginView(LayoutInflater inflater) {
        loginView = inflater.inflate(R.layout.my_navi_user_head_layout, navigation, false);
        userName = loginView.findViewById(R.id.tv_my_user_name);
        userId = loginView.findViewById(R.id.tv_my_user_id);
        userHead = loginView.findViewById(R.id.iv_my_user_head);
        View home = loginView.findViewById(R.id.cl_my_user_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        View settings = loginView.findViewById(R.id.iv_my_user_settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        follow = loginView.findViewById(R.id.tv_my_user_follow);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        followed = loginView.findViewById(R.id.tv_my_user_followed);
        followed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onUserUpdateFailure(ExceptionHandle.ResponseThrowable responseThrowable) {

    }

    @Override
    public void onUserUpdateSuccess(User user) {
        if (navigation.getHeaderCount() != 0) {
            navigation.removeHeaderView(navigation.getHeaderView(0));
        }
        navigation.addHeaderView(loginView);
        GlideApp.with(this).load(user.large_avatar).placeholder(R.drawable.my_user_head_normal).error(R.drawable.my_user_head_normal).into(userHead);
        userName.setText(user.name);
        userId.setText(user.id);
        follow.setText(String.format(getString(R.string.my_follow), user.following_count));
        followed.setText(String.format(getString(R.string.my_followed), user.followers_count));
    }

    @Override
    public void clearUserStatus() {
        if (navigation.getHeaderCount() != 0) {
            navigation.removeHeaderView(navigation.getHeaderView(0));
        }
        navigation.addHeaderView(unloginView);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}

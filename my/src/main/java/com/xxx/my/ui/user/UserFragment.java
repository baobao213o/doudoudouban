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
import com.xxx.library.utils.CommonLogger;
import com.xxx.my.R;

import io.reactivex.functions.Consumer;

/**
 * Created by gaoruochen on 18-3-20.
 */

public class UserFragment extends BaseFragment implements IUserFragment {


    public final static int USER_NAVI_MESSAGE = 11;
    public final static int USER_NAVI_BOOK = 12;
    public final static int USER_NAVI_MOVIE = 13;
    public final static int USER_NAVI_MUSIC = 14;
    public final static int USER_NAVI_DIARY = 15;
    public final static int USER_NAVI_EXIT = 16;

    public interface IDrawerClose {
        void closeDrawer(int closeType);
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
        RxBusManager.getInstance().registerEvent(Integer.class, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                switch (integer) {
                    case USER_NAVI_MESSAGE:
                        RxBusManager.getInstance().post(AccountHelper.RXBUS_UPDATE_USER_STATUS);
                        break;
                    case USER_NAVI_BOOK:

                        break;
                    case USER_NAVI_MOVIE:

                        break;
                    case USER_NAVI_MUSIC:

                        break;
                    case USER_NAVI_DIARY:

                        break;
                    case USER_NAVI_EXIT:
                        AccountHelper.getInstance().removeAllAccount();
                        RxBusManager.getInstance().post(AccountHelper.RXBUS_CLEAR_USER_STATUS);
                        break;
                    default:
                        break;
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                CommonLogger.e(throwable.getMessage());
            }
        });
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
                int selectedType = 0;
                int id = item.getItemId();
                if (id == R.id.navigation_message) {
                    selectedType = USER_NAVI_MESSAGE;
                } else if (id == R.id.navigation_book) {
                    selectedType = USER_NAVI_BOOK;
                } else if (id == R.id.navigation_movie) {
                    selectedType = USER_NAVI_MOVIE;
                } else if (id == R.id.navigation_music) {
                    selectedType = USER_NAVI_MUSIC;
                } else if (id == R.id.navigation_diary) {
                    selectedType = USER_NAVI_DIARY;
                } else if (id == R.id.navigation_exit) {
                    selectedType = USER_NAVI_EXIT;
                }
                if (listener != null) {
                    listener.closeDrawer(selectedType);
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
                System.out.println(1213213);
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
        MenuItem menuItem = navigation.getMenu().findItem(R.id.navigation_exit);
        menuItem.setVisible(true);

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
        MenuItem menuItem = navigation.getMenu().findItem(R.id.navigation_exit);
        menuItem.setVisible(false);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}

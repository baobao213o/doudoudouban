package com.xxx.library.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;
import com.xxx.library.BaseApplication;
import com.xxx.library.R;
import com.xxx.library.utils.DeviceUtil;

/**
 * Created by gaoruochen on 18-6-12.
 */

public class LoadingLayoutHelper {

    private final static String TAG = "loading";

    public static void addLoadingView(ViewGroup viewGroup) {
        View loadingView = viewGroup.findViewWithTag(TAG);
        if (loadingView == null) {
            loadingView = LayoutInflater.from(BaseApplication.getInstance()).inflate(R.layout.common_loading_layout, viewGroup, false);
            loadingView.setTag(TAG);
            int h = DeviceUtil.getScreenHeight();
            viewGroup.addView(loadingView, DeviceUtil.getScreenWidth(), h);
            ObjectAnimator animator = ObjectAnimator.ofFloat(loadingView, "translationY", h, 0);
            animator.setDuration(500);
            animator.start();
        }
        AVLoadingIndicatorView view_loading = loadingView.findViewById(R.id.view_loading);
        view_loading.show();
        view_loading.setVisibility(View.VISIBLE);
        TextView tv_loading_fail = loadingView.findViewById(R.id.tv_loading_fail);
        tv_loading_fail.setVisibility(View.GONE);
    }

    public static void removeLoadingView(final ViewGroup viewGroup) {
        final View loadingView = viewGroup.findViewWithTag(TAG);
        if (loadingView != null) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(loadingView, "translationY", 0, loadingView.getHeight());
            animator.setDuration(500);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    AVLoadingIndicatorView view_loading = loadingView.findViewById(R.id.view_loading);
                    view_loading.hide();
                    viewGroup.removeView(loadingView);
                }
            });
            animator.start();
        }
    }

    public static void addFailureView(ViewGroup viewGroup, String error, View.OnClickListener onClickListener) {
        View loadingView = viewGroup.findViewWithTag(TAG);
        if (loadingView == null) {
            loadingView = LayoutInflater.from(BaseApplication.getInstance()).inflate(R.layout.common_loading_layout, viewGroup, false);
            loadingView.setTag(TAG);
            int width = viewGroup.getWidth();
            int height = viewGroup.getHeight();
            if (width != 0 && height != 0) {
                viewGroup.addView(loadingView, new ViewGroup.LayoutParams(width, height));
            } else {
                viewGroup.addView(loadingView);
            }
        }
        AVLoadingIndicatorView view_loading = loadingView.findViewById(R.id.view_loading);
        view_loading.hide();
        TextView tv_loading_fail = loadingView.findViewById(R.id.tv_loading_fail);
        if (!TextUtils.isEmpty(error)) {
            tv_loading_fail.setText(error);
        }
        tv_loading_fail.setOnClickListener(onClickListener);
        tv_loading_fail.setVisibility(View.VISIBLE);

    }


}

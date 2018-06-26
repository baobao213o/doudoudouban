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

    public static final int Loading_success = 0;
    public static final int Loading_fail = 1;
    public static final int Loading_loading = 2;

    private final static String TAG = "loading";
    private final static String TAG_GONE = "tag_gone";
    private final static String TAG_INVISIBLE = "tag_invisible";
    private final static String TAG_MANUAL_GONE = "tag_manual_gone";

    public static void addLoadingView(ViewGroup viewGroup) {
        addLoadingView(viewGroup, 0);
    }


    public static void addLoadingView(ViewGroup viewGroup, int requiredHeight) {
        if (viewGroup.getVisibility() == View.GONE || viewGroup.getVisibility() == View.INVISIBLE) {
            viewGroup.setVisibility(View.VISIBLE);
        }
        int childCount = viewGroup.getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View child = viewGroup.getChildAt(i);
                switch (child.getVisibility()) {
                    case View.VISIBLE:
                        if (child.getTag() != TAG) {
                            child.setVisibility(View.GONE);
                            child.setTag(TAG_MANUAL_GONE);
                        }
                        break;
                    case View.INVISIBLE:
                        child.setVisibility(View.GONE);
                        child.setTag(TAG_INVISIBLE);
                        break;
                    case View.GONE:
                        if (child.getTag() != TAG_MANUAL_GONE) {
                            child.setTag(TAG_GONE);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        View loadingView = viewGroup.findViewWithTag(TAG);
        if (loadingView == null) {
            loadingView = LayoutInflater.from(BaseApplication.getInstance()).inflate(R.layout.common_loading_layout, viewGroup, false);
            loadingView.setTag(TAG);
            int h;
            if (requiredHeight > 0) {
                h = requiredHeight;
            } else {
                h = viewGroup.getHeight() < DeviceUtil.getScreenHeaightWithoutToolbar() ? DeviceUtil.getScreenHeaightWithoutToolbar() : viewGroup.getHeight();
            }

            viewGroup.addView(loadingView, DeviceUtil.getScreenWidth(), h);
        }
        //不加会被已覆盖的控件抢夺焦点
        loadingView.setOnClickListener(null);
        AVLoadingIndicatorView view_loading = loadingView.findViewById(R.id.view_loading);
        view_loading.show();
        view_loading.setVisibility(View.VISIBLE);
        TextView tv_loading_fail = loadingView.findViewById(R.id.tv_loading_fail);
        tv_loading_fail.setVisibility(View.GONE);
    }

    public static void removeLoadingView(final ViewGroup viewGroup) {
        final View loadingView = viewGroup.findViewWithTag(TAG);
        if (loadingView != null) {
            int childCount = viewGroup.getChildCount();
            if (childCount > 0) {
                for (int i = 0; i < childCount; i++) {
                    View child = viewGroup.getChildAt(i);
                    if (child.getTag() == TAG_INVISIBLE) {
                        child.setVisibility(View.INVISIBLE);
                    } else if (child.getTag() != TAG_GONE) {
                        child.setVisibility(View.VISIBLE);
                    }
                }
            }

            final AVLoadingIndicatorView view_loading = loadingView.findViewById(R.id.view_loading);
            ObjectAnimator animator = ObjectAnimator.ofFloat(loadingView, "translationY", 0, loadingView.getHeight());
            animator.setDuration(500);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view_loading.hide();
                    viewGroup.removeView(loadingView);
                }
            });
            animator.start();
        }
    }

    public static void addFailureView(ViewGroup viewGroup, String error, View.OnClickListener onClickListener) {
        addFailureView(viewGroup, error, onClickListener, 0);
    }

    public static void addFailureView(ViewGroup viewGroup, String error, View.OnClickListener onClickListener, int requiredHeight) {
        if (viewGroup.getVisibility() == View.GONE || viewGroup.getVisibility() == View.INVISIBLE) {
            viewGroup.setVisibility(View.VISIBLE);
        }
        int childCount = viewGroup.getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View child = viewGroup.getChildAt(i);
                switch (child.getVisibility()) {
                    case View.VISIBLE:
                        if (child.getTag() != TAG) {
                            child.setVisibility(View.GONE);
                            child.setTag(TAG_MANUAL_GONE);
                        }
                        break;
                    case View.INVISIBLE:
                        child.setVisibility(View.GONE);
                        child.setTag(TAG_INVISIBLE);
                        break;
                    case View.GONE:
                        if (child.getTag() != TAG_MANUAL_GONE) {
                            child.setTag(TAG_GONE);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        View loadingView = viewGroup.findViewWithTag(TAG);
        if (loadingView == null) {
            loadingView = LayoutInflater.from(BaseApplication.getInstance()).inflate(R.layout.common_loading_layout, viewGroup, false);
            loadingView.setTag(TAG);
            int h;
            if (requiredHeight > 0) {
                h = requiredHeight;
            } else {
                h = viewGroup.getHeight() < DeviceUtil.getScreenHeaightWithoutToolbar() ? DeviceUtil.getScreenHeaightWithoutToolbar() : viewGroup.getHeight();
            }
            int w = DeviceUtil.getScreenWidth();
            viewGroup.addView(loadingView, w, h);
        }
        //不加会被已覆盖的控件抢夺焦点
        loadingView.setOnClickListener(null);
        AVLoadingIndicatorView view_loading = loadingView.findViewById(R.id.view_loading);
        view_loading.hide();
        TextView tv_loading_fail = loadingView.findViewById(R.id.tv_loading_fail);
        if (!TextUtils.isEmpty(error)) {
            tv_loading_fail.setText(error);
        }
        tv_loading_fail.setOnClickListener(new OnClickWrapper(loadingView, onClickListener));
        tv_loading_fail.setVisibility(View.VISIBLE);

    }

    private static class OnClickWrapper implements View.OnClickListener {

        private AVLoadingIndicatorView view_loading;
        private TextView tv_loading_fail;
        private View.OnClickListener onClickListener;

        OnClickWrapper(View loadingView, View.OnClickListener onClickListener) {
            view_loading = loadingView.findViewById(R.id.view_loading);
            tv_loading_fail = loadingView.findViewById(R.id.tv_loading_fail);
            this.onClickListener = onClickListener;
        }

        @Override
        public void onClick(View v) {
            tv_loading_fail.setVisibility(View.GONE);
            view_loading.show();
            view_loading.setVisibility(View.VISIBLE);
            onClickListener.onClick(v);
        }
    }


    public static boolean isViewAdded(ViewGroup viewGroup) {
        View loadingView = viewGroup.findViewWithTag(TAG);
        return loadingView != null;
    }


}

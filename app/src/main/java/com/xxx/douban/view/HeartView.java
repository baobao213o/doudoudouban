package com.xxx.douban.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.xxx.douban.R;
import com.xxx.library.utils.SVGUtil;

import java.util.ArrayList;

/**
 * x=16×sin3α
 * y=13×cosα-5×cos2α-2×cos3α-cos4α
 */

public class HeartView extends View {

    public interface AniFinishListener {
        void aniFinish();
    }


    private Paint mPaint;
    private Point currentPoint;
    private Path path;
    private int offsetX, offsetY;
    private float screenRate;
    private AniFinishListener mListener;
    private Bitmap lolipop;

    private Bitmap candyBmp;

    private ValueAnimator candyAni;
    private ArrayList<FlowWidget> candyList;

    private ValueAnimator heartAni;
    private int screenH;

    private Matrix mtx;
    private int candyW;


    public HeartView(Context context) {
        this(context, null);
    }

    public HeartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.pink));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        path = new Path();

        lolipop = SVGUtil.getBitmapFromDrawable(R.drawable.main_svg_lolipop);
        candyBmp = SVGUtil.getBitmapFromDrawable(R.drawable.main_svg_candy);
        candyW = candyBmp.getWidth();
        candyList = new ArrayList<>();

        mtx = new Matrix();
    }

    public void setListener(AniFinishListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenH = h;

        screenRate = w / 480;
        mPaint.setStrokeWidth(5 * screenRate);
        offsetX = w / 2;
        offsetY = (int) (h / 2 - 80 * screenRate);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (currentPoint == null) {
            startHeartAnimationMotion();// 执行动画
        }
        path.lineTo(currentPoint.getX(), currentPoint.getY());
        canvas.drawPath(path, mPaint);
        canvas.drawBitmap(lolipop, currentPoint.getX() - 10 * screenRate, currentPoint.getY() - 45 * screenRate, null);

        for (FlowWidget candy : candyList) {
            mtx.setTranslate(-candyW / 2, 0);
            mtx.postTranslate(candy.x, candy.y);
            mtx.preRotate(candy.angle,candyW / 2,candyW / 2);
            canvas.drawBitmap(candyBmp, mtx, null);
        }
    }

    private void startCandyAnimation(float touchX, float touchY) {
        FlowWidget candy = new FlowWidget(touchX, touchY);
        candyList.add(candy);

        if (candyAni == null || !candyAni.isRunning()) {
            candyAni = ValueAnimator.ofFloat(0.0f, 1.0f);
            candyAni.setDuration(5000);
            candyAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    for (int i = 0; i < candyList.size(); i++) {
                        float speed = candyList.get(i).fallSpeed;
                        candyList.get(i).y += speed;
                        candyList.get(i).angle += candyList.get(i).rotateSpeed;
                        if (candyList.get(i).y > screenH) {
                            candyList.remove(i);
                        }

                    }
                }
            });
        }
        if (!candyAni.isRunning()) {
            candyAni.start();
        }
    }


    private Point getHeartPoint(float angle) {
        float t = (float) (angle / Math.PI);
        float x = (float) (12 * screenRate * (16 * Math.pow(Math.sin(t), 3)));
        float y = (float) (-12 * screenRate * (13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t)));
        return new Point(offsetX + (int) x, offsetY + (int) y);
    }


    private void startHeartAnimationMotion() {
        Point startPoint = getHeartPoint(0);
        path.moveTo(startPoint.x, startPoint.y);
        Point endPoint = getHeartPoint(20);

        heartAni = ValueAnimator.ofObject(new HeartShapeEvaluator(), startPoint, endPoint);
        heartAni.setDuration(5000);
        heartAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });
        heartAni.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mListener.aniFinish();
            }
        });
        heartAni.setInterpolator(new LinearInterpolator());//设置插值器
        heartAni.start();
    }


    class HeartShapeEvaluator implements TypeEvaluator {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            float angle = fraction * 20;
            return getHeartPoint(angle);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                startCandyAnimation(event.getX(), event.getY());
                break;
            default:
                break;
        }
        return true;
    }

    public void destroy() {
        if (candyAni != null) {
            candyAni.cancel();
            candyAni = null;
        }
        heartAni.cancel();
        heartAni = null;
    }


    private class Point {

        private float x;
        private float y;

        Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        private float getX() {
            return x;
        }

        private float getY() {
            return y;
        }

    }

    private class FlowWidget {

        private int angle;
        //下落速度
        private float fallSpeed;
        //自转速度
        private int rotateSpeed;

        private float x, y;

        FlowWidget(float x, float y) {
            this.x = x;
            this.y = y;
            fallSpeed = (float) (Math.random() * 2) + (float) (Math.random() * 3) * screenRate;
            rotateSpeed = (int) (Math.random() * 10);
        }

    }

}

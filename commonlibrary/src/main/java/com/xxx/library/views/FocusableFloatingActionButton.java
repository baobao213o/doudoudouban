package com.xxx.library.views;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class FocusableFloatingActionButton extends FloatingActionButton {

    public boolean isFocus = true;

    public FocusableFloatingActionButton(Context context) {
        super(context);
    }

    public FocusableFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusableFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isFocus) {
                    return false;
                }
                break;
        }
        return super.dispatchTouchEvent(event);

    }
}

package com.xxx.library.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.nightonke.boommenu.BoomMenuButton;

public class FocusableBoomMenu extends BoomMenuButton {

    public boolean isFocus = true;

    public FocusableBoomMenu(Context context) {
        super(context);
    }

    public FocusableBoomMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusableBoomMenu(Context context, AttributeSet attrs, int defStyleAttr) {
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

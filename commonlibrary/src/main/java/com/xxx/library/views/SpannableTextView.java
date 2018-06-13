package com.xxx.library.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;

import com.xxx.library.R;

/**
 * Created by gaoruochen on 18-6-7.
 */

public class SpannableTextView extends AppCompatTextView {

    public SpannableTextView(Context context) {
        super(context, null);
    }

    public SpannableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    private int getLastCharIndexForLimitTextView(String content, int width, int maxLine) {
        TextPaint textPaint = getPaint();
        StaticLayout staticLayout = new StaticLayout(content, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        if (staticLayout.getLineCount() > maxLine)
            return staticLayout.getLineStart(maxLine) - 1;//exceed
        else return -1;//not exceed the max line
    }

    public void limitTextViewString(String textString, int maxFirstShowCharCount) {
        int width = getWidth();//在recyclerView和ListView中，由于复用的原因，这个TextView可能以前就画好了，能获得宽度
        if (width == 0) width = 1000;//获取textView的实际宽度，这里可以用各种方式（一般是dp转px写死）填入TextView的宽度
        int lastCharIndex = getLastCharIndexForLimitTextView(textString, width, 10);
        //返回-1表示没有达到行数限制
        if (lastCharIndex < 0 && textString.length() <= maxFirstShowCharCount) {
            //如果行数没超过限制
            setText(textString);
            return;
        }
        //如果超出了行数限制
        setMovementMethod(LinkMovementMethod.getInstance());//this will deprive the recyclerView's focus
        if (lastCharIndex > maxFirstShowCharCount || lastCharIndex < 0) {
            lastCharIndex = maxFirstShowCharCount;
        }
        //构造spannableString
        String explicitText = null;
        String explicitTextAll;
        if (textString.charAt(lastCharIndex) == '\n') {//manual enter
            explicitText = textString.substring(0, lastCharIndex);
        } else if (lastCharIndex > 12) {
            //如果最大行数限制的那一行到达12以后则直接显示 显示更多
            explicitText = textString.substring(0, lastCharIndex - 12);
        }
        int sourceLength = explicitText.length();
        String showMore = "显示更多";
        explicitText = explicitText + "  " + showMore;
        final SpannableString mSpan = new SpannableString(explicitText);


        String dismissMore = "收起";
        explicitTextAll = textString + "  " + dismissMore;
        final SpannableString mSpanALL = new SpannableString(explicitTextAll);
        mSpanALL.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.blue_3));
                ds.setAntiAlias(true);
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
                setText(mSpan);
                setOnClickListener(null);
            }
        }, textString.length(), explicitTextAll.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mSpan.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.blue_3));
                ds.setAntiAlias(true);
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {//"...show more" click event
                setText(mSpanALL);
                setOnClickListener(null);
            }
        }, sourceLength, explicitText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置为“显示更多”状态下的TextVie
        setText(mSpan);
    }

}

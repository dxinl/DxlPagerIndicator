package com.mx.dengxinliang.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by DengXinliang on 2015/9/6.
 */
public class TabPagerIndicator extends View implements PagerIndicator {
    public TabPagerIndicator(Context context) {
        super(context);
    }

    public TabPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TabPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setViewPager(ViewPager viewPager) {

    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPagerChangeListener) {

    }
}

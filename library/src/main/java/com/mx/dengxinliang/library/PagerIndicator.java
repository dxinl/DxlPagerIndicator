package com.mx.dengxinliang.library;

import android.support.v4.view.ViewPager;

/**
 * Created by dxinliang on 9/5/15.
 */
public interface PagerIndicator {
	void setViewPager(ViewPager viewPager);
	void setOnPageChangeListener(ViewPager.OnPageChangeListener onPagerChangeListener);
}

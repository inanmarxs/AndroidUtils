package com.oldfeel.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义的viewpager,解决viewpager与HorizontalListView不兼容的问题
 * 
 * @author oldfeel
 * 
 *         Create on: 2014年11月8日
 */
public class CustomViewPager extends ViewPager {

	public CustomViewPager(Context context) {
		super(context);
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected boolean canScroll(View arg0, boolean arg1, int arg2, int arg3,
			int arg4) {
		if (arg0 instanceof HorizontalListView) {
			return true;
		}
		return super.canScroll(arg0, arg1, arg2, arg3, arg4);
	}
}

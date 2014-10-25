package com.oldfeel.base;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class BasePagerAdapter extends FragmentPagerAdapter {
	ArrayList<Fragment> list = new ArrayList<Fragment>();

	public BasePagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public void add(Fragment fragment) {
		list.add(fragment);
	}

	@Override
	public Fragment getItem(int position) {
		return list.get(position);
	}

	@Override
	public int getCount() {
		return list.size();
	}

}
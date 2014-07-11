package com.museda.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;

public class MainViewPageAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> fragments = null;
	private JazzyViewPager mPager;

	public MainViewPageAdapter(Context context, FragmentManager fm, JazzyViewPager pager) {
		super(fm);
		fragments = new ArrayList<Fragment>();
		mPager = pager;
	}

	@Override
	public Fragment instantiateItem(ViewGroup container, int position) {
		Fragment fragment = (Fragment) super.instantiateItem(container, position);
		mPager.setObjectForPosition(fragment, position);
		return fragment;
	}

	public void addFragment(Fragment fragment) {
		if (null != fragment)
			fragments.add(fragment);
	}

	@Override
	public Fragment getItem(int position) {

		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

}

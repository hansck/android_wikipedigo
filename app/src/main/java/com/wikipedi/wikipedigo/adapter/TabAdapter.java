package com.wikipedi.wikipedigo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hans CK on 13-Jan-17.
 */

public class TabAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments = new ArrayList<>();
	private List<String> titles = new ArrayList<>();

	public TabAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles.get(position);
	}

	public void addFragment(Fragment fragment, String title) {
		fragments.add(fragment);
		titles.add(title);
	}
}
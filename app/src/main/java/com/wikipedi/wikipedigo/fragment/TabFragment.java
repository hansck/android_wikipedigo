package com.wikipedi.wikipedigo.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wikipedi.wikipedigo.R;
import com.wikipedi.wikipedigo.adapter.TabAdapter;
import com.wikipedi.wikipedigo.util.Constants;

import org.androidannotations.annotations.EFragment;

/**
 * Created by Hans CK on 13-Feb-17.
 */

@EFragment(R.layout.fragment_tab)
public class TabFragment extends Fragment {

	public static TabAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tab, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewPager);
		setupViewPager(viewPager);
	}

	private void setupViewPager(ViewPager vPager) {
		adapter = new TabAdapter(getChildFragmentManager());
		PhotoListFragment_ photoFragment = new PhotoListFragment_().newInstance(Constants.General.TAB_GALLERY);
		adapter.addFragment(photoFragment, getString(R.string.gallery));

		PhotoListFragment_ favoriteFragment = new PhotoListFragment_().newInstance(Constants.General.TAB_FAVORITE);
		adapter.addFragment(favoriteFragment, getString(R.string.favorite));

		vPager.setAdapter(adapter);
		TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabLayout);
		tabLayout.setupWithViewPager(vPager);
	}
}
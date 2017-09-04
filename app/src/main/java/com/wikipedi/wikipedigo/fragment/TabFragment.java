package com.wikipedi.wikipedigo.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
	private int[] tabIcons = {
		R.drawable.tab_timeline,
		R.drawable.tab_gallery,
		R.drawable.tab_favorite
	};

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

		PhotoListFragment_ timelineFragment = new PhotoListFragment_().newInstance(Constants.General.TAB_TIMELINE);
		adapter.addFragment(timelineFragment, getString(R.string.timeline));

		PhotoListFragment_ photoFragment = new PhotoListFragment_().newInstance(Constants.General.TAB_GALLERY);
		adapter.addFragment(photoFragment, getString(R.string.gallery));

		PhotoListFragment_ favoriteFragment = new PhotoListFragment_().newInstance(Constants.General.TAB_FAVORITE);
		adapter.addFragment(favoriteFragment, getString(R.string.favorite));

		vPager.setAdapter(adapter);

		TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabLayout);
		tabLayout.setupWithViewPager(vPager);

		tabLayout.getTabAt(Constants.General.TAB_TIMELINE).setCustomView(setTab(getString(R.string.timeline), tabIcons[0]));
		tabLayout.getTabAt(Constants.General.TAB_GALLERY).setCustomView(setTab(getString(R.string.gallery), tabIcons[1]));
		tabLayout.getTabAt(Constants.General.TAB_FAVORITE).setCustomView(setTab(getString(R.string.favorite), tabIcons[2]));
	}

	private View setTab(String title, int icon) {
		View tab = LayoutInflater.from(getActivity()).inflate(R.layout.item_tab, null);
		((TextView) tab.findViewById(R.id.label)).setText(title);
		((ImageView) tab.findViewById(R.id.icon)).setImageResource(icon);
		return tab;
	}
}
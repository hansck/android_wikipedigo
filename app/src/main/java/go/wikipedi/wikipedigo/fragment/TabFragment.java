package go.wikipedi.wikipedigo.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EFragment;

import go.wikipedi.wikipedigo.R;
import go.wikipedi.wikipedigo.adapter.TabAdapter;

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
		PhotoListFragment photoFragment = new PhotoListFragment_();
		photoFragment.setArguments(new Bundle());
		adapter.addFragment(photoFragment, getString(R.string.gallery));

		FavoriteListFragment favoriteFragment = new FavoriteListFragment_();
		favoriteFragment.setArguments(new Bundle());
		adapter.addFragment(favoriteFragment, getString(R.string.favorite));
		vPager.setAdapter(adapter);

		TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabLayout);
		tabLayout.setupWithViewPager(vPager);
	}
}

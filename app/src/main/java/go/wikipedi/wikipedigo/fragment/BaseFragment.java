package go.wikipedi.wikipedigo.fragment;

import android.support.v4.app.Fragment;

import go.wikipedi.wikipedigo.util.OnBackPressedListener;

/**
 * Created by E460 on 20/01/2017.
 */

public class BaseFragment extends Fragment implements OnBackPressedListener {

	public BaseFragment() {
	}

	@Override
	public boolean onBackPressed() {
		return false;
	}
}

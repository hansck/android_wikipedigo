package com.wikipedi.wikipedigo.util;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.wikipedi.wikipedigo.R;

/**
 * Created by E460 on 12/01/2017.
 */

public class Common {

	private static Common instance = new Common();

	public static Common getInstance() {
		return instance;
	}

	private Common() {
	}

	public void showSnackbar(Activity activity, String text) {
		try {
			Snackbar.make(activity.getCurrentFocus(), text, Snackbar.LENGTH_LONG).show();
		} catch (NullPointerException e) {
			e.printStackTrace();
			View view = activity.getWindow().getDecorView().findViewById(R.id.content);
			if (view != null) Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
		}
	}

	public void hideSoftKeyboard(Activity activity) {
		try {
			InputMethodManager i = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (activity.getCurrentFocus() != null) {
				i.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

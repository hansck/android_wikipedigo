package com.wikipedi.wikipedigo.model.object;

import android.content.SharedPreferences;

import com.wikipedi.wikipedigo.util.Constants;

/**
 * Created by Hans CK on 17-Feb-17.
 */

public class UserPreferences {

	private SharedPreferences keyStore;
	private static UserPreferences instance = new UserPreferences();

	private UserPreferences() {
		//empty constructor, this is a singleton
	}

	public static UserPreferences getInstance() {
		return instance;
	}

	public void setKeyStore(SharedPreferences sp) {
		keyStore = sp;
	}

	public SharedPreferences getKeyStore() {
		return keyStore;
	}

	public void setPreferences(String sortBy, String sortMethod) {
		SharedPreferences.Editor editor = keyStore.edit();
		editor.putString(Constants.Sort.SORT_BY, sortBy);
		editor.putString(Constants.Sort.SORT_METHOD, sortMethod);
		editor.apply();
	}

	public String getSortMethod() {
		if (keyStore.getString(Constants.Sort.SORT_METHOD, "").equals("")) {
			SharedPreferences.Editor editor = keyStore.edit();
			editor.putString(Constants.Sort.SORT_METHOD, Constants.Sort.ASCENDING);
			editor.apply();
		}
		return keyStore.getString(Constants.Sort.SORT_METHOD, "");
	}

	public String getSortBy() {
		if (keyStore.getString(Constants.Sort.SORT_BY, "").equals("")) {
			SharedPreferences.Editor editor = keyStore.edit();
			editor.putString(Constants.Sort.SORT_BY, Constants.Sort.DATE);
			editor.apply();
		}
		return keyStore.getString(Constants.Sort.SORT_BY, "");
	}

	public int getFavoriteCount() {
		if (isFreshInstalled()) resetFavoriteCount();
		return keyStore.getInt(Constants.Favorite.LEFT_COUNT, 0);
	}

	public void resetFavoriteCount() {
		SharedPreferences.Editor editor = keyStore.edit();
		editor.putInt(Constants.Favorite.LEFT_COUNT, Constants.Favorite.DEFAULT_COUNT);
		editor.apply();
	}

	public void reduceFavoriteCount() {
		SharedPreferences.Editor editor = keyStore.edit();
		int left = keyStore.getInt(Constants.Favorite.LEFT_COUNT, 0);
		editor.putInt(Constants.Favorite.LEFT_COUNT, left - 1);
		editor.apply();
	}

	public boolean isFreshInstalled() {
		SharedPreferences.Editor editor = keyStore.edit();
		boolean fresh = keyStore.getBoolean(Constants.Favorite.FRESH_INSTALLED, true);
		if (fresh) {
			editor.putBoolean(Constants.Favorite.FRESH_INSTALLED, false);
			editor.apply();
		}
		return fresh;
	}
}
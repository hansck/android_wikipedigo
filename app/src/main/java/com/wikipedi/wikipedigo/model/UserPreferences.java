package com.wikipedi.wikipedigo.model;

import android.content.SharedPreferences;

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
		editor.putString("sortBy", sortBy);
		editor.putString("sortMethod", sortMethod);
		editor.apply();
	}

	public String getSortMethod() {
		if (keyStore.getString("sortMethod", "").equals("")) {
			SharedPreferences.Editor editor = keyStore.edit();
			editor.putString("sortMethod", "ascending");
			editor.apply();
		}
		return keyStore.getString("sortMethod", "");
	}

	public String getSortBy() {
		if (keyStore.getString("sortBy", "").equals("")) {
			SharedPreferences.Editor editor = keyStore.edit();
			editor.putString("sortBy", "date");
			editor.apply();
		}
		return keyStore.getString("sortBy", "");
	}
}
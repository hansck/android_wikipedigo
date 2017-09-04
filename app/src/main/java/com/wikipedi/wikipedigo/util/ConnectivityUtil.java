package com.wikipedi.wikipedigo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.wikipedi.wikipedigo.application.BaseApplication;

/**
 * Created by E460 on 12/01/2017.
 */

public class ConnectivityUtil {

	private ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);

	public static ConnectivityUtil instance = new ConnectivityUtil();

	public ConnectivityUtil() {
		// Singleton, empty constructor
	}

	public static ConnectivityUtil getInstance() {
		return instance;
	}

	public boolean isNetworkConnected() {
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		return info != null && info.isConnected();
	}
}

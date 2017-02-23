package com.wikipedi.wikipedigo.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.wikipedi.wikipedigo.application.BaseApplication;
import okhttp3.ResponseBody;

/**
 * Created by E460 on 12/01/2017.
 */

public class ConnectivityUtils {

	private ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);

	public static ConnectivityUtils instance = new ConnectivityUtils();

	public ConnectivityUtils() {
		// Singleton, empty constructor
	}

	public static ConnectivityUtils getInstance() {
		return instance;
	}

	public boolean isNetworkConnected() {
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		return info != null && info.isConnected();
	}
}

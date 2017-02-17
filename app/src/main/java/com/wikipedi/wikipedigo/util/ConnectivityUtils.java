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

	public boolean isSuccess(int code) {
		return code >= 200 && code < 207;
	}

	public void failedRequest(Activity activity, int responseCode, ResponseBody errorBody) {
		//Log.d("FAILED REQUEST", "" + responseCode);
		String retval = "";
		switch (responseCode) {
			case 400:
				retval = Constants.ResponseError.C400;
				break;
			case 401:
				retval = Constants.ResponseError.C401;
				break;
			case 402:
				retval = Constants.ResponseError.C402;
				break;
			case 403:
				retval = Constants.ResponseError.C403;
				break;
			case 404:
				retval = Constants.ResponseError.C404;
				break;
			case 405:
				retval = Constants.ResponseError.C405;
				break;
			case 406:
				retval = Constants.ResponseError.C406;
				break;
			case 407:
				retval = Constants.ResponseError.C407;
				break;
			case 408:
				retval = Constants.ResponseError.C408;
				break;
			case 409:
				retval = Constants.ResponseError.C409;
				break;
			case 410:
				retval = Constants.ResponseError.C410;
				break;
			case 411:
				retval = Constants.ResponseError.C411;
				break;
			case 412:
				retval = Constants.ResponseError.C412;
				break;
			case 413:
				retval = Constants.ResponseError.C413;
				break;
			case 414:
				retval = Constants.ResponseError.C414;
				break;
			case 415:
				retval = Constants.ResponseError.C415;
				break;
			case 416:
				retval = Constants.ResponseError.C416;
				break;
			case 417:
				retval = Constants.ResponseError.C417;
				break;
			default:
				retval = Constants.ResponseError.DEFAULT;
				break;
		}
		Common.getInstance().showSnackbar(activity, retval);
	}
}

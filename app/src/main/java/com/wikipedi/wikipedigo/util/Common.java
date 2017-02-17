package com.wikipedi.wikipedigo.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

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

	public boolean verifyPermission(int[] grantResults) {
		if (grantResults.length < 1) {
			return false;
		}
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	public String getQueryString(String root, Map<String, String> map) {
		String query = root;
		if (map != null) {
			query += "?";
			TreeMap<String, String> treemap = new TreeMap<>(map);
			for (Map.Entry<String, String> entry : treemap.entrySet()) {
				query += entry.getKey() + "=" + entry.getValue() + "&";
			}
			query = query.substring(0, query.length() - 1);
		}
		return query;
	}

	public String getTimestamp(Context context, Date date) {
		DateTime utc = new DateTime(DateTimeZone.UTC);
		DateTimeZone tz = DateTimeZone.forID("Asia/Indonesia");
		LocalDateTime now = (utc.toDateTime(tz)).toLocalDateTime();
		LocalDateTime obj = new LocalDateTime(date);

		int diff = Minutes.minutesBetween(obj, now).getMinutes();
		if (diff < Constants.DateDifferent.JUST_NOW) {
			return Constants.DateDifferentText.JUST_NOW;
		} else if (diff <= Constants.DateDifferent.JUST_MINUTES) {
			return Constants.DateDifferentText.JUST_MINUTES;
		} else if (diff <= Constants.DateDifferent.FEW_MINUTES) {
			return String.format(Constants.DateDifferentText.FEW_MINUTES, diff);
		} else {
			diff = Hours.hoursBetween(obj, now).getHours();
			if (diff <= Constants.DateDifferent.FEW_HOURS) {
				return String.format(Constants.DateDifferentText.FEW_HOURS, diff);
			} else {
				diff = Days.daysBetween(obj, now).getDays();
				if (diff <= Constants.DateDifferent.FEW_DAYS) {
					return String.format(Constants.DateDifferentText.FEW_DAYS, diff);
				} else {
					return obj.toString(Constants.DateFormat.MONTH_DATE);
				}
			}
		}
	}

	public String decodeURL(String url) {
		try {
			return URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public String encodeURL(String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

}

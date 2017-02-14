package go.wikipedi.wikipedigo.util;

import android.Manifest;

/**
 * Created by Dimpos Sitorus on 6/20/2016.
 */
public class Constants {

	public static class Permissions {
		public static final String[] CAMERA = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
	}

	public static class DateFormat {
		public static final String DATE = "yyyy-MM-dd";
		public static final String DATETIME = "yyyyMMddHHmmss";
		public static final String FULL_SHORT = "EEE, d MMM yyyy";
		public static final String MONTH_DATE = "MMM d";
		public static final String MONTH_YEAR = "MMMM yyyy";
	}

	public static class TimeFormat {
		public static final String TIME = "HH:mm";
	}

	public static class DateDifferent {
		public static final int JUST_NOW = 1;
		public static final int JUST_MINUTES = 10;
		public static final int FEW_MINUTES = 59;
		public static final int FEW_HOURS = 23;
		public static final int FEW_DAYS = 3;
	}

	public static class DateDifferentText {
		public static final String JUST_NOW = "Just now";
		public static final String JUST_MINUTES = "A few minutes ago";
		public static final String FEW_MINUTES = "%d minutes ago";
		public static final String FEW_HOURS = "%d hours ago";
		public static final String FEW_DAYS = "%d days ago";
	}

	public static class ResponseError {
		public static String C400 = "Bad Request";
		public static String C401 = "Unauthorized";
		public static String C402 = "Payment Required";
		public static String C403 = "Forbidden";
		public static String C404 = "Not Found";
		public static String C405 = "Method Not Allowed";
		public static String C406 = "Not Acceptable";
		public static String C407 = "Proxy Authentication Required";
		public static String C408 = "Request Timeout";
		public static String C409 = "Conflict";
		public static String C410 = "Gone";
		public static String C411 = "Length Required";
		public static String C412 = "Precondition Failed";
		public static String C413 = "Request Entity Too Large";
		public static String C414 = "Request-URI Too Long";
		public static String C415 = "Unsupported Media Type";
		public static String C416 = "Requested Range Not Satisfiable";
		public static String C417 = "Expectation Failed";
		public static String DEFAULT = "Oops, Something went wrong";
	}
}

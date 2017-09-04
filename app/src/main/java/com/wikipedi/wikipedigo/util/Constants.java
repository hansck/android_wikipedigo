package com.wikipedi.wikipedigo.util;

/**
 * Created by Dimpos Sitorus on 6/20/2016.
 */
public class Constants {

	public static class General {
		public static String TAB_INDEX = "tabIndex";
		public static int TAB_TIMELINE = 0;
		public static int TAB_GALLERY = 1;
		public static int TAB_FAVORITE = 2;
	}

	public static class Photo {
		public static String PHOTO = "photo";
		public static String CREATED_AT = "createdAt";
		public static String NAME = "name";
		public static String FAV_COUNT = "favoriteCount";
		public static String PREFS = "prefs";
		public static String QUERY = "query";
		public static String LIST = "list";
		public static final int MAX_ITEM = 42;
	}

	public static class Sort {
		public static String SORT_BY = "sortBy";
		public static String SORT_METHOD = "sortMethod";
		public static String DATE = "date";
		public static String POPULARITY = "popularity";
		public static String ALPHABETIC = "alphabetic";
		public static String ASCENDING = "ascending";
		public static String DESCENDING = "descending";
	}

	public static class DateTimeFormat {
		public static final String FULL_SHORT = "EEE, d MMM yyyy";
	}

	public static class Favorite {
		public static final String FRESH_INSTALLED = "freshInstalled";
		public static final String LEFT_COUNT = "leftCount";
		public static final int DEFAULT_COUNT = 3;
	}

	public static class Ads {
		public static final int MAX_CLICK = 5;
	}
}
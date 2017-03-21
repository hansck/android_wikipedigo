package com.wikipedi.wikipedigo.util;

import com.wikipedi.wikipedigo.model.Photo;

import java.util.Comparator;

/**
 * Created by Hans CK on 17-Feb-17.
 */

public class Comparators {

	public static Comparator<Photo> dateLatest = new Comparator<Photo>() {
		@Override
		public int compare(Photo lhs, Photo rhs) {
			return rhs.getCreatedAt().compareTo(lhs.getCreatedAt());
		}
	};

	public static Comparator<Photo> dateOldest = new Comparator<Photo>() {
		@Override
		public int compare(Photo lhs, Photo rhs) {
			return lhs.getCreatedAt().compareTo(rhs.getCreatedAt());
		}
	};

	public static Comparator<Photo> mostPopular = new Comparator<Photo>() {
		@Override
		public int compare(Photo lhs, Photo rhs) {
			return rhs.getPopularity() - lhs.getPopularity();
		}
	};

	public static Comparator<Photo> lessPopular = new Comparator<Photo>() {
		@Override
		public int compare(Photo lhs, Photo rhs) {
			return lhs.getPopularity() - rhs.getPopularity();
		}
	};

	public static Comparator<Photo> alphabeticAscending = new Comparator<Photo>() {
		@Override
		public int compare(Photo lhs, Photo rhs) {
			return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
		}
	};

	public static Comparator<Photo> alphabeticDescending = new Comparator<Photo>() {
		@Override
		public int compare(Photo lhs, Photo rhs) {
			return rhs.getName().toLowerCase().compareTo(lhs.getName().toLowerCase());
		}
	};
}

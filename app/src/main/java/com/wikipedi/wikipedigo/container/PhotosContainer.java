package com.wikipedi.wikipedigo.container;

import com.wikipedi.wikipedigo.api.APIRequest;
import com.wikipedi.wikipedigo.api.BaseResponse;
import com.wikipedi.wikipedigo.model.Favorite;
import com.wikipedi.wikipedigo.model.HiddenIgo;
import com.wikipedi.wikipedigo.model.Photo;
import com.wikipedi.wikipedigo.model.UserPreferences;
import com.wikipedi.wikipedigo.util.BaseRunnable;
import com.wikipedi.wikipedigo.util.Comparators;
import com.wikipedi.wikipedigo.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static io.realm.Case.INSENSITIVE;

/**
 * Created by E460 on 17/01/2017.
 */

public class PhotosContainer {

	private RealmList<Photo> photos = new RealmList<>();
	private RealmList<Photo> favorites = new RealmList<>();
	private RealmList<HiddenIgo> hiddenIgos = new RealmList<>();
	private Realm realm = Realm.getDefaultInstance();
	private static PhotosContainer instance = new PhotosContainer();

	public PhotosContainer() {

	}

	public static PhotosContainer getInstance() {
		return instance;
	}

	public void fetchPhotos(final Runnable onSuccess, final Runnable onFailure) {
		Map<String, String> queries = new HashMap<>();
		APIRequest.getInstance().getService().getPhotos(queries).enqueue(new Callback<BaseResponse<List<Photo>>>() {
			@Override
			public void onResponse(Call<BaseResponse<List<Photo>>> call, Response<BaseResponse<List<Photo>>> response) {
				List<Photo> fetchedPhotos = response.body().getValue();
				if (fetchedPhotos != null && fetchedPhotos.size() > 0) {
					photos = new RealmList<>(fetchedPhotos.toArray(new Photo[fetchedPhotos.size()]));
					if (realm.where(Photo.class).count() != photos.size()) {
						insertIgo();
						getAllIgo();
					}
				}
				onSuccess.run();
			}

			@Override
			public void onFailure(Call<BaseResponse<List<Photo>>> call, Throwable t) {
				t.printStackTrace();
				getAllIgo();
				onFailure.run();
			}
		});
	}

	public void updatePhotos(final Runnable onSuccess, final Runnable onFailure) {
		Map<String, String> queries = new HashMap<>();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		String fromQuery = year + "-" + month + "-" + date;
		queries.put("from", fromQuery);
		APIRequest.getInstance().getService().getPhotos(queries).enqueue(new Callback<BaseResponse<List<Photo>>>() {
			@Override
			public void onResponse(Call<BaseResponse<List<Photo>>> call, Response<BaseResponse<List<Photo>>> response) {
				List<Photo> fetchedPhotos = response.body().getValue();
				if (fetchedPhotos != null && fetchedPhotos.size() > 0) {
					photos = new RealmList<>(fetchedPhotos.toArray(new Photo[fetchedPhotos.size()]));
					insertIgo();
				}
				onSuccess.run();
			}

			@Override
			public void onFailure(Call<BaseResponse<List<Photo>>> call, Throwable t) {
				t.printStackTrace();
				onFailure.run();
			}
		});
	}

	public void fetchHiddenIgo() {
		APIRequest.getInstance().getService().getHiddenIgo().enqueue(new Callback<BaseResponse<List<HiddenIgo>>>() {
			@Override
			public void onResponse(Call<BaseResponse<List<HiddenIgo>>> call, Response<BaseResponse<List<HiddenIgo>>> response) {
				List<HiddenIgo> values = response.body().getValue();
				if (values != null && values.size() > 0) {
					hiddenIgos = new RealmList<>(values.toArray(new HiddenIgo[values.size()]));
					if (realm.where(Photo.class).count() != photos.size()) {
						insertHiddenIgo();
						getAllHiddenIgo();
						getFavoriteIgo();
					}
				}
			}

			@Override
			public void onFailure(Call<BaseResponse<List<HiddenIgo>>> call, Throwable t) {
				t.printStackTrace();
				getAllHiddenIgo();
			}
		});
	}

	private void insertIgo() {
		realm.beginTransaction();
		realm.copyToRealmOrUpdate(photos);
		realm.commitTransaction();
	}

	private void insertHiddenIgo() {
		realm.beginTransaction();
		realm.copyToRealmOrUpdate(hiddenIgos);
		realm.commitTransaction();
	}

	public void getAllIgo() {
		photos.clear();
		RealmResults<Photo> results = realm.where(Photo.class)
			.findAllSorted(Constants.Photo.CREATED_AT, Sort.DESCENDING).distinct(Constants.Photo.NAME);
//		photos.addAll(getDistinctPhotos(results));
		photos.addAll(results);
//		sortIgo();
	}

	public void getAllHiddenIgo() {
		hiddenIgos.clear();
		RealmResults<HiddenIgo> results = realm.where(HiddenIgo.class).findAll();
		hiddenIgos.addAll(results);
	}

	public RealmList<Photo> getSameIgo(String name) {
		RealmList<Photo> galleryPhotos = new RealmList<>();
		RealmResults<Photo> results = realm.where(Photo.class).equalTo(Constants.Photo.NAME, name)
			.findAllSorted(Constants.Photo.CREATED_AT, Sort.DESCENDING);
		galleryPhotos.addAll(results.subList(0, results.size()));
		return galleryPhotos;
	}

	public void getFavoriteIgo() {
		favorites.clear();
		RealmResults<Favorite> results = realm.where(Favorite.class).findAll();
		if (results.size() > 0) {
			RealmQuery<Photo> query = realm.where(Photo.class);
			int i = 0;
			for (Favorite fav : results) {
				if (i != 0) {
					query = query.or();
				}
				query = query.equalTo(Constants.Photo.NAME, fav.getName());
				i++;
			}
			RealmResults<Photo> favResults = query.findAllSorted(Constants.Photo.CREATED_AT, Sort.DESCENDING).distinct(Constants.Photo.NAME);
			favorites.addAll(favResults);
		}
	}

	public void updateIgo(Photo photo, boolean isFavorite) {
		realm.beginTransaction();
		if (isFavorite) {
			Favorite favorite = new Favorite(photo.getName());
			realm.copyToRealmOrUpdate(favorite);
		} else {
			RealmResults<Favorite> result = realm.where(Favorite.class).equalTo(Constants.Photo.NAME, photo.getName()).findAll();
			result.deleteAllFromRealm();
		}
		realm.commitTransaction();
	}

	public boolean checkIfFav(String query) {
		RealmResults<Favorite> results = realm.where(Favorite.class).equalTo(Constants.Photo.NAME, query).findAll();
		return results.size() != 0;
	}

	public void searchAllIgo(String search, BaseRunnable<RealmList<Photo>> onFound) {
		RealmResults<Photo> results = realm.where(Photo.class).contains(Constants.Photo.NAME, search, INSENSITIVE)
			.findAllSorted(Constants.Photo.CREATED_AT, Sort.DESCENDING).distinct(Constants.Photo.NAME);
		RealmList<Photo> result = new RealmList<>();
		result.addAll(results);
		onFound.run(result);
	}

	public void searchFavoriteIgo(String search, BaseRunnable<RealmList<Photo>> onFound) {
		RealmResults<Favorite> results = realm.where(Favorite.class).contains(Constants.Photo.NAME, search, Case.INSENSITIVE).findAll();
		RealmList<Photo> result = new RealmList<>();
		if (results.size() > 0) {
			RealmQuery<Photo> query = realm.where(Photo.class);
			int i = 0;
			for (Favorite fav : results) {
				if (i != 0) {
					query = query.or();
				}
				query = query.equalTo(Constants.Photo.NAME, fav.getName());
				i++;
			}
			RealmResults<Photo> favResults = query.findAllSorted(Constants.Photo.CREATED_AT, Sort.DESCENDING).distinct(Constants.Photo.NAME);
			result.addAll(favResults);
		}
		onFound.run(result);
	}

	private int getTopPopularity(String name) {
		RealmResults<Photo> results = realm.where(Photo.class).equalTo(Constants.Photo.NAME, name)
			.findAllSorted(Constants.Photo.CREATED_AT, Sort.DESCENDING);
		return results.max(Constants.Photo.FAV_COUNT).intValue();
	}

	private void setPopularity(Photo p) {
		int popularity = getTopPopularity(p.getName());
		if (popularity > 0) {
			realm.beginTransaction();
			p.setPopularity(getTopPopularity(p.getName()));
			realm.commitTransaction();
		}
	}

	public RealmList<Photo> getPhotos() {
		return photos;
	}

	public RealmList<Photo> getFavorites() {
		return favorites;
	}

	public RealmList<HiddenIgo> getHiddenIgos() {
		return hiddenIgos;
	}

	public void sortIgo() {
		switch (UserPreferences.getInstance().getSortBy()) {
			case "date":
				if (UserPreferences.getInstance().getSortMethod().equals(Constants.Sort.ASCENDING)) {
					Collections.sort(photos, Comparators.dateLatest);
				} else {
					Collections.sort(photos, Comparators.dateOldest);
				}
				break;
			case "popularity":
				if (UserPreferences.getInstance().getSortMethod().equals(Constants.Sort.ASCENDING)) {
					Collections.sort(photos, Comparators.mostPopular);
				} else {
					Collections.sort(photos, Comparators.lessPopular);
				}
				break;
			case "alphabetic":
				if (UserPreferences.getInstance().getSortMethod().equals(Constants.Sort.ASCENDING)) {
					Collections.sort(photos, Comparators.alphabeticAscending);
				} else {
					Collections.sort(photos, Comparators.alphabeticDescending);
				}
				break;
		}
	}
}
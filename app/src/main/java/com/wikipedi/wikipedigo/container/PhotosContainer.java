package com.wikipedi.wikipedigo.container;

import java.util.ArrayList;
import java.util.List;

import com.wikipedi.wikipedigo.api.APIRequest;
import com.wikipedi.wikipedigo.model.Photo;
import com.wikipedi.wikipedigo.util.BaseRunnable;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by E460 on 17/01/2017.
 */

public class PhotosContainer {

	private RealmList<Photo> photos = new RealmList<>();
	private RealmList<Photo> favorites = new RealmList<>();
	private Realm realm = Realm.getDefaultInstance();
	private static PhotosContainer instance = new PhotosContainer();

	public PhotosContainer() {

	}

	public static PhotosContainer getInstance() {
		return instance;
	}

	public void fetchPhotos(final Runnable onSuccess, final Runnable onFailure) {
		APIRequest.getInstance().getService().getPhotos().enqueue(new Callback<List<Photo>>() {
			@Override
			public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
				photos = new RealmList<>(response.body().toArray(new Photo[response.body().size()]));
				if (realm.where(Photo.class).count() != photos.size()) {
					insertIgo();
					getAllIgo();
				}
				onSuccess.run();
			}

			@Override
			public void onFailure(Call<List<Photo>> call, Throwable t) {
				t.printStackTrace();
				getAllIgo();
				onFailure.run();
			}
		});
	}

	public void updatePhotos(final Runnable onSuccess, final Runnable onFailure) {
		APIRequest.getInstance().getService().getPhotos().enqueue(new Callback<List<Photo>>() {
			@Override
			public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
				photos = new RealmList<>(response.body().toArray(new Photo[response.body().size()]));
				if (realm.where(Photo.class).count() != photos.size()) {
					insertIgo();}
				onSuccess.run();
			}

			@Override
			public void onFailure(Call<List<Photo>> call, Throwable t) {
				t.printStackTrace();
				onFailure.run();
			}
		});
	}

	private void insertIgo() {
		realm.executeTransaction(new Realm.Transaction() {
			@Override
			public void execute(Realm bgRealm) {
				for (Photo photo : photos) {
					if (realm.where(Photo.class).equalTo("id", photo.getId()).count() == 0) {
						realm.copyToRealmOrUpdate(photos);
					} else {
						break;
					}
				}
			}
		});
	}

	public void getAllIgo() {
		photos.clear();
		RealmResults<Photo> results = realm.where(Photo.class).findAllSorted("createdAt", Sort.DESCENDING);
		photos.addAll(getDistinctPhotos(results));
	}

	public RealmList<Photo> getSameIgo(String name) {
		RealmList<Photo> galleryPhotos = new RealmList<>();
		RealmResults<Photo> results = realm.where(Photo.class).equalTo("name", name).findAllSorted("createdAt", Sort.DESCENDING);
		galleryPhotos.addAll(results.subList(0, results.size()));
		return galleryPhotos;
	}

	public void getFavoriteIgo() {
		favorites.clear();
		RealmResults<Photo> results = realm.where(Photo.class).equalTo("isFavorite", true).findAllSorted("createdAt", Sort.DESCENDING);
		favorites.addAll(getDistinctPhotos(results));
	}

	public void updateIgo(Photo photo, boolean isFavorite) {
		realm.beginTransaction();
		photo.setFavorite(isFavorite);
		realm.copyToRealmOrUpdate(photo);
		realm.commitTransaction();
	}

	public boolean checkIfFav(String query) {
		RealmResults<Photo> results = realm.where(Photo.class).equalTo("id", query).findAll();
		return results.get(0).isFavorite();
	}

	private void clearData() {
		realm.beginTransaction();
		realm.delete(Photo.class);
		realm.commitTransaction();
	}

	public void searchAllIgo(String query, BaseRunnable<RealmList<Photo>> onFound) {
		RealmResults<Photo> results = realm.where(Photo.class).contains("name", query, Case.INSENSITIVE).findAllSorted("createdAt", Sort.DESCENDING);
		RealmList<Photo> result = new RealmList<>();
		result.addAll(getDistinctPhotos(results));
		onFound.run(result);
	}

	public void searchFavoriteIgo(String query, BaseRunnable<RealmList<Photo>> onFound) {
		RealmResults<Photo> results = realm.where(Photo.class).contains("name", query, Case.INSENSITIVE).equalTo("isFavorite", true).findAllSorted("createdAt", Sort.DESCENDING);
		RealmList<Photo> result = new RealmList<>();
		result.addAll(getDistinctPhotos(results));
		onFound.run(result);
	}

	public RealmList<Photo> getPhotos() {
		return photos;
	}

	public RealmList<Photo> getFavorites() {
		return favorites;
	}

	private ArrayList<Photo> getDistinctPhotos(RealmResults<Photo> results) {
		ArrayList<Photo> distinctPhotos = new ArrayList<>();
		for (Photo p : realm.copyFromRealm(results)) {
			boolean exist = false;
			for (Photo distinct : distinctPhotos) {
				if (p.getName().equals(distinct.getName())) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				distinctPhotos.add(p);
			}
		}
		return distinctPhotos;
	}
}
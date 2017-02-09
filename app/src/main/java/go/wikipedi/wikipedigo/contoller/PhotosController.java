package go.wikipedi.wikipedigo.contoller;

import java.util.List;

import go.wikipedi.base.BaseRunnable;
import go.wikipedi.base.api.APIRequest;
import go.wikipedi.wikipedigo.model.Photo;
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

public class PhotosController {

	private RealmList<Photo> photos = new RealmList<>();
	private Realm realm = Realm.getDefaultInstance();
	private static PhotosController instance = new PhotosController();

	private PhotosController() {

	}

	public static PhotosController getInstance() {
		return instance;
	}

	public void fetchPhotos(final Runnable onSuccess, final Runnable onFailure) {
		APIRequest.getInstance().getService().getPhotos().enqueue(new Callback<List<Photo>>() {
			@Override
			public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
				photos = new RealmList<>(response.body().toArray(new Photo[response.body().size()]));
				if (realm.where(Photo.class).count() != photos.size()) {
					insertData();
				}
				onSuccess.run();
			}

			@Override
			public void onFailure(Call<List<Photo>> call, Throwable t) {
				t.printStackTrace();
				getData();
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
					insertData();
				}
				onSuccess.run();
			}

			@Override
			public void onFailure(Call<List<Photo>> call, Throwable t) {
				t.printStackTrace();
				onFailure.run();
			}
		});
	}

	private void insertData() {
		realm.executeTransaction(new Realm.Transaction() {
			@Override
			public void execute(Realm bgRealm) {
				for (Photo photo : photos) {
					if (realm.where(Photo.class).equalTo("image", photo.getImage()).count() == 0) {
						realm.copyToRealmOrUpdate(photos);
					} else {
						break;
					}
				}
			}
		});
	}

	public void getData() {
		RealmResults<Photo> results = realm.where(Photo.class).findAllSorted("createdAt", Sort.DESCENDING);
		photos.addAll(results.subList(0, results.size()));
	}

	private void clearData() {
		realm.beginTransaction();
		realm.delete(Photo.class);
		realm.commitTransaction();
	}

	public void searchPhotos(String query, BaseRunnable<RealmList<Photo>> onFound) {
		RealmResults<Photo> realmResults = realm.where(Photo.class).contains("name", query, Case.INSENSITIVE).findAllSorted("name", Sort.ASCENDING);
		RealmList<Photo> result = new RealmList<>();
		result.addAll(realmResults.subList(0, realmResults.size()));
		onFound.run(result);
	}

	public RealmList<Photo> getPhotos() {
		return photos;
	}
}

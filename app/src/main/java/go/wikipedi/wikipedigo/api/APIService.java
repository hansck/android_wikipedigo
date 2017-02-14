package go.wikipedi.wikipedigo.api;

import java.util.List;

import go.wikipedi.wikipedigo.model.Photo;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by E460 on 12/01/2017.
 * This class can be edited to adjust with your apps
 */

public interface APIService {

	@GET("vn4cb")
	Call<List<Photo>> getPhotos();
}

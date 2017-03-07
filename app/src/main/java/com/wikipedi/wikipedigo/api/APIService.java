package com.wikipedi.wikipedigo.api;

import java.util.List;

import com.wikipedi.wikipedigo.model.HiddenIgo;
import com.wikipedi.wikipedigo.model.Photo;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by E460 on 12/01/2017.
 * This class can be edited to adjust with your apps
 */

public interface APIService {

	@GET("vn4cb")
	Call<List<Photo>> getPhotos();

	@GET("ruai1")
	Call<List<HiddenIgo>> getHiddenIgo();
}
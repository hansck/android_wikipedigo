package com.wikipedi.wikipedigo.api;

import android.util.SparseArray;

import java.util.List;
import java.util.Map;

import com.wikipedi.wikipedigo.model.HiddenIgo;
import com.wikipedi.wikipedigo.model.Photo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by E460 on 12/01/2017.
 * This class can be edited to adjust with your apps
 */

public interface APIService {

	@GET("wikipedigo")
	Call<BaseResponse<List<Photo>>> getPhotos(@QueryMap Map<String, String> queries);

	@GET("wikipedigo/hidden")
	Call<BaseResponse<List<HiddenIgo>>> getHiddenIgo();
}
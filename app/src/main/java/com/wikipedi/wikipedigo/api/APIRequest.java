package com.wikipedi.wikipedigo.api;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wikipedi.wikipedigo.model.deserializer.DateDeserializer;
import com.wikipedi.wikipedigo.util.CacheInterceptor;

import java.security.cert.CertificateException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by E460 on 12/01/2017.
 */

public class APIRequest {

	private static APIRequest instance = new APIRequest();

	CacheInterceptor interceptor = new CacheInterceptor();
	static SharedPreferences keyStore;

	private Gson gson = new GsonBuilder().
			registerTypeAdapter(Date.class, new DateDeserializer()).
			excludeFieldsWithoutExposeAnnotation().
			create();

	private OkHttpClient cacheClient = new OkHttpClient.Builder().
		addNetworkInterceptor(interceptor).connectTimeout(10, TimeUnit.SECONDS).build();

	private Retrofit retrofit = new Retrofit.Builder().client(cacheClient).baseUrl(APIKey.BASE_URL_DEV).
			addConverterFactory(GsonConverterFactory.create(gson)).build();

	APIService service = retrofit.create(APIService.class);

	APIRequest() {
		// Empty constructor, this is singleton
	}

	public static APIRequest getInstance() {
		return instance;
	}

	public void setKeyStore(SharedPreferences _keyStore) {
		keyStore = _keyStore;
	}

	public Retrofit getRetrofit() {
		return retrofit;
	}

	public Gson getGson() {
		return gson;
	}

	String getUserId() {
		return keyStore.getString("userID", "");
	}

	String getToken() {
		return "bearer " + keyStore.getString("token", "");
	}

	public void saveCache(String key, String value) {
		keyStore.edit().putString(key, value).apply();
	}

	public APIService getService() {
		return service;
	}
}

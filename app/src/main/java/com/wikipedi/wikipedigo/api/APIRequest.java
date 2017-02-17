package com.wikipedi.wikipedigo.api;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

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

import com.wikipedi.wikipedigo.model.deserializer.DateDeserializer;
import com.wikipedi.wikipedigo.model.deserializer.LocalDateDeserializer;
import com.wikipedi.wikipedigo.model.deserializer.LocalTimeDeserializer;
import com.wikipedi.wikipedigo.util.CacheInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by E460 on 12/01/2017.
 */

public class APIRequest {

	private static APIRequest instance = new APIRequest();

	CacheInterceptor interceptor;
	static SharedPreferences keyStore;

	private Gson gson = new GsonBuilder().
			registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer()).
			registerTypeAdapter(LocalDate.class, new LocalDateDeserializer()).
			registerTypeAdapter(Date.class, new DateDeserializer()).
			excludeFieldsWithoutExposeAnnotation().
			create();

	private OkHttpClient getUnsafeOkHttpClient() {
		try {
			final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws
						CertificateException {
				}

				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws
						CertificateException {
				}

				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new java.security.cert.X509Certificate[]{};
				}
			}};

			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			});

			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.sslSocketFactory(sslSocketFactory);
			builder.hostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			interceptor = new CacheInterceptor();
			return builder.addNetworkInterceptor(interceptor).connectTimeout(5, TimeUnit.SECONDS).build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Retrofit retrofit = new Retrofit.Builder().client(getUnsafeOkHttpClient()).baseUrl(APIKey.BASE_URL_DEV).
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

package com.wikipedi.wikipedigo.application;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.wikipedi.wikipedigo.R;
import com.wikipedi.wikipedigo.util.ConnectivityReceiver;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

/**
 * Created by E460 on 12/01/2017.
 */

public class BaseApplication extends Application {

	private static Context appContext;
	private static BaseApplication mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		appContext = getApplicationContext();
		mInstance = this;

		// Initialize Realm
		Realm.init(this);

		// Set Stetho Monitoring
//		Stetho.initialize(Stetho.newInitializerBuilder(this)
//				.enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
//				.enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
//				.build());

		//Set Twitter Fabric
		TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.twitter_consumer_key),
				getString(R.string.twitter_consumer_secret));
		Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());
		Fabric.with(this, new Crashlytics());
	}

	public static synchronized BaseApplication getInstance() {
		return mInstance;
	}

	public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
		ConnectivityReceiver.connectivityReceiverListener = listener;
	}

	public static Context getAppContext() {
		return appContext;
	}
}

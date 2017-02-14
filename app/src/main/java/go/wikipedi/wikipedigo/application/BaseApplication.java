package go.wikipedi.wikipedigo.application;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by E460 on 12/01/2017.
 */

public class BaseApplication extends Application {

	private static Context appContext;

	@Override
	public void onCreate() {
		super.onCreate();
		appContext = getApplicationContext();

		// Initialize Realm
		Realm.init(this);

		// Set Stetho Monitoring
		Stetho.initialize(Stetho.newInitializerBuilder(this)
				.enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
				.enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
				.build());
	}

	public static Context getAppContext() {
		return appContext;
	}
}

package com.wikipedi.wikipedigo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.wikipedi.wikipedigo.R;
import com.wikipedi.wikipedigo.application.BaseApplication;
import com.wikipedi.wikipedigo.fragment.TabFragment_;
import com.wikipedi.wikipedigo.model.UserPreferences;
import com.wikipedi.wikipedigo.util.ConnectivityReceiver;
import com.wikipedi.wikipedigo.util.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

	private FragmentManager fm;

	//region Listener
	@AfterViews
	void initViews() {
		checkConnection();
		SharedPreferences pref = getSharedPreferences(Constants.Photo.PREFS, Context.MODE_PRIVATE);
		UserPreferences.getInstance().setKeyStore(pref);

		fm = getSupportFragmentManager();
		Fragment fragment = new TabFragment_();
		navigateTo(fragment);
	}
	//endregion

	//region Override methods
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			super.onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		BaseApplication.getInstance().setConnectivityListener(this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onNetworkConnectionChanged(boolean isConnected) {
		if (!isConnected) {
			Intent intent = new Intent(this, NoInternetActivity_.class);
			startActivity(intent);
			finish();
		}
	}
	//endregion

	//region Private methods
	private void navigateTo(Fragment fragment) {
		if (!isFinishing()) {
			FragmentTransaction ft = fm.beginTransaction();
			fragment.setArguments(new Bundle());
			ft.replace(R.id.fragmentContainer, fragment);
			ft.commit();
		}
	}

	private void checkConnection() {
		boolean isConnected = ConnectivityReceiver.isConnected();
		if (!isConnected) {
			Intent intent = new Intent(this, NoInternetActivity_.class);
			startActivity(intent);
			finish();
		}
	}
	//endregion
}
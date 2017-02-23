package com.wikipedi.wikipedigo.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wikipedi.wikipedigo.R;
import com.wikipedi.wikipedigo.util.ConnectivityReceiver;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_no_internet)
public class NoInternetActivity extends AppCompatActivity {

	@AfterViews
	void initViews() {

	}

	@Click(R.id.btnRetry)
	void onRetry() {
		boolean isConnected = ConnectivityReceiver.isConnected();
		if (isConnected) {
			Intent intent = new Intent(this, MainActivity_.class);
			startActivity(intent);
			finish();
		} else {
			View view = this.getWindow().getDecorView().findViewById(R.id.content);
			Snackbar.make(view, "Sorry! Not connected to internet", Snackbar.LENGTH_LONG).show();
		}
	}
}
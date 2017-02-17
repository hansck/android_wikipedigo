package com.wikipedi.wikipedigo.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.viewpagerindicator.CirclePageIndicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.wikipedi.wikipedigo.R;
import com.wikipedi.wikipedigo.adapter.AlbumAdapter;
import com.wikipedi.wikipedigo.adapter.PhotoSlideAdapter;
import com.wikipedi.wikipedigo.container.PhotosContainer;
import com.wikipedi.wikipedigo.model.Photo;
import com.wikipedi.wikipedigo.model.PhotoClickContainer;
import com.wikipedi.wikipedigo.util.OnListItemSelected;
import io.realm.RealmList;

@EActivity(R.layout.activity_photo_detail)
public class PhotoDetailActivity extends AppCompatActivity {

	private AlbumAdapter adapter;
	private int photoIdx = 0;
	private Photo photo;
	private RealmList<Photo> galleryPhotos = new RealmList<>();
	private Menu menu;

	@ViewById
	ViewPager currentPhoto;
	@ViewById
	RecyclerView imageList;
	@ViewById
	RelativeLayout listContainer;
	@ViewById
	CirclePageIndicator index;

	@AfterViews
	void initViews() {
		if (PhotoClickContainer.getInstance().checkClicks()) {
			showAds();
		}
		Bundle data = getIntent().getExtras();
		if (data != null) {
			photo = data.getParcelable("photo");
		}
		setTitle(photo.getName());
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		galleryPhotos = PhotosContainer.getInstance().getSameIgo(photo.getName());
		adapter = new AlbumAdapter(this, galleryPhotos, new OnListItemSelected() {
			@Override
			public void onClick(int position) {
				photoIdx = position;
				showPhoto(photoIdx);
			}
		});

		LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		imageList.setLayoutManager(lm);
		imageList.setAdapter(adapter);
		currentPhoto.setAdapter(new PhotoSlideAdapter(this, galleryPhotos, R.layout.item_photo_gallery));
		currentPhoto.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				imageList.scrollToPosition(position);
				photoIdx = position;
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		index.setViewPager(currentPhoto);
		if (galleryPhotos.size() == 1) {
			listContainer.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		getMenuInflater().inflate(R.menu.favorite_bar, menu);
		if (PhotosContainer.getInstance().checkIfFav(photo.getId())) {
			menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.favorite_click));
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_favorite) {
			toggleFavorite();
		} else {
			onBackPressed();
		}
		return true;
	}

	//region Private methods
	private void showPhoto(int idx) {
		imageList.scrollToPosition(idx);
		currentPhoto.setCurrentItem(idx, true);
	}

	private void showAds() {
		final InterstitialAd mInterstitialAd = new InterstitialAd(this);
		mInterstitialAd.setAdUnitId(getString(R.string.list_interstitial));
		AdRequest adRequest = new AdRequest.Builder().build();
		mInterstitialAd.loadAd(adRequest);
		mInterstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				if (mInterstitialAd.isLoaded()) {
					mInterstitialAd.show();
				}
			}
		});
	}

	private void toggleFavorite() {
		boolean isFavorite = !PhotosContainer.getInstance().checkIfFav(photo.getId());
		for (Photo otherPhoto : galleryPhotos) {
			PhotosContainer.getInstance().updateIgo(otherPhoto, isFavorite);
			if (isFavorite) {
				menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.favorite_click));
			} else {
				menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.favorite_border));
			}
		}
		PhotosContainer.getInstance().getFavoriteIgo();
	}
	//endregion
}
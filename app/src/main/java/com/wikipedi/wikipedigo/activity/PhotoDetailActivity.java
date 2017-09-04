package com.wikipedi.wikipedigo.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.viewpagerindicator.CirclePageIndicator;
import com.wikipedi.wikipedigo.R;
import com.wikipedi.wikipedigo.adapter.AlbumAdapter;
import com.wikipedi.wikipedigo.adapter.PhotoSlideAdapter;
import com.wikipedi.wikipedigo.fragment.ShareDialog;
import com.wikipedi.wikipedigo.model.manager.AdsManager;
import com.wikipedi.wikipedigo.model.manager.PhotoClickManager;
import com.wikipedi.wikipedigo.model.manager.PhotosManager;
import com.wikipedi.wikipedigo.model.object.Photo;
import com.wikipedi.wikipedigo.model.object.UserPreferences;
import com.wikipedi.wikipedigo.util.Common;
import com.wikipedi.wikipedigo.util.Constants;
import com.wikipedi.wikipedigo.util.OnListItemSelected;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.realm.RealmList;

@EActivity(R.layout.activity_photo_detail)
public class PhotoDetailActivity extends AppCompatActivity {

	private AlbumAdapter adapter;
	private PhotoSlideAdapter slideAdapter;
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
		// Initiate Advertisings
		if (PhotoClickManager.getInstance().checkClicks()) {
			AdsManager.getInstance().loadInterstitialAds();
		}

		Bundle data = getIntent().getExtras();
		if (data != null) {
			photo = data.getParcelable(Constants.Photo.PHOTO);
		}
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(photo.getName());

		galleryPhotos = PhotosManager.getInstance().getSameIgo(photo.getName());
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
		slideAdapter = new PhotoSlideAdapter(this, galleryPhotos, R.layout.item_photo_preview);
		currentPhoto.setAdapter(slideAdapter);
		currentPhoto.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				photoIdx = position;
				imageList.scrollToPosition(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		index.setViewPager(currentPhoto);
	}

	//region Override methods
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		getMenuInflater().inflate(R.menu.menu_photo_detail, menu);
		if (PhotosManager.getInstance().checkIfFav(photo.getName())) {
			menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.favorite_click));
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		} else {
			switch (item.getItemId()) {
				case R.id.action_favorite:
					checkFavoriteCount();
					break;
				case R.id.action_share:
					showShareDialog();
					break;
				default:
					onBackPressed();
			}
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.enter_left, R.anim.exit_right);
	}
	//endregion

	//region Private methods
	private void showShareDialog() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		ShareDialog dialog = new ShareDialog();
		Bundle data = new Bundle();
		data.putString("name", photo.getName());
		data.putString("imageUrl", photo.getImage());
		dialog.setArguments(data);
		dialog.show(fragmentManager, "Share");
	}

	private void checkFavoriteCount() {
		boolean isFavorite = !PhotosManager.getInstance().checkIfFav(photo.getName());
		if (isFavorite) {
			if (UserPreferences.getInstance().getFavoriteCount() > 0) {
				toggleFavorite(isFavorite);
				UserPreferences.getInstance().reduceFavoriteCount();
			} else {
				showWatchAdsDialog();
			}
		} else {
			toggleFavorite(isFavorite);
		}
	}

	private void toggleFavorite(boolean isFavorite) {
		for (Photo otherPhoto : galleryPhotos) {
			PhotosManager.getInstance().updateIgo(otherPhoto, isFavorite);
			if (isFavorite) {
				menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.favorite_click));
			} else {
				menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.favorite_border));
			}
		}
		PhotosManager.getInstance().getFavoriteIgo();
	}

	public void showWatchAdsDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.favorite_run_out))
			.setCancelable(true)
			.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					boolean success = AdsManager.getInstance().showRewarAds();
					if (!success) Common.getInstance().showAlert(PhotoDetailActivity.this, getString(R.string.no_ads));
				}
			})
			.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					dialogInterface.dismiss();
				}
			})
			.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					dialog.dismiss();
				}
			})
			.create().show();
	}

	private void showPhoto(int idx) {
		imageList.scrollToPosition(idx);
		currentPhoto.setCurrentItem(idx, true);
	}
	//endregion
}
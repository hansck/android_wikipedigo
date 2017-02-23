package com.wikipedi.wikipedigo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.viewpagerindicator.CirclePageIndicator;
import com.wikipedi.wikipedigo.R;
import com.wikipedi.wikipedigo.adapter.AlbumAdapter;
import com.wikipedi.wikipedigo.adapter.PhotoSlideAdapter;
import com.wikipedi.wikipedigo.container.PhotosContainer;
import com.wikipedi.wikipedigo.model.Photo;
import com.wikipedi.wikipedigo.model.PhotoClickContainer;
import com.wikipedi.wikipedigo.util.Constants;
import com.wikipedi.wikipedigo.util.OnListItemSelected;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
	ImageView dummyImage;
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
			photo = data.getParcelable(Constants.Photo.PHOTO);
		}
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(photo.getName());
		setDummyImage(photo);

		galleryPhotos = PhotosContainer.getInstance().getSameIgo(photo.getName());
		adapter = new AlbumAdapter(this, galleryPhotos, new OnListItemSelected() {
			@Override
			public void onClick(int position) {
				photoIdx = position;
				showPhoto(photoIdx);
				setDummyImage(galleryPhotos.get(position));
			}
		});

		LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		imageList.setLayoutManager(lm);
		imageList.setAdapter(adapter);
		slideAdapter = new PhotoSlideAdapter(this, galleryPhotos, R.layout.item_photo_gallery);
		currentPhoto.setAdapter(slideAdapter);
		currentPhoto.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				photoIdx = position;
				imageList.scrollToPosition(position);
				setDummyImage(galleryPhotos.get(position));
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
		getMenuInflater().inflate(R.menu.menu_photo_detail, menu);
		if (PhotosContainer.getInstance().checkIfFav(photo.getId())) {
			menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.favorite_click));
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_favorite:
				toggleFavorite();
				break;
			case R.id.action_facebook:
				shareToFacebook();
				break;
			case R.id.action_twitter:
				shareToTwitter();
				break;
			case R.id.action_others:
				shareToOtherApps();
				break;
			default:
				onBackPressed();
		}
		return true;
	}

	//region Private methods
	public Uri getLocalBitmapUri(ImageView view) {
		Bitmap bmp = ((GlideBitmapDrawable) view.getDrawable()).getBitmap();
		Uri bmpUri = null;
		try {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
				File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".jpeg");
				FileOutputStream out = new FileOutputStream(file);
				bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.close();
				bmpUri = Uri.fromFile(file);
			} else {
				File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
				bmpUri = FileProvider.getUriForFile(PhotoDetailActivity.this, "com.codepath.fileprovider", file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmpUri;
	}

	private void shareToOtherApps() {
		Uri uri = getLocalBitmapUri(dummyImage);
		Intent shareIntent = new Intent();
		shareIntent.setType("image/*");
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.shared_notice));
		shareIntent.putExtra(Intent.EXTRA_TEXT, photo.getName());
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		startActivity(Intent.createChooser(shareIntent, "Share Image"));
	}

	private void shareToTwitter() {
		Uri uri = getLocalBitmapUri(dummyImage);
		TweetComposer.Builder builder = new TweetComposer.Builder(this)
				.text(getString(R.string.shared_notice) + "\n" + photo.getName())
				.image(uri);
		builder.show();
	}

	private void shareToFacebook() {
		FacebookSdk.sdkInitialize(getApplicationContext());
		ShareLinkContent linkContent = new ShareLinkContent.Builder()
				.setContentTitle("IGO Mania")
				.setContentDescription(getString(R.string.shared_notice) + "\n" + photo.getName())
				.setImageUrl(Uri.parse(galleryPhotos.get(photoIdx).getImage()))
				.build();
		ShareDialog.show(PhotoDetailActivity.this, linkContent);
	}

	private void showPhoto(int idx) {
		imageList.scrollToPosition(idx);
		currentPhoto.setCurrentItem(idx, true);
	}

	private void setDummyImage(Photo temp) {
		Glide.with(this)
				.load(temp.getImage())
				.animate(R.anim.grow_from_middle)
				.placeholder(R.drawable.ic_face)
				.into(dummyImage);
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
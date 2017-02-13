package go.wikipedi.wikipedigo.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.viewpagerindicator.CirclePageIndicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import go.wikipedi.base.OnListItemSelected;
import go.wikipedi.wikipedigo.R;
import go.wikipedi.wikipedigo.adapter.AlbumAdapter;
import go.wikipedi.wikipedigo.adapter.PhotoSlideAdapter;
import go.wikipedi.wikipedigo.model.Photo;
import go.wikipedi.wikipedigo.model.PhotoClickContainer;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import static android.R.attr.data;

/**
 * Created by Hans CK on 06-Feb-17.
 */

@EFragment(R.layout.fragment_photo_detail)
public class PhotoDetailFragment extends BaseFragment {

	private AlbumAdapter adapter;
	private int photoIdx = 0;
	private Photo photo;
	private RealmList<Photo> galleryPhotos = new RealmList<>();
	private Realm realm = Realm.getDefaultInstance();

	@ViewById
	ViewPager currentPhoto;
	@ViewById
	RecyclerView imageList;
	@ViewById
	RelativeLayout listContainer;
	@ViewById
	CirclePageIndicator index;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (PhotoClickContainer.getInstance().checkClicks()) {
			showAds();
		}
		Bundle data = getArguments();
		if (data != null) {
			photo = data.getParcelable("photo");
		}
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(photo.getName());
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		RealmResults<Photo> results = realm.where(Photo.class).equalTo("name", photo.getName()).findAll();
		galleryPhotos.addAll(results.subList(0, results.size()));
		adapter = new AlbumAdapter(getContext(), galleryPhotos, new OnListItemSelected() {
			@Override
			public void onClick(int position) {
				photoIdx = position;
				showPhoto(photoIdx);
			}
		});
		return null;
	}

	@AfterViews
	void initViews() {
		LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
		imageList.setLayoutManager(lm);
		imageList.setAdapter(adapter);
		currentPhoto.setAdapter(new PhotoSlideAdapter(getContext(), galleryPhotos, R.layout.item_photo_slide));
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

	private void showPhoto(int idx) {
		imageList.scrollToPosition(idx);
		currentPhoto.setCurrentItem(idx, true);
	}

	private void showAds() {
		final InterstitialAd mInterstitialAd = new InterstitialAd(getActivity());
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

			@Override
			public void onAdClosed() {

			}

			@Override
			public void onAdFailedToLoad(int errorCode) {

			}
		});
	}
}
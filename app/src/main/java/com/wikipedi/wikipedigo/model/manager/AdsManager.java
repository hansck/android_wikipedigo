package com.wikipedi.wikipedigo.model.manager;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.wikipedi.wikipedigo.R;
import com.wikipedi.wikipedigo.model.object.UserPreferences;

/**
 * Created by Hans CK on 30-Aug-17.
 */

public class AdsManager implements RewardedVideoAdListener {

	private static AdsManager instance = new AdsManager();
	private AdRequest adRequest;
	private InterstitialAd interstitialAds;
	private RewardedVideoAd rewardAds;
	private Context context;

	public AdsManager() {

	}

	public static AdsManager getInstance() {
		return instance;
	}

	public void initAds(Context context) {
		this.context = context;
		MobileAds.initialize(context, context.getString(R.string.ads_app_id));

		// Init interstitial ads
		interstitialAds = new InterstitialAd(context);
		interstitialAds.setAdUnitId(context.getString(R.string.ads_interstitial));
		interstitialAds.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				if (interstitialAds.isLoaded()) {
					interstitialAds.show();
				}
			}
		});

		// Init reward ads
		rewardAds = MobileAds.getRewardedVideoAdInstance(context);
		rewardAds.setRewardedVideoAdListener(this);

		// Init ad request
		adRequest = new AdRequest.Builder()
			.addTestDevice("48193F9C648EAA313FB3D131691335CB")
			.build();

		loadRewardAds();
	}

	public AdRequest loadBannerAds() {
		return adRequest;
	}

	public void loadInterstitialAds() {
		interstitialAds.loadAd(adRequest);
	}

	private void loadRewardAds() {
		rewardAds.loadAd(context.getString(R.string.ads_reward), adRequest);
	}

	public boolean showRewarAds() {
		if (rewardAds.isLoaded()) {
			rewardAds.show();
		}
		return rewardAds.isLoaded();
	}

	//region Listeners
	@Override
	public void onRewarded(RewardItem reward) {
		Toast.makeText(context, "onRewarded! currency: " + reward.getType() + "  amount: " +
			reward.getAmount(), Toast.LENGTH_SHORT).show();
		UserPreferences.getInstance().resetFavoriteCount();
		loadRewardAds();
	}

	@Override
	public void onRewardedVideoAdLoaded() {
	}

	@Override
	public void onRewardedVideoAdOpened() {
	}

	@Override
	public void onRewardedVideoStarted() {
	}

	@Override
	public void onRewardedVideoAdClosed() {
		loadRewardAds();
	}

	@Override
	public void onRewardedVideoAdLeftApplication() {
		loadRewardAds();
	}

	@Override
	public void onRewardedVideoAdFailedToLoad(int i) {
	}
	//endregion

}

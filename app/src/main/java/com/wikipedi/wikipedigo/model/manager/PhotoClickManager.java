package com.wikipedi.wikipedigo.model.manager;

import com.wikipedi.wikipedigo.util.Constants;

/**
 * Created by Hans CK on 10-Feb-17.
 */

public class PhotoClickManager {

	private static PhotoClickManager instance = new PhotoClickManager();

	private int clicks;

	public PhotoClickManager() {

	}

	public static PhotoClickManager getInstance() {
		return instance;
	}

	public int getClicks() {
		return clicks;
	}

	public void setClicks(int clicks) {
		this.clicks = clicks;
	}

	public boolean checkClicks() {
		if (clicks == Constants.Ads.MAX_CLICK) {
			clicks = 0;
			return true;
		} else {
			clicks++;
			return false;
		}
	}
}

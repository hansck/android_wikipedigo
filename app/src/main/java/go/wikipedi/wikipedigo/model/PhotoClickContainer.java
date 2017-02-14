package go.wikipedi.wikipedigo.model;

/**
 * Created by Hans CK on 10-Feb-17.
 */

public class PhotoClickContainer {

	private static PhotoClickContainer instance = new PhotoClickContainer();
	private static final int MAX_CLICKS = 5;

	private int clicks;

	public PhotoClickContainer() {

	}

	public static PhotoClickContainer getInstance() {
		return instance;
	}

	public int getClicks() {
		return clicks;
	}

	public void setClicks(int clicks) {
		this.clicks = clicks;
	}

	public boolean checkClicks() {
		if (clicks == MAX_CLICKS) {
			clicks = 0;
			return true;
		} else {
			clicks++;
			return false;
		}
	}
}

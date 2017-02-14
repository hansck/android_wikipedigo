package go.wikipedi.wikipedigo.model;

import io.realm.RealmModel;

/**
 * Created by Hans CK on 13-Feb-17.
 */

public class Favorite implements RealmModel {

	private String id;

	public Favorite() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}

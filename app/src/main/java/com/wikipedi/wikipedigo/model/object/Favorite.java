package com.wikipedi.wikipedigo.model.object;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Hans CK on 13-Feb-17.
 */

public class Favorite extends RealmObject implements RealmModel {

	@PrimaryKey
	private String name;

	public Favorite() {

	}

	public Favorite(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
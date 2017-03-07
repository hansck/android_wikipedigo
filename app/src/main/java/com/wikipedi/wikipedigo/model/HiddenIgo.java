package com.wikipedi.wikipedigo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Hans CK on 02-Mar-17.
 */

public class HiddenIgo extends RealmObject {

	@SerializedName("name")
	@Expose
	@PrimaryKey
	private String name;

	public HiddenIgo() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

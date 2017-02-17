package com.wikipedi.wikipedigo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by E460 on 13/01/2017.
 */

public class Photo extends RealmObject implements Parcelable {

	@SerializedName("id")
	@Expose
	@PrimaryKey
	private String id;

	@SerializedName("name")
	@Expose
	@Index
	private String name;

	@SerializedName("image")
	@Expose
	private String image;

	@SerializedName("created_at")
	@Expose
	private Date createdAt;

	@SerializedName("favorite_count")
	@Expose
	private int retweet;

	private boolean isFavorite;

	public Photo() {

	}

	public Photo(String name, String imageLink, Date createdAt) {
		this.name = name;
		this.image = imageLink;
		this.createdAt = createdAt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean favorite) {
		isFavorite = favorite;
	}

	public Photo copy() {
		return new Photo(name, image, createdAt);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getRetweet() {
		return retweet;
	}

	public void setRetweet(int retweet) {
		this.retweet = retweet;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(image);
		dest.writeInt(retweet);
		dest.writeLong(createdAt.getTime());
		dest.writeByte((byte) (isFavorite ? 1 : 0));
	}

	protected Photo(Parcel in) {
		id = in.readString();
		name = in.readString();
		image = in.readString();
		retweet = in.readInt();
		createdAt = new Date(in.readLong());
		isFavorite = in.readByte() != 0;
	}

	public static final Creator<Photo> CREATOR = new Creator<Photo>() {
		@Override
		public Photo createFromParcel(Parcel in) {
			return new Photo(in);
		}

		@Override
		public Photo[] newArray(int size) {
			return new Photo[size];
		}
	};
}

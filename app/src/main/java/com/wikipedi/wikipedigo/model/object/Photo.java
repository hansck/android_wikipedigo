package com.wikipedi.wikipedigo.model.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wikipedi.wikipedigo.api.APIKey;

import java.util.Date;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by E460 on 13/01/2017.
 */

public class Photo extends RealmObject implements RealmModel, Parcelable {

	@SerializedName("_id")
	@Expose
	@PrimaryKey
	private String id;

	@SerializedName("name")
	@Expose
	@Index
	private String name;

	@SerializedName("imageId")
	@Expose
	private String image;

	@SerializedName("created_at")
	@Expose
	private Date createdAt;

	@SerializedName("favorite_count")
	@Expose
	private int favoriteCount;

	@SerializedName("width")
	@Expose
	private int width;

	@SerializedName("height")
	@Expose
	private int height;

	@SerializedName("hidden")
	@Expose
	private boolean hidden;

	private int popularity;

	public Photo() {

	}

	public Photo(String name, String image, Date createdAt, int popularity, int width, int height, boolean hidden) {
		this.name = name;
		this.image = image;
		this.createdAt = createdAt;
		this.popularity = popularity;
		this.width = width;
		this.height = height;
		this.hidden = hidden;
	}


	//region Getter Setter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return APIKey.IMAGE_URL + image + "/" + image + ".jpg";
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	//endregion

	//region Parcelable
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(image);
		dest.writeInt(favoriteCount);
		dest.writeLong(createdAt.getTime());
		dest.writeInt(popularity);
		dest.writeInt(width);
		dest.writeInt(height);
		dest.writeByte((byte) (hidden ? 1 : 0));
	}

	protected Photo(Parcel in) {
		id = in.readString();
		name = in.readString();
		image = in.readString();
		favoriteCount = in.readInt();
		createdAt = new Date(in.readLong());
		popularity = in.readInt();
		width = in.readInt();
		height = in.readInt();
		hidden = in.readByte() != 0;
	}
	//endregion
}

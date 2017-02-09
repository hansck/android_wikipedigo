package go.wikipedi.wikipedigo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by E460 on 13/01/2017.
 */

public class Photo extends RealmObject implements Parcelable {

	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("image")
	@Expose
	private String image;

	@SerializedName("created_at")
	@Expose
	private Date createdAt;

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

	public Photo copy() {
		return new Photo(name, image, createdAt);
	}

	public boolean isContains(String text) {
		return name.toLowerCase().contains(text.toLowerCase());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(image);
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

	protected Photo(Parcel in) {
		name = in.readString();
		image = in.readString();
	}

}

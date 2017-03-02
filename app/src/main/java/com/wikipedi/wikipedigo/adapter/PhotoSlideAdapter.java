package com.wikipedi.wikipedigo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wikipedi.wikipedigo.R;
import com.wikipedi.wikipedigo.model.Photo;
import com.wikipedi.wikipedigo.util.Common;

import java.util.List;

public class PhotoSlideAdapter extends PagerAdapter {

	private Context context;
	private List<Photo> photos;
	private int resPhoto;
	private ImageView imageView;

	public PhotoSlideAdapter(Context context, List<Photo> photos, int resId) {
		resPhoto = resId;
		this.context = context;
		this.photos = photos;
	}

	@Override
	public Object instantiateItem(ViewGroup collection, int position) {
		LayoutInflater inflater = LayoutInflater.from(context);
		ViewGroup layout = (ViewGroup) inflater.inflate(resPhoto, collection, false);
		imageView = (ImageView) layout.findViewById(R.id.photo);
		showPhoto(imageView, position);
		collection.addView(layout);
		return layout;
	}

	@Override
	public void destroyItem(ViewGroup collection, int position, Object view) {
		collection.removeView((View) view);
	}

	@Override
	public int getCount() {
		return photos.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	private void showPhoto(ImageView photo, int position) {
		Common.getInstance().setImage(context, photo, photos.get(position).getImage());
	}
}
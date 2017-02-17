package com.wikipedi.wikipedigo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import com.wikipedi.wikipedigo.R;
import com.wikipedi.wikipedigo.model.Photo;

public class PhotoSlideAdapter extends PagerAdapter {

	private Context mContext;
	private List<Photo> photos;
	private int resPhoto;

	public PhotoSlideAdapter(Context context, List<Photo> photos, int resId) {
		mContext = context;
		resPhoto = resId;
		this.photos = photos;
	}

	@Override
	public Object instantiateItem(ViewGroup collection, int position) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewGroup layout = (ViewGroup) inflater.inflate(resPhoto, collection, false);
		final ImageView photo = (ImageView) layout.findViewById(R.id.photo);
		showPhoto(photo, position);
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
		Glide.with(mContext)
				.load(photos.get(position).getImage())
				.animate(R.anim.grow_from_middle)
				.placeholder(R.drawable.ic_face)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(photo);
	}
}
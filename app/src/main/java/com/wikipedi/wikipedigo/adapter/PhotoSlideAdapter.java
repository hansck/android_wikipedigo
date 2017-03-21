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

import uk.co.jakelee.vidsta.VidstaPlayer;

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

		// add video listener
		layout.setTag(position);
		VidstaPlayer videoView = (VidstaPlayer) layout.findViewById(R.id.video);
		if (null != photos.get(position).getVideoLink()) {
			String videoUrl = photos.get(position).getVideoLink();
			videoView.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.GONE);

			videoView.setVideoSource(videoUrl);
			videoView.requestFocus();
			videoView.start();
		} else {
			videoView.setVisibility(View.GONE);
			imageView.setVisibility(View.VISIBLE);
		}
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

	public void onPageScrolled(View view) {
		VidstaPlayer videoView = (VidstaPlayer) view.findViewById(R.id.video);
		if (videoView != null && videoView.isPlaying()) {
			videoView.pause();
		}
	}
}
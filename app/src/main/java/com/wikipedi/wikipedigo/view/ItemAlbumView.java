package com.wikipedi.wikipedigo.view;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.wikipedi.wikipedigo.R;

@EViewGroup(R.layout.item_photo_slide)
public class ItemAlbumView extends RelativeLayout {

	private Context context;

	@ViewById
	RelativeLayout background;
	@ViewById
	ImageView photo;

	public ItemAlbumView(Context context) {
		super(context);
		this.context = context;
	}

	public void bind(String url) {
		Glide.with(context)
				.load(url)
				.animate(R.anim.grow_from_middle)
				.placeholder(R.drawable.ic_face)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(photo);
	}
}

package com.wikipedi.wikipedigo.view;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wikipedi.wikipedigo.R;
import com.wikipedi.wikipedigo.model.object.Photo;
import com.wikipedi.wikipedigo.util.Common;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by E460 on 13/01/2017.
 */

@EViewGroup(R.layout.item_photo_grid)
public class ItemPhotoView extends RelativeLayout {

	private Context context;

	@ViewById
	ImageView image;
	@ViewById
	TextView name;

	public ItemPhotoView(Context context) {
		super(context);
		this.context = context;
	}

	public void bind(Photo photo) {
		name.setText(photo.getName());
		Common.getInstance().setImage(context, image, photo.getImage());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}
}

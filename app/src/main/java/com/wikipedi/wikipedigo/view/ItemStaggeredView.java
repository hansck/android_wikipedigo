package com.wikipedi.wikipedigo.view;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wikipedi.wikipedigo.R;
import com.wikipedi.wikipedigo.model.object.Photo;
import com.wikipedi.wikipedigo.util.Common;
import com.wikipedi.wikipedigo.util.Constants;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Hans CK on 24-Aug-17.
 */

@EViewGroup(R.layout.item_photo_staggered)
public class ItemStaggeredView extends RelativeLayout {

	private Context context;
	private static SimpleDateFormat formatter = new SimpleDateFormat(Constants.DateTimeFormat.FULL_SHORT, Locale.US);

	@ViewById
	TextView name;
	@ViewById
	TextView date;
	@ViewById
	ImageView image;
	@ViewById
	RelativeLayout imageContainer;

	public ItemStaggeredView(Context context) {
		super(context);
		this.context = context;
	}

	public void bind(Photo photo) {
		date.setText(formatter.format(photo.getCreatedAt()));
		name.setText(photo.getName());
		int width = MeasureSpec.getSize(photo.getWidth());
		int height = width * photo.getHeight() / photo.getHeight();
		setMeasuredDimension(width, height);
		Common.getInstance().setImageWithPlaceholder(context, image, photo.getImage());
	}
}

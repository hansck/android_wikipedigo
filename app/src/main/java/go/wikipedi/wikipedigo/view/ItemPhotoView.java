package go.wikipedi.wikipedigo.view;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import go.wikipedi.wikipedigo.R;
import go.wikipedi.wikipedigo.model.Photo;

/**
 * Created by E460 on 13/01/2017.
 */

@EViewGroup(R.layout.item_photo)
public class ItemPhotoView extends RelativeLayout {

	private Context context;

	@ViewById(R.id.img_photo)
	ImageView imgPhoto;

	@ViewById(R.id.tv_name)
	TextView name;

	public ItemPhotoView(Context context) {
		super(context);
		this.context = context;
	}

	public void bind(Photo photo) {
		name.setText(photo.getName());
		Glide
				.with(context)
				.load(photo.getImage())
				.placeholder(R.drawable.ic_face)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(imgPhoto);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}
}

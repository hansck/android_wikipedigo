package go.wikipedi.wikipedigo.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import go.wikipedi.wikipedigo.R;
import go.wikipedi.wikipedigo.model.Photo;

/**
 * Created by Hans CK on 06-Feb-17.
 */

@EFragment(R.layout.fragment_photo_detail)
public class PhotoDetailFragment extends BaseFragment {

	@ViewById(R.id.img_full)
	ImageView fullImage;

	private Photo photo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle data = getArguments();
		if (data != null) {
			photo = data.getParcelable("photo");
		}
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(photo.getName());
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		return null;
	}

	@AfterViews
	void initViews() {
		Glide.with(this)
				.load(photo.getImage())
				.animate(R.anim.grow_from_middle)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(fullImage);

	}
}

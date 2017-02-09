package go.wikipedi.wikipedigo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import go.wikipedi.base.OnListItemSelected;
import go.wikipedi.base.RecyclerViewAdapterBase;
import go.wikipedi.base.ViewWrapper;
import go.wikipedi.wikipedigo.model.Photo;
import go.wikipedi.wikipedigo.view.ItemAlbumView;
import go.wikipedi.wikipedigo.view.ItemAlbumView_;

public class AlbumAdapter extends RecyclerViewAdapterBase<Photo, ItemAlbumView> {

	private Context context;
	private OnListItemSelected listener;

	public AlbumAdapter(Context context, List<Photo> images, OnListItemSelected onItemSelectedListener) {
		this.context = context;
		items = images;
		listener = onItemSelectedListener;
	}

	@Override
	protected ItemAlbumView onCreateItemView(ViewGroup parent, int viewType) {
		return ItemAlbumView_.build(context);
	}

	@Override
	public void onBindViewHolder(final ViewWrapper<ItemAlbumView> holder, int position) {
		ItemAlbumView view = holder.getView();
		String url = getItem(position).getImage();
		view.bind(url);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				listener.onClick(holder.getAdapterPosition());
			}
		});
	}
}

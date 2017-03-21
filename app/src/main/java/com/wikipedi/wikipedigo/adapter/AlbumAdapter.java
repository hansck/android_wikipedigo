package com.wikipedi.wikipedigo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.wikipedi.wikipedigo.model.Photo;
import com.wikipedi.wikipedigo.util.OnListItemSelected;
import com.wikipedi.wikipedigo.view.ItemAlbumView;
import com.wikipedi.wikipedigo.view.ItemAlbumView_;
import com.wikipedi.wikipedigo.view.ViewWrapper;

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

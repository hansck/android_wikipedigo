package com.wikipedi.wikipedigo.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.wikipedi.wikipedigo.model.object.Photo;
import com.wikipedi.wikipedigo.util.Constants;
import com.wikipedi.wikipedigo.util.OnItemSelectedListener;
import com.wikipedi.wikipedigo.util.OnLastItemVisibleListener;
import com.wikipedi.wikipedigo.view.ItemStaggeredView;
import com.wikipedi.wikipedigo.view.ItemStaggeredView_;
import com.wikipedi.wikipedigo.view.ViewWrapper;

import io.realm.RealmList;

/**
 * Created by E460 on 24/08/2017.
 */

public class PhotosTimelineAdapter extends RecyclerViewAdapterBase<Photo, ItemStaggeredView> {

	private int itemCount = 0;
	private OnItemSelectedListener onItemSelectedListener;
	private OnLastItemVisibleListener onLastItemVisibleListener;

	public PhotosTimelineAdapter(Context context, RealmList<Photo> items) {
		super(context, items);
		itemCount = Constants.Photo.MAX_ITEM;
	}

	public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
		this.onItemSelectedListener = onItemSelectedListener;
	}

	public void setOnLastItemVisibleListener(OnLastItemVisibleListener onLastItemVisibleListener) {
		this.onLastItemVisibleListener = onLastItemVisibleListener;
	}

	@Override
	protected ItemStaggeredView onCreateItemView(ViewGroup parent, int viewType) {
		return ItemStaggeredView_.build(context);
	}

	@Override
	public void onBindViewHolder(final ViewWrapper<ItemStaggeredView> holder, final int position) {
		Photo item = getItem(position);
		if (item != null) {
			holder.getView().bind(getItem(position));
			holder.getView().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					onItemSelectedListener.onItemSelected(position);
				}
			});
		}
	}

	public void setItems(RealmList<Photo> items) {
		this.items = items;
		resetItemCount();
		notifyDataSetChanged();
	}

	@Override
	@Nullable
	public Photo getItem(int position) {
		if (items != null) {
			if (items.size() > 0 && position < items.size()) {
				return items.get(position);
			} else {
				return null;
			}
		}
		return null;
	}

	@Override
	public int getItemCount() {
		return itemCount;
	}

	public void showNextItems() {
		itemCount += Constants.Photo.MAX_ITEM;
		if (itemCount > items.size()) {
			itemCount = items.size();
		}
		notifyDataSetChanged();
	}

	public void refresh() {
		notifyDataSetChanged();
	}

	private void resetItemCount() {
		itemCount = Constants.Photo.MAX_ITEM;
		if (itemCount > items.size()) {
			itemCount = items.size();
		}
	}
}

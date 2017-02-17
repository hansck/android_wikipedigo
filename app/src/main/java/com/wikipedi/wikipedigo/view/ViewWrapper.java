package com.wikipedi.wikipedigo.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by E460 on 12/01/2017.
 */

public class ViewWrapper<V extends View> extends RecyclerView.ViewHolder {

	private V view;

	public ViewWrapper(V itemView) {
		super(itemView);
		view = itemView;
	}

	public V getView() {
		return view;
	}
}

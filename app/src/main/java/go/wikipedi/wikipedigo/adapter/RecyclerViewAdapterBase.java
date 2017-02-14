package go.wikipedi.wikipedigo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import go.wikipedi.wikipedigo.view.ViewWrapper;

/**
 * Created by E460 on 12/01/2017.
 */

public abstract class RecyclerViewAdapterBase<T, V extends View> extends RecyclerView.Adapter<ViewWrapper<V>> {

	protected Context context;
	protected List<T> items = new ArrayList<>();

	public RecyclerViewAdapterBase(){

	}

	public RecyclerViewAdapterBase(Context context, List<T> items) {
		this.context = context;
		this.items = items;
	}

	@Override
	public ViewWrapper<V> onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewWrapper<>(onCreateItemView(parent, viewType));
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	protected abstract V onCreateItemView(ViewGroup parent, int viewType);

	public T getItem(int position) {
		return items.get(position);
	}
}

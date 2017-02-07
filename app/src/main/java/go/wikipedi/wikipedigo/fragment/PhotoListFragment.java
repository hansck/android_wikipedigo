package go.wikipedi.wikipedigo.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.IgnoreWhen;
import org.androidannotations.annotations.ViewById;

import go.wikipedi.base.BaseRunnable;
import go.wikipedi.base.Common;
import go.wikipedi.base.OnItemSelectedListener;
import go.wikipedi.base.OnLastItemVisibleListener;
import go.wikipedi.wikipedigo.R;
import go.wikipedi.wikipedigo.adapter.PhotosAdapter;
import go.wikipedi.wikipedigo.contoller.PhotosController;
import go.wikipedi.wikipedigo.model.Photo;
import io.realm.RealmList;


@EFragment(R.layout.fragment_photo_list)
public class PhotoListFragment extends BaseFragment {

	private String search;
	private GridLayoutManager layoutManager;
	private PhotosAdapter adapter;
	private SearchView searchView;
	private MenuItem searchItem;

	//region annotations
	@ViewById(R.id.rv_container)
	RecyclerView container;
	@ViewById(R.id.content)
	SwipeRefreshLayout swipeRefreshLayout;
	@ViewById(R.id.tv_alert)
	TextView alertText;
	@ViewById
	ProgressBar onLoading;

	@AfterViews
	void initViews() {
		PhotosController.getInstance().getData();
		adapter = new PhotosAdapter(getContext(), PhotosController.getInstance().getPhotos());
		adapter.setOnItemSelectedListener(onItemSelected);
		adapter.setOnLastItemVisibleListener(onLastItemVisible);
		container.setAdapter(adapter);
		layoutManager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
		container.setLayoutManager(layoutManager);
		container.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
					adapter.showNextItems();
				}
			}
		});
		if (PhotosController.getInstance().getPhotos().size() == 0) {
			initPhotos();
		}
		swipeRefreshLayout.setOnRefreshListener(onUpdatePhotos);
	}

	@IgnoreWhen(IgnoreWhen.State.VIEW_DESTROYED)
	void setLoading(boolean loading) {
		onLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
		container.setVisibility(loading ? View.GONE : View.VISIBLE);
	}
	//endregion

	void showFullImage(Photo photo) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		PhotoDetailFragment fragment = new PhotoDetailFragment_();
		Bundle bundle = new Bundle();
		bundle.putParcelable("photo", photo);
		fragment.setArguments(bundle);
		ft.replace(R.id.fragmentContainer, fragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	void searchPhoto(String query) {
		if (query.trim().length() == 0) {
			adapter.setItems(PhotosController.getInstance().getPhotos());
		} else {
			PhotosController.getInstance().searchPhotos(query, new BaseRunnable<RealmList<Photo>>() {
				@Override
				public void run(RealmList<Photo> object) {
					if (object.size() == 0) {
						alertText.setVisibility(View.VISIBLE);
					} else {
						alertText.setVisibility(View.GONE);
					}
					adapter.setItems(object);
				}
			});
		}
	}

	//region override methods
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		return null;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.search_bar, menu);
		super.onCreateOptionsMenu(menu, inflater);
		searchItem = menu.findItem(R.id.action_find);
		searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		searchView.setQueryHint(getString(R.string.text_search_igo));
		searchView.setOnQueryTextListener(onSearchPhoto);
		if (getArguments().getString("query") != null && !getArguments().getString("query").equals("")) {
			searchItem.expandActionView();
			searchView.setQuery(getArguments().getString("query"), false);
		}
	}

	@Override
	public boolean onBackPressed() {
		swipeRefreshLayout.setEnabled(true);
		if (swipeRefreshLayout.isRefreshing()) {
			swipeRefreshLayout.setRefreshing(false);
			return true;
		}
		return super.onBackPressed();
	}

	@Override
	public void onResume() {
		if (searchItem != null) {
			Bundle saved = getArguments();
			searchView.setQuery(saved.getString("query"), false);
			container.getLayoutManager().onRestoreInstanceState(saved.getParcelable("list"));
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		Bundle saved = getArguments();
		saved.putString("query", search);
		saved.putParcelable("list", container.getLayoutManager().onSaveInstanceState());
		super.onPause();
	}
	//endregion

	//region Private methods
	private void initPhotos() {
		setLoading(true);
		PhotosController.getInstance().fetchPhotos(new Runnable() {
			@Override
			public void run() {
				adapter.setItems(PhotosController.getInstance().getPhotos());
				setLoading(false);
			}
		}, new Runnable() {
			@Override
			public void run() {
				adapter.setItems(PhotosController.getInstance().getPhotos());
				setLoading(false);
			}
		});
	}
	//endregion

	//region listeners
	OnItemSelectedListener onItemSelected = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(int position) {
			showFullImage(adapter.getItem(position));
		}
	};

	OnLastItemVisibleListener onLastItemVisible = new OnLastItemVisibleListener() {
		@Override
		public void onLastItemVisible() {
			adapter.showNextItems();
		}
	};

	SearchView.OnQueryTextListener onSearchPhoto = new SearchView.OnQueryTextListener() {
		@Override
		public boolean onQueryTextSubmit(String query) {
			search = query;
			searchPhoto(query);
			return false;
		}

		@Override
		public boolean onQueryTextChange(String newText) {
			search = newText;
			searchPhoto(newText);
			return false;
		}
	};

	SwipeRefreshLayout.OnRefreshListener onUpdatePhotos = new SwipeRefreshLayout.OnRefreshListener() {
		@Override
		public void onRefresh() {
			PhotosController.getInstance().updatePhotos(new Runnable() {
				@Override
				public void run() {
					swipeRefreshLayout.setRefreshing(false);
					adapter.notifyDataSetChanged();
				}
			}, new Runnable() {
				@Override
				public void run() {
					swipeRefreshLayout.setRefreshing(false);
					Common.getInstance().showSnackbar(getActivity(), getString(R.string.no_internet));
				}
			});
		}
	};
	//endregion
}

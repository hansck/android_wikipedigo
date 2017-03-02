package com.wikipedi.wikipedigo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.wikipedi.wikipedigo.R;
import com.wikipedi.wikipedigo.activity.PhotoDetailActivity_;
import com.wikipedi.wikipedigo.activity.SortActivity_;
import com.wikipedi.wikipedigo.adapter.PhotosAdapter;
import com.wikipedi.wikipedigo.container.PhotosContainer;
import com.wikipedi.wikipedigo.model.Photo;
import com.wikipedi.wikipedigo.util.BaseRunnable;
import com.wikipedi.wikipedigo.util.Common;
import com.wikipedi.wikipedigo.util.Constants;
import com.wikipedi.wikipedigo.util.OnItemSelectedListener;
import com.wikipedi.wikipedigo.util.OnLastItemVisibleListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.IgnoreWhen;
import org.androidannotations.annotations.ViewById;

import io.realm.RealmList;


@EFragment(R.layout.fragment_photo_list)
public class PhotoListFragment extends BaseFragment {

	private String search;
	private PhotosAdapter adapter;
	private SearchView searchView;
	private MenuItem searchItem;

	@ViewById
	RecyclerView container;
	@ViewById
	SwipeRefreshLayout swipeRefreshLayout;
	@ViewById
	TextView alert;
	@ViewById
	ProgressBar onLoading;
	@ViewById
	AdView adView;

	public PhotoListFragment() {

	}

	public PhotoListFragment_ newInstance(int index) {
		PhotoListFragment_ fragment = new PhotoListFragment_();
		Bundle args = new Bundle();
		args.putInt(Constants.General.TAB_INDEX, index);
		fragment.setArguments(args);
		return fragment;
	}

	public int getTabIndex() {
		Bundle args = getArguments();
		return args.getInt(Constants.General.TAB_INDEX);
	}

	// region Listeners
	@AfterViews
	void initViews() {
		if (getTabIndex() == Constants.General.TAB_GALLERY) {
			PhotosContainer.getInstance().getAllIgo();
			adapter = new PhotosAdapter(getContext(), PhotosContainer.getInstance().getPhotos());
		} else if (getTabIndex() == Constants.General.TAB_FAVORITE) {
			PhotosContainer.getInstance().getFavoriteIgo();
			adapter = new PhotosAdapter(getContext(), PhotosContainer.getInstance().getFavorites());
		}
		adapter.setOnItemSelectedListener(onItemSelected);
		adapter.setOnLastItemVisibleListener(onLastItemVisible);
		container.setAdapter(adapter);
		GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
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
		if (PhotosContainer.getInstance().getPhotos().size() == 0) {
			initPhotos();
		}
		swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

		MobileAds.initialize(getActivity(), getString(R.string.list_banner));
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}

	@IgnoreWhen(IgnoreWhen.State.VIEW_DESTROYED)
	void setLoading(boolean loading) {
		onLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
		container.setVisibility(loading ? View.GONE : View.VISIBLE);
	}

	@IgnoreWhen(IgnoreWhen.State.VIEW_DESTROYED)
	void setAlert(boolean loading) {
		alert.setVisibility(loading ? View.VISIBLE : View.GONE);
		container.setVisibility(loading ? View.GONE : View.VISIBLE);
	}
	//endregion

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
		inflater.inflate(R.menu.menu_home, menu);
		super.onCreateOptionsMenu(menu, inflater);
		searchItem = menu.findItem(R.id.action_find);
		searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		searchView.setQueryHint(getString(R.string.text_search_igo));
		searchView.setOnQueryTextListener(onSearchPhoto);
		MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				setAlert(false);
				Common.getInstance().hideSoftKeyboard(getActivity());
				return true;
			}

			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				setAlert(false);
				return true;
			}
		});
		if (getArguments().getString(Constants.Photo.QUERY) != null && !getArguments().getString(Constants.Photo.QUERY).equals("")) {
			searchItem.expandActionView();
			searchView.setQuery(getArguments().getString(Constants.Photo.QUERY), false);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_sort) {
			navigateToSort();
		}
		return true;
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
		super.onResume();
		if (searchItem != null) {
			Bundle saved = getArguments();
			searchView.setQuery(saved.getString(Constants.Photo.QUERY), false);
			container.getLayoutManager().onRestoreInstanceState(saved.getParcelable(Constants.Photo.LIST));
		}
		if (adView != null) {
			adView.resume();
		}
		if (getTabIndex() == Constants.General.TAB_FAVORITE) {
			adapter.setItems(PhotosContainer.getInstance().getFavorites());
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		Bundle saved = getArguments();
		saved.putString(Constants.Photo.QUERY, search);
		saved.putParcelable(Constants.Photo.LIST, container.getLayoutManager().onSaveInstanceState());
		if (adView != null) {
			adView.pause();
		}
		if (swipeRefreshLayout != null) {
			swipeRefreshLayout.setRefreshing(false);
			swipeRefreshLayout.destroyDrawingCache();
			swipeRefreshLayout.clearAnimation();
		}
	}

	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == Activity.RESULT_OK) {
				adapter.refresh();
			}
		}
	}
	//endregion

	//region Private methods
	private void initPhotos() {
		setLoading(true);
		PhotosContainer.getInstance().fetchPhotos(new Runnable() {
			@Override
			public void run() {
				adapter.setItems(PhotosContainer.getInstance().getPhotos());
				setLoading(false);
			}
		}, new Runnable() {
			@Override
			public void run() {
				adapter.setItems(PhotosContainer.getInstance().getPhotos());
				setLoading(false);
			}
		});
	}

	private void searchPhoto(String query) {
		if (getTabIndex() == Constants.General.TAB_GALLERY) {
			if (query.trim().length() == 0) {
				adapter.setItems(PhotosContainer.getInstance().getPhotos());
			} else {
				PhotosContainer.getInstance().searchAllIgo(query, new BaseRunnable<RealmList<Photo>>() {
					@Override
					public void run(RealmList<Photo> object) {
						setSearchResult(object);
					}
				});
			}
		} else if (getTabIndex() == Constants.General.TAB_FAVORITE) {
			if (query.trim().length() == 0) {
				adapter.setItems(PhotosContainer.getInstance().getFavorites());
			} else {
				PhotosContainer.getInstance().searchFavoriteIgo(query, new BaseRunnable<RealmList<Photo>>() {
					@Override
					public void run(RealmList<Photo> object) {
						setSearchResult(object);
					}
				});
			}
		}
	}

	private void setSearchResult(RealmList<Photo> object) {
		if (object.size() == 0) {
			setAlert(true);
		} else {
			setAlert(false);
		}
		adapter.setItems(object);
	}

	private void showPhotoDetail(Photo photo) {
		Intent intent = new Intent(getActivity(), PhotoDetailActivity_.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable(Constants.Photo.PHOTO, photo);
		intent.putExtras(bundle);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.enter_right, R.anim.exit_left);
	}

	private void navigateToSort() {
		Intent intent = new Intent(getActivity(), SortActivity_.class);
		startActivityForResult(intent, 1);
		getActivity().overridePendingTransition(R.anim.enter_right, R.anim.exit_left);
	}
	//endregion

	//region listeners
	OnItemSelectedListener onItemSelected = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(int position) {
			if (swipeRefreshLayout.isRefreshing()) {
				swipeRefreshLayout.setRefreshing(false);
			}
			showPhotoDetail(adapter.getItem(position));
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

	SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
		@Override
		public void onRefresh() {
			if (getTabIndex() == Constants.General.TAB_GALLERY) {
				PhotosContainer.getInstance().updatePhotos(new Runnable() {
					@Override
					public void run() {
						if (swipeRefreshLayout != null) {
							if (swipeRefreshLayout.isRefreshing()) {
								swipeRefreshLayout.setRefreshing(false);
								PhotosContainer.getInstance().getAllIgo();
								adapter.setItems(PhotosContainer.getInstance().getPhotos());
							}
						}
					}
				}, new Runnable() {
					@Override
					public void run() {
						if (swipeRefreshLayout != null) {
							if (swipeRefreshLayout.isRefreshing()) {
								swipeRefreshLayout.setRefreshing(false);
							}
							Common.getInstance().showSnackbar(getActivity(), getString(R.string.no_internet));
						}
					}
				});
			} else if (getTabIndex() == Constants.General.TAB_FAVORITE) {
				PhotosContainer.getInstance().getFavoriteIgo();
				adapter.refresh();
				swipeRefreshLayout.setRefreshing(false);
			}
		}
	};
	//endregion
}

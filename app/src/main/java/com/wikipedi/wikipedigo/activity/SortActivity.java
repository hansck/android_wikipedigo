package com.wikipedi.wikipedigo.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wikipedi.wikipedigo.R;
import com.wikipedi.wikipedigo.container.PhotosContainer;
import com.wikipedi.wikipedigo.model.UserPreferences;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_sort)
public class SortActivity extends AppCompatActivity {

	@ViewById
	LinearLayout dateContainer;
	@ViewById
	LinearLayout popularityContainer;
	@ViewById
	LinearLayout alphabeticContainer;
	@ViewById
	TextView ascending;
	@ViewById
	TextView descending;

	private String sortMethod = UserPreferences.getInstance().getSortMethod();
	private String sortBy = UserPreferences.getInstance().getSortBy();

	@AfterViews
	void initViews() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		if (sortMethod.equals("ascending")) {
			ascending.setBackgroundColor(ContextCompat.getColor(this, R.color.choosed));
		} else {
			descending.setBackgroundColor(ContextCompat.getColor(this, R.color.choosed));
		}

		if (sortBy.equals("date")) {
			isDate();
		} else if (sortBy.equals("popularity")) {
			isPopularity();
		} else if (sortBy.equals("alphabetic")) {
			isAlphabetic();
		}
	}

	@Click(R.id.dateContainer)
	void onDate() {
		if (!sortBy.equals("date")) {
			isDate();
		}
	}

	@Click(R.id.popularityContainer)
	void onPopularity() {
		if (!sortBy.equals("popularity")) {
			isPopularity();
		}
	}

	@Click(R.id.alphabeticContainer)
	void onAlphabetic() {
		if (!sortBy.equals("alphabetic")) {
			isAlphabetic();
		}
	}

	@Click(R.id.ascending)
	void onAscending() {
		if (!sortMethod.equals("ascending")) {
			sortMethod = "ascending";
			toggleSortMethod();
		}
	}

	@Click(R.id.descending)
	void onDescending() {
		if (!sortMethod.equals("descending")) {
			sortMethod = "descending";
			toggleSortMethod();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_editable, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				super.onBackPressed();
				break;
			case R.id.action_save:
				savePreferences();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void savePreferences() {
		UserPreferences.getInstance().setPreferences(sortBy, sortMethod);
		PhotosContainer.getInstance().sortIgo();
		backToList();
	}

	private void backToList() {
		Intent returnIntent = new Intent();
		setResult(Activity.RESULT_OK, returnIntent);
		finish();
	}

	private void isDate() {
		sortBy = "date";
		ascending.setText(getString(R.string.latest));
		descending.setText(getString(R.string.oldest));
		toggleSortBy();
	}

	private void isPopularity() {
		sortBy = "popularity";
		ascending.setText(getString(R.string.most_popular));
		descending.setText(getString(R.string.less_popular));
		toggleSortBy();
	}

	private void isAlphabetic() {
		sortBy = "alphabetic";
		ascending.setText(getString(R.string.ascending));
		descending.setText(getString(R.string.descending));
		toggleSortBy();
	}

	private void toggleSortBy() {
		switch (sortBy) {
			case "date":
				dateContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.choosed));
				popularityContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
				alphabeticContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
				break;
			case "popularity":
				dateContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
				popularityContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.choosed));
				alphabeticContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
				break;
			case "alphabetic":
				dateContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
				popularityContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
				alphabeticContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.choosed));
				break;
			default:
				break;
		}
	}

	private void toggleSortMethod() {
		if (sortMethod.equals("ascending")) {
			ascending.setBackgroundColor(ContextCompat.getColor(this, R.color.choosed));
			descending.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
		} else if (sortMethod.equals("descending")) {
			ascending.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
			descending.setBackgroundColor(ContextCompat.getColor(this, R.color.choosed));
		}
	}
}
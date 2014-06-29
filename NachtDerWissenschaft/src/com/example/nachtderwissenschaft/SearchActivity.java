package com.example.nachtderwissenschaft;

import butterknife.ButterKnife;
import com.cengalabs.flatui.FlatUI;
import com.example.dao.DaoSession;
import com.example.fragments.FragmentSearch;
import de.greenrobot.event.EventBus;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class SearchActivity extends FragmentActivity implements ActivityInterface {
	public DaoSession mDaoSession;
	public static final String jsonSearch = "PARAMETERSEARCHJSON";
	private String mQuery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handleIntent(getIntent());
		FlatUI.initDefaultValues(this);
		FlatUI.setDefaultTheme(FlatUI.GRAPE);
		getActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(this, FlatUI.GRAPE, false, 2));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_search);
		ButterKnife.inject(this);

		if (savedInstanceState == null) {
			if (mQuery != null) {
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				fragmentTransaction.add(R.id.activitysearchcontainter, new FragmentSearch().newInstance(mQuery));
				fragmentTransaction.commit();
			}
		}
	}

	public void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			mQuery = intent.getStringExtra(SearchManager.QUERY);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public DaoSession getDaoSession() {
		return mDaoSession;
	}

	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}

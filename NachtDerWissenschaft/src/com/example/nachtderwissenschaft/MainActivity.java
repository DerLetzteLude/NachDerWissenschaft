package com.example.nachtderwissenschaft;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.astuetz.PagerSlidingTabStrip;
import com.cengalabs.flatui.FlatUI;
import com.example.dao.DaoMaster;
import com.example.dao.DaoSession;
import com.example.fragments.FragmentEventList;
import com.example.fragments.FragmentFavoritenListe;
import com.example.fragments.FragmentFilter;
import com.example.fragments.FragmentInstituteList;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.github.amlcurran.*;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionItemTarget;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;

import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import Events.EventSetSortItems;
import Events.ResetEvent;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

public class MainActivity extends FragmentActivity implements ActivityInterface {
	SearchView mSearchView;
	@InjectView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
	@InjectView(R.id.pager) ViewPager pager;
	@InjectView(R.id.tabs) PagerSlidingTabStrip tabs;
	final String PREFS_NAME = "MyPrefsFile";

	DaoSession mDaoSession;
	private MyPagerAdapter adapter;
	EventBus bus = EventBus.getDefault();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FlatUI.initDefaultValues(this);
		FlatUI.setDefaultTheme(FlatUI.GRAPE);
		getActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(this, FlatUI.GRAPE, false, 2));
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);

		adapter = new MyPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(2);

		tabs.setViewPager(pager);
		tabs.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				setMenuItems(arg0);
				bus.post(new ResetEvent());
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		MyDatabase data = new MyDatabase(getApplicationContext());
		data.getReadableDatabase();

		SQLiteOpenHelper helper = new DaoMaster.DevOpenHelper(this, "wissensdatenbank.db", null);
		SQLiteDatabase db = helper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		mDaoSession = daoMaster.newSession();

		if (savedInstanceState == null) {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.add(R.id.right_drawer, new FragmentFilter(), FragmentFilter.TAG);
			fragmentTransaction.commit();
		}
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		if (settings.getBoolean("my_first_time", true)) {
			Log.i("Comments", "First time");
			new ShowcaseView.Builder(this).setTarget(new ActionItemTarget(this, R.id.action_filters))

			.setContentText("Sortierung und Filter").hideOnTouchOutside().setStyle(com.github.amlcurran.showcaseview.R.style.ShowcaseView)
					.hideOnTouchOutside().build();
			settings.edit().putBoolean("my_first_time", false).commit();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mSearchView != null) {
			mSearchView.setQuery("", false);
			mSearchView.setIconified(true);
		}

	}

	void setMenuItems(int i) {
		if (i == 0) {
			String sorters[] = { "Nach Alphabet", "Nach Institut", "Nach Typ" };
			bus.post(new EventSetSortItems(sorters, true));
		}
		if (i == 1) {
			String sorters[] = { "Nach Alphabet", "Nach Haltestelle" };
			bus.post(new EventSetSortItems(sorters, false));
		}
		if (i == 2) {
			String sorters[] = { "Nach Alphabet", "Nach Institut", "Nach Typ" };
			bus.post(new EventSetSortItems(sorters, true));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
		mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		mSearchView.setQueryHint("Suchbegriff");
		mSearchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.RIGHT));
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Crouton.cancelAllCroutons();
		switch (item.getItemId()) {
		case R.id.action_filters:
			if (mDrawerLayout.isDrawerOpen(Gravity.END)) {
				mDrawerLayout.closeDrawer(Gravity.END);
			} else {
				mDrawerLayout.openDrawer(Gravity.END);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public DaoSession getDaoSession() {
		return mDaoSession;
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "Veranstaltungen", "Institute", "Favoriten" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = FragmentEventList.newInstance(null);
				break;
			case 1:
				fragment = FragmentInstituteList.newInstance();
				break;
			case 2:
				fragment = FragmentFavoritenListe.newInstance();
				break;
			}
			return fragment;

		}
	}

	class MyDatabase extends SQLiteAssetHelper {

		private static final String DATABASE_NAME = "wissensdatenbank.db";
		private static final int DATABASE_VERSION = 1;

		public MyDatabase(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
	}

}

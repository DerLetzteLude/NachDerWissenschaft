package com.example.nachtderwissenschaft;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cengalabs.flatui.FlatUI;
import com.example.dao.DaoMaster;
import com.example.dao.DaoSession;
import com.example.dao.Institut;
import com.example.fragments.FragmentEventDetails;
import com.example.fragments.FragmentMap;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MapActivity extends FragmentActivity implements ActivityInterface {
	public DaoSession mDaoSession;
	public static final String jsonInstitut = "PARAMETERINSTITUTJSON";
	FrameLayout mapcontainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FlatUI.initDefaultValues(this);
		FlatUI.setDefaultTheme(FlatUI.GRAPE);
		getActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(this, FlatUI.GRAPE, false, 2));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_map);
		ButterKnife.inject(this);

		SQLiteOpenHelper helper = new DaoMaster.DevOpenHelper(this, "wissensdatenbank.db", null);
		SQLiteDatabase db = helper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		mDaoSession = daoMaster.newSession();
		Bundle bundle = getIntent().getExtras();
		String jsonInstitutString = null;
		Institut institut = null;
		if (bundle != null) {
			jsonInstitutString = bundle.getString(jsonInstitut);
			Gson gson = new Gson();
			institut = gson.fromJson(jsonInstitutString, Institut.class);
		}

		if (savedInstanceState == null) {
			if (institut != null) {
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				fragmentTransaction.add(R.id.activitymapcontainter, new FragmentMap().newInstance(institut), FragmentEventDetails.TAG);
				fragmentTransaction.commit();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.details, menu);
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

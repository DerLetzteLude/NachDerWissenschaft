package com.example.nachtderwissenschaft;

import butterknife.ButterKnife;

import com.cengalabs.flatui.FlatUI;
import com.example.dao.DaoMaster;
import com.example.dao.DaoSession;
import com.example.fragments.FragmentEventDetails;
import com.example.fragments.FragmentMap;
import de.greenrobot.event.EventBus;
import Events.EventFullScreenMap;
import Events.EventOpenMap;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class EventdetailsActivity extends FragmentActivity implements ActivityInterface {
	public DaoSession mDaoSession;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);

		FlatUI.initDefaultValues(this);
		FlatUI.setDefaultTheme(FlatUI.GRAPE);
		getActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(this, FlatUI.GRAPE, false, 2));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_eventdetails);
		ButterKnife.inject(this);

		SQLiteOpenHelper helper = new DaoMaster.DevOpenHelper(this, "wissensdatenbank.db", null);
		SQLiteDatabase db = helper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		mDaoSession = daoMaster.newSession();
		Bundle bundle = getIntent().getExtras();
		String jsonevent = null;
		if (bundle != null) {
			jsonevent = bundle.getString("EVENT");
		}
		if (savedInstanceState == null) {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			fragmentTransaction
					.add(R.id.eventdetailscontainer, new FragmentEventDetails().newInstance(jsonevent), FragmentEventDetails.TAG);
			fragmentTransaction.commit();
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

	public void onEvent(EventOpenMap event) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		Fragment frag = FragmentMap.newInstance(event.getInstitut());
		fragmentTransaction.replace(R.id.mapcontainer, frag);
		fragmentTransaction.commit();
	}
	
	public void onEvent(EventFullScreenMap event){
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		Fragment frag = FragmentMap.newInstance(event.getInstitut());
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.replace(R.id.eventdetailscontainer, frag);
		fragmentTransaction.commit();
	}

}

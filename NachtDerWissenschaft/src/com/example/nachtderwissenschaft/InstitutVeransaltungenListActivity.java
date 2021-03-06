package com.example.nachtderwissenschaft;

import com.cengalabs.flatui.FlatUI;
import com.example.dao.DaoMaster;
import com.example.dao.DaoSession;
import com.example.fragments.FragmentEventDetails;
import com.example.fragments.FragmentEventList;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class InstitutVeransaltungenListActivity extends FragmentActivity implements ActivityInterface {
	public DaoSession mDaoSession;
	public static final String PARAMETERINSTITUTJSON = "PARAMETERINSTITUTJSON";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FlatUI.initDefaultValues(this);
		FlatUI.setDefaultTheme(FlatUI.GRAPE);		
		getActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(this, FlatUI.GRAPE, false, 2));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_eventdetails);

		SQLiteOpenHelper helper = new DaoMaster.DevOpenHelper(this, "wissensdatenbank.db", null);
		SQLiteDatabase db = helper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		mDaoSession = daoMaster.newSession();

		
	
		
		String mInstitutString = getIntent().getStringExtra(InstitutVeransaltungenListActivity.PARAMETERINSTITUTJSON);
		
		if (savedInstanceState == null) {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			new FragmentEventList();
			fragmentTransaction.add(R.id.eventdetailscontainer, FragmentEventList.newInstance(mInstitutString), FragmentEventDetails.TAG);
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
}

package com.example.fragments;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cengalabs.flatui.views.FlatTextView;
import com.example.adapters.AdapterVeranstaltung;
import com.example.dao.DaoMaster;
import com.example.dao.DaoSession;
import com.example.dao.Veranstaltung;
import com.example.nachtderwissenschaft.EventdetailsActivity;
import com.example.nachtderwissenschaft.R;
import com.google.gson.Gson;
import com.nirhart.parallaxscroll.views.ParallaxListView;
import com.quentindommerc.superlistview.SuperListview;

public class FragmentSearch extends Fragment {

	@InjectView(R.id.searchlist) ParallaxListView searchList;

	public static final String TAG = "FRAGMENTSEARCH";
	private AdapterVeranstaltung mAdapter;
	private boolean hasParallaxHeader = false;
	String mQuerry;

	public FragmentSearch() {
		super();
	}

	public static FragmentSearch newInstance(String search) {
		FragmentSearch f = new FragmentSearch();
		Bundle args = new Bundle();
		args.putString("SEARCH", search);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mQuerry = getArguments().getString("SEARCH");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_search, container, false);
		ButterKnife.inject(this, v);
		TextView headerText;
		headerText = new TextView(getActivity());
		headerText.setHeight(200);
		headerText.setTextSize(40);
		headerText.setTextColor(getResources().getColor(com.cengalabs.flatui.R.color.grape_primary));
		headerText.setText("Suche: " + mQuerry);
		headerText.setGravity(Gravity.CENTER);
		headerText.setTypeface(null, Typeface.BOLD);
		headerText.setBackgroundResource(R.drawable.card_background_padding);
		searchList.addParallaxedHeaderView(headerText);
		hasParallaxHeader = true;
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		SQLiteOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "wissensdatenbank.db", null);
		SQLiteDatabase db = helper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		DaoSession mDaoSession = daoMaster.newSession();
		mAdapter = new AdapterVeranstaltung(getActivity(), R.layout.listitem_veranstaltung, mDaoSession);
		searchList.setAdapter(mAdapter);
		searchList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				int i = arg2;
				if (hasParallaxHeader) {
					i = i - 1;
				}
				Intent intent = new Intent(getActivity(), EventdetailsActivity.class);
				Gson gson = new Gson();
				Veranstaltung event = mAdapter.getItem(i);
				intent.putExtra("EVENT", gson.toJson(event));
				startActivity(intent);
			}
		});
		mAdapter.setFavorite(false);
		mAdapter.searchEvents(mQuerry);

	}

}

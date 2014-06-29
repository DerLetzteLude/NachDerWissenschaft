package com.example.fragments;

import com.example.adapters.AdapterVeranstaltung;
import com.example.dao.Institut;
import com.example.dao.Veranstaltung;
import com.example.nachtderwissenschaft.ActivityInterface;
import com.example.nachtderwissenschaft.EventdetailsActivity;
import com.example.nachtderwissenschaft.R;
import com.google.gson.Gson;
import com.nirhart.parallaxscroll.views.ParallaxListView;

import de.greenrobot.event.EventBus;
import icepick.Icepick;
import butterknife.ButterKnife;
import butterknife.InjectView;
import Events.EventSortList;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class FragmentEventList extends Fragment {

	@InjectView(R.id.parallaxlist) ParallaxListView listView;

	public static final String TAG = "FRAGMENTEVENTLIST";

	private EventBus bus = EventBus.getDefault();
	private AdapterVeranstaltung mAdapter;
	private ActivityInterface mInterface;
	private Institut mInstitut;
	private boolean hasParallaxHeader = false;
	TextView headerText;

	public static FragmentEventList newInstance(String institutJson) {
		FragmentEventList f = new FragmentEventList();
		Bundle args = new Bundle();
		args.putString("institutJson", institutJson);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mInterface = (ActivityInterface) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement ActivityInterface");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_eventlist, container, false);
		ButterKnife.inject(this, v);
		if (mInstitut != null) {
			headerText = new TextView(getActivity());
			headerText.setHeight(200);
			headerText.setTextSize(30);
			headerText.setTextColor(getResources().getColor(com.cengalabs.flatui.R.color.grape_primary));
			headerText.setText("PARALLAXED");
			headerText.setGravity(Gravity.CENTER);
			headerText.setTypeface(null, Typeface.BOLD);
			headerText.setText(mInstitut.getName());
			headerText.setBackgroundResource(R.drawable.card_background_padding);
			listView.addParallaxedHeaderView(headerText);
		}

		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mAdapter = new AdapterVeranstaltung(getActivity(), R.layout.listitem_veranstaltung, mInterface.getDaoSession());
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

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
		if (mInstitut != null) {
			mAdapter.setInstitut(mInstitut);
		}
		mAdapter.setSorting(new EventSortList(EventSortList.SORT_NAME));
		mAdapter.loadEvents();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Icepick.restoreInstanceState(this, savedInstanceState);
		bus.register(this);
		Bundle bundle = this.getArguments();
		String institutString = bundle.getString("institutJson");

		if (institutString != null && institutString.length() > 0) {
			Gson gson = new Gson();
			mInstitut = gson.fromJson(institutString, Institut.class);
		}
		if (mInstitut != null) {
			hasParallaxHeader = true;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Icepick.saveInstanceState(this, outState);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void onEvent(EventSortList pojo) {
		Gson gson = new Gson();
		String currentSortString = gson.toJson(mAdapter.getSorting());
		String newSorting = gson.toJson(pojo);
		if (currentSortString.contentEquals(newSorting)) {
			Log.i("TEST", "gleicher inhalt");
		} else {
			mAdapter.setSorting(pojo);
			mAdapter.loadEvents();
		}
	}

}

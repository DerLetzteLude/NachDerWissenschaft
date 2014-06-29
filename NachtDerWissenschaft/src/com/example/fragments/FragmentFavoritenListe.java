package com.example.fragments;

import com.cengalabs.flatui.views.FlatTextView;
import com.example.adapters.AdapterVeranstaltung;
import com.example.dao.InstitutDao;
import com.example.dao.Veranstaltung;
import com.example.dao.VeranstaltungDao;
import com.example.nachtderwissenschaft.ActivityInterface;
import com.example.nachtderwissenschaft.EventdetailsActivity;
import com.example.nachtderwissenschaft.R;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import icepick.Icepick;
import butterknife.ButterKnife;
import Events.EventSortList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FragmentFavoritenListe extends Fragment {

	public static final String TAG = "FRAGMENTFAVORITENLISTE";
	FlatTextView txtEmpty;
	ListView listView;
	AdapterVeranstaltung mAdapter;
	ActivityInterface mInterface;
	VeranstaltungDao mVeranstaltungDao;
	InstitutDao mInstitutDao;

	public ActionMode mActionMode;

	public static FragmentFavoritenListe newInstance() {
		FragmentFavoritenListe f = new FragmentFavoritenListe();
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
		View v = inflater.inflate(R.layout.fragment_favoriten, container, false);
		listView = (ListView) v.findViewById(R.id.searchlist);
		txtEmpty = (FlatTextView) v.findViewById(R.id.emptyview);
		ButterKnife.inject(this, v);
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
				Intent intent = new Intent(getActivity(), EventdetailsActivity.class);
				Gson gson = new Gson();
				Veranstaltung event = mAdapter.getItem(arg2);
				intent.putExtra("EVENT", gson.toJson(event));
				startActivity(intent);
			}
		});
		mVeranstaltungDao = mInterface.getDaoSession().getVeranstaltungDao();
		mInstitutDao = mInterface.getDaoSession().getInstitutDao();
		mAdapter.setFavorite(true);
		mAdapter.loadEvents();
		if (mAdapter.isEmpty()) {
			txtEmpty.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Icepick.restoreInstanceState(this, savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Icepick.saveInstanceState(this, outState);
	}

	@Override
	public void onPause() {
		Crouton.cancelAllCroutons();
		super.onPause();
	}	

	public void onEvent(EventSortList pojo) {
		mAdapter.setSorting(pojo);
		mAdapter.loadEvents();

	}



}

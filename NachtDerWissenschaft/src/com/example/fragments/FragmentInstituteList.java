package com.example.fragments;

import com.example.adapters.AdapterInstitut;
import com.example.dao.Institut;
import com.example.dao.InstitutDao;
import com.example.dao.VeranstaltungDao;
import com.example.nachtderwissenschaft.ActivityInterface;
import com.example.nachtderwissenschaft.InstitutVeransaltungenListActivity;
import com.example.nachtderwissenschaft.MapActivity;
import com.example.nachtderwissenschaft.R;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;
import icepick.Icepick;
import butterknife.ButterKnife;
import butterknife.InjectView;
import Events.EventSortList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentInstituteList extends Fragment {

	MenuItem mMenuFilter;
	public static final String TAG = "FRAGMENTINSTITUTE";
	@InjectView(R.id.searchlist) ListView listView;
	AdapterInstitut mAdapter;
	ActivityInterface mInterface;
	VeranstaltungDao mVeranstaltungDao;
	InstitutDao mInstitutDao;

	public static FragmentInstituteList newInstance() {
		FragmentInstituteList f = new FragmentInstituteList();
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mInterface = (ActivityInterface) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement ActivityInterface");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main, container, false);
		ButterKnife.inject(this, v);

		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Institut institut = mAdapter.getItem(arg2);
				Gson gson = new Gson();
				String InstitutString = gson.toJson(institut, Institut.class);
				Intent intent = new Intent(getActivity(), InstitutVeransaltungenListActivity.class);
				intent.putExtra(InstitutVeransaltungenListActivity.PARAMETERINSTITUTJSON, InstitutString);
				startActivity(intent);

			}
		});
		mAdapter = new AdapterInstitut(getActivity(), R.layout.listitem_institut, mInterface.getDaoSession(), new MapClickListener() {

			@Override
			public void onBtnClick(int position) {
				Institut inst = mAdapter.getItem(position);
				Gson gson = new Gson();
				String InstitutString = gson.toJson(inst, Institut.class);
				Intent intent = new Intent(getActivity(), MapActivity.class);
				intent.putExtra(MapActivity.jsonInstitut, InstitutString);
				startActivity(intent);
				// double latitude =
				// Double.valueOf(inst.getHaltestelleBreite());
				// double longitude =
				// Double.valueOf(inst.getHaltestelleLaenge());
				// String label = inst.getName();
				// String uriBegin = "geo:" + latitude + "," + longitude;
				// String query = latitude + "," + longitude + "(" + label +
				// ")";
				// String encodedQuery = Uri.encode(query);
				// String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
				// Uri uri = Uri.parse(uriString);
				// Intent mapIntent = new
				// Intent(android.content.Intent.ACTION_VIEW, uri);
				// startActivity(mapIntent);

			}
		});
		listView.setAdapter(mAdapter);
		mVeranstaltungDao = mInterface.getDaoSession().getVeranstaltungDao();
		mInstitutDao = mInterface.getDaoSession().getInstitutDao();
		mAdapter.loadInstituts();
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
		if (mMenuFilter != null) {
			mMenuFilter.setVisible(true);
		}
		super.onPause();

	}

	public void onEvent(EventSortList pojo) {
		Gson gson = new Gson();
		String currentSort = gson.toJson(mAdapter.getSorting());
		String newSort = gson.toJson(pojo);
		if (currentSort.contentEquals(newSort)) {
			Log.i("TEST", "gleicher Inhalt institute");
		} else {
			mAdapter.setSorting(pojo);
			mAdapter.loadInstituts();
		}
	}

	public interface MapClickListener {
		public abstract void onBtnClick(int position);
	}

}

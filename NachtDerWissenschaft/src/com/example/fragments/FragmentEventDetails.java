package com.example.fragments;

import com.cengalabs.flatui.views.FlatTextView;
import com.example.dao.Institut;
import com.example.dao.InstitutDao;
import com.example.dao.Veranstaltung;
import com.example.dao.VeranstaltungDao;
import com.example.nachtderwissenschaft.ActivityInterface;
import com.example.nachtderwissenschaft.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import icepick.Icepick;
import Events.EventCheckFavorites;
import Events.EventFullScreenMap;
import Events.EventOnMapTouch;
import Events.EventOpenMap;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class FragmentEventDetails extends Fragment {

	@InjectView(R.id.detailtitle) FlatTextView txtVeranstaltungTitle;
	@InjectView(R.id.detailtext) FlatTextView txtVeranstaltungText;
	@InjectView(R.id.zeit) FlatTextView txtZeit;
	@InjectView(R.id.imageHeader) ImageView imgHeader;
	@InjectView(R.id.detailscrollview) ParallaxScrollView mScrollView;
	@InjectView(R.id.mapcontainer) FrameLayout mMapContainer;
	@InjectView(R.id.haltestelle) FlatTextView txtHaltestelle;
	@InjectView(R.id.institutname) FlatTextView txtInstitutName;
	@InjectView(R.id.buttonfullscreenmap) ImageButton btnFullScreenMap;

	public static final String TAG = "FRAGMENTEVENTDETAILS";
	VeranstaltungDao mVeranstaltungsDao;
	InstitutDao mInstitutDao;
	ActivityInterface mInterface;
	Veranstaltung mVeranstaltung;
	Institut mInstitut;
	Veranstaltung mEvent;

	MenuItem menuFavorite;

	public static FragmentEventDetails newInstance(String jsonEvent) {
		FragmentEventDetails f = new FragmentEventDetails();
		Bundle args = new Bundle();
		args.putString("EVENT", jsonEvent);
		f.setArguments(args);
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
		View v = inflater.inflate(R.layout.fragment_eventdetails, container, false);
		ButterKnife.inject(this, v);
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mVeranstaltungsDao = mInterface.getDaoSession().getVeranstaltungDao();
		mInstitutDao = mInterface.getDaoSession().getInstitutDao();
		String jsonevent = getArguments().getString("EVENT");
		Gson gson = new Gson();
		mVeranstaltung = gson.fromJson(jsonevent, Veranstaltung.class);
		mInstitut = mInstitutDao.load(mVeranstaltung.getInstitutId());
		txtVeranstaltungTitle.setText(mVeranstaltung.getTitel());
		txtVeranstaltungText.setText(mVeranstaltung.getInhalt());
		txtHaltestelle.setText(mInstitut.getHaltestelleName());
		txtInstitutName.setText(mInstitut.getName());
		txtZeit.setText(mVeranstaltung.getZeit());

		Picasso.with(getActivity()).load(R.drawable.img_halle_night).centerCrop().fit().into(imgHeader);
		// Playservice check
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
		if (status == ConnectionResult.SUCCESS) {
			EventBus bus = EventBus.getDefault();
			bus.post(new EventOpenMap(mInstitut, mVeranstaltung));
			btnFullScreenMap.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					EventBus bus2 = EventBus.getDefault();
					bus2.post(new EventFullScreenMap(mInstitut,mVeranstaltung));

				}
			});
		} else {
			ImageView imageView = new ImageView(getActivity());
			imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			Picasso.with(getActivity()).load(mVeranstaltung.getMapUrl()).into(imageView);
			mMapContainer.addView(imageView);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		EventBus.getDefault().register(this);
		super.onCreate(savedInstanceState);
		Icepick.restoreInstanceState(this, savedInstanceState);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Icepick.saveInstanceState(this, outState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_favorite:
			
			mVeranstaltung.setFavorit(!mVeranstaltung.getFavorit());
			mVeranstaltungsDao.update(mVeranstaltung);
			if (mVeranstaltung.getFavorit()) {
				Crouton.makeText(getActivity(), "gespeicher in Favoriten", Style.CONFIRM).show();
				
			}
			EventBus.getDefault().post(new EventCheckFavorites());
			favoriteCheck();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menuFavorite = menu.findItem(R.id.action_favorite);
		menuFavorite.setVisible(true);
		favoriteCheck();
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onPause() {
		Crouton.cancelAllCroutons();
		super.onPause();
	}

	public void favoriteCheck() {
		if (mVeranstaltung != null && mVeranstaltung.getFavorit() != null) {
			if (mVeranstaltung.getFavorit()) {				
				menuFavorite.setIcon(android.R.drawable.btn_star_big_on);
			} else {			
				menuFavorite.setIcon(android.R.drawable.btn_star_big_off);
			}
		}
	}

	public void onEvent(EventOnMapTouch maptouch) {
		mScrollView.requestDisallowInterceptTouchEvent(true);
	}

}

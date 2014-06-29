package com.example.fragments;

import Events.EventOnMapTouch;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.dao.Institut;
import com.example.dao.Veranstaltung;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

public class FragmentMap extends SupportMapFragment implements GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	Institut institut;	
	GoogleMap map;
	LatLng latlng;
	MarkerOptions markerUser;
	MarkerOptions markerZiel;
	private LocationRequest mLocationRequest;
	private LocationClient mLocationClient;

	public FragmentMap() {
		super();

	}

	public static FragmentMap newInstance(Veranstaltung ver, Institut inst) {
		FragmentMap f = new FragmentMap();
		Bundle args = new Bundle();
		Gson gson = new Gson();
		args.putString("EVENT", gson.toJson(ver, Veranstaltung.class));
		args.putString("INSTITUT", gson.toJson(inst, Institut.class));
		f.setArguments(args);
		return f;
	}
	
	public static FragmentMap newInstance(Institut inst) {
		FragmentMap f = new FragmentMap();
		Bundle args = new Bundle();
		Gson gson = new Gson();		
		args.putString("INSTITUT", gson.toJson(inst, Institut.class));
		f.setArguments(args);
		return f;
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		String jsonInstitut = getArguments().getString("INSTITUT");
		Gson gson = new Gson();		
		institut = gson.fromJson(jsonInstitut, Institut.class);
		mLocationClient = new LocationClient(this.getActivity().getApplicationContext(), this, this);
		mLocationClient.connect();

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
		View v = super.onCreateView(layoutInflater, viewGroup, bundle);
		TouchableWrapper frameLayout = new TouchableWrapper(getActivity());
		frameLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		((ViewGroup) v).addView(frameLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		initMap();
		return v;
	}

	private void initMap() {
		map = getMap();
		if (map != null) {
			map.getUiSettings().setRotateGesturesEnabled(false);
			map.getUiSettings().setTiltGesturesEnabled(false);
			map.setMyLocationEnabled(false);
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

			LatLng position = new LatLng(Double.valueOf(institut.getMarkerBreite()), Double.valueOf(institut.getMarkerLaenge()));
			markerZiel = new MarkerOptions();
			markerZiel.position(position);
			markerZiel.title("Institut");
			markerZiel.snippet(institut.getName());
			map.addMarker(markerZiel);

			CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(14).build();
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
	}

	public void setUserPosition(Location arg0) {
		map.clear();
		if (markerZiel != null) {
			map.addMarker(markerZiel);
		}
		if (markerUser != null) {
			LatLng positionUser = new LatLng(arg0.getLatitude(), arg0.getLongitude());
			markerUser.position(positionUser);
			map.addMarker(markerUser);
		} else {
			LatLng positionUser = new LatLng(arg0.getLatitude(), arg0.getLongitude());
			markerUser = new MarkerOptions();
			markerUser.position(positionUser);
			markerUser.title("Ich");
			markerUser.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
			map.addMarker(markerUser);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	public class TouchableWrapper extends FrameLayout {

		public TouchableWrapper(Context context) {
			super(context);
		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent event) {
			EventBus bus = EventBus.getDefault();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				bus.post(new EventOnMapTouch());
				break;
			case MotionEvent.ACTION_UP:
				bus.post(new EventOnMapTouch());
				break;
			}
			return super.dispatchTouchEvent(event);
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		Location mCurrentLocation = mLocationClient.getLastLocation();
		if (mCurrentLocation != null) {
			LatLng positionUser = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
			markerUser = new MarkerOptions();
			markerUser.position(positionUser);
			markerUser.title("Ich");
			markerUser.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
			map.addMarker(markerUser);
		}
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		int MILLISECONDS_PER_SECOND = 1000;

		int UPDATE_INTERVAL_IN_SECONDS = 5;
		long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

		int FASTEST_INTERVAL_IN_SECONDS = 1;

		long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

		mLocationRequest.setInterval(UPDATE_INTERVAL);

		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		mLocationClient.requestLocationUpdates(mLocationRequest, this);

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location arg0) {
		setUserPosition(arg0);

	}

	@Override
	public void onPause() {
		if (mLocationClient != null) {
			mLocationClient.removeLocationUpdates(this);
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		int MILLISECONDS_PER_SECOND = 1000;

		int UPDATE_INTERVAL_IN_SECONDS = 5;
		long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

		int FASTEST_INTERVAL_IN_SECONDS = 1;

		long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

		mLocationRequest.setInterval(UPDATE_INTERVAL);

		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		
		if(mLocationClient!=null && mLocationClient.isConnected()){
			mLocationClient.requestLocationUpdates(mLocationRequest, this);
		}
		
		super.onResume();
	}

}

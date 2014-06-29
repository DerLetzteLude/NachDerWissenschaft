package com.example.fragments;

import com.dd.CircularProgressButton;
import com.example.nachtderwissenschaft.ActivityInterface;
import com.example.nachtderwissenschaft.R;

import icepick.Icepick;
import butterknife.ButterKnife;
import butterknife.InjectView;
import Events.EventSetSortItems;
import Events.EventSortList;
import Events.ResetEvent;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import de.greenrobot.event.EventBus;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TableLayout;

public class FragmentFilter extends Fragment {

	public static final String TAG = "FRAGMENTFILTER";

	@InjectView(R.id.filterbarrierefrei) CircularProgressButton mfilterBarriere;
	@InjectView(R.id.filterbisdreizehn) CircularProgressButton mfilterBisDreizen;
	@InjectView(R.id.filterfamilienfreundlich) CircularProgressButton mfilterFamilienfreundlich;
	@InjectView(R.id.filterjugendliche) CircularProgressButton mfilterJugendliche;
	@InjectView(R.id.filterwissenschaftsjahr) CircularProgressButton mfilterWissenschaftsjahr;
	@InjectView(R.id.filterlayout) TableLayout mFilterLayout;
	@InjectView(R.id.sortingspinner) Spinner mSpinner;
	EventBus bus = EventBus.getDefault();
	ActivityInterface mInterface;

	boolean boolFilterBarrierefrei = false;
	boolean boolFilterBisDreizehn = false;
	boolean boolFilterFamilienfreundlich = false;
	boolean boolFilterJugendliche = false;
	boolean boolFilterWissenschaftsjahr = false;

	int sorting = 0;

	OnCheckedChangeListener listener;

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
		View v = inflater.inflate(R.layout.fragment_filterdrawer, container, false);
		ButterKnife.inject(this, v);
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		String sorters[] = { "Nach Alphabet", "nach Institut", "Nach Typ" };
		setSortItems(new EventSetSortItems(sorters, true));

		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.filterbarrierefrei:					
					if (mfilterBarriere.getProgress() == 0) {
						setChecked(mfilterBarriere);
						boolFilterBarrierefrei = true;
					} else {
						mfilterBarriere.setProgress(0);
						boolFilterBarrierefrei = false;
					}
					applyFilters();
					break;
				case R.id.filterbisdreizehn:					
					if (mfilterBisDreizen.getProgress() == 0) {
						setChecked(mfilterBisDreizen);
						boolFilterBisDreizehn = true;
					} else {
						mfilterBisDreizen.setProgress(0);
						boolFilterBisDreizehn = false;
					}
					applyFilters();
					break;
				case R.id.filterfamilienfreundlich:					
					if (mfilterFamilienfreundlich.getProgress() == 0) {
						setChecked(mfilterFamilienfreundlich);
						boolFilterFamilienfreundlich = true;
					} else {
						mfilterFamilienfreundlich.setProgress(0);
						boolFilterFamilienfreundlich = false;
					}
					applyFilters();
					break;
				case R.id.filterjugendliche:				
					if (mfilterJugendliche.getProgress() == 0) {
						setChecked(mfilterJugendliche);
						boolFilterJugendliche = true;
					} else {
						mfilterJugendliche.setProgress(0);
						boolFilterJugendliche = false;
					}
					applyFilters();
					break;
				case R.id.filterwissenschaftsjahr:					
					if (mfilterWissenschaftsjahr.getProgress() == 0) {
						setChecked(mfilterWissenschaftsjahr);
						boolFilterWissenschaftsjahr = true;
					} else {
						mfilterWissenschaftsjahr.setProgress(0);
						boolFilterWissenschaftsjahr = false;
					}
					applyFilters();
					break;
				default:
					break;
				}
			}

		};
		mfilterBarriere.setOnClickListener(listener);
		mfilterBisDreizen.setOnClickListener(listener);
		mfilterFamilienfreundlich.setOnClickListener(listener);
		mfilterJugendliche.setOnClickListener(listener);
		mfilterWissenschaftsjahr.setOnClickListener(listener);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Icepick.restoreInstanceState(this, savedInstanceState);
		bus.register(this);
		setHasOptionsMenu(true);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.i("TEST", "Filter onSaveInstanceState");
		super.onSaveInstanceState(outState);
		Icepick.saveInstanceState(this, outState);

	}

	public void applyFilters() {
		EventSortList pojo = new EventSortList();
		pojo.setBoolFilterBarrierefrei(boolFilterBarrierefrei);
		pojo.setBoolFilterBisDreizehn(boolFilterBisDreizehn);
		pojo.setBoolFilterFamilienfreundlich(boolFilterFamilienfreundlich);
		pojo.setBoolFilterJugendliche(boolFilterJugendliche);
		pojo.setBoolFilterWissenschaftsjahr(boolFilterWissenschaftsjahr);
		pojo.setSorting(sorting);
		bus.post(pojo);
	}

	private void setChecked(final CircularProgressButton button) {
		ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
		widthAnimation.setDuration(500);
		widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Integer value = (Integer) animation.getAnimatedValue();
				button.setProgress(value);
			}
		});
		widthAnimation.start();
	}

	public void setSortItems(EventSetSortItems event) {
		final String[] sorterItems = event.getSorters();
		final Boolean showFilters = event.getFilterVisible();
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
				event.getSorters());
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(spinnerArrayAdapter);

		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				if (sorterItems[arg2].contentEquals("Nach Alphabet")) {
					sorting = EventSortList.SORT_NAME;
				}
				if (sorterItems[arg2].contentEquals("Nach Institut")) {
					sorting = EventSortList.SORT_INSTITUT;
				}
				if (sorterItems[arg2].contentEquals("Nach Typ")) {
					sorting = EventSortList.SORT_TYPE;
				}
				if (sorterItems[arg2].contentEquals("Nach Haltestelle")) {
					sorting = EventSortList.SORT_STOP;
				}
				if (showFilters) {
					mFilterLayout.setVisibility(View.VISIBLE);
				} else {
					mFilterLayout.setVisibility(View.GONE);
				}
				applyFilters();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void onEvent(ResetEvent reset) {
		boolFilterBarrierefrei = false;
		boolFilterBisDreizehn = false;
		boolFilterFamilienfreundlich = false;
		boolFilterJugendliche = false;
		boolFilterWissenschaftsjahr = false;
		mfilterBarriere.setProgress(0);
		mfilterBisDreizen.setProgress(0);
		mfilterFamilienfreundlich.setProgress(0);
		mfilterJugendliche.setProgress(0);
		mfilterWissenschaftsjahr.setProgress(0);
		sorting = 0;
		bus.post(new EventSortList());
	}

	public void onEvent(EventSetSortItems event) {
		setSortItems(event);
	}

}

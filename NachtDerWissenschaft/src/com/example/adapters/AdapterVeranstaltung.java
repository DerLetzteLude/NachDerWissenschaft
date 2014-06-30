package com.example.adapters;

import com.cengalabs.flatui.views.FlatTextView;
import com.example.dao.DaoSession;
import com.example.dao.Institut;
import com.example.dao.InstitutDao;
import com.example.dao.Veranstaltung;
import com.example.dao.VeranstaltungDao;
import com.example.dao.VeranstaltungDao.Properties;
import com.example.nachtderwissenschaft.R;
import com.google.android.gms.internal.gt;

import de.greenrobot.dao.query.QueryBuilder;
import Events.EventSortList;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterVeranstaltung extends ArrayAdapter<Veranstaltung> {
	private Context mContext;
	private int mLayoutResourceId;
	private InstitutDao mInstitutDao;
	private VeranstaltungDao mVeranstaltungDao;
	EventSortList mSorting;
	private int mLastPosition = -1;

	Institut mInstitut;
	Boolean mFavorites = false;

	public AdapterVeranstaltung(Context context, int resource, DaoSession daoSession) {
		super(context, resource);
		mContext = context;
		mLayoutResourceId = resource;
		mInstitutDao = daoSession.getInstitutDao();
		mVeranstaltungDao = daoSession.getVeranstaltungDao();
		mSorting = new EventSortList();
	}

	public void setSorting(EventSortList sort) {
		mSorting = sort;
	}

	public EventSortList getSorting() {
		return mSorting;
	}

	public Institut getmInstitut() {
		return mInstitut;
	}

	public void setInstitut(Institut mInstitut) {
		this.mInstitut = mInstitut;
	}

	public void setFavorite(Boolean favorites) {
		mFavorites = favorites;
	}

	public void resetAdapter() {
		mSorting = new EventSortList();

	}

	public void searchEvents(String querry) {
		this.clear();
		QueryBuilder<Veranstaltung> qb = mVeranstaltungDao.queryBuilder();
		qb.whereOr(Properties.Titel.like("%" + querry + "%"), Properties.Inhalt.like("%" + querry + "%"));
		this.addAll(qb.list());
	}

	public void loadEvents() {
		this.clear();
		QueryBuilder<Veranstaltung> qb = mVeranstaltungDao.queryBuilder();
		if (mInstitut != null) {
			qb.where(Properties.InstitutId.eq(mInstitut.getId()));
		}
		if (mSorting != null) {
			if (mSorting.getSorting() == EventSortList.SORT_NAME) {
				qb.orderAsc(Properties.Titel);
			}
			if (mSorting.getSorting() == EventSortList.SORT_INSTITUT) {
				qb.orderAsc(Properties.InstitutId);
			}
			if (mSorting.getSorting() == EventSortList.SORT_TYPE) {
				qb.orderAsc(Properties.Typ);
			}
			if (mSorting.isBoolFilterBarrierefrei()) {
				qb.where(Properties.Barrierefrei.eq(true));
			}
			if (mSorting.isBoolFilterBisDreizehn()) {
				qb.where(Properties.BisDreizehn.eq(true));
			}
			if (mSorting.isBoolFilterFamilienfreundlich()) {
				qb.where(Properties.Familie.eq(true));
			}
			if (mSorting.isBoolFilterJugendliche()) {
				qb.where(Properties.Jugendliche.eq(true));
			}
			if (mSorting.isBoolFilterWissenschaftsjahr()) {
				qb.where(Properties.Wissenschaftsjahr.eq(true));
			}
		}
		if (mFavorites) {
			qb.where(Properties.Favorit.eq(true));
		}

		this.addAll(qb.list());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(mContext, mLayoutResourceId, null);
			holder = new ViewHolder();
			holder.txtInstitut = (FlatTextView) convertView.findViewById(R.id.institut);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.veranstaltungtitle);
			holder.topLeft = (ImageView) convertView.findViewById(R.id.imagetopleft);
			holder.topRight = (ImageView) convertView.findViewById(R.id.imagetopright);
			holder.bottomLeft = (ImageView) convertView.findViewById(R.id.imagebottomleft);
			holder.bottomRight = (ImageView) convertView.findViewById(R.id.imagebottomright);
			holder.txtTyp = (FlatTextView) convertView.findViewById(R.id.veranstaltungstyp);
			holder.txtZeit = (FlatTextView) convertView.findViewById(R.id.zeit);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		holder.bottomLeft.setVisibility(View.INVISIBLE);
		holder.bottomRight.setVisibility(View.INVISIBLE);
		holder.topLeft.setVisibility(View.INVISIBLE);
		holder.topRight.setVisibility(View.INVISIBLE);
		Veranstaltung ver = this.getItem(position);
		holder.txtTitle.setText(this.getItem(position).getTitel());
		holder.txtInstitut.setText(mInstitutDao.load(this.getItem(position).getInstitutId()).getName());
		holder.txtTyp.setText(this.getItem(position).getTyp());
		holder.txtZeit.setText(this.getItem(position).getZeit());

		if (ver.getBarrierefrei()) {
			holder.topLeft.setVisibility(View.VISIBLE);
		}
		if (ver.getBisDreizehn()) {
			holder.topRight.setVisibility(View.VISIBLE);
		}
		if (ver.getFamilie()) {
			holder.bottomLeft.setVisibility(View.VISIBLE);
		}
		if (ver.getJugendliche()) {
			holder.bottomRight.setVisibility(View.VISIBLE);
		}

		TranslateAnimation animation = null;
		if (position > mLastPosition) {
			animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

			animation.setDuration(600);
			convertView.startAnimation(animation);
			mLastPosition = position;
		}

		return convertView;
	}

	static class ViewHolder {
		public TextView txtTitle;
		public FlatTextView txtInstitut, txtTyp, txtZeit;
		public ImageView topLeft, topRight, bottomLeft, bottomRight;

	}

}

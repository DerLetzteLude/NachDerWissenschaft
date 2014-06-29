package com.example.adapters;

import com.cengalabs.flatui.views.FlatTextView;
import com.example.dao.DaoSession;
import com.example.dao.Institut;
import com.example.dao.InstitutDao;
import com.example.dao.InstitutDao.Properties;
import com.example.dao.Veranstaltung;
import com.example.dao.VeranstaltungDao;
import com.example.fragments.FragmentInstituteList.MapClickListener;
import com.example.nachtderwissenschaft.R;

import de.greenrobot.dao.query.QueryBuilder;
import Events.EventSortList;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterInstitut extends ArrayAdapter<Institut> {

	private Context mContext;
	private int mLayoutResourceId;
	private InstitutDao mInstitutdao;
	private VeranstaltungDao mVeranstaltungsDao;
	private MapClickListener mapClickListener;
	private EventSortList mSorting;
	private Institut mInstitut;

	public AdapterInstitut(Context context, int resource, DaoSession daoSession, MapClickListener mapClickListener) {
		super(context, resource);
		mContext = context;
		mLayoutResourceId = resource;
		mInstitutdao = daoSession.getInstitutDao();
		mVeranstaltungsDao = daoSession.getVeranstaltungDao();
		this.mapClickListener = mapClickListener;

	}

	public EventSortList getSorting() {
		return mSorting;
	}

	public AdapterInstitut(Context context, int resource, DaoSession daoSession) {
		super(context, resource);
		mContext = context;
		mLayoutResourceId = resource;
		mInstitutdao = daoSession.getInstitutDao();
		mVeranstaltungsDao = daoSession.getVeranstaltungDao();
	}
	
	public void setSorting(EventSortList sort) {
		mSorting = sort;
	}
	
	public void resetAdapter() {
		mSorting = null;		
	}

	public void loadInstituts() {

		this.clear();
		QueryBuilder<Institut> qb = mInstitutdao.queryBuilder();
		if (mSorting != null) {
			if (mSorting.getSorting() == EventSortList.SORT_NAME) {
				qb.orderAsc(Properties.Name);
			}
			if (mSorting.getSorting() == EventSortList.SORT_STOP) {
				qb.orderAsc(Properties.HaltestelleName);
			}
		}
		this.addAll(qb.list());

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(mContext, mLayoutResourceId, null);
			holder = new ViewHolder();
			holder.txtInstitut = (FlatTextView) convertView.findViewById(R.id.instituttitel);
			holder.txtHaltestelle = (TextView) convertView.findViewById(R.id.haltestelle);
			holder.txtAnzahl = (TextView) convertView.findViewById(R.id.veranstaltungsanzahl);
			holder.imgImage = (ImageView) convertView.findViewById(R.id.institutimage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		Institut inst = this.getItem(position);
		holder.txtInstitut.setText(inst.getName());
		QueryBuilder<Veranstaltung> qb = mVeranstaltungsDao.queryBuilder();
		qb.where(VeranstaltungDao.Properties.InstitutId.eq(inst.getId()));
		holder.txtAnzahl.setText(String.valueOf(qb.count()));
		holder.txtHaltestelle.setText(inst.getHaltestelleName());
		final int number = position;
		holder.imgImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mapClickListener.onBtnClick(number);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		public FlatTextView txtInstitut;
		public TextView txtAnzahl, txtHaltestelle;
		public ImageView imgImage;

	}

}

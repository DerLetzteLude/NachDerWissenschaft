package com.example.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table INSTITUT.
*/
public class InstitutDao extends AbstractDao<Institut, Long> {

    public static final String TABLENAME = "INSTITUT";

    /**
     * Properties of entity Institut.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Beschreibungkurz = new Property(2, String.class, "beschreibungkurz", false, "BESCHREIBUNGKURZ");
        public final static Property Beschreibunglang = new Property(3, String.class, "beschreibunglang", false, "BESCHREIBUNGLANG");
        public final static Property Bildurl = new Property(4, String.class, "bildurl", false, "BILDURL");
        public final static Property Weburl = new Property(5, String.class, "weburl", false, "WEBURL");
        public final static Property HaltestelleName = new Property(6, String.class, "haltestelleName", false, "HALTESTELLE_NAME");
        public final static Property HaltestelleLaenge = new Property(7, String.class, "haltestelleLaenge", false, "HALTESTELLE_LAENGE");
        public final static Property HaltestelleBreite = new Property(8, String.class, "haltestelleBreite", false, "HALTESTELLE_BREITE");
        public final static Property MarkerLaenge = new Property(9, String.class, "markerLaenge", false, "MARKER_LAENGE");
        public final static Property MarkerBreite = new Property(10, String.class, "markerBreite", false, "MARKER_BREITE");
    };

    private DaoSession daoSession;


    public InstitutDao(DaoConfig config) {
        super(config);
    }
    
    public InstitutDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'INSTITUT' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'NAME' TEXT," + // 1: name
                "'BESCHREIBUNGKURZ' TEXT," + // 2: beschreibungkurz
                "'BESCHREIBUNGLANG' TEXT," + // 3: beschreibunglang
                "'BILDURL' TEXT," + // 4: bildurl
                "'WEBURL' TEXT," + // 5: weburl
                "'HALTESTELLE_NAME' TEXT," + // 6: haltestelleName
                "'HALTESTELLE_LAENGE' TEXT," + // 7: haltestelleLaenge
                "'HALTESTELLE_BREITE' TEXT," + // 8: haltestelleBreite
                "'MARKER_LAENGE' TEXT," + // 9: markerLaenge
                "'MARKER_BREITE' TEXT);"); // 10: markerBreite
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'INSTITUT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Institut entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String beschreibungkurz = entity.getBeschreibungkurz();
        if (beschreibungkurz != null) {
            stmt.bindString(3, beschreibungkurz);
        }
 
        String beschreibunglang = entity.getBeschreibunglang();
        if (beschreibunglang != null) {
            stmt.bindString(4, beschreibunglang);
        }
 
        String bildurl = entity.getBildurl();
        if (bildurl != null) {
            stmt.bindString(5, bildurl);
        }
 
        String weburl = entity.getWeburl();
        if (weburl != null) {
            stmt.bindString(6, weburl);
        }
 
        String haltestelleName = entity.getHaltestelleName();
        if (haltestelleName != null) {
            stmt.bindString(7, haltestelleName);
        }
 
        String haltestelleLaenge = entity.getHaltestelleLaenge();
        if (haltestelleLaenge != null) {
            stmt.bindString(8, haltestelleLaenge);
        }
 
        String haltestelleBreite = entity.getHaltestelleBreite();
        if (haltestelleBreite != null) {
            stmt.bindString(9, haltestelleBreite);
        }
 
        String markerLaenge = entity.getMarkerLaenge();
        if (markerLaenge != null) {
            stmt.bindString(10, markerLaenge);
        }
 
        String markerBreite = entity.getMarkerBreite();
        if (markerBreite != null) {
            stmt.bindString(11, markerBreite);
        }
    }

    @Override
    protected void attachEntity(Institut entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Institut readEntity(Cursor cursor, int offset) {
        Institut entity = new Institut( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // beschreibungkurz
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // beschreibunglang
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // bildurl
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // weburl
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // haltestelleName
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // haltestelleLaenge
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // haltestelleBreite
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // markerLaenge
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // markerBreite
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Institut entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setBeschreibungkurz(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setBeschreibunglang(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setBildurl(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setWeburl(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setHaltestelleName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setHaltestelleLaenge(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setHaltestelleBreite(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setMarkerLaenge(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setMarkerBreite(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Institut entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Institut entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
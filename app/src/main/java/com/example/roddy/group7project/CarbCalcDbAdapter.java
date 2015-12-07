package com.example.roddy.group7project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CarbCalcDbAdapter {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Trips";

    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";

    private static final String TAG = "CarbCalcDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    public static final String TABLE_NAME_TRIP = "Trip";
    public static final String KEY_TRIP_ID = "_id";
    public static final String KEY_CATEGORY = "Category";
    public static final String KEY_VEHICLE_TYPE = "VehicleType";
    public static final String KEY_DISTANCE = "Distance";
    public static final String KEY_CO2 = "Carbon";
    public static final String KEY_DATE = "Date";
    public static final String KEY_NOTE = "Note";


    private static final String SQL_CREATE_TRIPS_DATABASE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TRIP + " (" +
                    KEY_TRIP_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    KEY_CATEGORY + TEXT_TYPE + ", "+ KEY_VEHICLE_TYPE  + TEXT_TYPE + ", " +
                    KEY_DISTANCE + REAL_TYPE + ", " + KEY_CO2 + REAL_TYPE + ", " + KEY_DATE + TEXT_TYPE + ", " +
                    KEY_NOTE + TEXT_TYPE + " );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME_TRIP;

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
           super(context, DATABASE_NAME, null, DATABASE_VERSION);
          //  super(context, "/data/data/com.example.tgk.groupProject/databases/Trips.db", null, DATABASE_VERSION);
          // super(context, context.getFilesDir().getPath(), null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.v(TAG, SQL_CREATE_TRIPS_DATABASE);
            db.execSQL(SQL_CREATE_TRIPS_DATABASE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            Log.w(TAG, "Upgrading database from version " + i + " to "
                    + i1 + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TRIP);
            onCreate(db);
        }
    }

    public CarbCalcDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public CarbCalcDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public Cursor fetchTripById (long id) throws SQLException {
        Log.w(TAG, String.valueOf(id));
        Cursor mCursor = null;
        if (id < 0)  {
            mCursor = mDb.query(TABLE_NAME_TRIP, new String[] {KEY_TRIP_ID,
                            KEY_CATEGORY, KEY_VEHICLE_TYPE, KEY_DISTANCE, KEY_CO2, KEY_DATE,
                            KEY_NOTE},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, TABLE_NAME_TRIP, new String[] {KEY_TRIP_ID,
                            KEY_CATEGORY, KEY_VEHICLE_TYPE, KEY_DISTANCE, KEY_CO2, KEY_DATE,
                            KEY_NOTE},
                    KEY_TRIP_ID + " = " + id, null, null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllTrips() {

        Cursor mCursor = mDb.query(TABLE_NAME_TRIP, new String[] {KEY_TRIP_ID,
                        KEY_CATEGORY, KEY_VEHICLE_TYPE, KEY_DISTANCE, KEY_CO2, KEY_DATE,
                        KEY_NOTE},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public long insertTrip(String category, String vehicleType, double distance, double co2,
                           String date, String note) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CATEGORY, category);
        initialValues.put(KEY_VEHICLE_TYPE, vehicleType);
        initialValues.put(KEY_DISTANCE, distance);
        initialValues.put(KEY_CO2, co2);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_NOTE, note);


        return mDb.insert(TABLE_NAME_TRIP, null, initialValues);
    }

    public void updateTrip(long id, String category, String vehicleType, double distance, double co2,
                           String date, String note) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(KEY_CATEGORY, category);
        updatedValues.put(KEY_VEHICLE_TYPE, vehicleType);
        updatedValues.put(KEY_DISTANCE, distance);
        updatedValues.put(KEY_CO2, co2);
        updatedValues.put(KEY_DATE, date);
        updatedValues.put(KEY_NOTE, note);
        final String selection = KEY_TRIP_ID + "=" + id;
        mDb.update(TABLE_NAME_TRIP, updatedValues, selection, null);
    }

    public Cursor fetchTripsByCategory(String category){
        final String selection = KEY_CATEGORY + "=" + "'" + category + "'";
        Cursor mCursor = mDb.query(TABLE_NAME_TRIP, new String[] {KEY_CO2}, selection,
                null, null, null, null);

        return mCursor;

    }

    public void removeTrip(long id) {
        // don't know if this works yet
        mDb.delete(TABLE_NAME_TRIP, "_id = " + id, null);
    }

    public boolean deleteAllTrips() {
        int doneDelete = 0;
        doneDelete = mDb.delete(TABLE_NAME_TRIP, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

}

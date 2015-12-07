/* Copyright © 2011-2013 mysamplecode.com, All rights reserved.
  This source code is provided to students of CST2335 for educational purposes only.
 */
package com.example.roddy.group7project;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class implements the database, including database connection and operation(query)
 */
public class TipDbAdapter {

    // The column name of the table
    public static final String EXPID = "_id";
    public static final String RESTNAME = "name";
    public static final String AMOUNT = "amount";
    public static final String TIPRATE = "tiprate";
    public static final String TIPAMOUNT = "tipamount";
    public static final String TOTALAMOUNT = "totalamount";
    public static final String NOTE = "note";
    public static final String CREATEDATE = "createdate";

    private static final String TAG = "ExpenseDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "Expense";
    private static final String SQLITE_TABLE = "ExpenseRecord";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    //Create the database
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    EXPID + " integer PRIMARY KEY autoincrement," +
                    RESTNAME + "," +
                    AMOUNT + "," +
                    TIPRATE + "," +
                    TIPAMOUNT + "," +
                    TOTALAMOUNT + "," +
                    NOTE + "," +
                    CREATEDATE + ");";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public TipDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public TipDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createExpense(String name, String amount, String tiprate, String tipamount,
                              String totalamount, String note, String date) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(RESTNAME, name);
        initialValues.put(AMOUNT, amount);
        initialValues.put(TIPRATE, tiprate);
        initialValues.put(TIPAMOUNT, tipamount);
        initialValues.put(TOTALAMOUNT, totalamount);
        initialValues.put(NOTE, note);
        initialValues.put(CREATEDATE, date);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public int deleteExpense(long expenseID){
        int count = 0;
        count = mDb.delete(SQLITE_TABLE, "_id=?", new String[]{Long.toString(expenseID)});
        Log.w(TAG, Integer.toString(count));
        return count;
    }

    public boolean deleteAllExpenses() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchExpenseByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {EXPID, RESTNAME, AMOUNT,
                            TIPRATE, TIPAMOUNT, TOTALAMOUNT, NOTE, CREATEDATE}, null, null,
                    null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {EXPID, RESTNAME, AMOUNT,
                            TIPRATE, TIPAMOUNT, TOTALAMOUNT, NOTE, CREATEDATE},
                    RESTNAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchExpenseByID(long ID) throws SQLException {
        Log.w(TAG, "Expense ID is" + ID);
        Cursor mCursor = null;
            mCursor = mDb.query(SQLITE_TABLE, new String[] {EXPID, RESTNAME, AMOUNT,
                            TIPRATE, TIPAMOUNT, TOTALAMOUNT, NOTE, CREATEDATE}, "_id=?", new String[]{Long.toString(ID)},
                    null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllExpenses() {

//        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {EXPID, RESTNAME, AMOUNT,
//                        TIPRATE, TIPAMOUNT, TOTALAMOUNT, CREATEDATE},
//                null, null, null, null, null);

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {EXPID, RESTNAME, TOTALAMOUNT, CREATEDATE},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomeCountries() {
//
//        createCountry("AFG","Afghanistan","Asia","Southern and Central Asia");
//        createCountry("ALB","Albania","Europe","Southern Europe");
//        createCountry("DZA","Algeria","Africa","Northern Africa");
//        createCountry("ASM","American Samoa","Oceania","Polynesia");
//        createCountry("AND","Andorra","Europe","Southern Europe");
//        createCountry("AGO","Angola","Africa","Central Africa");
//        createCountry("AIA","Anguilla","North America","Caribbean");

    }

}


package com.example.roddy.group7project;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * the class name is A2_DbAdapter
 *
 * it is used to handle the sqlite database for activity two
 *
 * @author Fang He on 2015-12-03
 *
 */
public class A2_DbAdapter {

    /**
     * columns in database
     */
    public static final String TASKID = "_id";
    public static final String TITLE = "title";
    public static final String CATEGORY = "category";
    public static final String DURATION = "duration";
    public static final String PORIORITY = "poriority";
    public static final String NOTE = "note";
    public static final String DATE = "date";

    /**
     * database information
     */
    private static final String TAG = "A2_DbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "A2_task";
    private static final String SQLITE_TABLE = "A2_AllTasks";
    private static final int DATABASE_VERSION = 1;

    /**
     * context field
     */
    private final Context mCtx;

    /**
     * sql to create table
     */
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    TASKID + " integer PRIMARY KEY autoincrement," +
                    TITLE + "," +
                    CATEGORY + "," +
                    DURATION + "," +
                    PORIORITY + "," +
                    NOTE + "," +
                    DATE + ");";

    /**
     * DatabaseHelper class to implement create insert delete and update function
     */
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

    /**
     * construction with parameter
     * @param ctx context object
     */
    public A2_DbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * open the database
     * @return the database object
     * @throws SQLException sql exception
     */
    public A2_DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * close the database
     */
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    /**
     * insert the new task to the database
     * @param title title information
     * @param category  category information
     * @param duration duration information
     * @param poriority poriority information
     * @param note note information
     * @param date date information
     * @return task id object
     */
    public long createExpense(String title, String category, String duration, String poriority,
                              String note, String date) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(TITLE, title);
        initialValues.put(CATEGORY, category);
        initialValues.put(DURATION, duration);
        initialValues.put(PORIORITY, poriority);
        initialValues.put(NOTE, note);
        initialValues.put(DATE, date);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    /**
     * delete task by id
     * @param taskID the task id
     * @return if true, delete successfully, otherwise, false
     */
    public int deleteTask(long taskID){
        int count = 0;
        count = mDb.delete(SQLITE_TABLE, "_id=?", new String[]{Long.toString(taskID)});
        Log.w(TAG, Integer.toString(count));
        return count;
    }

    /**
     * delete all records
     * @return  if true, delete successfully, otherwise, false
     */
    public boolean deleteAll() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    /**
     * the records by name
     * @param inputText name information
     * @return cursor information
     * @throws SQLException sql exception
     */
    public Cursor fetchExpenseByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {TASKID, TITLE, CATEGORY,
                            DURATION, PORIORITY, NOTE, DATE}, null, null,
                    null, null, null);        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {TASKID, TITLE, CATEGORY,
                            DURATION, PORIORITY, NOTE, DATE},
                    CATEGORY + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * get records for special taskid
     * @param ID taskid
     * @return the cursor object
     * @throws SQLException sql exception
     */
    public Cursor fetchExpenseByID(long ID) throws SQLException {
        Log.w(TAG, "taskID is" + ID);
        Cursor mCursor = null;
            mCursor = mDb.query(SQLITE_TABLE, new String[] {TASKID, TITLE, CATEGORY,
                            DURATION, PORIORITY, NOTE, DATE}, "_id=?", new String[]{Long.toString(ID)},
                    null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * find all records
     * @return the cursor object
     */
    public Cursor fetchAllExpenses() {


        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {TASKID, TITLE, CATEGORY,
                        DURATION, PORIORITY, NOTE, DATE},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * update task based the taskid
     * @param values new information
     * @param id taskid
     * @return the int
     */
    public int updateEntry(ContentValues values,long id) {

        Log.w(TAG, "update taskID is" + id);
        return mDb.update(SQLITE_TABLE, values, TASKID + " = ?",
                new String[] { id+"" });
    }




}

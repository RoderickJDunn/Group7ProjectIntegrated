package com.example.roddy.group7project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactDbAdapter {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Contacts";

    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";

    private static final String TAG = "ContactDbAdapter";
    private DatabaseHelper contactDbHelper;
    private SQLiteDatabase contactDb;

    private final Context contactCtx;

    public static final String TABLE_NAME_CONTACT = "Contact";
    public static final String KEY_CONTACT_ID = "_id";
    public static final String KEY_FIRST_NAME = "FirstName";
    public static final String KEY_LAST_NAME= "LastName";
    public static final String KEY_PHONE_NUMBER = "Phone";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_CONTACT_DATE = "Date";
    public static final String KEY_CONTACT_NOTE = "Note";
    public static final String KEY_CONTACT_SUMMARY = "Summary";


    private static final String SQL_CREATE_CONTACTS_DATABASE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_CONTACT + " (" + KEY_CONTACT_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    KEY_FIRST_NAME + TEXT_TYPE + ", "+ KEY_LAST_NAME + TEXT_TYPE + ", " + KEY_PHONE_NUMBER + REAL_TYPE + ", " +
                    KEY_EMAIL + REAL_TYPE + ", " + KEY_CONTACT_DATE + TEXT_TYPE + ", " + KEY_CONTACT_NOTE + TEXT_TYPE + ", " +
                    KEY_CONTACT_SUMMARY + TEXT_TYPE + " );";


    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME_CONTACT;



    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.v(TAG, SQL_CREATE_CONTACTS_DATABASE);
            db.execSQL(SQL_CREATE_CONTACTS_DATABASE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            Log.w(TAG, "Upgrading database from version " + i + " to "
                    + i1 + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CONTACT);
            onCreate(db);
        }

    }

    public ContactDbAdapter(Context ctx) {
        this.contactCtx = ctx;
    }

    public ContactDbAdapter open() throws SQLException {
        contactDbHelper = new DatabaseHelper(contactCtx);
        contactDb = contactDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (contactDbHelper != null) {
            contactDbHelper.close();
        }
    }

    public Cursor fetchContactById (long id) throws SQLException {
        Log.w(TAG, String.valueOf(id));
        Cursor cCursor = null;
        if (id < 0)  {
            cCursor = contactDb.query(TABLE_NAME_CONTACT, new String[] {KEY_CONTACT_ID,
                            KEY_FIRST_NAME, KEY_LAST_NAME, KEY_PHONE_NUMBER, KEY_EMAIL, KEY_CONTACT_DATE,
                            KEY_CONTACT_NOTE, KEY_CONTACT_SUMMARY},
                    null, null, null, null, null);

        }
        else {
            cCursor = contactDb.query(true, TABLE_NAME_CONTACT, new String[] {KEY_CONTACT_ID,
                            KEY_FIRST_NAME, KEY_LAST_NAME, KEY_PHONE_NUMBER, KEY_EMAIL, KEY_CONTACT_DATE,
                            KEY_CONTACT_NOTE, KEY_CONTACT_SUMMARY},
                    KEY_CONTACT_ID + " = " + id, null, null, null, null, null);
        }
        if (cCursor != null) {
            cCursor.moveToFirst();
        }
        return cCursor;

    }

    public Cursor fetchAllContacts() {

        Cursor cCursor = contactDb.query(TABLE_NAME_CONTACT, new String[] {KEY_CONTACT_ID,
                        KEY_FIRST_NAME, KEY_LAST_NAME, KEY_PHONE_NUMBER, KEY_EMAIL, KEY_CONTACT_DATE,
                        KEY_CONTACT_NOTE, KEY_CONTACT_SUMMARY},
                null, null, null, null, null);

        if (cCursor != null) {
            cCursor.moveToFirst();
        }
        return cCursor;
    }

    public long insertContact(String firstName, String lastName, Integer phoneNumber, String email,
                              String cDate, String cNote, String cSummary) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FIRST_NAME, firstName);
        initialValues.put(KEY_LAST_NAME, lastName);
        initialValues.put(KEY_PHONE_NUMBER, phoneNumber);
        initialValues.put(KEY_EMAIL, email);
        initialValues.put(KEY_CONTACT_DATE, cDate);
        initialValues.put(KEY_CONTACT_NOTE, cNote);
        initialValues.put(KEY_CONTACT_SUMMARY, cSummary);

        return contactDb.insert(TABLE_NAME_CONTACT, null, initialValues);
    }

    public void deleteContact(long id) {
        contactDb.delete(TABLE_NAME_CONTACT, "_id = " + id, null);
    }

}

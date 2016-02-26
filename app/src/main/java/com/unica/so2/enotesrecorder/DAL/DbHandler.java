package com.unica.so2.enotesrecorder.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.unica.so2.enotesrecorder.Helper.DatabaseHelper;

import java.util.Date;

public class DbAdapter {

    private static final String DATABASE_TABLE = "note";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DATE = "last_edit";
    public static final String KEY_ID = "_id";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_RATING = "rating";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public DbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * @param title the title of the note
     * @param content the json content of the note
     * @param rating the rating of the note
     * @return rowId or -1 if failed
     */
    public long createNote(String title, String content, double rating) {
        ContentValues initialValues = new ContentValues();
        long msTime = System.currentTimeMillis();
        Date currentDateTime = new Date(msTime);

        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_CONTENT, content);
        initialValues.put(KEY_DATE, currentDateTime.toString());
        initialValues.put(KEY_RATING, rating);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the note with the given Id
     *
     * @param id id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteNote(long id) {

        return mDb.delete(DATABASE_TABLE, KEY_ID + "=" + id, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllNotes() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ID, KEY_TITLE,
                KEY_CONTENT,KEY_DATE,KEY_RATING}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the note that matches the given id
     *
     * @param id id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchNote(long id) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ID,
                                KEY_TITLE,KEY_CONTENT,KEY_DATE,KEY_RATING}, KEY_ID + "=" + id, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the id, and it is altered to use the title and body
     * values passed in
     *
     * @param id id of note to update
     * @param title value to set note title to
     * @param content value to set note json content to
     * @param rating value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateNote(long id, String title, String content, double rating) {
        ContentValues args = new ContentValues();
        long msTime = System.currentTimeMillis();
        Date currentDateTime = new Date(msTime);

        args.put(KEY_TITLE, title);
        args.put(KEY_CONTENT, content);
        args.put(KEY_DATE, currentDateTime.toString());
        args.put(KEY_RATING, rating);

        return mDb.update(DATABASE_TABLE, args, KEY_ID + "=" + id, null) > 0;
    }
}

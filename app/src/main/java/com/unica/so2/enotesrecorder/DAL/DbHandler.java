package com.unica.so2.enotesrecorder.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.unica.so2.enotesrecorder.Helper.GenericHelper;
import com.unica.so2.enotesrecorder.Helper.JsonHelper;
import com.unica.so2.enotesrecorder.Model.Note;

import java.util.ArrayList;
import java.util.Date;

public class DbHandler extends SQLiteOpenHelper implements NoteRepository {

    private static final String TAG = "DbHandler";
    private SQLiteDatabase _db;

    private static final String DB_NAME = "eNotesRecorder";
    private static final int DB_VERSION = 2;
    private static final String TABLE_NAME = "note";
    private static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
    private static final String CREATE_TABLE =
            "create table note (" +
                    "_id integer primary key autoincrement," +
                    "title text not null," +
                    "content text not null," +
                    "lastEdit text not null,"+
                    "rating float);" ;


    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_LAST_EDIT = "lastEdit";
    public static final String KEY_RATING = "rating";


    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param context the Context within which to work
     */
    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * This method opens the database
     */
    public void open() throws SQLException {
        _db = this.getWritableDatabase();
    }
    /**
     * This method closes the database
     */
    public void close() {
        _db.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    /**
     * Create a new note in the database, using the note provided. If the note is
     * successfully created return the new id of that note, otherwise return
     * a -1 to indicate failure.
     *
     * @param note note to add
     * @return id or -1 if failed
     */
    @Override
    public long addNote(Note note) {
        long result;
        try {
            ContentValues initialValues = new ContentValues();
            long msTime = System.currentTimeMillis();
            Date currentDateTime = new Date(msTime);

            initialValues.put(KEY_TITLE, note.getTitle());
            initialValues.put(KEY_CONTENT, JsonHelper.serializeContent(note.getContent()));
            initialValues.put(KEY_LAST_EDIT, GenericHelper.getStringFromDate(currentDateTime));
            initialValues.put(KEY_RATING, note.getRating());
            result = _db.insert(TABLE_NAME, null, initialValues);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            result = -1;
        }
        return result;
    }

    /**
     * Delete from the database the note with the given id
     *
     * @param id id of note to delete
     * @return true if deleted, false otherwise
     */
    @Override
    public boolean deleteNote(long id) {
        boolean isNoteDeleted = false;
        try {
            isNoteDeleted = _db.delete(TABLE_NAME, KEY_ID + "=" + id, null) > 0;
        }
        catch(Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return isNoteDeleted;
    }

    /**
     * Update the note using the note provided. The note to be updated is
     * specified using the id, and it is altered to use the values passed in
     *
     * @return true if the note was successfully updated, false otherwise
     */
    @Override
    public boolean updateNote(Note note) {
        boolean isNoteUpdated = false;
        try {
            ContentValues args = new ContentValues();
            long msTime = System.currentTimeMillis();
            Date currentDateTime = new Date(msTime);

            args.put(KEY_TITLE, note.getTitle());
            args.put(KEY_CONTENT, JsonHelper.serializeContent(note.getContent()));
            args.put(KEY_LAST_EDIT, GenericHelper.getStringFromDate(currentDateTime));
            args.put(KEY_RATING, note.getRating());

            isNoteUpdated = _db.update(TABLE_NAME, args, KEY_ID + "=" + note.getId(), null) > 0;
        }
        catch(Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return isNoteUpdated;
    }

    /**
     * Return the Note that matches the given id
     *
     * @param id id of note to retrieve
     * @return Note with the given id
     */
    @Override
    public Note getNote(long id) {
        Note note = new Note();

        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE _id=" + id;
            Cursor cursor = _db.rawQuery(query, null);

            if(cursor.moveToFirst()) {
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(JsonHelper.deserializeContent(cursor.getString(2)));
                note.setLastEdit(GenericHelper.stringToDate(cursor.getString(3)));
                note.setRating(cursor.getFloat(4));
            }
            cursor.close();
        }
        catch(Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return note;
    }

    /**
     * Return the list of all notes in the database, order by date in descending order
     * and then by title in ascending order
     *
     * @return ArrayList<Note> of all the notes ordered
     */
    @Override
    public ArrayList<Note> getAllNotesDescendingDate() {
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_LAST_EDIT + " DESC," + KEY_TITLE + " ASC";
        return getNoteListFromQuery(query);
    }

    /**
     * Return the list of all notes in the database, order by date in descending order
     * and then by title in ascending order
     *
     * @return ArrayList<Note> of all the notes ordered
     */
    @Override
    public ArrayList<Note> getAllNotesAscendingDate() {
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_LAST_EDIT + "," + KEY_TITLE + " ASC";
        return getNoteListFromQuery(query);
    }

    /**
     * Return the list of all notes in the database, order by date in descending order
     * and then by title in ascending order
     *
     * @return ArrayList<Note> of all the notes ordered
     */
    @Override
    public ArrayList<Note> getAllNotesDescendingRating() {
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_RATING + " DESC";
        return getNoteListFromQuery(query);
    }

    /**
     * Return the list of all notes in the database, order by rating in descending order
     *
     * @return ArrayList<Note> of all the notes ordered
     */
    @Override
    public ArrayList<Note> getAllNotesAscendingRating() {
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_RATING + " ASC";
        return getNoteListFromQuery(query);
    }

    /**
     * Return the list of all notes in the databasewith the given rating value
     *
     * @return ArrayList<Note> of all the proper notes
     */
    @Override
    public ArrayList<Note> getAllNotesByRate(float rating) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_RATING + " = " + rating;
        return getNoteListFromQuery(query);
    }

    /**
     * Return the list of all notes in the database selected with the options specified in the query
     * passed as parameter
     *
     * @query the 'SELECT' query
     * @return ArrayList<Note> of all the notes ordered
     */
    @Override
    public ArrayList<Note> getNoteListFromQuery(String query) {
        ArrayList<Note> list = new ArrayList<>();

        try {
            Cursor cursor = _db.rawQuery(query,null);

            while (cursor.moveToNext()) {
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(JsonHelper.deserializeContent(cursor.getString(2)));
                note.setLastEdit(GenericHelper.stringToDate(cursor.getString(3)));
                note.setRating(cursor.getFloat(4));
                list.add(note);
            }

            cursor.close();
        }
        catch(Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return list;
    }

    /**
     * Return the number of notes in the table of the database
     *
     * @return int the number of notes
     */
    @Override
    public int getNoteCount() {
        int count = 0;

        try {
            String QUERY = "SELECT * FROM " + TABLE_NAME;
            Cursor cursor = _db.rawQuery(QUERY, null);
            count = cursor.getCount();
            cursor.close();
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return count;
    }

}
package com.unica.so2.enotesrecorder.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.unica.so2.enotesrecorder.Model.Note;

import java.util.ArrayList;
import java.util.Date;

public class DbHandler extends SQLiteOpenHelper implements NoteRepository {

    private SQLiteDatabase _db;

    private static final String DB_NAME = "eNotesRecorder";
    private static final int DB_VERSION = 2;
    private static final String TABLE_NAME = "note";
    private static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
    private static final String CREATE_TABLE =
            "create table note (" +
                    "_id integer primary key autoincrement," +
                    "title text not null," +
                    "lastEdit text not null,"+
                    "content text not null," +
                    "rating double);" ;


    public static final String KEY_TITLE = "title";
    public static final String KEY_LAST_EDIT = "last_edit";
    public static final String KEY_ID = "_id";
    public static final String KEY_CONTENT = "content";
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

    public void open() throws SQLException {
        _db = this.getWritableDatabase();
    }

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

    @Override
    public int getNoteCount() {
        return 0;
    }

    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * @param note
     * @return rowId or -1 if failed
     */
    @Override
    public long addNote(Note note) {
        ContentValues initialValues = new ContentValues();
        long msTime = System.currentTimeMillis();
        Date currentDateTime = new Date(msTime);

        initialValues.put(KEY_TITLE, note.getTitle());
        initialValues.put(KEY_CONTENT, note.getContent().toString());
        initialValues.put(KEY_LAST_EDIT, currentDateTime.toString());
        initialValues.put(KEY_RATING, note.getRating());
        return _db.insert(TABLE_NAME, null, initialValues);
    }

    /**
     * Delete the note with the given Id
     *
     * @param id id of note to delete
     * @return true if deleted, false otherwise
     */
    @Override
    public boolean deleteNote(long id) {
        return _db.delete(TABLE_NAME, KEY_ID + "=" + id, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @return Cursor over all notes
     */
    @Override
    public ArrayList<Note> getAllNotes() {

        return new ArrayList<Note>();
    }

    /**
     * Return a Cursor positioned at the note that matches the given id
     *
     * @param id id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    @Override
    public Note getNote(long id) {

        return new Note();
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
    @Override
    public boolean updateNote(long id, String title, String content, double rating) {
        ContentValues args = new ContentValues();
        long msTime = System.currentTimeMillis();
        Date currentDateTime = new Date(msTime);

        args.put(KEY_TITLE, title);
        args.put(KEY_CONTENT, content);
        args.put(KEY_LAST_EDIT, currentDateTime.toString());
        args.put(KEY_RATING, rating);

        return _db.update(TABLE_NAME, args, KEY_ID + "=" + id, null) > 0;
    }


}

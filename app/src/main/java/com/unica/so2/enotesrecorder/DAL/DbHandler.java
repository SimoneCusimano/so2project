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
import com.unica.so2.enotesrecorder.Model.Content;
import com.unica.so2.enotesrecorder.Model.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

            SimpleDateFormat dateFormat =  new SimpleDateFormat("dd MM dd yyyy - HH:mm:ss", Locale.ITALY);
            String lastEditString = dateFormat.format(currentDateTime);

            initialValues.put(KEY_LAST_EDIT, lastEditString);
            initialValues.put(KEY_RATING, note.getRating());
            result = _db.insert(TABLE_NAME, null, initialValues);
        }catch (Exception e){
            Log.e("problem",e+"");
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
        return _db.delete(TABLE_NAME, KEY_ID + "=" + id, null) > 0;
    }

    /**
     * Update the note using the note provided. The note to be updated is
     * specified using the id, and it is altered to use the values passed in
     *
     * @return true if the note was successfully updated, false otherwise
     */
    @Override
    public boolean updateNote(Note note) {
        ContentValues args = new ContentValues();
        long msTime = System.currentTimeMillis();
        Date currentDateTime = new Date(msTime);

        args.put(KEY_TITLE, note.getTitle());
        args.put(KEY_CONTENT, JsonHelper.serializeContent(note.getContent()));

        SimpleDateFormat dateFormat =  new SimpleDateFormat("dd MM dd yyyy - HH:mm:ss", Locale.ITALY);
        String lastEditString = dateFormat.format(currentDateTime);

        args.put(KEY_LAST_EDIT, lastEditString);
        args.put(KEY_RATING, note.getRating());

        return _db.update(TABLE_NAME, args, KEY_ID + "=" + note.getId(), null) > 0;
    }

    /**
     * Return the Note that matches the given id
     *
     * @param id id of note to retrieve
     * @return Note with the given id
     */
    @Override
    public Note getNote(long id) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE _id=" + id;
        Cursor cursor = _db.rawQuery(query, null);
        Note note = new Note();
        if(cursor.moveToFirst()) {
            note.setId(cursor.getString(0));
            note.setTitle(cursor.getString(1));
            note.setContent(JsonHelper.deserializeContent(cursor.getString(2)));
            note.setLastEdit(GenericHelper.stringToDate(cursor.getString(3)));
            note.setRating(cursor.getFloat(4));

            cursor.close();
        }

        return note;
    }

    /**
     * Return the list of all notes in the database, order by date in descending order
     * and then by title in ascending order
     *
     * @return ArrayList<Note> of all the notes
     */
    public ArrayList<Note> getAllNotesDescendingDate() {
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_LAST_EDIT + " DESC," + KEY_TITLE + " ASC";

        Cursor cursor = _db.rawQuery(query,null);
        ArrayList<Note> list = new ArrayList<>();

        while(cursor.moveToNext()) {
            Note note = new Note();
            note.setId(cursor.getString(0));
            note.setTitle(cursor.getString(1));
            note.setContent(JsonHelper.deserializeContent(cursor.getString(2)));
            note.setLastEdit(GenericHelper.stringToDate(cursor.getString(3)));
            note.setRating(cursor.getFloat(4));

            list.add(note);
        }

        cursor.close();
        return list;
    }

    /**
     * Return the list of all notes in the database, order by date in descending order
     * and then by title in ascending order
     *
     * @return ArrayList<Note> of all the notes
     */
    public ArrayList<Note> getAllNotesAscendingDate() {
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_LAST_EDIT + "," + KEY_TITLE + " ASC";

        Cursor cursor = _db.rawQuery(query,null);
        ArrayList<Note> list= new ArrayList<>();

        while(cursor.moveToNext()){
            Note note = new Note();
            note.setId(cursor.getString(0));
            note.setTitle(cursor.getString(1));
            note.setContent(JsonHelper.deserializeContent(cursor.getString(2)));
            note.setLastEdit(GenericHelper.stringToDate(cursor.getString(3)));
            note.setRating(cursor.getFloat(4));

            list.add(note);
        }

        cursor.close();
        return list;
    }

    /**
     * Return the list of all notes in the database, order by date in descending order
     * and then by title in ascending order
     *
     * @return ArrayList<Note> of all the notes
     */
    public ArrayList<Note> getAllNotesDescendingRating() {
        ArrayList<Note> noteList;
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_RATING + " DESC";

        Cursor cursor = _db.rawQuery(query,null);
        ArrayList<Note> list= new ArrayList<>();

        while(cursor.moveToNext()){
            Note note=new Note();
            note.setId(cursor.getString(0));
            note.setTitle(cursor.getString(1));
            note.setContent(JsonHelper.deserializeContent(cursor.getString(2)));
            note.setLastEdit(GenericHelper.stringToDate(cursor.getString(3)));
            note.setRating(cursor.getFloat(4));

            list.add(note);
        }

        cursor.close();
        return list;
    }

    /**
     * Return the list of all notes in the database, order by rating in descending order
     *
     * @return ArrayList<Note> of all the notes
     */
    public ArrayList<Note> getAllNotesAscendingRating() {
        ArrayList<Note> noteList;
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_RATING + " ASC";

        Cursor cursor = _db.rawQuery(query,null);
        ArrayList<Note> list= new ArrayList<>();

        while(cursor.moveToNext()){
            Note note=new Note();
            note.setId(cursor.getString(0));
            note.setTitle(cursor.getString(1));
            note.setContent(JsonHelper.deserializeContent(cursor.getString(2)));
            note.setLastEdit(GenericHelper.stringToDate(cursor.getString(3)));
            note.setRating(cursor.getFloat(4));

            list.add(note);
        }

        cursor.close();
        return list;
    }

    /**
     * Return the list of all notes in the database, order by rating in descending order
     *
     * @return ArrayList<Note> of all the notes
     */
    public ArrayList<Note> getAllNotesByRate(float rating) {
        ArrayList<Note> noteList;
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_RATING + " = " + rating;

        Cursor cursor = _db.rawQuery(query,null);
        ArrayList<Note> list= new ArrayList<>();

        while(cursor.moveToNext()){
            Note note=new Note();
            note.setId(cursor.getString(0));
            note.setTitle(cursor.getString(1));
            note.setContent(JsonHelper.deserializeContent(cursor.getString(2)));
            note.setLastEdit(GenericHelper.stringToDate(cursor.getString(3)));
            note.setRating(cursor.getFloat(4));

            list.add(note);
        }

        cursor.close();
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

        try{
            String QUERY = "SELECT * FROM " + TABLE_NAME;
            Cursor cursor = _db.rawQuery(QUERY, null);
            count = cursor.getCount();
            cursor.close();

        }
        catch (Exception e){
            Log.e("error", e + "");
        }
        return count;
    }


}

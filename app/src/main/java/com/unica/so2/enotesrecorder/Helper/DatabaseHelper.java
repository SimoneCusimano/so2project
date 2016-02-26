package com.unica.so2.enotesrecorder.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "NotesDbAdapter";

    private static final String DATABASE_NAME = "eNotesRecorder";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_CREATE =
            "create table note (" +
                    "_id integer primary key autoincrement," +
                    "title text not null," +
                    "lastEdit text not null,"+
                    "content text not null," +
                    "rating double);" ;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }
}

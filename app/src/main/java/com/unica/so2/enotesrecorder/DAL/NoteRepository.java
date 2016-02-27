package com.unica.so2.enotesrecorder.DAL;

import android.database.SQLException;
import com.unica.so2.enotesrecorder.Model.Note;
import java.util.ArrayList;


public interface NoteRepository {

    long addNote(Note note);

    ArrayList<Note> getAllNotes();

    int getNoteCount();

    boolean updateNote(long id, String title, String content, double rating);

    boolean deleteNote(long id);

    Note getNote(long id) throws SQLException;


}

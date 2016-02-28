package com.unica.so2.enotesrecorder.DAL;

import android.database.SQLException;
import com.unica.so2.enotesrecorder.Model.Note;
import java.util.ArrayList;


public interface NoteRepository {

    long addNote(Note note);

    ArrayList<Note> getAllNotesDescendingDate();

    ArrayList<Note> getAllNotesAscendingDate();

    ArrayList<Note> getAllNotesDescendingRating();

    ArrayList<Note> getAllNotesAscendingRating();

    ArrayList<Note> getAllNotesByRate(float rating);

    int getNoteCount();

    boolean updateNote(Note note);

    boolean deleteNote(long id);

    Note getNote(long id) throws SQLException;


}

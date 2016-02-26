package com.unica.so2.enotesrecorder.DAL;

import com.unica.so2.enotesrecorder.Model.Note;

import java.util.ArrayList;


public interface NoteRepository {

    void addNote(Note note);

    ArrayList<Note> getAllNote();

    int getNoteCount();
}

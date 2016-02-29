package com.unica.so2.enotesrecorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import com.unica.so2.enotesrecorder.Adapter.NoteAdapter;
import com.unica.so2.enotesrecorder.DAL.DbHandler;
import com.unica.so2.enotesrecorder.Model.Note;

import java.util.ArrayList;

/**
 * An activity representing a list of Notes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EditNoteActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class NoteListActivity extends AppCompatActivity {

    private static final int ACTIVITY_EDIT = 0;
    private ListView _notesList;
    private FloatingActionButton _refresh;
    private int DEFAULT_SORTING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        _refresh = (FloatingActionButton) findViewById(R.id.refreshFloatingActionButton);
        _notesList = (ListView) findViewById(android.R.id.list);
        DEFAULT_SORTING = R.id.action_sortByLastEditAsc;
        createListView(DEFAULT_SORTING);

        _notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(parent.getContext(), EditNoteActivity.class);
                i.putExtra(DbHandler.KEY_ID, id);
                startActivityForResult(i, ACTIVITY_EDIT);

            }
        });

        _refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refreshing", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                createListView(DEFAULT_SORTING);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                // User chose the "Add" action, mark the current item
                Intent i = new Intent(this, NewNoteActivity.class);
                startActivity(i);
                return true;
            case R.id.action_sortByLastEditAsc:
            case R.id.action_sortByLastEditDesc:
            case R.id.action_sortByRatingAsc:
            case R.id.action_sortByRatingDesc:
                DEFAULT_SORTING = item.getItemId();
                createListView(DEFAULT_SORTING);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_actionbar, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        createListView(DEFAULT_SORTING);
    }

    private void createListView(int choose) {
        // Get all of the notes from the database and create the item list
        ArrayList<Note> notesArrayList = getNoteListSortByParam(choose);
        // Create the adapter to convert the array to views
        NoteAdapter adapter = new NoteAdapter(this, notesArrayList);
        // Attach the adapter to a ListView
        _notesList.setAdapter(adapter);
    }

    private ArrayList<Note> getNoteListSortByParam(int choose) {
        ArrayList<Note> notesArrayList;
        DbHandler dbHandler = new DbHandler(this);
        dbHandler.open();

        switch(choose) {
            case R.id.action_sortByLastEditAsc:
                notesArrayList = dbHandler.getAllNotesAscendingDate();
                break;
            case R.id.action_sortByLastEditDesc:
                notesArrayList = dbHandler.getAllNotesDescendingDate();
                break;
            case R.id.action_sortByRatingAsc:
                notesArrayList = dbHandler.getAllNotesAscendingRating();
                break;
            case R.id.action_sortByRatingDesc:
                notesArrayList = dbHandler.getAllNotesDescendingRating();
                break;
            default:
                notesArrayList = dbHandler.getAllNotesAscendingDate();
                    break;
        }

        dbHandler.close();

        return notesArrayList;
    }
}
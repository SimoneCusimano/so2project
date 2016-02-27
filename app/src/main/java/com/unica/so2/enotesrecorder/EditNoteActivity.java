package com.unica.so2.enotesrecorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.unica.so2.enotesrecorder.DAL.DbHandler;

import java.util.Date;


public class EditNoteActivity extends Activity {
    public static String curText = "";
    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;

    private Cursor note;

    private DbHandler mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new DbHandler(this);
        mDbHelper.open();

        setContentView(R.layout.activity_new_note);
        setTitle(R.string.app_name);

        mTitleText = (EditText) findViewById(R.id.title);
        //mBodyText = (EditText) findViewById(R.id.content);


        mRowId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(DbHandler.KEY_ID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(DbHandler.KEY_ID)
                    : null;
        }

        populateFields();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(DbHandler.KEY_ID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.noteedit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch (item.getItemId()) {
            case R.id.menu_about:

		    	/* Here is the introduce about myself
                AlertDialog.Builder dialog = new AlertDialog.Builder(NoteEdit.this);
                dialog.setTitle("About");
                dialog.setMessage("Hello! I'm Heng, the creator of this application. This application is created for learning." +
                                " Using it on trading or any others activity that is related to business is strictly forbidden."
                                +"If there is any bug is found please freely e-mail me. "+
                                "\n\tedisonthk@gmail.com"
                );
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                dialog.show();
                return true;
            case R.id.menu_delete:
                if(note != null){
                    note.close();
                    note = null;
                }
                if(mRowId != null){
                    mDbHelper.deleteNote(mRowId);
                }
                finish();

                return true;
            case R.id.menu_save:
                saveState();
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }*/
        return true;
    }

    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();

        long msTime = System.currentTimeMillis();
        Date currentDateTime = new Date(msTime);

        if(mRowId == null){
            //long id = mDbHelper.createNote(title, body, currentDateTime,);
            /*if(id > 0){
                mRowId = id;
            }else{
                Log.e("saveState", "failed to create note");
            }*/
        }else{
            //if(!mDbHelper.updateNote(mRowId, title, body, curDate)){
            //    Log.e("saveState","failed to update note");
            //}
        }
    }


    private void populateFields() {
        /*if (mRowId != null) {
            note = mDbHelper.getNote(mRowId);
            startManagingCursor(note);
            mTitleText.setText(note.getString(
                    note.getColumnIndexOrThrow(DbHandler.KEY_TITLE)));
            mBodyText.setText(note.getString(
                    note.getColumnIndexOrThrow(DbHandler.KEY_BODY)));
            curText = note.getString(
                    note.getColumnIndexOrThrow(DbHandler.KEY_BODY));
        }*/
    }
}

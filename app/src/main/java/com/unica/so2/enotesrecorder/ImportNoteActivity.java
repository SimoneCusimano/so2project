package com.unica.so2.enotesrecorder;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.unica.so2.enotesrecorder.DAL.DbHandler;
import com.unica.so2.enotesrecorder.Helper.JsonHelper;
import com.unica.so2.enotesrecorder.Model.Note;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ImportNoteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri data = getIntent().getData();
        if (data != null) {
            long noteId;
            getIntent().setData(null);
            try {
                noteId = importData(data);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Import aborted", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // launch home Activity (with FLAG_ACTIVITY_CLEAR_TOP) here…
            Intent i = new Intent(this, EditNoteActivity.class);
            i.putExtra(DbHandler.KEY_ID, noteId);
            startActivityForResult(i, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
    }

    private long importData(Uri data) {
        long importedNoteId = -1;
        final String scheme = data.getScheme();

        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            try {
                ContentResolver cr = this.getContentResolver();
                InputStream is = cr.openInputStream(data);
                if (is == null) {
                    importedNoteId = -1;
                }
                else {
                    StringBuilder buf = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String str;
                    while ((str = reader.readLine()) != null) {
                        buf.append(str).append("\n");
                    }
                    is.close();

                    Note note = JsonHelper.deserializeNote(buf.toString());
                    note.setId(null);
                    DbHandler dbHandler = new DbHandler(this);
                    importedNoteId = dbHandler.addNote(note);
                    dbHandler.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return importedNoteId;
    }
}

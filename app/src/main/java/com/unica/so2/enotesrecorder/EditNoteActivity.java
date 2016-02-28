package com.unica.so2.enotesrecorder;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.unica.so2.enotesrecorder.DAL.DbHandler;
import com.unica.so2.enotesrecorder.Helper.FileHelper;
import com.unica.so2.enotesrecorder.Helper.JsonHelper;
import com.unica.so2.enotesrecorder.Model.Content;
import com.unica.so2.enotesrecorder.Model.Note;

import java.io.File;


public class EditNoteActivity extends Activity {
    private EditText _titleEditText, _descriptionEditText;
    private RatingBar _ratingBar;
    private ImageButton _play, _stop, _fastForward, _fastBackward;
    private TextView _counterAudio;
    private Long _noteId;
    private FloatingActionButton _update;
    private String _outputFile;
    private MediaPlayer _mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_new);
        LinearLayout buttonsAreaLinearLayout = (LinearLayout)findViewById(R.id.buttonsAreaLinearLayout);
        buttonsAreaLinearLayout.addView(findViewById(R.id.editButtonsLinearLayout));
        setTitle(R.string.app_name);

        _mediaPlayer = new MediaPlayer();
        _titleEditText = (EditText) findViewById(R.id.title);
        _descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        _ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        _play = (ImageButton) findViewById(R.id.playImageButton);
        _stop = (ImageButton) findViewById(R.id.stopImageButton);
        _fastForward = (ImageButton) findViewById(R.id.fastForwardImageButton);
        _fastBackward = (ImageButton) findViewById(R.id.fastBackwardImageButton);
        _counterAudio = (TextView) findViewById(R.id.timerValueTextView);
        _update = (FloatingActionButton)findViewById(R.id.saveFloatingActionButton);
        _outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.unica.so2.enotesrecorder/PLACEHOLDER.3gp";


        _noteId = (savedInstanceState == null) ? null : (Long) savedInstanceState.getSerializable(DbHandler.KEY_ID);
        if (_noteId == null) {
            Bundle extras = getIntent().getExtras();
            _noteId = extras != null ? extras.getLong(DbHandler.KEY_ID) : null;
        }

        FillWidgetsValue();

        _update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Updating...", Toast.LENGTH_SHORT).show();

                SaveNote();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SaveNote();
        outState.putSerializable(DbHandler.KEY_ID, _noteId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SaveNote();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FillWidgetsValue();
    }

    private void FillWidgetsValue() {
        try {
            DbHandler dbHandler = new DbHandler(this);
            Note note = dbHandler.getNote(_noteId);
            dbHandler.close();

            _titleEditText.setText(note.getTitle());
            _descriptionEditText.setText(note.getContent().getDescription());
            _ratingBar.setRating(note.getRating());
            _mediaPlayer.setDataSource(_outputFile);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void sendEmail() {
        Content content = new Content();
        content.setDescription(_descriptionEditText.getText().toString());
        content.setAudio(FileHelper.encodeFileInString(new File(_outputFile)));

        Note note = new Note();
        String noteTitle = _titleEditText.getText().toString();
        note.setTitle(noteTitle);
        note.setRating(_ratingBar.getRating());
        note.setContent(content);


        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Note: " + _titleEditText.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, _descriptionEditText.getText().toString());
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.unica.so2.enotesrecorder/" + noteTitle + ".dat";
        FileHelper.writeJsonToFile(filePath, JsonHelper.serializeNote(note), this);
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(EditNoteActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void SaveNote() {
        Content content = new Content();
        content.setDescription(_descriptionEditText.getText().toString());
        content.setAudio(FileHelper.encodeFileInString(new File(_outputFile)));

        Note note = new Note();
        note.setTitle(_titleEditText.getText().toString());
        note.setRating(_ratingBar.getRating());
        note.setContent(content);

        DbHandler dbHandler = new DbHandler(this);
        boolean result = dbHandler.updateNote(note);
        if (result) {
            Toast.makeText(getApplicationContext(), "Note Updated", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Unable to Update the Note", Toast.LENGTH_SHORT).show();
        }
        dbHandler.close();
    }
}

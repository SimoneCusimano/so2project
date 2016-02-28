package com.unica.so2.enotesrecorder;

import android.app.Activity;
import android.media.MediaPlayer;
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
import com.unica.so2.enotesrecorder.Helper.AudioHelper;
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

    private void SaveNote() {
        Content content = new Content();
        content.setDescription(_descriptionEditText.getText().toString());
        content.setAudio(AudioHelper.encodeFileInString(new File(_outputFile)));

        Note note = new Note();
        note.setTitle(_titleEditText.getText().toString());
        note.setRating(_ratingBar.getRating());
        note.setContent(content);

        DbHandler dbHandler = new DbHandler(this);
        boolean result = dbHandler.updateNote(note);
        if (result) {
            Toast.makeText(getApplicationContext(), "Note Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Unable to Update the Note", Toast.LENGTH_SHORT).show();
        }
        dbHandler.close();
    }
}

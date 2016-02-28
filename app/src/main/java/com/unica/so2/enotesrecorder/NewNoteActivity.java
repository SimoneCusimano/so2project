package com.unica.so2.enotesrecorder;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.unica.so2.enotesrecorder.DAL.DbHandler;
import com.unica.so2.enotesrecorder.Helper.FileHelper;
import com.unica.so2.enotesrecorder.Model.Content;
import com.unica.so2.enotesrecorder.Model.Note;

import java.io.File;
import java.io.IOException;


public class NewNoteActivity extends Activity {
    ImageButton _cancel, _stop, _record;
    private EditText _titleEditText, _descriptionEditText;
    private RatingBar _ratingBar;
    FloatingActionButton _save;
    private boolean _isRecording = false; // It handles the Rec/Pause Button state
    private MediaRecorder _mediaRecorder;
    private String _outputFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_new);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        LinearLayout buttonsAreaLinearLayout = (LinearLayout)findViewById(R.id.buttonsAreaLinearLayout);
        buttonsAreaLinearLayout.addView(findViewById(R.id.newButtonsLinearLayout));

        _cancel = (ImageButton)findViewById(R.id.cancelImageButton);
        _stop = (ImageButton)findViewById(R.id.stopImageButton);
        _record = (ImageButton)findViewById(R.id.recImageButton);
        _save = (FloatingActionButton)findViewById(R.id.saveFloatingActionButton);
        _titleEditText = (EditText) findViewById(R.id.title);
        _descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        _ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        _stop.setEnabled(false);
        _cancel.setEnabled(false);
        _outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.unica.so2.enotesrecorder/PLACEHOLDER.3gp";


        _record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (_isRecording) {
                        // Pause stuff
                        _record.setImageResource(R.drawable.ic_mic_black_48dp);

                    } else {
                        // Recording Mode
                        _record.setImageResource(R.drawable.ic_pause_black_48dp);

                        File directory = new File(_outputFile).getParentFile();
                        if (!directory.exists()) {
                            directory.mkdirs();
                        }

                        // Check if _mediaRecorder has been aborted (Cancel Event) and it needs to be reconfigured
                        if (_mediaRecorder == null) {
                            _mediaRecorder = new MediaRecorder();
                            _mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            _mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                            _mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                            _mediaRecorder.setOutputFile(_outputFile);
                        }

                        _mediaRecorder.prepare();
                        _mediaRecorder.start();

                        _stop.setEnabled(true);
                        _cancel.setEnabled(true);

                        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();
                    }

                    _isRecording = !_isRecording;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        _stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    _mediaRecorder.stop();
                    _mediaRecorder.release();
                    _mediaRecorder = null;
                    _record.setImageResource(R.drawable.ic_mic_black_48dp);

                    _record.setEnabled(false);
                    _stop.setEnabled(false);
                    _cancel.setEnabled(true);

                    Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_SHORT).show();
                }
                catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        });

        _cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (_mediaRecorder != null) {
                        _mediaRecorder.reset();
                    }

                    File file = new File(_outputFile);
                    file.delete();
                    _record.setImageResource(R.drawable.ic_mic_black_48dp);

                    _record.setEnabled(true);
                    _stop.setEnabled(false);
                    _cancel.setEnabled(false);

                    Toast.makeText(getApplicationContext(), "Recording aborted", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        _save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Saving...", Toast.LENGTH_SHORT).show();

                Content content = new Content();
                content.setDescription(_descriptionEditText.getText().toString());
                content.setAudio(FileHelper.encodeFileInString(new File(_outputFile)));

                Note note = new Note();
                note.setTitle(_titleEditText.getText().toString());
                note.setRating(_ratingBar.getRating());
                note.setContent(content);

                DbHandler dbHandler = new DbHandler(v.getContext());
                long result = dbHandler.addNote(note);
                if(result != -1) {
                    Toast.makeText(getApplicationContext(), "Note Created", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Unable to Create the Note", Toast.LENGTH_SHORT).show();
                }
                dbHandler.close();
            }
        });
    }
}
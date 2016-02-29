package com.unica.so2.enotesrecorder;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import java.util.concurrent.TimeUnit;


public class EditNoteActivity extends AppCompatActivity {
    private static final String TAG = "EditNoteActivity";

    private EditText _titleEditText, _descriptionEditText;
    private RatingBar _ratingBar;
    private ImageButton _play, _stop, _fastForward, _fastBackward;
    private TextView _counterAudio;
    private Long _noteId;
    private FloatingActionButton _update;
    private String _currentFilePath;
    private MediaPlayer _mediaPlayer;
    private double _startTime = 0;
    private double _finalTime = 0;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private Handler _handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_new);
        // to avoid the automatically appear of the keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        LinearLayout buttonsAreaLinearLayout = (LinearLayout)findViewById(R.id.buttonsAreaLinearLayout);
        LinearLayout ll = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.content_note_edit_buttons, null);
        buttonsAreaLinearLayout.addView(ll);
        setTitle(R.string.app_name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _mediaPlayer = new MediaPlayer();
        _titleEditText = (EditText) findViewById(R.id.titleEditText);
        _descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        _ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        _play = (ImageButton) findViewById(R.id.playImageButton);
        _stop = (ImageButton) findViewById(R.id.stopImageButton);
        _fastForward = (ImageButton) findViewById(R.id.fastForwardImageButton);
        _fastBackward = (ImageButton) findViewById(R.id.fastBackwardImageButton);
        _counterAudio = (TextView) findViewById(R.id.timerValueTextView);
        _update = (FloatingActionButton)findViewById(R.id.saveFloatingActionButton);
        _currentFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.unica.so2.enotesrecorder/PLACEHOLDER.3gp";

        _stop.setEnabled(false);
        _fastForward.setEnabled(false);
        _fastBackward.setEnabled(false);

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
                saveNote();
            }
        });

        _play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    _fastBackward.setEnabled(true);
                    _fastForward.setEnabled(true);
                    _stop.setEnabled(true);

                    if(_mediaPlayer.isPlaying()){
                        _play.setImageResource(R.drawable.ic_play_arrow_black_48dp);
                        _mediaPlayer.pause();

                        _handler.postDelayed(UpdateSongTime, 100);
                    }
                    else {
                        _play.setImageResource(R.drawable.ic_pause_black_48dp);
                        _mediaPlayer.prepare();
                    }
                }
                catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }

            }
        });
        _stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _play.setImageResource(R.drawable.ic_play_arrow_black_48dp);
                _stop.setEnabled(false);
                _fastForward.setEnabled(false);
                _fastBackward.setEnabled(false);
                _mediaPlayer.stop();
            }
        });
        _fastBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) _startTime;

                if((temp+forwardTime)<= _finalTime){
                    _startTime = _startTime + forwardTime;
                    _mediaPlayer.seekTo((int) _startTime);
                    //Toast.makeText(getApplicationContext(),"You have Jumped forward 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });
        _fastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) _startTime;

                if((temp-backwardTime)>0){
                    _startTime = _startTime - backwardTime;
                    _mediaPlayer.seekTo((int) _startTime);
                    //Toast.makeText(getApplicationContext(),"You have Jumped backward 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });
        _mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer player) {
                player.start();
                _finalTime = _mediaPlayer.getDuration();
                _startTime = _mediaPlayer.getCurrentPosition();
            }
        });
        _mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                try {
                    Log.i("Completion Listener", "Song Complete");
                    mp.stop();
                    mp.reset();
                    mp.setDataSource(_currentFilePath);
                    _play.setImageResource(R.drawable.ic_play_arrow_black_48dp);
                    _stop.setEnabled(false);
                    _fastForward.setEnabled(false);
                    _fastBackward.setEnabled(false);
                }
                catch(Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveNote();
        outState.putSerializable(DbHandler.KEY_ID, _noteId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveNote();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FillWidgetsValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sendMail:
                sendNoteByEmail();
                return true;

            case R.id.action_deleteNote:
                DbHandler dbHandler = new DbHandler(this);
                dbHandler.open();
                dbHandler.deleteNote(_noteId);
                dbHandler.close();

                Intent i = new Intent(this, NoteListActivity.class);
                startActivity(i);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void FillWidgetsValue() {
        try {
            DbHandler dbHandler = new DbHandler(this);
            dbHandler.open();
            Note note = dbHandler.getNote(_noteId);
            dbHandler.close();

            _titleEditText.setText(note.getTitle());
            _descriptionEditText.setText(note.getContent().getDescription());
            _ratingBar.setRating(note.getRating());
            FileHelper.decodeStringInFile(note.getContent().getAudio(), _currentFilePath);
            _mediaPlayer.setDataSource(_currentFilePath);

            _counterAudio.setText(
                    String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) _finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) _finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) _finalTime))));
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    protected void sendNoteByEmail() {
        Content content = new Content();
        content.setDescription(_descriptionEditText.getText().toString());
        content.setAudio(FileHelper.encodeFileInString(new File(_currentFilePath)));

        Note note = new Note();
        String noteTitle = _titleEditText.getText().toString();
        note.setTitle(noteTitle);
        note.setRating(_ratingBar.getRating());
        note.setContent(content);


        Intent sendEmailIntent = new Intent(Intent.ACTION_SEND);
        sendEmailIntent.setData(Uri.parse("mailto:"));
        sendEmailIntent.setType("message/rfc822");
        sendEmailIntent.putExtra(Intent.EXTRA_SUBJECT, "Note: " + _titleEditText.getText().toString());
        sendEmailIntent.putExtra(Intent.EXTRA_TEXT, _descriptionEditText.getText().toString());
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.unica.so2.enotesrecorder/" + noteTitle + ".eNote";
        FileHelper.writeJsonToFile(filePath, JsonHelper.serializeNote(note), this);
        sendEmailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));

        try {
            startActivity(Intent.createChooser(sendEmailIntent, "Send mail..."));
            finish();
        }
        catch (android.content.ActivityNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void saveNote() {
        Content content = new Content();
        content.setDescription(_descriptionEditText.getText().toString());
        content.setAudio(FileHelper.encodeFileInString(new File(_currentFilePath)));

        Note note = new Note();
        note.setTitle(_titleEditText.getText().toString());
        note.setRating(_ratingBar.getRating());
        note.setContent(content);

        DbHandler dbHandler = new DbHandler(this);
        dbHandler.open();
        boolean result = dbHandler.updateNote(note);
        dbHandler.close();
        if (result) {
            Toast.makeText(getApplicationContext(), "Note Updated", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Unable to Update the Note", Toast.LENGTH_SHORT).show();
        }

    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            _startTime = _mediaPlayer.getCurrentPosition();
            _counterAudio.setText(
                    String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) _startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) _startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) _startTime))));
            _handler.postDelayed(this, 100);
        }
    };
}

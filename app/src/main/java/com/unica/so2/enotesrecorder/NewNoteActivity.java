package com.unica.so2.enotesrecorder;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class NewNoteActivity extends Activity {
    ImageButton cancel,stop,record;
    FloatingActionButton save;
    private boolean isRecording = false; // It handles the Rec/Pause Button state
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_new_note);

        cancel = (ImageButton)findViewById(R.id.cancelButton);
        stop = (ImageButton)findViewById(R.id.stopButton);
        record = (ImageButton)findViewById(R.id.recButton);
        save = (FloatingActionButton)findViewById(R.id.saveFloatingActionButton);

        stop.setEnabled(false);
        cancel.setEnabled(false);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.unica.so2.enotesrecorder/PLACEHOLDER.3gp";


        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isRecording)
                    {
                        // Pause stuff
                        record.setBackgroundResource(R.drawable.ic_pause_black_48dp);
                    }
                    else
                    {
                        // Recording Mode
                        record.setBackgroundResource(R.drawable.ic_mic_black_48dp);

                        File directory = new File(outputFile).getParentFile();
                        if (!directory.exists())
                        {
                            directory.mkdirs();
                        }

                        // Check if myAudioRecorder has been aborted (Cancel Event) and it needs to be reconfigured
                        if (myAudioRecorder == null)
                        {
                            myAudioRecorder = new MediaRecorder();
                            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                            myAudioRecorder.setOutputFile(outputFile);
                        }

                        myAudioRecorder.prepare();
                        myAudioRecorder.start();

                        record.setEnabled(true);
                        stop.setEnabled(true);
                        cancel.setEnabled(true);

                        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();
                    }

                    isRecording = !isRecording;
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    myAudioRecorder.stop();
                    myAudioRecorder.release();
                    myAudioRecorder = null;
                    record.setBackgroundResource(R.drawable.ic_mic_black_48dp);

                    record.setEnabled(false);
                    stop.setEnabled(false);
                    cancel.setEnabled(true);

                    Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_SHORT).show();
                }
                catch (IllegalStateException e)
                {
                    e.printStackTrace();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if(myAudioRecorder != null)
                    {
                        myAudioRecorder.reset();
                    }

                    File file = new File(outputFile);
                    file.delete();
                    record.setBackgroundResource(R.drawable.ic_mic_black_48dp);

                    record.setEnabled(true);
                    stop.setEnabled(false);
                    cancel.setEnabled(false);

                    Toast.makeText(getApplicationContext(), "Recording aborted", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Note saved", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
package com.unica.so2.enotesrecorder;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class NewNoteActivity extends Activity {
    Button cancel,stop,record;
    FloatingActionButton save;
    private boolean isRecording = false; // It handles the Rec/Pause Button state
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        cancel = (Button)findViewById(R.id.cancelButton);
        stop = (Button)findViewById(R.id.stopButton);
        record = (Button)findViewById(R.id.recButton);

        stop.setEnabled(false);
        cancel.setEnabled(false);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.unica.so2.enotesrecorder/PLACEHOLDER.3gp";;

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording)
                {
                    // Pause stuff
                    record.setBackgroundResource(R.drawable.ic_pause_black_48dp);

                }
                else
                {
                    // Recording Mode
                    record.setBackgroundResource(R.drawable.ic_mic_black_48dp);

                    // Check if myAudioRecorder has been aborted (Cancel Event) and it needs to be reconfigured
                    if (myAudioRecorder == null)
                    {
                        myAudioRecorder = new MediaRecorder();
                        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                        myAudioRecorder.setOutputFile(outputFile);
                    }

                    try
                    {
                        myAudioRecorder.prepare();
                        myAudioRecorder.start();
                    }
                    catch (IllegalStateException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch (IOException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                record.setEnabled(false);
                stop.setEnabled(true);
                cancel.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder = null;

                record.setEnabled(false);
                stop.setEnabled(false);
                cancel.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    myAudioRecorder.reset();
                    File file = new File(outputFile);
                    file.delete();
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                record.setEnabled(true);
                stop.setEnabled(false);
                cancel.setEnabled(false);

                Toast.makeText(getApplicationContext(), "Recording aborted", Toast.LENGTH_LONG).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // Save stuff...
                }
            });
    }
}
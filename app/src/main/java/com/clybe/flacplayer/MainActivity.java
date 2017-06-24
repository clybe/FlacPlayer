package com.clybe.flacplayer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.clybe.flacplayer.jni.LibFlac;
import com.clybe.flacplayer.player.FlacPlayer;
import com.clybe.flacplayer.recorder.FlacRecorder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String File_Raw_Name = "pcm.raw";
    public static final String File_Flac_Name = "out.wav";

    private FlacRecorder flacRecorder;
    private FlacPlayer flacPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Example of a call to a native method

        findViewById(R.id.btn_record).setOnClickListener(this);
        findViewById(R.id.btn_play_record).setOnClickListener(this);

        flacRecorder = new FlacRecorder();
        flacPlayer = new FlacPlayer();

        Trace.e(LibFlac.stringFromJNI());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_record:
                if (flacRecorder.isRecording()) {
                    ((Button) v).setText("Start Recording");
                    flacRecorder.release();
                } else {
                    ((Button) v).setText("Stop Recording");
                    flacRecorder.start();
                }
                break;
            case R.id.btn_play_record:
                LibFlac.decode();
                flacPlayer.play();
                break;
        }
    }
}

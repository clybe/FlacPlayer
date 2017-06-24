package com.clybe.flacplayer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.clybe.flacplayer.recorder.FlacRecorder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public static final String File_Raw_Name = "pcm.raw";
    public static final String File_Flac_Name = "out.wav";

    private FlacRecorder flacRecorder;

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
        findViewById(R.id.btn_stop_record).setOnClickListener(this);

        flacRecorder = new FlacRecorder();

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_record:
                flacRecorder.start();
                break;
            case R.id.btn_stop_record:
                flacRecorder.release();
                break;
        }
    }
}

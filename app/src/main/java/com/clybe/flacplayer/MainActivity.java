package com.clybe.flacplayer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.clybe.flacplayer.player.FlacPlayer;
import com.clybe.flacplayer.processer.DecodingThread;
import com.clybe.flacplayer.recorder.FlacRecorder;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String Record_Raw_FileName = "pcm.raw";
    public static final String Record_Wav_FileName = "out.wav";

    public static final String Flac_Decoded_FileName = "flac.wav";//flac文件decode出来的wav文件

    private FlacRecorder flacRecorder;
    private FlacPlayer flacPlayer;
    private DecodingThread decodingThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Example of a call to a native method

        findViewById(R.id.btn_record).setOnClickListener(this);
        findViewById(R.id.btn_play_record).setOnClickListener(this);
        findViewById(R.id.btn_decode_flac_file).setOnClickListener(this);
        findViewById(R.id.btn_play_flac_decoded_file).setOnClickListener(this);

        flacRecorder = new FlacRecorder();
        flacPlayer = new FlacPlayer();

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
                flacPlayer.play(FileUtils.getPath(Record_Wav_FileName));
                break;
            case R.id.btn_decode_flac_file:
                new MaterialFilePicker()
                        .withActivity(this)
                        .withRequestCode(1)
                        .withFilter(Pattern.compile(".*\\.flac$")) // Filtering files and directories by file name using regexp
                        .withFilterDirectories(true) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();
                break;
            case R.id.btn_play_flac_decoded_file:
                flacPlayer.play(FileUtils.getPath(Flac_Decoded_FileName));
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Trace.d("filePath is " + filePath + ", begin decoding");

            if (decodingThread != null) {
                decodingThread.forceStop();//force stop decoding
            }

            final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                    "Processing...", true);
            dialog.setCancelable(false);
            dialog.show();

            decodingThread = new DecodingThread(
                    filePath,
                    FileUtils.getPath(Flac_Decoded_FileName),
                    new DecodingThread.DecodeCallback() {
                        @Override
                        public void onSuccess() {
                            decodingThread = null;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "decoding success", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFailed(Exception e) {
                            decodingThread = null;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "decoding fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
            decodingThread.start();
        }

    }

}

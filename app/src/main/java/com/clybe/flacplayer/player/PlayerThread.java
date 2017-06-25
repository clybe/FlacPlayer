package com.clybe.flacplayer.player;

import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;

import com.clybe.flacplayer.Trace;
import com.clybe.flacplayer.recorder.RecorderConfig;

import java.io.FileInputStream;

/**
 * Created by caiyu on 2017/6/24.
 */

public class PlayerThread extends Thread {

    public boolean isRunning = false;

    private String filePath;

    public PlayerThread(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        isRunning = true;

        try {
            int bufferSizeInBytes = AudioRecord.getMinBufferSize(RecorderConfig.sampleRateInHz,
                    RecorderConfig.channelConfig, RecorderConfig.audioFormat);

            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, // stream mode
                    RecorderConfig.sampleRateInHz, // sample rate
                    RecorderConfig.channelConfig, // single or stereo
                    RecorderConfig.audioFormat, // bit format
                    bufferSizeInBytes,
                    AudioTrack.MODE_STREAM);

            audioTrack.play();

            FileInputStream fis = new FileInputStream(filePath);
            byte[] buffer = new byte[bufferSizeInBytes];
            int readSize;

            while (isRunning && (readSize = fis.read(buffer, 0, bufferSizeInBytes)) > 0) {
                audioTrack.write(buffer, 0, readSize);
            }

            audioTrack.stop();
        } catch (Exception e) {
            Trace.e("PlayerThread exception:" + e.getMessage());
        }

    }
}

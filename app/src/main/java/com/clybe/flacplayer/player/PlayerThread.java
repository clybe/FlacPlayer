package com.clybe.flacplayer.player;

import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;

import com.clybe.flacplayer.FileUtils;
import com.clybe.flacplayer.MainActivity;
import com.clybe.flacplayer.Trace;
import com.clybe.flacplayer.recorder.RecorderConfig;

import java.io.FileInputStream;

/**
 * Created by caiyu on 2017/6/24.
 */

public class PlayerThread extends Thread {

    @Override
    public void run() {

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

            FileInputStream fis = new FileInputStream(FileUtils.getFile(MainActivity.File_Raw_Name));
            byte[] buffer = new byte[bufferSizeInBytes];
            int readSize = 0;

            while ((readSize = fis.read(buffer, 0, bufferSizeInBytes)) > 0) {
                audioTrack.write(buffer, 0, readSize);
            }

            audioTrack.stop();
        } catch (Exception e) {
            Trace.e("PlayerThread exception:" + e.getMessage());
        }

    }
}

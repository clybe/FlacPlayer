package com.clybe.flacplayer.recorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

/**
 * Created by caiyu on 2017/6/24.
 */

public class FlacRecorder implements IRecorder {

    //AudioRecord params
    private int audioSource = MediaRecorder.AudioSource.MIC;
    private static int sampleRateInHz = 44100;
    private static int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
    private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int bufferSizeInBytes;

    //Record state
    private static final int State_Init = 0;
    private static final int State_Recording = 1;
    private static final int State_Release = 2;
    private int recordState = State_Init;

    private AudioRecord audioRecord;
    private RecorderThread recorderThread;

    public FlacRecorder() {
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
                channelConfig, audioFormat);
        // 创建AudioRecord对象
        audioRecord = new AudioRecord(audioSource, sampleRateInHz,
                channelConfig, audioFormat, bufferSizeInBytes);

    }

    @Override
    public int getBufferSize() {
        return bufferSizeInBytes;
    }

    public void start() {
        if (recordState == State_Init) {
            recordState = State_Recording;
            audioRecord.startRecording();
            recorderThread = new RecorderThread(this);
            recorderThread.start();
        }

    }

    @Override
    public int read(byte[] bytes, int buffSize) throws Throwable {
        return audioRecord.read(bytes, 0, buffSize);
    }

    @Override
    public void release() {
        if (recordState == State_Recording) {
            recordState = State_Release;
            try {
                recorderThread.isRecord = false;
                recorderThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            audioRecord.stop();
            audioRecord.release();
        }
    }

    @Override
    public AudioRecord getAudioRecord() {
        return audioRecord;
    }

}

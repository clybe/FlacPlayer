package com.clybe.flacplayer.recorder;

import android.media.AudioRecord;

/**
 * Created by caiyu on 2017/6/24.
 */

public class FlacRecorder implements IRecorder {

    private int bufferSizeInBytes;

    //Record state
    private static final int State_Init = 0;
    private static final int State_Recording = 1;
    private static final int State_Release = 2;
    private int recordState = State_Init;

    private AudioRecord audioRecord;
    private RecorderThread recorderThread;

    public FlacRecorder() {
        bufferSizeInBytes = AudioRecord.getMinBufferSize(RecorderConfig.sampleRateInHz,
                RecorderConfig.channelConfig, RecorderConfig.audioFormat);
    }

    @Override
    public int getBufferSize() {
        return bufferSizeInBytes;
    }

    public void start() {
        if (recordState == State_Init) {
            // 创建AudioRecord对象
            audioRecord = new AudioRecord(RecorderConfig.audioSource, RecorderConfig.sampleRateInHz,
                    RecorderConfig.channelConfig, RecorderConfig.audioFormat, bufferSizeInBytes);
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
                recorderThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            audioRecord.release();

            //reset recordState to init
            recordState = State_Init;

            //Send Stop event(switch UI state)
        }
    }

    @Override
    public boolean isRecording() {
        return recordState == State_Recording;
    }


}

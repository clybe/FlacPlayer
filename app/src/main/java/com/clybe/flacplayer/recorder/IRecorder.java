package com.clybe.flacplayer.recorder;

/**
 * Created by caiyu on 2017/6/24.
 */

public interface IRecorder {

    int getBufferSize();

    void start() throws Throwable;

    int read(byte[] bytes, int buffSize) throws Throwable;

    void release();

    boolean isRecording();
}

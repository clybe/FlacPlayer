package com.clybe.flacplayer.processer;

import com.clybe.flacplayer.FileUtils;
import com.clybe.flacplayer.Trace;
import com.clybe.flacplayer.jni.LibFlac;

import java.io.File;

/**
 * Created by caiyu on 2017/6/25.
 */

public class DecodingThread extends Thread {

    private String inFilePath;
    private String outFilePath;
    private DecodeCallback decodeCallback;

    public DecodingThread(String inFilePath,
                          String outFilePath,
                          DecodeCallback decodeCallback) {
        this.inFilePath = inFilePath;
        this.outFilePath = outFilePath;
        this.decodeCallback = decodeCallback;
    }

    @Override
    public void run() {
        try {
            LibFlac.decode(inFilePath, outFilePath);
            if (decodeCallback != null) {
                decodeCallback.onSuccess();
            }
        } catch (Exception e) {
            Trace.e("decoding failed:" + e.getMessage());
            //remove decoded file
            File file = new File(outFilePath);
            if (file.exists()) {
                file.delete();
            }
            decodeCallback.onFailed(e);
        }

    }

    public void forceStop() {
        try {

            File file = FileUtils.getFile(outFilePath);
            if (file.exists()) {
                file.delete();
            }
            stop();
        } catch (Exception e) {
            Trace.e("forceStop exception " + e.getMessage());
        }

    }


    public interface DecodeCallback {
        void onSuccess();

        void onFailed(Exception e);
    }
}


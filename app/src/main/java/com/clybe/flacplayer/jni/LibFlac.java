package com.clybe.flacplayer.jni;

/**
 * Created by caiyu on 2017/6/24.
 */

public class LibFlac {

    static {
        System.loadLibrary("flacplayer");
    }

    native public static String stringFromJNI();

    /**
     * @param inFilePath  flac input file
     * @param outFilePath wav output file
     * @return
     */
    native public static String decode(String inFilePath, String outFilePath);

}

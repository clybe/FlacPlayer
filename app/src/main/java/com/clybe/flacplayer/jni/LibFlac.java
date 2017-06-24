package com.clybe.flacplayer.jni;

/**
 * Created by caiyu on 2017/6/24.
 */

public class LibFlac {

    static {
        System.loadLibrary("flacplayer");
    }
    
    native public static String stringFromJNI();

    native public static String decode();

}

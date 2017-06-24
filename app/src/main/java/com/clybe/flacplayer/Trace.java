package com.clybe.flacplayer;

import android.util.Log;

/**
 * Created by caiyu on 2017/6/24.
 */

public class Trace {

    public static final String TAG = "FlacPalyer";

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void d(String msg) {
        Log.d(TAG, msg);
    }
}

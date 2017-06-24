package com.clybe.flacplayer.recorder;

import android.media.AudioFormat;
import android.media.MediaRecorder;

/**
 * Created by caiyu on 2017/6/24.
 */

public class RecorderConfig {
    //AudioRecord params
    public static final int audioSource = MediaRecorder.AudioSource.MIC;
    public static final int sampleRateInHz = 44100;
    public static final int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
    public static final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
}

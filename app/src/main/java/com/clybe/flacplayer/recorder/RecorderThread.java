package com.clybe.flacplayer.recorder;

import android.media.AudioRecord;

import com.clybe.flacplayer.FileUtils;
import com.clybe.flacplayer.MainActivity;
import com.clybe.flacplayer.Trace;
import com.clybe.flacplayer.processer.ProcesserThread;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by caiyu on 2017/6/24.
 */

public class RecorderThread extends Thread {

    private int bufferSizeInBytes;
    private IRecorder iRecorder;

    public RecorderThread(IRecorder iRecorder) {
        this.bufferSizeInBytes = iRecorder.getBufferSize();
        this.iRecorder = iRecorder;
    }

    @Override
    public void run() {
        Trace.d("RecorderThread begin");
        byte[] bufferData = new byte[bufferSizeInBytes];

        int readSize = 0;
        final LinkedBlockingQueue<Object> processorQueue = new LinkedBlockingQueue<>();

        ProcesserThread processerThread = new ProcesserThread(processorQueue);
        processerThread.start();

        try {
            while (iRecorder.isRecording() == true) {
                readSize = iRecorder.read(bufferData, bufferSizeInBytes);
                if (AudioRecord.ERROR_INVALID_OPERATION != readSize) {
                    processorQueue.put(Arrays.copyOf(bufferData, bufferData.length));
                }
            }

            processorQueue.put("stop");
            processerThread.join();

            copyWaveFile(MainActivity.File_Raw_Name, MainActivity.File_Flac_Name);

        } catch (Throwable e) {

        }

        Trace.d("RecorderThread end");
    }


    private void copyWaveFile(String inFilename, String outFilename) {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = RecorderConfig.sampleRateInHz;
        int channels = 2;
        long byteRate = 16 * RecorderConfig.sampleRateInHz * channels / 8;
        byte[] data = new byte[bufferSizeInBytes];
        try {
            in = new FileInputStream(FileUtils.getFile(inFilename));
            out = new FileOutputStream(FileUtils.getFile(outFilename));
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;
            WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                    longSampleRate, channels, byteRate);
            while (in.read(data) != -1) {
                out.write(data);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen,
                                     long totalDataLen, long longSampleRate, int channels, long byteRate)
            throws IOException {
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = 16; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }
}

package com.clybe.flacplayer.processer;

import com.clybe.flacplayer.FileUtils;
import com.clybe.flacplayer.MainActivity;
import com.clybe.flacplayer.Trace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by caiyu on 2017/6/24.
 */

public class ProcesserThread extends Thread {


    private LinkedBlockingQueue<Object> linkedBlockingDeque;

    public ProcesserThread(LinkedBlockingQueue<Object> linkedBlockingDeque) {
        this.linkedBlockingDeque = linkedBlockingDeque;
    }

    @Override
    public void run() {
        Trace.d("ProcesserThread begin");
        Object bufferObject;
        FileOutputStream fos = null;
        try {
            File fileRaw = FileUtils.getFile(MainActivity.Record_Raw_FileName);
            if (fileRaw.exists()) {
                fileRaw.delete();
            }
            fos = new FileOutputStream(fileRaw);// 建立一个可存取字节的文件
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            while ((bufferObject = linkedBlockingDeque.take()) != null) {
                if (bufferObject instanceof byte[]) {
                    writeDataToFile(fos, (byte[]) bufferObject);
                } else {
                    //buffer end
                    break;
                }
            }

            try {
                fos.close();// 关闭写入流
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Trace.d("ProcesserThread end");
    }


    private void writeDataToFile(FileOutputStream fos, byte[] buffer) {
        try {
            fos.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

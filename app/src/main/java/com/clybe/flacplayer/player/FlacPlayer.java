package com.clybe.flacplayer.player;

import java.io.File;

/**
 * Created by caiyu on 2017/6/24.
 */

public class FlacPlayer {

    PlayerThread playerThread;

    public void play(String filePatch) {
        if (playerThread != null && playerThread.isRunning) {
            playerThread.isRunning = false;
            try {
                playerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        File file = new File(filePatch);
        if(file.exists()){
            playerThread = new PlayerThread(filePatch);
            playerThread.start();
        }
    }
}

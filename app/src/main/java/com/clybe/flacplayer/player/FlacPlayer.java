package com.clybe.flacplayer.player;

/**
 * Created by caiyu on 2017/6/24.
 */

public class FlacPlayer {

    public void play() {
        new PlayerThread().start();
    }
}

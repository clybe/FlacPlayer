package com.clybe.flacplayer;

import android.app.Application;

/**
 * Created by caiyu on 2017/6/24.
 */

public class MyApp extends Application {


    public static MyApp myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
    }


    public static MyApp getContext() {
        return myApp;
    }
}

package com.clybe.flacplayer;

import java.io.File;

/**
 * Created by caiyu on 2017/6/24.
 */

public class FileUtils {
    public static File rootDir;

    public synchronized static File getRootDir() {
        if (rootDir == null) {
            rootDir = MyApp.getContext().getExternalCacheDir();
        }
        return rootDir;
    }

    public static File getFile(String fileName) {
        return new File(getRootDir(), fileName);
    }
}

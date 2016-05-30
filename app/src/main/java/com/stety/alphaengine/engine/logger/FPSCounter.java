package com.stety.alphaengine.engine.logger;

import android.util.Log;

/**
 * EN: Meter FPS.
 * CS: Měřič FPS.
 */
public class FPSCounter
{
    long startTime = System.nanoTime();
    int frames = 0;

    /**
     * EN: Write FPS into ADB logs.
     * CS: vypíše FPS do ADB logů.
     */
    public void logFrame()
    {
        frames++;
        if (System.nanoTime() - startTime >= 1000000000)
        {
            Log.d(Logs.tagFPSCounter, "FPS: " + frames);
            frames = 0;
            startTime = System.nanoTime();
        }
    }
}
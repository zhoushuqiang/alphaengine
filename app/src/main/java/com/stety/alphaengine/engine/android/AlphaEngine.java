package com.stety.alphaengine.engine.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.stety.alphaengine.engine.logger.Logs;

/**
 * EN: Main Activity for Engine-game.
 * CS: Hlavní aktivita pro Engine-hru.
 */
public class AlphaEngine extends Activity {

    public static Context context;
    static AssetManager sAssetManager;
    static private GLSurfaceView glSurfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        sAssetManager = getAssets();
        LibJNIWrapper.createAssetManager(sAssetManager);

        glSurfaceView = new GLSurfaceView(this);

        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportGLES3 = configurationInfo.reqGlEsVersion >= 0x30000;
        if (supportGLES3) {
            glSurfaceView.setEGLContextClientVersion(3);
            Renderer.GLESVersion = 3;
            if (Logs.Logs)
                Log.d("GLES version", "Created GLES 3 context.");
        } else {
            glSurfaceView.setEGLContextClientVersion(2);
            Renderer.GLESVersion = 2;
            if (Logs.Logs)
                Log.d("GLES version", "Created GLES 2 context.");
        }

        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); //For working emulator.
        final Renderer renderer = new Renderer();
        glSurfaceView.setRenderer(renderer);

        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                glSurfaceView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        Renderer.event = event;
                        Renderer.input();
                    }
                });
                return true;
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            glSurfaceView.setPreserveEGLContextOnPause(true);
            Renderer.IsPreserveEGLContextOnPause = true;
        }

        setContentView(glSurfaceView);
        context = this;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        Renderer.firstCount = true;
        final View rootView = getWindow().getDecorView();
        super.onResume();

        switch (Build.VERSION.SDK_INT) {
            default:
            case Build.VERSION_CODES.LOLLIPOP:
            case Build.VERSION_CODES.KITKAT:
                rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                break;
            case Build.VERSION_CODES.JELLY_BEAN_MR2:
            case Build.VERSION_CODES.JELLY_BEAN_MR1:
            case Build.VERSION_CODES.JELLY_BEAN:
            case Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1:
            case Build.VERSION_CODES.ICE_CREAM_SANDWICH:
            case Build.VERSION_CODES.HONEYCOMB_MR2:
            case Build.VERSION_CODES.HONEYCOMB_MR1:
            case Build.VERSION_CODES.HONEYCOMB:
                rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                rootView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if (visibility == View.SYSTEM_UI_FLAG_VISIBLE) {
                            rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                        }
                    }
                });
                break;
            case Build.VERSION_CODES.GINGERBREAD_MR1:
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
        }
        glSurfaceView.onResume();
    }

    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
        Renderer.firstCount = true;
        Renderer.pause();
    }

    @Override
    public void onBackPressed() {
        //android.os.Process.killProcess(android.os.Process.myPid()); This is kill app.

        //Full stop app.
        System.exit(0);
    }
}

package com.stety.alphaengine.engine.android;

import android.opengl.ETC1Util;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import com.stety.alphaengine.engine.helpers.EngineSetHelpers;
import com.stety.alphaengine.engine.helpers.MatrixHelper;
import com.stety.alphaengine.engine.logger.FPSCounter;
import com.stety.alphaengine.engine.logger.Logs;
import com.stety.alphaengine.engine.objects.Scene;
import com.stety.alphaengine.game.Menu.Menu;
import com.stety.alphaengine.game.programs.Programs;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * EN: Renderer for all engine.
 * CS: Renderer pro celý engine.
 */
public class Renderer implements GLSurfaceView.Renderer {
    //game loop
    static final int TICKS_PER_SECOND = 60;
    static final int SKIP_TICKS = 1000000000 / TICKS_PER_SECOND; //nanoseconds
    static final int MAX_FRAMESKIP = 5;
    public static byte GLESVersion;
    //float interpolation = 0; not using
    public static boolean firstCount = true;
    //scene
    public static Scene scene = null;
    //Input
    public static MotionEvent event = null;
    static boolean firstCreated = true;
    static boolean IsPreserveEGLContextOnPause = false;
    long next_game_tick = 0;
    int loops = 0;
    FPSCounter FPScounter = new FPSCounter();

    /**
     * EN: Call when is OpenGLES surface pause.
     * CS: Zavoláno, když je OpenGL povrch pozastaven.
     */
    public static void pause() {
        if (Logs.Logs) {
            Log.d(Logs.tagRenderer, "OpenGLES surface is pause.");
        }
        if (!IsPreserveEGLContextOnPause) {
            scene.pause();
        }
    }

    public static void input() {
        scene.input();
    }

    /**
     * EN: Call, when is created OpenGLES surface.
     * CS: Zavoláno, když je vytvořen OpenGL povrch.
     *
     * @param gl     GL
     * @param config config EGL
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if (firstCreated) //Only on start. No on wake up.
        {
            String extensions = gl.glGetString(GL10.GL_EXTENSIONS);
            /* Unofficial support
            if (extensions.contains("GL_IMG_texture_compression_pvrtc"))
            {
                if (Logs.Logs)
                    Log.d(Logs.tagRenderer, "PVRTC texture compression is available.");
                EngineSetHelpers.textureCompressionUse = EngineSetHelpers.textureCompressionUse.PVRTC;
            }
            else
                if (extensions.contains("GL_OES_texture_compression_S3TC") || extensions.contains("GL_EXT_texture_compression_s3tc"))
                {
                    if (Logs.Logs)
                        Log.d(Logs.tagRenderer, "S3TC (DXTn/DXTC) texture compression is available.");
                    EngineSetHelpers.textureCompressionUse = EngineSetHelpers.textureCompressionUse.S3TC;
                }
                else*/
                    /* So far doesn't support
                    if (extensions.contains("GL_AMD_compressed_ATC_texture") || extensions.contains("GL_ATI_texture_compression_atitc"))
                    {
                        if (Logs.Logs)
                        Log.d(Logs.tagRenderer, "ATITC (ATC) texture compression is available.");
                    EngineSetHelpers.textureCompressionUse = EngineSetHelpers.textureCompressionUse.ATITC;
                    }
                    else*/
            if (GLESVersion >= 3 || extensions.contains("OES_compressed_ETC2_RGB8_texture") && extensions.contains("OES_compressed_ETC2_RGBA8_texture") && extensions.contains("OES_compressed_ETC2_punchthroughA_RGBA8_texture")) {
                if (Logs.Logs)
                    Log.d(Logs.tagRenderer, "ETC2 texture compression is available :)");
                EngineSetHelpers.textureCompressionUse = EngineSetHelpers.textureCompression.ETC2;
            } else if (ETC1Util.isETC1Supported()) {
                if (Logs.Logs)
                    Log.d(Logs.tagRenderer, "ETC1 texture compression is available.");
                EngineSetHelpers.textureCompressionUse = EngineSetHelpers.textureCompression.ETC1;
            }

            if (Logs.Logs) {
                Log.d(Logs.tagRenderer, "OpenGLES surface is created, only on start.");
                Log.d(Logs.tagRenderer, "Available OPENGLES extensions are: " + extensions);
            }
            Renderer.scene = new Menu();
            scene.created();
            firstCreated = false;
        } else {
            if (IsPreserveEGLContextOnPause)
                scene.pause();
        }

        //This called on start and on wake up -> ->
        if (Logs.Logs) {
            Log.d(Logs.tagRenderer, "OpenGLES surface is created, on start and wake up.");
        }

        scene.unlock();
        Programs.compile();
    }

    /**
     * EN: Call, when is changed (size) OpenGLES surface.
     * CS: Zavoláno, když je změněn (velikost) OpenGL povrch.
     *
     * @param gl     GL
     * @param width  Width display in pixels.
     * @param height Height display in pixels.
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (Logs.Logs) {
            Log.d(Logs.tagRenderer, "OpenGLES surface is changed.");
        }
        MatrixHelper.width = (short) width;
        MatrixHelper.height = (short) height;
        MatrixHelper.factorWidth = (float) MatrixHelper.width / 20000;
        MatrixHelper.factorHeight = (float) MatrixHelper.height / 20000;
        MatrixHelper.ratio = (float) MatrixHelper.width / MatrixHelper.height;
        MatrixHelper.ratioRound = (short) (MatrixHelper.ratio * 100);
        GLES20.glViewport(0, 0, MatrixHelper.width, MatrixHelper.height);
        scene.changed();
    }

    /**
     * EN: Call, when is draw every frame.
     * CS: Zavoláno, při vykreslení každého snímku.
     *
     * @param gl GL
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        //For actual time
        if (firstCount) {
            next_game_tick = System.nanoTime();
            firstCount = false;
        }

        loops = 0;
        while (System.nanoTime() > next_game_tick && loops < MAX_FRAMESKIP) //nanoseconds
        {
            scene.update();
            next_game_tick += SKIP_TICKS;
            loops++;
        }

        //interpolation = (float)(System.nanoTime() + SKIP_TICKS - next_game_tick) / (float)SKIP_TICKS; //nanoseconds, not using
        scene.draw3D(/*interpolation*/);
        scene.draw2D(/*interpolation*/);
        if (Logs.Logs && Logs.logFPS) {
            FPScounter.logFrame();
        }
    }
}

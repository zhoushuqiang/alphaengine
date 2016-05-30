package com.stety.alphaengine.engine.helpers;

import android.opengl.GLES20;

/**
 * Helper for Alpha - blending.
 */
public class AlphaHelper {

    public static void EnableAlphaIn3D() {
        // Enable blending
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void EnableAlphaIn2D() {
        // Enable depth testing
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        // Enable blending
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void DisableAlphaIn3D() {
        // Disable blending
        GLES20.glDisable(GLES20.GL_BLEND);
    }

    public static void DisableAlphaIn2D() {
        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        // Disable blending
        GLES20.glDisable(GLES20.GL_BLEND);
    }
}

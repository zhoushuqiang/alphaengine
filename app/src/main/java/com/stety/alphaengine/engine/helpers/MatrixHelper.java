package com.stety.alphaengine.engine.helpers;

/**
 * EN: Store for matrix.
 * CS: Úložiště pro matice.
 */
public class MatrixHelper
{
    /**
     * Width screen.
     */
    public static short width = 0;
    /**
     * Height screen.
     */
    public static short height = 0;

    /**
     * For multiply width (width/20000)
     */
    public static float factorWidth = 0;
    /**
     * For multiply height (height/20000)
     */
    public static float factorHeight = 0;

    /**
     * ration screen (width/height)
     */
    public static float ratio = 0;
    /**
     * round to 2 decimal - 1.77777 --> 177
     */
    public static short ratioRound = 0;

    /**
     * Matrix for camera = View matrix.
     */
    public static float[] ViewMatrix = new float[16];
    /**
     * Combined matrix. (Model + View + Projection)
     */
    public static float[] MVPMatrix = new float[16];
    /**
     * Matrix for transform model.
     */
    public static float[] ModelMatrix = new float[16];
    /**
     * Matrix for transfer 3D to 2D. For display.
     */
    public static float[] projectionMatrix = new float[16];
    /**
     * Helper matrix for rotate model.
     */
    public static float[] rotateMatrix = new float[32];
    /**
     * Matrix for skybox, as camera matrix.
     */
    public static float[] viewMatrixSkybox = new float[16];
    /**
     * Helper matrix for calculations model.
     */
    public static float[] tempMatrix = new float[16];

    /**
     * EN: Reset Matrix.
     * CS: Resetuje matice.
     */
    public static void resetMatrix()
    {
        ViewMatrix = new float[16];
        MVPMatrix = new float[16];
        ModelMatrix = new float[16];
        projectionMatrix = new float[16];
        rotateMatrix = new float[32];
        viewMatrixSkybox = new float[16];
        tempMatrix = new float[16];
    }
}

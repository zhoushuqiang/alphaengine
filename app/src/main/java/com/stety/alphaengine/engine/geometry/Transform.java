package com.stety.alphaengine.engine.geometry;

import android.opengl.Matrix;

import com.stety.alphaengine.engine.helpers.MatrixHelper;

/**
 * EN: Class for transform model.
 * CS: Třída pro transformaci modelu.
 */
public class Transform
{
    /**
     * EN: Set identity matrix. (Reset Matrix)
     * CS: Nastaví identitu matice. (Resetuje Matici)
     */
    public static void identity()
    {
        Matrix.setIdentityM(MatrixHelper.ModelMatrix, 0);
    }

    /**
     * EN: Scale model.
     * CS: Měřítko modelu.
     *
     * @param x EN: Ratio for X
     *          CS: Poměr pro X
     * @param y EN: Ratio for Y
     *          CS: Poměr pro Y
     * @param z EN: Ratio for Z
     *          CS: Poměr pro Z
     */
    public static void scale(float x, float y, float z)
    {
        Matrix.scaleM(MatrixHelper.ModelMatrix, 0, x, y, z);
    }

    /**
     * EN: Translate model in place using the axis (x, y, z).
     * CS: Přeloží model v prostoru pomocí os (x, y, z)
     *
     * @param x EN: distance for axis X
     *          CS: vzdálenost pro osu X
     * @param y EN: distance for axis Y
     *          CS: vzdálenost pro osu Y
     * @param z EN: distance for axis Z
     *          CS: vzdálenost pro osu Z
     */
    public static void translate(float x, float y, float z)
    {
        Matrix.translateM(MatrixHelper.ModelMatrix, 0, x, y, z);
    }

    /**
     * EN: Rotates model in place by angle (in degrees), around the axis (x, y, z).
     * CS: Otočí model v prostoru o úhel (ve stupních), okolo osy (x, y, z)
     *
     * @param a            EN: angle to rotate in degrees
     *                     CS: úhel ve stupních pro otočení
     * @param rotationAxis EN: Osa, according to which the object will rotate.
     *                     CS: Osa, podle který se bude objekt rotovat.
     */
    public static void rotate(float a, Axis.axis rotationAxis)
    {
        float x = 0f;
        float y = 0f;
        float z = 0f;

        switch (rotationAxis)
        {
            case x:
                x = 1f;
                y = 0f;
                z = 0f;
                break;
            case y:
                x = 0f;
                y = 1f;
                z = 0f;
                break;
            case z:
                x = 0f;
                y = 0f;
                z = 1f;
                break;
        }
        Matrix.setRotateM(MatrixHelper.rotateMatrix, 0, a, x, y, z);
        Matrix.multiplyMM(MatrixHelper.rotateMatrix, 16, MatrixHelper.ModelMatrix, 0, MatrixHelper.rotateMatrix, 0);
        System.arraycopy(MatrixHelper.rotateMatrix, 16, MatrixHelper.ModelMatrix, 0, 16);
    }
}

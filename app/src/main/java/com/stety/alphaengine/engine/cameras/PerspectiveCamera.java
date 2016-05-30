package com.stety.alphaengine.engine.cameras;

import android.opengl.Matrix;

import com.stety.alphaengine.engine.helpers.MatrixHelper;

/**
 * EN: Perspective projection camera.
 * CS: Perspektivní projekční kamera.
 */
public class PerspectiveCamera
{
    /**
     * Position of camera on X axis.
     */
    public float xPosition;
    /**
     * Position of camera on Y axis.
     */
    public float yPosition;
    /**
     * Position of camera on Z axis.
     */
    public float zPosition;

    /**
     * Rotation around the X axis (up or down).
     */
    public float xRotation;

    /**
     * Rotation around the Y axis (left or right).
     */
    public float yRotation;

    /**
     * Rotation around the Z axis (tilt the left or tilt the right).
     */
    public float zRotation;

    /**
     * EN: Create Orthographic camera.
     * CS: Vytvoření orthografické kamery.
     *
     * @param xPosition Position of camera on X axis.
     * @param yPosition Position of camera on Y axis.
     * @param zPosition Position of camera on Z axis.
     * @param xRotation Rotation around the X axis (up or down).
     * @param yRotation Rotation around the Y axis (left or right).
     * @param zRotation Rotation around the Z axis (tilt the left or tilt the right).
     */
    public PerspectiveCamera(float xPosition, float yPosition, float zPosition, float xRotation,
                             float yRotation, float zRotation)
    {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.zPosition = zPosition;
        this.xRotation = xRotation;
        this.yRotation = yRotation;
        this.zRotation = zRotation;
    }

    /**
     * Defines a projection matrix in terms of a field of view angle, an
     * aspect ratio, and z clip planes.
     *
     * @param m      the float array that holds the perspective matrix
     * @param offset the offset into float array m where the perspective
     *               matrix data is written
     * @param fovy   field of view in y direction, in degrees
     * @param aspect width to height aspect ratio of the viewport
     * @param zNear
     * @param zFar
     */
    private static void perspectiveM(float[] m, int offset, float fovy, float aspect, float zNear,
                                     float zFar)
    {
        float f = 1.0f / (float) Math.tan(fovy * (Math.PI / 360.0));
        float rangeReciprocal = 1.0f / (zNear - zFar);

        m[offset] = f / aspect;
        m[offset + 1] = 0.0f;
        m[offset + 2] = 0.0f;
        m[offset + 3] = 0.0f;

        m[offset + 4] = 0.0f;
        m[offset + 5] = f;
        m[offset + 6] = 0.0f;
        m[offset + 7] = 0.0f;

        m[offset + 8] = 0.0f;
        m[offset + 9] = 0.0f;
        m[offset + 10] = (zFar + zNear) * rangeReciprocal;
        m[offset + 11] = -1.0f;

        m[offset + 12] = 0.0f;
        m[offset + 13] = 0.0f;
        m[offset + 14] = 2.0f * zFar * zNear * rangeReciprocal;
        m[offset + 15] = 0.0f;
    }

    /**
     * EN: Introductory set camera. (Set projection camera.)
     * CS: Úvodní nastavení kamery. (Nastavení projekční matice.)
     *
     * @param fovy  Field of view in y direction, in degrees.
     * @param ratio Width to height aspect ratio of the viewport.
     * @param zNear Near clip plane.
     * @param zFar  Far clip plane.
     */
    public void setProjection(float fovy, float ratio, float zNear, float zFar)
    {
        perspectiveM(MatrixHelper.projectionMatrix, 0, fovy, ratio, zNear, zFar);
    }

    /**
     * EN: Update position and rotation of camera.
     * This must be called before draw any objects.
     * CS: Aktualizuje pozici a rotaci kamery.
     * Tato funkce musí být zavolána před vykreslením jakéhokoliv objektu.
     */
    public void update()
    {
        while (yRotation >= 360)
        {
            yRotation -= 360;
        }
        while (yRotation <= -1)
        {
            yRotation += 360;
        }
        while (xRotation >= 360)
        {
            xRotation -= 360;
        }
        while (xRotation <= -1)
        {
            xRotation += 360;
        }
        while (zRotation >= 360)
        {
            zRotation -= 360;
        }
        while (zRotation <= -1)
        {
            zRotation += 360;
        }
        Matrix.setIdentityM(MatrixHelper.ViewMatrix, 0);
        Matrix.rotateM(MatrixHelper.ViewMatrix, 0, zRotation, 0f, 0f, 1f);
        Matrix.rotateM(MatrixHelper.ViewMatrix, 0, -xRotation, 1f, 0f, 0f);
        Matrix.rotateM(MatrixHelper.ViewMatrix, 0, yRotation, 0f, 1f, 0f);

        System.arraycopy(MatrixHelper.ViewMatrix, 0, MatrixHelper.viewMatrixSkybox, 0, MatrixHelper.ViewMatrix.length);//skybox

        Matrix.translateM(MatrixHelper.ViewMatrix, 0, -xPosition, 0f, 0f);
        Matrix.translateM(MatrixHelper.ViewMatrix, 0, 0f, -yPosition, 0f);
        Matrix.translateM(MatrixHelper.ViewMatrix, 0, 0f, 0f, zPosition);
    }
}

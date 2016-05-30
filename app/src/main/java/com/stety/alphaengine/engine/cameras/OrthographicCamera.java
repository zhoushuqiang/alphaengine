package com.stety.alphaengine.engine.cameras;

import android.opengl.Matrix;

import com.stety.alphaengine.engine.helpers.MatrixHelper;

/**
 * EN: Orthographic projection camera.
 * CS: Orthografická projekční kamera.
 */
public class OrthographicCamera
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
    public OrthographicCamera(float xPosition, float yPosition, float zPosition, float xRotation,
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
     * EN: Introductory set camera. (Set projection camera.)
     * CS: Úvodní nastavení kamery. (Nastavení projekční matice.)
     *
     * @param left   left clip plane.
     * @param right  right clip plane.
     * @param bottom bottom clip plane.
     * @param top    top clip plane.
     * @param near   near clip plane.
     * @param far    far clip plane.
     */
    public void setProjection(float left, float right, float bottom, float top, float near,
                              float far)
    {
        Matrix.orthoM(MatrixHelper.projectionMatrix, 0, left, right, bottom, top, near, far);
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
        Matrix.translateM(MatrixHelper.ViewMatrix, 0, -xPosition, 0f, 0f);
        Matrix.translateM(MatrixHelper.ViewMatrix, 0, 0f, -yPosition, 0f);
        Matrix.translateM(MatrixHelper.ViewMatrix, 0, 0f, 0f, zPosition);
    }
}

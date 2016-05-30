package com.stety.alphaengine.engine.objects;

import android.opengl.GLES20;

import com.stety.alphaengine.engine.constants.Constants;
import com.stety.alphaengine.engine.geometry.Axis;
import com.stety.alphaengine.engine.geometry.Point3D;
import com.stety.alphaengine.engine.geometry.Transform;
import com.stety.alphaengine.engine.helpers.TextReaderHelper;
import com.stety.alphaengine.engine.helpers.VertexDataHelper;

/**
 * EN: Dynamic game object with position, it is object for translate, rotate, etc...
 * As is car, boot, people, etc...
 * CS: Dynamický herní objekt s pozicí, to je objekt pro překlady, rotaci, atd...
 * Jako je auto, boot, člověk, atd...
 */
public abstract class GameObjectP
{
    public final int[] buffers = new int[1];
    public Point3D position = new Point3D(0.0f, 0.0f, 0.0f);
    public float scale = 1.0f;
    public Axis.axis rotationAxis = Axis.axis.x;
    public float rotation = 0.0f;
    public int[] bufferId = new int[1]; //For access.
    public int vertices = 0; //Number of vertices.

    /**
     * EN: Create buffers. Call in create.
     * CS: Vytvoří buffery. Zavolej v create.
     */
    public void initialization()
    {
        GLES20.glGenBuffers(buffers.length, buffers, 0);
        bufferId = buffers;
    }

    /**
     * EN: Load 3GL model. Call in unlock.
     * CS: Načte 3GL model. Zavolej v unlock.
     *
     * @param name name of model
     */
    public void load3GLmodel(String name)
    {
        float[] vertexData = TextReaderHelper.read3GLFileFromAssets("models/" + name);
        vertices = VertexDataHelper.load(vertexData, buffers, Constants.POSITION_3D_DATA_SIZE);
    }

    /**
     * EN: Draw object.
     * CS: Vykreslí objekt.
     */
    public abstract void draw();

    /**
     * EN: Set position, scale and rotation. Call in this draw.
     * CS: Nastaví pozici, měřítko a rotaci. Zavolej v tomto draw.
     */
    public void location()
    {
        Transform.identity();
        Transform.translate(position.x, position.y, position.z);
        Transform.scale(scale, scale, scale);
        Transform.rotate(rotation, rotationAxis);
    }

    /**
     * EN: Delete buffers. Call in cleanUp.
     * CS: Smaže buffery. Zavolej v cleanUp.
     */
    public void delete()
    {
        GLES20.glDeleteBuffers(buffers.length, buffers, 0);
        bufferId = null;
    }
}

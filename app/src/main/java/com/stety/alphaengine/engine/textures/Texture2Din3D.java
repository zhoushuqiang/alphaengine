package com.stety.alphaengine.engine.textures;

import android.opengl.GLES20;

import com.stety.alphaengine.engine.constants.Constants;
import com.stety.alphaengine.engine.geometry.Axis;
import com.stety.alphaengine.engine.geometry.Point3D;
import com.stety.alphaengine.engine.geometry.Transform;
import com.stety.alphaengine.engine.helpers.TextureHelper;
import com.stety.alphaengine.engine.helpers.VertexDataHelper;
import com.stety.alphaengine.game.programs.ptProgram;

/**
 * 2D Texture in 3D (And Alpha, but only PNG, no compress as ETC...!!)
 * For Alpha compress (ETC...) you must use Texture2DAlpha.
 */
public class Texture2Din3D {
    public final int[] buffers = new int[1];
    public Point3D position = new Point3D(0.0f, 0.0f, 0.0f);
    public float scale = 1.0f;
    public Axis.axis rotationAxis = Axis.axis.x;
    public float rotation = 0.0f;
    public int vertices = 0; // Number of vertices.
    public int texture = 0;

    public Texture2Din3D(float scale, float rotation, Axis.axis rotationAxis, Point3D position) {
        this.scale = scale;
        this.rotation = rotation;
        this.rotationAxis = rotationAxis;
        this.position = position;
    }

    /**
     * EN: Create buffers. Call in create.
     * CS: Vytvoří buffery. Zavolej v create.
     */
    public void initialization() {
        GLES20.glGenBuffers(buffers.length, buffers, 0);
    }

    public void load(String name, int minFilter, int magFilter) {
        float[] vertexData = {1, 1, 1, 1, 0, -1, 1, 1, 0, 0, -1, -1, 1, 0, 1, 1, 1, 1, 1, 0, -1, -1, 1, 0, 1, 1, -1, 1, 1, 1,};

        vertices = VertexDataHelper.load(vertexData, buffers, Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE);
        texture = TextureHelper.loadTextureFromAssets(name, minFilter, magFilter);
    }

    /**
     * EN: Draw object.
     * CS: Vykreslí objekt.
     */
    public void draw() {
        location();
        ptProgram.use();
        ptProgram.draw(buffers, 0, vertices, texture);
    }

    /**
     * EN: Set position, scale and rotation. Call in this draw.
     * CS: Nastaví pozici, měřítko a rotaci. Zavolej v tomto draw.
     */
    public void location() {
        Transform.identity();
        Transform.translate(position.x, position.y, position.z);
        Transform.scale(scale, scale, scale);
        Transform.rotate(rotation, rotationAxis);
    }

    /**
     * EN: Delete buffers and texture. Call in cleanUp.
     * CS: Smaže buffery a texturu. Zavolej v cleanUp.
     */
    public void delete() {
        GLES20.glDeleteBuffers(buffers.length, buffers, 0);
        TextureHelper.deleteTexture(texture);
    }
}

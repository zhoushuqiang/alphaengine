package com.stety.alphaengine.engine.objects;

import android.opengl.GLES20;

import com.stety.alphaengine.engine.constants.Constants;
import com.stety.alphaengine.engine.geometry.Axis;
import com.stety.alphaengine.engine.geometry.Point3D;
import com.stety.alphaengine.engine.geometry.Transform;
import com.stety.alphaengine.engine.helpers.TextReaderHelper;
import com.stety.alphaengine.engine.helpers.TextureHelper;
import com.stety.alphaengine.engine.helpers.VertexDataHelper;

/**
 * EN: Dynamic game object with position and texture, is object for translate, rotate, etc...
 * As is car, boot, people, etc...
 * CS: Dynamický herní objekt s pozicí a texturou, je objekt pro překlady, rotaci, atd...
 * Jako je auto, boot, člověk, atd...
 */
public abstract class GameObjectPT {
    public final int[] buffers = new int[1];
    public Point3D position = new Point3D(0.0f, 0.0f, 0.0f);
    public float scale = 1.0f;
    public Axis.axis rotationAxis = Axis.axis.x;
    public float rotation = 0.0f;
    public int[] bufferId = new int[1]; // For access.
    public int vertices = 0; // Number of vertices.
    public int texture;

    /**
     * EN: Create buffers. Call in create.
     * CS: Vytvoří buffery. Zavolej v create.
     */
    public void initialization() {
        GLES20.glGenBuffers(buffers.length, buffers, 0);
        bufferId = buffers;
    }

    /**
     * EN: Load texture. Call in unlock.
     * CS: Načte texturu. Zavolej v unlock
     *
     * @param textureName Name of texture in Assets without .png.
     * @param minFilter   Minification filter use for texture.
     * @param magFilter   Magnification filter use for texture.
     */
    public void loadTexture(String textureName, int minFilter, int magFilter) {
        texture = TextureHelper.loadUncompressTextureFromAssets(textureName, minFilter, magFilter);
    }

    /**
     * EN: Load 3GL model. Call in unlock.
     * CS: Načte 3GL model. Zavolej v unlock.
     *
     * @param name name of model
     */
    public void load3GLmodel(String name) {
        float[] vertexData = TextReaderHelper.read3GLFileFromAssets("models/" + name);
        vertices = VertexDataHelper.load(vertexData, buffers, Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE);
    }

    /**
     * EN: Load 3GL model and load texture. Call in unlock.
     * CS: Načte 3GL model a načte texturu. Zavolej v unlock.
     *
     * @param modelName   Name of model
     * @param textureName Name of texture in Assets without .png.
     * @param minFilter   Minification filter use for texture.
     * @param magFilter   Magnification filter use for texture.
     */
    public void loads(String modelName, String textureName, int minFilter, int magFilter) {
        float[] vertexData = TextReaderHelper.read3GLFileFromAssets(modelName);
        vertices = VertexDataHelper.load(vertexData, buffers, Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE);
        texture = TextureHelper.loadUncompressTextureFromAssets(textureName, minFilter, magFilter);
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
        bufferId = null;
    }
}

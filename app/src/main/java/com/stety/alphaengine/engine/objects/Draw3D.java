package com.stety.alphaengine.engine.objects;

import android.opengl.GLES20;

import com.stety.alphaengine.engine.constants.Constants;
import com.stety.alphaengine.engine.helpers.TextReaderHelper;
import com.stety.alphaengine.engine.helpers.TextureHelper;
import com.stety.alphaengine.engine.helpers.VertexDataHelper;
import com.stety.alphaengine.game.programs.Programs;
import com.stety.alphaengine.game.programs.ptProgram;

import java.util.HashMap;
import java.util.Map;

/**
 * EN: Static game object is not object for translate, rotate, etc...
 * Only for draw. As is wall, house, etc...
 * CS: Statický herní objekt není objekt pro překlady, rotaci, atd...
 * Pouze pro vykreslení. Jako je zeď, dům, atd...
 */

public class Draw3D {
    public static Programs.programs usingProgram;
    final int[] buffers;
    public float rotate = 0.0f;
    int[] vertices; // Number of vertices.
    int drawOrderCount = 0;
    int textureOrderCount = 0;
    int[] textures;
    Map<String, Integer> mapDrawOrder = new HashMap<>();
    Map<String, Integer> mapTextureOrder = new HashMap<>();

    public Draw3D(int objects, int textureObjects) {
        buffers = new int[objects];
        vertices = new int[objects];
        textures = new int[textureObjects];
    }

    /**
     * EN: Create buffers. Call in create.
     * CS: Vytvoří buffery. Zavolej v create.
     */
    public void initialization() {
        GLES20.glGenBuffers(buffers.length, buffers, 0);
    }


    private void add(float[] vertexData, int order, int stride) {
        vertices[order] = VertexDataHelper.load(vertexData, buffers[order], stride);
    }

    /**
     * EN: Load 3GL model. Call in unlock.
     * CS: Načte 3GL model. Zavolej v unlock.
     *
     * @param name     name of model
     * @param drawName name for draw models
     */
    public void load3GLmodelP(String name, String drawName) {
        float[] vertexData = TextReaderHelper.read3GLFileFromAssets("models/" + name);
        mapDrawOrder.put(drawName, drawOrderCount);
        add(vertexData, drawOrderCount, Constants.POSITION_3D_DATA_SIZE);
        drawOrderCount++;
    }

    /**
     * EN: Load 3GL model and load compress texture. Call in unlock.
     * CS: Načte 3GL model a načte kopresovanou texturu. Zavolej v unlock.
     *
     * @param name        name of model
     * @param drawName    name for draw models
     * @param textureName Name of texture in Assets.
     * @param minFilter   Minification filter use for texture.
     * @param magFilter   Magnification filter use for texture.
     */
    public void load3GLmodelPT(String name, String drawName, String textureName, int minFilter,
                               int magFilter) {
        //Vertices
        float[] vertexData = TextReaderHelper.read3GLFileFromAssets("models/" + name);
        mapDrawOrder.put(drawName, drawOrderCount);
        add(vertexData, drawOrderCount, Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE);
        drawOrderCount++;
        //Texture
        mapTextureOrder.put(drawName, textureOrderCount);
        textures[textureOrderCount] = TextureHelper.loadTextureFromAssets(textureName, minFilter, magFilter);
        textureOrderCount++;
    }

    /**
     * Coming soon.
     *
     * @param name     name of model
     * @param drawName name for draw models
     */
    public void load3GLmodelPN(String name, String drawName) {
        float[] vertexData = TextReaderHelper.read3GLFileFromAssets("models/" + name);
        mapDrawOrder.put(drawName, drawOrderCount);
        add(vertexData, drawOrderCount, Constants.POSITION_3D_DATA_SIZE + Constants.NORMAL_3D_DATA_SIZE);
        drawOrderCount++;
    }

    /**
     * Coming soon.
     */
    public void load3GLmodelPTN(String name, String drawName, String textureName, int minFilter,
                                int magFilter) {
        //Vertices
        float[] vertexData = TextReaderHelper.read3GLFileFromAssets("models/" + name);
        mapDrawOrder.put(drawName, drawOrderCount);
        add(vertexData, drawOrderCount, Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE + Constants.NORMAL_3D_DATA_SIZE);
        drawOrderCount++;
        //Texture
        mapTextureOrder.put(drawName, textureOrderCount);
        textures[textureOrderCount] = TextureHelper.loadTextureFromAssets(textureName, minFilter, magFilter);
        textureOrderCount++;
    }

    /**
     * EN: Draw object.
     * CS: Vykreslí objekt.
     *
     * @param drawName Name object, which will draws.
     */
    public void draw(String drawName) {
        int order = mapDrawOrder.get(drawName);
        switch (usingProgram) {
            case ptProgram: {
                int textureOrder = mapTextureOrder.get(drawName);
                ptProgram.draw(buffers, order, vertices[order], textures[textureOrder]);
            }
        }
    }

    /**
     * EN: Delete buffers, textures and everything else.. Call in cleanUp.
     * CS: Smaže buffery, textury a všechno ostatní.. Zavolej v cleanUp.
     */
    public void delete() {
        GLES20.glDeleteBuffers(buffers.length, buffers, 0);
        GLES20.glDeleteTextures(textures.length, textures, 0);
        vertices = null;
        mapDrawOrder = null;
        mapTextureOrder = null;
        usingProgram = null;
        drawOrderCount = 0;
        textureOrderCount = 0;
        rotate = 0;
    }

    /**
     * EN: Here is reset some things for new load (when OnResume), e.g. Draw3D.pause();
     * CS: Zde se resetují některé věci pro nové načtení (při OnResume), např. Draw3D.pause();
     */
    public void pause() {
        mapDrawOrder = new HashMap<>();
        mapTextureOrder = new HashMap<>();
        drawOrderCount = 0;
        textureOrderCount = 0;
    }
}

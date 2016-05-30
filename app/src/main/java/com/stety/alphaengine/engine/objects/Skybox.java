package com.stety.alphaengine.engine.objects;

import android.opengl.GLES20;

import com.stety.alphaengine.engine.helpers.MatrixHelper;
import com.stety.alphaengine.engine.helpers.TextureHelper;
import com.stety.alphaengine.engine.helpers.VertexDataHelper;
import com.stety.alphaengine.game.programs.skyboxProgram;

import java.nio.ByteBuffer;

import static android.opengl.Matrix.setIdentityM;

/**
 * Skybox.
 */
public class Skybox {
    public final int[] buffers = new int[1];
    public final ByteBuffer indexArray = ByteBuffer.allocateDirect(6 * 6).put(new byte[]{
            // Front
            1, 3, 0, 0, 3, 2,

            // Back
            4, 6, 5, 5, 6, 7,

            // Left
            0, 2, 4, 4, 2, 6,

            // Right
            5, 7, 1, 1, 7, 3,

            // Top
            5, 1, 4, 4, 1, 0,

            // Bottom
            6, 2, 7, 7, 2, 3});
    public int texture;
    public int[] bufferId = new int[1]; //For access.

    /**
     * EN: Create buffers. Call in create.
     * CS: Vytvoří buffery. Zavolej v create.
     */
    public void initialization() {
        GLES20.glGenBuffers(buffers.length, buffers, 0);
        bufferId = buffers;
    }

    public void load(String[] textures) {


        float[] vertexData = {-1, 1, 1,     // (0) Top-left near
                1, 1, 1,     // (1) Top-right near
                -1, -1, 1,     // (2) Bottom-left near
                1, -1, 1,     // (3) Bottom-right near
                -1, 1, -1,     // (4) Top-left far
                1, 1, -1,     // (5) Top-right far
                -1, -1, -1,     // (6) Bottom-left far
                1, -1, -1      // (7) Bottom-right far
        };

        VertexDataHelper.load(vertexData, buffers);

        texture = TextureHelper.loadUncompressCubeMap(textures);
    }

    public void draw() {
        setIdentityM(MatrixHelper.ModelMatrix, 0);
        skyboxProgram.use();
        skyboxProgram.draw(bufferId, 0, texture, indexArray);
    }

    /**
     * EN: Delete buffers. Call in cleanUp.
     * CS: Smaže buffery. Zavolej v cleanUp.
     */
    public void delete() {
        GLES20.glDeleteBuffers(buffers.length, buffers, 0);
        bufferId = null;
    }
}

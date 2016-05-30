package com.stety.alphaengine.engine.textures;

import android.opengl.GLES20;

import com.stety.alphaengine.engine.constants.Constants;
import com.stety.alphaengine.engine.geometry.Axis;
import com.stety.alphaengine.engine.geometry.Point3D;
import com.stety.alphaengine.engine.helpers.EngineSetHelpers;
import com.stety.alphaengine.engine.helpers.TextureHelper;
import com.stety.alphaengine.engine.helpers.VertexDataHelper;
import com.stety.alphaengine.game.programs.ptAlphaEtc1Program;
import com.stety.alphaengine.game.programs.ptProgram;

/**
 * Texture2Din3DAlpha (= as Compress) 2D in 3D.
 * Not using with PNG!! Only with ETC1,2...
 * If you want use PNG, you must create  Texture2D, no Texture2DAlpha.
 */
public class Texture2Din3DAlpha extends Texture2Din3D {
    public int textureAlpha = 0;

    public Texture2Din3DAlpha(float scale, float rotation, Axis.axis rotationAxis, Point3D position) {
        super(scale, rotation, rotationAxis, position);
    }

    @Override
    public void load(String name, int minFilter, int magFilter) {
        float[] vertexData = {1, 1, 1, 1, 0, -1, 1, 1, 0, 0, -1, -1, 1, 0, 1, 1, 1, 1, 1, 0, -1, -1, 1, 0, 1, 1, -1, 1, 1, 1,};

        vertices = VertexDataHelper.load(vertexData, buffers, Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE);
        texture = TextureHelper.loadTextureFromAssets(name, minFilter, magFilter);
        switch (EngineSetHelpers.textureCompressionUse) {
            case ETC1:
                String nameAlpha = name + "_alpha";
                textureAlpha = TextureHelper.loadTextureFromAssets(nameAlpha, minFilter, magFilter);
                break;
        }
    }

    /**
     * EN: Draw object.
     * CS: Vykreslí objekt.
     */
    @Override
    public void draw() {
        location();
        switch (EngineSetHelpers.textureCompressionUse) {
            case ETC1:
                ptAlphaEtc1Program.use();
                ptAlphaEtc1Program.draw(buffers, 0, vertices, texture, textureAlpha);
                break;
            default:
                ptProgram.use();
                ptProgram.draw(buffers, 0, vertices, texture);
                break;
        }
    }

    /**
     * EN: Delete buffers and texture. Call in cleanUp.
     * CS: Smaže buffery a texturu. Zavolej v cleanUp.
     */
    public void delete() {
        GLES20.glDeleteBuffers(buffers.length, buffers, 0);
        TextureHelper.deleteTexture(texture);
        switch (EngineSetHelpers.textureCompressionUse) {
            case ETC1:
                TextureHelper.deleteTexture(textureAlpha);
                break;
        }
    }
}

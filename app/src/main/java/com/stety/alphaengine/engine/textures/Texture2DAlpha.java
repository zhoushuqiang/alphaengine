package com.stety.alphaengine.engine.textures;

import android.opengl.GLES20;

import com.stety.alphaengine.engine.constants.Constants;
import com.stety.alphaengine.engine.helpers.EngineSetHelpers;
import com.stety.alphaengine.engine.helpers.TextureHelper;
import com.stety.alphaengine.engine.helpers.VertexDataHelper;
import com.stety.alphaengine.game.programs.texture2DAlphaEtc1Program;
import com.stety.alphaengine.game.programs.texture2DProgram;

/**
 * Texture2dAlphaC (= as Compress)
 * Not using with PNG!! Only with ETC1,2...
 * If you want use PNG, you must create  Texture2D, no Texture2DAlpha.
 */
public class Texture2DAlpha extends Texture2D {
    public int textureAlpha = 0;

    /**
     * EN: Load texture, without cut. Call in unlock.
     * CS: Načte texturu, bez ořezu. Zavolej v unlock.
     *
     * @param minFilter        Minification filter use for texture.
     * @param magFilter        Magnification filter use for texture.
     * @param upperLeftCorner  Texture upper left corner position (and scale) - 4:3, 3:2, 5:3 and 16:9.
     * @param lowerRightCorner Texture lower right corner position (and scale) - 4:3, 3:2, 5:3 and 16:9.
     */
    public void loadNoMR(String textureName, int minFilter, int magFilter, float[] upperLeftCorner,
                         float[] lowerRightCorner) {
        factorize(upperLeftCorner, lowerRightCorner);
        float[] vertexData = {lowerRightCornerFactorized[0], -upperLeftCornerFactorized[1], -1, 1, 0, upperLeftCornerFactorized[0], -upperLeftCornerFactorized[1], -1, 0, 0, upperLeftCornerFactorized[0], -lowerRightCornerFactorized[1], -1, 0, 1, lowerRightCornerFactorized[0], -upperLeftCornerFactorized[1], -1, 1, 0, upperLeftCornerFactorized[0], -lowerRightCornerFactorized[1], -1, 0, 1, lowerRightCornerFactorized[0], -lowerRightCornerFactorized[1], -1, 1, 1};

        vertices = VertexDataHelper.load(vertexData, buffers, Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE);
        texture = TextureHelper.loadTextureFromAssets(textureName, minFilter, magFilter);
        switch (EngineSetHelpers.textureCompressionUse) {
            case ETC1:
                String nameAlpha = textureName + "_alpha";
                textureAlpha = TextureHelper.loadTextureFromAssets(nameAlpha, minFilter, magFilter);
                break;
        }
    }

    /**
     * EN: Load texture. For all aspect ratio. Call in unlock.
     * CS: Načte texturu. Pro všechny poměry stran. Zavolej v unlock.
     *
     * @param minFilter        Minification filter use for texture.
     * @param magFilter        Magnification filter use for texture.
     * @param upperLeftCorner  Texture upper left corner position (and scale) - 4:3, 3:2, 5:3 and 16:9.
     * @param lowerRightCorner Texture lower right corner position (and scale) - 4:3, 3:2, 5:3 and 16:9.
     */
    public void load(String textureName, int minFilter, int magFilter, float[] upperLeftCorner,
                     float[] lowerRightCorner) {
        factorize(upperLeftCorner, lowerRightCorner);
        float[] vertexData = {lowerRightCornerFactorized[0], -upperLeftCornerFactorized[1], -1, 1, 0, upperLeftCornerFactorized[0], -upperLeftCornerFactorized[1], -1, 0, 0, upperLeftCornerFactorized[0], -lowerRightCornerFactorized[1], -1, 0, 1, lowerRightCornerFactorized[0], -upperLeftCornerFactorized[1], -1, 1, 0, upperLeftCornerFactorized[0], -lowerRightCornerFactorized[1], -1, 0, 1, lowerRightCornerFactorized[0], -lowerRightCornerFactorized[1], -1, 1, 1};

        vertices = VertexDataHelper.load(vertexData, buffers, Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE);
        texture = TextureHelper.loadTextureMultiRatio(textureName, minFilter, magFilter);
        switch (EngineSetHelpers.textureCompressionUse) {
            case ETC1:
                String nameAlpha = textureName + "_alpha";
                textureAlpha = TextureHelper.loadTextureMultiRatio(nameAlpha, minFilter, magFilter);
                break;
            default:
                break;
        }
    }

    /**
     * For POT (Power Of Two) (with crop - lower right corner)
     * EN: Load texture. Call in changed. For all aspect ratio.
     * CS: Načte texturu. Zavolej v changed. Zavolej v unlock.
     *
     * @param minFilter          Minification filter use for texture.
     * @param magFilter          Magnification filter use for texture.
     * @param upperLeftCorner    Texture upper left corner position (and scale) - 4:3, 3:2, 5:3 and 16:9.
     * @param lowerRightCorner   Texture lower right corner position (and scale) - 4:3, 3:2, 5:3 and 16:9.
     * @param lowerRightCutCoord Texture coordination for crop - 4:3, 3:2, 5:3 and 16:9.
     */
    public void loadCut(String textureName, int minFilter, int magFilter, float[] upperLeftCorner,
                        float[] lowerRightCorner, float[] lowerRightCutCoord) {
        factorize(upperLeftCorner, lowerRightCorner, lowerRightCutCoord);
        float[] vertexData = {lowerRightCornerFactorized[0], -upperLeftCornerFactorized[1], -1, lowerRightCutCoordTrue[0], 0, upperLeftCornerFactorized[0], -upperLeftCornerFactorized[1], -1, 0, 0, upperLeftCornerFactorized[0], -lowerRightCornerFactorized[1], -1, 0, lowerRightCutCoordTrue[1], lowerRightCornerFactorized[0], -upperLeftCornerFactorized[1], -1, lowerRightCutCoordTrue[0], 0, upperLeftCornerFactorized[0], -lowerRightCornerFactorized[1], -1, 0, lowerRightCutCoordTrue[1], lowerRightCornerFactorized[0], -lowerRightCornerFactorized[1], -1, lowerRightCutCoordTrue[0], lowerRightCutCoordTrue[1]};

        vertices = VertexDataHelper.load(vertexData, buffers, Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE);
        texture = TextureHelper.loadTextureMultiRatio(textureName, minFilter, magFilter);
        switch (EngineSetHelpers.textureCompressionUse) {
            case ETC1:
                String nameAlpha = textureName + "_alpha";
                textureAlpha = TextureHelper.loadTextureMultiRatio(nameAlpha, minFilter, magFilter);
                break;
        }
    }

    /**
     * For POT (Power Of Two) (with crop - upper left and lower right corner)
     * EN: Load texture for button. Call in changed.  For all aspect ratio.
     * CS: Načte texturu pro tlačítko. Zavolej v changed.
     *
     * @param minFilter          Minification filter use for texture.
     * @param magFilter          Magnification filter use for texture.
     * @param upperLeftCorner    Texture upper left corner position (and scale) - 4:3, 3:2, 5:3 and 16:9.
     * @param lowerRightCorner   Texture lower right corner position (and scale) - 4:3, 3:2, 5:3 and 16:9.
     * @param lowerRightCutCoord Texture coordination for crop - 4:3, 3:2, 5:3 and 16:9.
     */
    public void loadCut(String textureName, int minFilter, int magFilter, float[] upperLeftCorner,
                        float[] lowerRightCorner, float[] upperLeftCutCoord,
                        float[] lowerRightCutCoord) {
        factorize(upperLeftCorner, lowerRightCorner, lowerRightCutCoord);

        float[] vertexData = {lowerRightCornerFactorized[0], -upperLeftCornerFactorized[1], -1, lowerRightCutCoordTrue[0], upperLeftCutCoordTrue[1], upperLeftCornerFactorized[0], -upperLeftCornerFactorized[1], -1, upperLeftCutCoordTrue[0], upperLeftCutCoordTrue[1], upperLeftCornerFactorized[0], -lowerRightCornerFactorized[1], -1, upperLeftCutCoordTrue[0], lowerRightCutCoordTrue[1], lowerRightCornerFactorized[0], -upperLeftCornerFactorized[1], -1, lowerRightCutCoordTrue[0], upperLeftCutCoordTrue[1], upperLeftCornerFactorized[0], -lowerRightCornerFactorized[1], -1, upperLeftCutCoordTrue[0], lowerRightCutCoordTrue[1], lowerRightCornerFactorized[0], -lowerRightCornerFactorized[1], -1, lowerRightCutCoordTrue[0], lowerRightCutCoordTrue[1]};

        vertices = VertexDataHelper.load(vertexData, buffers, Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE);
        texture = TextureHelper.loadTextureMultiRatio(textureName, minFilter, magFilter);
        switch (EngineSetHelpers.textureCompressionUse) {
            case ETC1:
                String nameAlpha = textureName + "_alpha";
                textureAlpha = TextureHelper.loadTextureMultiRatio(nameAlpha, minFilter, magFilter);
                break;
        }
    }

    /**
     * EN: Draw object.
     * CS: Vykreslí objekt.
     */
    public void draw() {
        switch (EngineSetHelpers.textureCompressionUse) {
            case ETC1:
                texture2DAlphaEtc1Program.use();
                texture2DAlphaEtc1Program.draw(buffers, 0, vertices, texture, textureAlpha);
                break;
            default:
                texture2DProgram.use();
                texture2DProgram.draw(buffers, 0, vertices, texture);
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

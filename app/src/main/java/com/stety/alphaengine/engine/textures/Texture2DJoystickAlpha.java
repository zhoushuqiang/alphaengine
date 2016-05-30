package com.stety.alphaengine.engine.textures;

import com.stety.alphaengine.engine.constants.Constants;
import com.stety.alphaengine.engine.helpers.EngineSetHelpers;
import com.stety.alphaengine.engine.helpers.MatrixHelper;
import com.stety.alphaengine.engine.helpers.TextureHelper;
import com.stety.alphaengine.engine.helpers.VertexDataHelper;
import com.stety.alphaengine.game.programs.texture2DProgramNoVBO;
import com.stety.alphaengine.game.programs.texture2DProgramNoVBOAlpha;

import java.nio.FloatBuffer;

/**
 * Texture2DJoystickAlpha (= as Compress) for joystick.
 * Not using with PNG!! Only with ETC1,2...
 * If you want use PNG, you must create  Texture2DJoystick, no Texture2DJoystickAlpha.
 */
public class Texture2DJoystickAlpha {
    public FloatBuffer vertexArray;
    public int vertices = 0; // Number of vertices.
    public int texture;
    public int textureAlpha;
    float[] upperLeftCornerFactorized;
    float[] lowerRightCornerFactorized;
    float[] lowerRightCutCoordTrue;
    float[] upperLeftCutCoordTrue;

    /**
     * Set position texture.
     *
     * @param upperLeftCorner  Texture upper left corner position (and scale) - 4:3, 3:2, 5:3 and 16:9.
     * @param lowerRightCorner Texture lower right corner position (and scale) - 4:3, 3:2, 5:3 and 16:9.
     */
    public void setPosition(float[] upperLeftCorner, float[] lowerRightCorner) {
        factorizeSetPosition(upperLeftCorner, lowerRightCorner);

        float[] vertexData = {lowerRightCornerFactorized[0], -upperLeftCornerFactorized[1], -1, 1, 0, upperLeftCornerFactorized[0], -upperLeftCornerFactorized[1], -1, 0, 0, upperLeftCornerFactorized[0], -lowerRightCornerFactorized[1], -1, 0, 1, lowerRightCornerFactorized[0], -upperLeftCornerFactorized[1], -1, 1, 0, upperLeftCornerFactorized[0], -lowerRightCornerFactorized[1], -1, 0, 1, lowerRightCornerFactorized[0], -lowerRightCornerFactorized[1], -1, 1, 1};

        vertices = VertexDataHelper.vertices(vertexData, Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE);
        vertexArray = VertexDataHelper.loadNoVBO(vertexData);
    }

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

        vertices = VertexDataHelper.vertices(vertexData, Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE);
        vertexArray = VertexDataHelper.loadNoVBO(vertexData);
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

        vertices = VertexDataHelper.vertices(vertexData, Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE);
        vertexArray = VertexDataHelper.loadNoVBO(vertexData);
        texture = TextureHelper.loadTextureMultiRatio(textureName, minFilter, magFilter);
        switch (EngineSetHelpers.textureCompressionUse) {
            case ETC1:
                String nameAlpha = textureName + "_alpha";
                textureAlpha = TextureHelper.loadTextureMultiRatio(nameAlpha, minFilter, magFilter);
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

        vertices = VertexDataHelper.vertices(vertexData, Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE);
        vertexArray = VertexDataHelper.loadNoVBO(vertexData);
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
        factorize(upperLeftCorner, lowerRightCorner, upperLeftCutCoord, lowerRightCutCoord);

        float[] vertexData = {lowerRightCornerFactorized[0], -upperLeftCornerFactorized[1], -1, lowerRightCutCoordTrue[0], upperLeftCutCoordTrue[1], upperLeftCornerFactorized[0], -upperLeftCornerFactorized[1], -1, upperLeftCutCoordTrue[0], upperLeftCutCoordTrue[1], upperLeftCornerFactorized[0], -lowerRightCornerFactorized[1], -1, upperLeftCutCoordTrue[0], lowerRightCutCoordTrue[1], lowerRightCornerFactorized[0], -upperLeftCornerFactorized[1], -1, lowerRightCutCoordTrue[0], upperLeftCutCoordTrue[1], upperLeftCornerFactorized[0], -lowerRightCornerFactorized[1], -1, upperLeftCutCoordTrue[0], lowerRightCutCoordTrue[1], lowerRightCornerFactorized[0], -lowerRightCornerFactorized[1], -1, lowerRightCutCoordTrue[0], lowerRightCutCoordTrue[1]};

        vertices = VertexDataHelper.vertices(vertexData, Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE);
        vertexArray = VertexDataHelper.loadNoVBO(vertexData);
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
                texture2DProgramNoVBOAlpha.use();
                texture2DProgramNoVBOAlpha.draw(vertexArray, vertices, texture, textureAlpha);
                break;
            default:
                texture2DProgramNoVBO.use();
                texture2DProgramNoVBO.draw(vertexArray, vertices, texture);
                break;
        }
    }

    /**
     * EN: Delete buffers and texture. Call in cleanUp.
     * CS: Smaže buffery a texturu. Zavolej v cleanUp.
     */
    public void delete() {
        TextureHelper.deleteTexture(texture);
        switch (EngineSetHelpers.textureCompressionUse) {
            case ETC1:
                TextureHelper.deleteTexture(textureAlpha);
                break;
        }
    }

    void factorize(float[] upperLeftCorner, float[] lowerRightCorner) {
        switch (MatrixHelper.ratioRound) {
            case 177:
            default: //16:9
                upperLeftCornerFactorized = new float[]{(upperLeftCorner[6] - 10000) / 10000, (upperLeftCorner[7] - 10000) / 10000};
                lowerRightCornerFactorized = new float[]{(lowerRightCorner[6] - 10000) / 10000, (lowerRightCorner[7] - 10000) / 10000};
                break;
            case 150: //3:2
                upperLeftCornerFactorized = new float[]{(upperLeftCorner[2] - 10000) / 10000, (upperLeftCorner[3] - 10000) / 10000};
                lowerRightCornerFactorized = new float[]{(lowerRightCorner[2] - 10000) / 10000, (lowerRightCorner[3] - 10000) / 10000};
                break;
            case 133: //4:3
                upperLeftCornerFactorized = new float[]{(upperLeftCorner[0] - 10000) / 10000, (upperLeftCorner[1] - 10000) / 10000};
                lowerRightCornerFactorized = new float[]{(lowerRightCorner[0] - 10000) / 10000, (lowerRightCorner[1] - 10000) / 10000};
                break;
            case 166: //5:3
                upperLeftCornerFactorized = new float[]{(upperLeftCorner[4] - 10000) / 10000, (upperLeftCorner[5] - 10000) / 10000};
                lowerRightCornerFactorized = new float[]{(lowerRightCorner[4] - 10000) / 10000, (lowerRightCorner[5] - 10000) / 10000};
                break;
        }
    }

    void factorize(float[] upperLeftCorner, float[] lowerRightCorner, float[] lowerRightCutCoord) {
        switch (MatrixHelper.ratioRound) {
            case 177:
            default: //16:9
                upperLeftCornerFactorized = new float[]{(upperLeftCorner[6] - 10000) / 10000, (upperLeftCorner[7] - 10000) / 10000};
                lowerRightCornerFactorized = new float[]{(lowerRightCorner[6] - 10000) / 10000, (lowerRightCorner[7] - 10000) / 10000};
                lowerRightCutCoordTrue = new float[]{lowerRightCutCoord[6], lowerRightCutCoord[7]};
                break;
            case 150: //3:2
                upperLeftCornerFactorized = new float[]{(upperLeftCorner[2] - 10000) / 10000, (upperLeftCorner[3] - 10000) / 10000};
                lowerRightCornerFactorized = new float[]{(lowerRightCorner[2] - 10000) / 10000, (lowerRightCorner[3] - 10000) / 10000};
                lowerRightCutCoordTrue = new float[]{lowerRightCutCoord[2], lowerRightCutCoord[3]};
                break;
            case 133: //4:3
                upperLeftCornerFactorized = new float[]{(upperLeftCorner[0] - 10000) / 10000, (upperLeftCorner[1] - 10000) / 10000};
                lowerRightCornerFactorized = new float[]{(lowerRightCorner[0] - 10000) / 10000, (lowerRightCorner[1] - 10000) / 10000};
                lowerRightCutCoordTrue = new float[]{lowerRightCutCoord[0], lowerRightCutCoord[1]};
                break;
            case 166: //5:3
                upperLeftCornerFactorized = new float[]{(upperLeftCorner[4] - 10000) / 10000, (upperLeftCorner[5] - 10000) / 10000};
                lowerRightCornerFactorized = new float[]{(lowerRightCorner[4] - 10000) / 10000, (lowerRightCorner[5] - 10000) / 10000};
                lowerRightCutCoordTrue = new float[]{lowerRightCutCoord[4], lowerRightCutCoord[5]};
                break;
        }
    }

    void factorize(float[] upperLeftCorner, float[] lowerRightCorner, float[] upperLeftCutCoord, float[] lowerRightCutCoord) {
        switch (MatrixHelper.ratioRound) {
            case 177:
            default: //16:9
                upperLeftCornerFactorized = new float[]{(upperLeftCorner[6] - 10000) / 10000, (upperLeftCorner[7] - 10000) / 10000};
                lowerRightCornerFactorized = new float[]{(lowerRightCorner[6] - 10000) / 10000, (lowerRightCorner[7] - 10000) / 10000};
                lowerRightCutCoordTrue = new float[]{lowerRightCutCoord[6], lowerRightCutCoord[7]};
                upperLeftCutCoordTrue = new float[]{upperLeftCutCoord[6], upperLeftCutCoord[7]};
                break;
            case 150: //3:2
                upperLeftCornerFactorized = new float[]{(upperLeftCorner[2] - 10000) / 10000, (upperLeftCorner[3] - 10000) / 10000};
                lowerRightCornerFactorized = new float[]{(lowerRightCorner[2] - 10000) / 10000, (lowerRightCorner[3] - 10000) / 10000};
                lowerRightCutCoordTrue = new float[]{lowerRightCutCoord[2], lowerRightCutCoord[3]};
                upperLeftCutCoordTrue = new float[]{upperLeftCutCoord[2], upperLeftCutCoord[3]};
                break;
            case 133: //4:3
                upperLeftCornerFactorized = new float[]{(upperLeftCorner[0] - 10000) / 10000, (upperLeftCorner[1] - 10000) / 10000};
                lowerRightCornerFactorized = new float[]{(lowerRightCorner[0] - 10000) / 10000, (lowerRightCorner[1] - 10000) / 10000};
                lowerRightCutCoordTrue = new float[]{lowerRightCutCoord[0], lowerRightCutCoord[1]};
                upperLeftCutCoordTrue = new float[]{upperLeftCutCoord[0], upperLeftCutCoord[1]};
                break;
            case 166: //5:3
                upperLeftCornerFactorized = new float[]{(upperLeftCorner[4] - 10000) / 10000, (upperLeftCorner[5] - 10000) / 10000};
                lowerRightCornerFactorized = new float[]{(lowerRightCorner[4] - 10000) / 10000, (lowerRightCorner[5] - 10000) / 10000};
                lowerRightCutCoordTrue = new float[]{lowerRightCutCoord[4], lowerRightCutCoord[5]};
                upperLeftCutCoordTrue = new float[]{upperLeftCutCoord[4], upperLeftCutCoord[5]};
                break;
        }
    }

    void factorizeSetPosition(float[] upperLeftCorner, float[] lowerRightCorner) {
        upperLeftCornerFactorized = new float[]{(upperLeftCorner[0] - 10000) / 10000, (upperLeftCorner[1] - 10000) / 10000};
        lowerRightCornerFactorized = new float[]{(lowerRightCorner[0] - 10000) / 10000, (lowerRightCorner[1] - 10000) / 10000};
    }
}

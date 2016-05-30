package com.stety.alphaengine.engine.textures;

import android.opengl.GLES20;

import com.stety.alphaengine.engine.constants.Constants;
import com.stety.alphaengine.engine.helpers.MatrixHelper;
import com.stety.alphaengine.engine.helpers.TextureHelper;
import com.stety.alphaengine.engine.helpers.VertexDataHelper;
import com.stety.alphaengine.game.programs.texture2DProgram;

/**
 * 2D Texture (And Alpha, but only PNG, no compress as ETC...!!)
 * For Alpha compress (ETC...) you must use Texture2DAlpha.
 */
public class Texture2D {
    public final int[] buffers = new int[1];
    public int vertices = 0; // Number of vertices.
    public int texture;

    float[] upperLeftCornerFactorized;
    float[] lowerRightCornerFactorized;
    float[] lowerRightCutCoordTrue;
    float[] upperLeftCutCoordTrue;

    /**
     * EN: Create buffers. Call in create.
     * CS: Vytvoří buffery. Zavolej v create.
     */
    public void initialization() {
        GLES20.glGenBuffers(buffers.length, buffers, 0);
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

        vertices = VertexDataHelper.load(vertexData, buffers, Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE);
        texture = TextureHelper.loadTextureFromAssets(textureName, minFilter, magFilter);
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

        vertices = VertexDataHelper.load(vertexData, buffers, Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE);
        texture = TextureHelper.loadTextureMultiRatio(textureName, minFilter, magFilter);
    }

    /**
     * EN: Draw object.
     * CS: Vykreslí objekt.
     */
    public void draw() {
        texture2DProgram.use();
        texture2DProgram.draw(buffers, 0, vertices, texture);
    }

    /**
     * EN: Delete buffers and texture. Call in cleanUp.
     * CS: Smaže buffery a texturu. Zavolej v cleanUp.
     */
    public void delete() {
        GLES20.glDeleteBuffers(buffers.length, buffers, 0);
        TextureHelper.deleteTexture(texture);
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
}

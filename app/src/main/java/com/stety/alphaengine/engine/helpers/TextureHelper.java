package com.stety.alphaengine.engine.helpers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.stety.alphaengine.engine.android.AlphaEngine;
import com.stety.alphaengine.engine.android.LibJNIWrapper;
import com.stety.alphaengine.engine.logger.Logs;

import java.io.IOException;
import java.io.InputStream;

import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glTexParameteri;

/**
 * Coming soon new function of compression textures.
 * EN: Helper for textures
 * CS: Pomocník pro textury.
 */
public class TextureHelper {
    /**
     * EN: Load the texture from assets.
     * CS: Načte texturu z assets.
     *
     * @param minFilter Minification filter use for texture.
     * @param magFilter Magnification filter use for texture.
     * @param name      Name of texture.
     * @return OpenGL texture.
     */
    public static int loadUncompressTextureFromAssets(String name, int minFilter, int magFilter) {
        final int[] textureHandle = new int[1];
        String nameFinal = "textures/" + name + ".png";

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0) {
            InputStream is;
            try {
                is = AlphaEngine.context.getAssets().open(nameFinal);
            } catch (IOException e) {
                throw new RuntimeException("Could not open asset: " + nameFinal, e);
            } catch (Resources.NotFoundException nfe) {
                throw new RuntimeException("Asset not found: " + nameFinal, nfe);
            }

            final Bitmap bitmap = BitmapFactory.decodeStream(is);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, magFilter);

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            if (minFilter == GLES20.GL_NEAREST_MIPMAP_NEAREST || minFilter == GLES20.GL_NEAREST_MIPMAP_LINEAR || minFilter == GLES20.GL_LINEAR_MIPMAP_NEAREST || minFilter == GLES20.GL_LINEAR_MIPMAP_LINEAR) {
                GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
            }

            bitmap.recycle();
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        } else {
            Log.w(Logs.tagTextureHelper, "Can not load texture.");
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }

    /**
     * EN: Load the compress texture from assets.
     * CS: Načte kompresovanou texturu z assets.
     *
     * @param minFilter Minification filter use for texture.
     * @param magFilter Magnification filter use for texture.
     * @param name      Name of texture.
     * @return OpenGL texture.
     */
    public static int loadTextureFromAssets(String name, int minFilter, int magFilter) {
        int handle;
        boolean isMipmapped = false;
        if (minFilter == GLES20.GL_NEAREST_MIPMAP_NEAREST || minFilter == GLES20.GL_NEAREST_MIPMAP_LINEAR || minFilter == GLES20.GL_LINEAR_MIPMAP_NEAREST || minFilter == GLES20.GL_LINEAR_MIPMAP_LINEAR)
            isMipmapped = true;
        switch (EngineSetHelpers.textureCompressionUse) {
            /* Unofficial support
            case PVRTC:
                handle = loadTexturePVRTC(name, minFilter, magFilter);
                break;
            case S3TC:
                handle = loadTextureS3TC(name, minFilter, magFilter);
                break;*/
            case ETC1:
            default:
                handle = loadTextureETC1_KTX(name, isMipmapped, minFilter, magFilter);
                break;
            case ETC2:
                handle = loadTextureETC2_KTX(name, isMipmapped, minFilter, magFilter);
                break;
        }

        return handle;
    }

    public static int loadTextureMultiRatio(String name, int minFilter,
                                            int magFilter) {
        String nameFinal;
        switch (MatrixHelper.ratioRound) {
            case 177:
            default: //16:9
                nameFinal = name + "16x9";
                break;
            case 150: //3:2
                nameFinal = name + "3x2";
                break;
            case 133: //4:3
                nameFinal = name + "4x3";
                break;
            case 166: //5:3
                nameFinal = name + "5x3";
                break;
        }

        int handle;
        boolean isMipmapped = false;
        if (minFilter == GLES20.GL_NEAREST_MIPMAP_NEAREST || minFilter == GLES20.GL_NEAREST_MIPMAP_LINEAR || minFilter == GLES20.GL_LINEAR_MIPMAP_NEAREST || minFilter == GLES20.GL_LINEAR_MIPMAP_LINEAR)
            isMipmapped = true;
        switch (EngineSetHelpers.textureCompressionUse) {
             /* Unofficial support
            case PVRTC:
                handle = loadTexturePVRTC(name, minFilter, magFilter);
                break;
            case S3TC:
                handle = loadTextureS3TC(name, minFilter, magFilter);
                break;*/
            case ETC1:
            default:
                handle = loadTextureETC1_KTX(nameFinal, isMipmapped, minFilter, magFilter);
                break;
            case ETC2:
                handle = loadTextureETC2_KTX(nameFinal, isMipmapped, minFilter, magFilter);
                break;
        }

        return handle;
    }

    /**
     * EN: Load the texture from assets, but choose true aspect ratio texture for screen.
     * CS: Načte texturu z assets, ale vybere správný poměr velikosti textury pro obrazovku.
     *
     * @param minFilter Minification filter use for texture.
     * @param magFilter Magnification filter use for texture.
     * @param name      Name of texture.
     * @return OpenGL texture.
     */
    public static int loadUncompressTextureMultiRatio(String name, int minFilter, int magFilter) {
        final int[] textureHandle = new int[1];
        String nameFinal;

        GLES20.glGenTextures(1, textureHandle, 0);

        switch (MatrixHelper.ratioRound) {
            case 177:
            default: //16:9
                nameFinal = "textures/" + name + "16x9.png";
                break;
            case 150: //3:2
                nameFinal = "textures/" + name + "3x2.png";
                break;
            case 133: //4:3
                nameFinal = "textures/" + name + "4x3.png";
                break;
            case 166: //5:3
                nameFinal = "textures/" + name + "5x3.png";
                break;
        }

        if (textureHandle[0] != 0) {
            InputStream is;
            try {
                is = AlphaEngine.context.getAssets().open(nameFinal);
            } catch (IOException e) {
                throw new RuntimeException("Could not open asset: " + nameFinal, e);
            } catch (Resources.NotFoundException nfe) {
                throw new RuntimeException("Asset not found: " + nameFinal, nfe);
            }

            final Bitmap bitmap = BitmapFactory.decodeStream(is);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, magFilter);

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            if (minFilter == GLES20.GL_NEAREST_MIPMAP_NEAREST || minFilter == GLES20.GL_NEAREST_MIPMAP_LINEAR || minFilter == GLES20.GL_LINEAR_MIPMAP_NEAREST || minFilter == GLES20.GL_LINEAR_MIPMAP_LINEAR) {
                GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
            }

            bitmap.recycle();
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        } else {
            Log.w(Logs.tagTextureHelper, "Can not load texture.");
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }

    /**
     * EN: Load the 6 textures from assets to cubemap, etc for skybox.
     * CS: Načte 6 textur z assets do cubemap, např. pro skybox.
     *
     * @param names Field of names textures.
     * @return OpenGL texture.
     */
    public static int loadUncompressCubeMap(String[] names) {
        final int[] textureHandle = new int[1];
        String[] namesFinal = {"textures/" + names[0] + ".png", "textures/" + names[1] + ".png", "textures/" + names[2] + ".png", "textures/" + names[3] + ".png", "textures/" + names[4] + ".png", "textures/" + names[5] + ".png"};

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0) {
            final Bitmap[] cubeBitmaps = new Bitmap[6];

            for (int i = 0; i < 6; i++) {
                InputStream is;
                try {
                    is = AlphaEngine.context.getAssets().open(namesFinal[i]);
                } catch (IOException e) {
                    throw new RuntimeException("Could not open asset: " + namesFinal[i], e);
                } catch (Resources.NotFoundException nfe) {
                    throw new RuntimeException("Asset not found: " + namesFinal[i], nfe);
                }

                cubeBitmaps[i] = BitmapFactory.decodeStream(is);
            }

            glBindTexture(GL_TEXTURE_CUBE_MAP, textureHandle[0]);

            // Linear filtering for minification and magnification
            glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            //Hide seams
            glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

            GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, cubeBitmaps[0], 0);
            GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, cubeBitmaps[1], 0);

            GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, cubeBitmaps[2], 0);
            GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, cubeBitmaps[3], 0);

            GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, cubeBitmaps[4], 0);
            GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, cubeBitmaps[5], 0);

            glBindTexture(GL_TEXTURE_2D, 0);

            for (Bitmap bitmap : cubeBitmaps) {
                bitmap.recycle();
            }
        } else {
            Log.w(Logs.tagTextureHelper, "Can not load texture.");
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }

    /**
     * EN: Delete texture.
     * CS: Smaže texturu.
     *
     * @param texture texture
     */
    public static void deleteTexture(int texture) {
        final int[] textureHandle = new int[1];
        textureHandle[0] = texture;
        GLES20.glDeleteTextures(1, textureHandle, 0);
    }

    private static int loadTextureETC1_KTX(String name, boolean isMipmapped, int minFilter,
                                           int magFilter) {
        return LibJNIWrapper.loadTextureETC_KTX("textures/" + name + ".etc1.ktx", isMipmapped, minFilter, magFilter);
    }

    private static int loadTextureETC2_KTX(String name, boolean isMipmapped, int minFilter,
                                           int magFilter) {
        return LibJNIWrapper.loadTextureETC_KTX("textures/" + name + ".etc2.ktx", isMipmapped, minFilter, magFilter);
    }

    //Unofficial support
    private static int loadTexturePVRTC(String name, int minFilter, int magFilter) {
        return LibJNIWrapper.loadTexturePVRTC("textures/" + name + ".pvr", minFilter, magFilter);
    }

    //Unofficial support
    private static int loadTextureS3TC(String name, int minFilter, int magFilter) {
        return LibJNIWrapper.loadTextureS3TC("textures/" + name + ".dds", minFilter, magFilter);
    }
}
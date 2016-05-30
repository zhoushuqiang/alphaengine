package com.stety.alphaengine.engine.android;

import android.content.res.AssetManager;

/**
 * Test.
 */
public class LibJNIWrapper {
    static {
        System.loadLibrary("engine");
    }

    public static native int loadTextureETC_KTX(String name, boolean isMipmapped, int minFilter,
                                                int magFilter);

    public static native int loadTexturePVRTC(String name, int minFilter, int magFilter);

    public static native int loadTextureS3TC(String name, int minFilter, int magFilter);

    public static native void createAssetManager(AssetManager assetManager);
}

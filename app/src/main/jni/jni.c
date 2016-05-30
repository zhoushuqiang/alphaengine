#include <jni.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

/*#include "file.h" podle me nenni potreba*/
#include "TextureLoader_ETC_KTX.h"
#include "TextureLoader_PVRTC.h"
#include "TextureLoader_S3TC.h"

JNIEXPORT jint JNICALL Java_com_stety_alphaengine_engine_android_LibJNIWrapper_loadTextureETC_1KTX (JNIEnv *env, jclass cls, jstring name, jboolean isMipmapped, jint minFilter, jint magFilter)
{
    const char *NameNativeString = (*env)->GetStringUTFChars(env, name, 0);
    GLuint handle = LoadTextureETC_KTX(NameNativeString, isMipmapped, minFilter, magFilter);
    (*env)->ReleaseStringUTFChars(env, name, NameNativeString);
    return handle;
}

JNIEXPORT jint JNICALL Java_com_stety_alphaengine_engine_android_LibJNIWrapper_loadTexturePVRTC (JNIEnv *env, jclass cls, jstring name, jint minFilter, jint magFilter)
{
    const char *NameNativeString = (*env)->GetStringUTFChars(env, name, 0);
    GLuint handle = LoadTexturePVRTC(NameNativeString, minFilter, magFilter);
    (*env)->ReleaseStringUTFChars(env, name, NameNativeString);
    return handle;
}

JNIEXPORT jint JNICALL Java_com_stety_alphaengine_engine_android_LibJNIWrapper_loadTextureS3TC (JNIEnv *env, jclass cls, jstring name, jint minFilter, jint magFilter)
{
    const char *NameNativeString = (*env)->GetStringUTFChars(env, name, 0);
    GLuint handle = LoadTextureS3TC(NameNativeString, minFilter, magFilter);
    (*env)->ReleaseStringUTFChars(env, name, NameNativeString);
    return handle;
}

JNIEXPORT void JNICALL Java_com_stety_alphaengine_engine_android_LibJNIWrapper_createAssetManager (JNIEnv *env, jclass cls, jobject assetManager)
{
    AAssetManager* mgr = AAssetManager_fromJava(env, assetManager);
    SetAssetManager(mgr);
}

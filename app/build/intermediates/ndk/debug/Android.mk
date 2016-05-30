LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := engine
LOCAL_CFLAGS := -DKTX_OPENGL_ES2=1 -DSUPPORT_SOFTWARE_ETC_UNPACK=0
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_LDLIBS := \
	-lGLESv2 -llog -landroid \

LOCAL_SRC_FILES := \
	/home/stety/Development/workspace/android-studio/alphaengine/app/src/main/jni/jni.c \
	/home/stety/Development/workspace/android-studio/alphaengine/app/src/main/jni/TextureLoader_S3TC.c \
	/home/stety/Development/workspace/android-studio/alphaengine/app/src/main/jni/TextureLoader_ETC_KTX.c \
	/home/stety/Development/workspace/android-studio/alphaengine/app/src/main/jni/file.c \
	/home/stety/Development/workspace/android-studio/alphaengine/app/src/main/jni/TextureLoader_PVRTC.c \
	/home/stety/Development/workspace/android-studio/alphaengine/app/src/main/jni/libktx/hashtable.c \
	/home/stety/Development/workspace/android-studio/alphaengine/app/src/main/jni/libktx/writer.c \
	/home/stety/Development/workspace/android-studio/alphaengine/app/src/main/jni/libktx/LICENSE \
	/home/stety/Development/workspace/android-studio/alphaengine/app/src/main/jni/libktx/loader.c \
	/home/stety/Development/workspace/android-studio/alphaengine/app/src/main/jni/libktx/README \
	/home/stety/Development/workspace/android-studio/alphaengine/app/src/main/jni/libktx/swap.c \
	/home/stety/Development/workspace/android-studio/alphaengine/app/src/main/jni/libktx/checkheader.c \
	/home/stety/Development/workspace/android-studio/alphaengine/app/src/main/jni/libktx/etcunpack.c \
	/home/stety/Development/workspace/android-studio/alphaengine/app/src/main/jni/libktx/loader.patch \

LOCAL_C_INCLUDES += /home/stety/Development/workspace/android-studio/alphaengine/app/src/main/jni
LOCAL_C_INCLUDES += /home/stety/Development/workspace/android-studio/alphaengine/app/src/debug/jni

include $(BUILD_SHARED_LIBRARY)

#pragma once

#include <sys/types.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>

void SetAssetManager( AAssetManager* pManager );

// Read the contents of the give file return the content and the file size
void ReadFile( const char* FileName, char** Content, unsigned int* Size );
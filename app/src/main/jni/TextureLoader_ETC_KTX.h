#pragma once

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include <stdbool.h>

GLuint LoadTextureETC_KTX(const char* TextureFileName, bool isMipmapped, int minFilter, int magFilter);


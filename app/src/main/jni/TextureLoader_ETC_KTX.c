#include "TextureLoader_ETC_KTX.h"
#include <assert.h>
#include <memory.h>
#include <stdio.h>
#include <stdlib.h>
#include <android/log.h>
#include "libktx/ktx.h"
#include "file.h"
#include <stdbool.h>

#define  Log(...)  __android_log_print( ANDROID_LOG_INFO, "TextureLoader", __VA_ARGS__ )
#define  LogError(...)  __android_log_print( ANDROID_LOG_ERROR, "TextureLoader", __VA_ARGS__ )

GLuint LoadTextureETC_KTX(const char* TextureFileName, bool isMipmapped, int minFilter, int magFilter)
{
    char* pData = NULL;
    unsigned int fileSize = 0;
    
    ReadFile(TextureFileName, &pData, &fileSize);

    GLuint handle = 0;
    GLenum target;
    GLboolean mipmapped = isMipmapped;
        
    KTX_error_code result = ktxLoadTextureM(pData, fileSize, &handle, &target, NULL, &mipmapped, NULL, NULL, NULL);
        
    if(result != KTX_SUCCESS)
    {
        LogError("ERROR: libktx couldn't load texture %s. Error: %d", TextureFileName, result);
        return 0;
    }

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter);

    //clean up
    free(pData);

    //unbind the texture
    glBindTexture( target, 0 );

    return handle;  
}
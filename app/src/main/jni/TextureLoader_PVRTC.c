#include "TextureLoader_PVRTC.h"
#include "file.h"
#include <assert.h>
#include <android/log.h>
#include <memory.h>

#define  Log(...)  __android_log_print( ANDROID_LOG_INFO, "TextureLoader", __VA_ARGS__ )
#define  LogError(...)  __android_log_print( ANDROID_LOG_ERROR, "TextureLoader", __VA_ARGS__ )

static void CheckGlError( const char* pFunctionName )
{
    GLint error = glGetError();
    if( error != GL_NO_ERROR )
    {
        LogError( "%s returned glError 0x%x\n", pFunctionName, error );
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Loads a PVRTC texture and returns a handle (mipmap support included)
//
// Extension defined here: http://www.khronos.org/registry/gles/extensions/IMG/IMG_texture_compression_pvrtc.txt
// File format defined here: http://www.imgtec.net/powervr/insider/docs/PVR%20File%20Format.Specification.1.0.11.External.pdf
typedef struct
{
    unsigned int       mVersion;
    unsigned int       mFlags;
    unsigned long long mPixelFormat;
    unsigned int       mColourSpace;
    unsigned int       mChannelType;
    unsigned int       mHeight;
    unsigned int       mWidth;
    unsigned int       mDepth;
    unsigned int       mNumSurfaces;
    unsigned int       mNumFaces;
    unsigned int       mMipmapCount;
    unsigned int       mMetaDataSize;
} __attribute__((packed)) PVRHeaderV3;  // Header must be packed because of the 64-bit mPixelFormat (ARM will pad 4 extra bytes to make it aligned)

GLuint LoadTexturePVRTC(const char* TextureFileName, int minFilter, int magFilter)
{
    // Load the texture file
    char* pData = NULL;
    unsigned int fileSize = 0;

    ReadFile( TextureFileName, &pData, &fileSize );

    // Grab the header
    PVRHeaderV3* pHeader = (PVRHeaderV3*)pData;

    // Generate handle
    GLuint handle;
    glGenTextures( 1, &handle );

    // Bind the texture
    glBindTexture( GL_TEXTURE_2D, handle );

    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter );
    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter );

    // Determine the format
    GLenum format;
    GLuint bitsPerPixel;

    switch( pHeader->mPixelFormat )
    {
        case 0:
        {
            // PVRTC 2bpp RGB
            format = GL_COMPRESSED_RGB_PVRTC_2BPPV1_IMG;
            bitsPerPixel = 2;
            break;
        }
        case 1:
        {
            // PVRTC 2bpp RGBA
            format = GL_COMPRESSED_RGBA_PVRTC_2BPPV1_IMG;
            bitsPerPixel = 2;
            break;
        }
        case 2:
        {
            // PVRTC 4bpp RGB
            format = GL_COMPRESSED_RGB_PVRTC_4BPPV1_IMG;
            bitsPerPixel = 4;
            break;
        }
        case 3:
        {
            // PVRTC 4bpp RGBA
            format = GL_COMPRESSED_RGBA_PVRTC_4BPPV1_IMG;
            bitsPerPixel = 4;
            break;
        }
        default:
        {
            // Unknown format
            assert(0);
            return 0;
        }
    }

    // Initialize the texture
    unsigned int offset = sizeof(PVRHeaderV3) + pHeader->mMetaDataSize;
    unsigned int mipWidth = pHeader->mWidth;
    unsigned int mipHeight = pHeader->mHeight;

    unsigned int mip = 0;
    do
    {
        // Determine size (width * height * bbp/8)
        // pixelDataSize must be at least two blocks (4x4 pixels for 4bpp, 8x4 pixels for 2bpp), so min size is 32
        unsigned int pixelDataSize = ( mipWidth * mipHeight * bitsPerPixel ) >> 3;
        pixelDataSize = (pixelDataSize < 32) ? 32 : pixelDataSize;

        // Upload texture data for this mip
        glCompressedTexImage2D(GL_TEXTURE_2D, mip, format, mipWidth, mipHeight, 0, pixelDataSize, pData + offset);

        // Next mips is half the size (divide by 2) with a min of 1
        mipWidth = mipWidth >> 1;
        mipWidth = ( mipWidth == 0 ) ? 1 : mipWidth;

        mipHeight = mipHeight >> 1;
        mipHeight = ( mipHeight == 0 ) ? 1 : mipHeight;

        // Move to next mip
        offset += pixelDataSize;
        mip++;
    } while(mip < pHeader->mMipmapCount);

    // clean up
    free( pData );

    glBindTexture( handle, 0 );

    // Return handle
    return handle;
}
#include "TextureLoader_S3TC.h"
#include "file.h"
#include <assert.h>
#include <android/log.h>
#include <memory.h>

#define  Log(...)  __android_log_print( ANDROID_LOG_INFO, "TextureLoader", __VA_ARGS__ )
#define  LogError(...)  __android_log_print( ANDROID_LOG_ERROR, "TextureLoader", __VA_ARGS__ )

static void CheckGlError( const char* pFunctionName)
{
    GLint error = glGetError();
    if( error != GL_NO_ERROR )
    {
        LogError( "%s returned glError 0x%x\n", pFunctionName, error );
    }
}

typedef struct
{
    unsigned int mSize;
    unsigned int mFlags;
    unsigned int mFourCC;
    unsigned int mRGBBitCount;
    unsigned int mRedBitMask;
    unsigned int mGreenBitMask;
    unsigned int mBlueBitMask;
    unsigned int mAlphaBitMask;
} DDSPixelFormat;

typedef struct
{
    char           mFileType[4];
    unsigned int   mSize;
    unsigned int   mFlags;
    unsigned int   mHeight;
    unsigned int   mWidth;
    unsigned int   mPitchOrLinearSize;
    unsigned int   mDepth;
    unsigned int   mMipMapCount;
    unsigned int   mReserved1[11];
    DDSPixelFormat mPixelFormat;
    unsigned int   mCaps;
    unsigned int   mCaps2;
    unsigned int   mCaps3;
    unsigned int   mCaps4;
    unsigned int   mReserved2;
} DDSHeader;

GLuint LoadTextureS3TC( const char* TextureFileName, int minFilter, int magFilter)
{
    // Load the texture file
    char* pData = NULL;
    unsigned int fileSize = 0;

    ReadFile( TextureFileName, &pData, &fileSize );

    // Read the header
    DDSHeader* pHeader = (DDSHeader*)pData;

    // Generate handle
    GLuint handle;
    glGenTextures( 1, &handle );

    // Bind the texture
    glBindTexture( GL_TEXTURE_2D, handle );

    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter );
    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter );

    // Determine texture format
    GLenum format;
    GLuint blockSize;
    switch( pHeader->mPixelFormat.mFourCC )
    {
        case 0x31545844:
        {
            //FOURCC_DXT1
            format = GL_COMPRESSED_RGBA_S3TC_DXT1_EXT;
            blockSize = 8;
            break;
        }
        case 0x33545844:
        {
            //FOURCC_DXT3
            format = GL_COMPRESSED_RGBA_S3TC_DXT3_EXT;
            blockSize = 16;
            break;
        }
        case 0x35545844:
        {
            //FOURCC_DXT5
            format = GL_COMPRESSED_RGBA_S3TC_DXT5_EXT;
            blockSize = 16;
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
    unsigned int offset = 0;
    unsigned int mipWidth = pHeader->mWidth;
    unsigned int mipHeight = pHeader->mHeight;

    unsigned int mip = 0;
    do
    {
        // Determine size
        // As defined in extension: size = ceil(<w>/4) * ceil(<h>/4) * blockSize
        unsigned int pixelDataSize = ((mipWidth + 3) >> 2) * ((mipHeight + 3) >> 2) * blockSize;

        // Upload texture data for this mip
        glCompressedTexImage2D( GL_TEXTURE_2D, mip, format, mipWidth, mipHeight, 0, pixelDataSize, (pData + sizeof(DDSHeader)) + offset );

        // Next mips is half the size (divide by 2) with a min of 1
        mipWidth = mipWidth >> 1;
        mipWidth = ( mipWidth == 0 ) ? 1 : mipWidth;

        mipHeight = mipHeight >> 1;
        mipHeight = ( mipHeight == 0 ) ? 1 : mipHeight;

        // Move to next mip map
        offset += pixelDataSize;
        mip++;
    } while(mip < pHeader->mMipMapCount);

    // clean up
    free( pData );

    glBindTexture( handle, 0 );

    // Return handle
    return handle;
}
#include <assert.h>
#include <memory.h>
#include <stdio.h>
#include <stdlib.h>

#include <sys/types.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>

#include "file.h"

AAssetManager* g_pManager = NULL;

void SetAssetManager( AAssetManager* pManager )
{
    g_pManager = pManager;
}

// Read the contents of the give file return the content and the file size.
// The calling function is responsible for free the memory allocated for Content.
void ReadFile( const char* pFileName, char** ppContent, unsigned int* pSize )
{  
    assert( g_pManager );

    AAsset* pFile = AAssetManager_open( g_pManager, pFileName, AASSET_MODE_UNKNOWN );

    if( pFile != NULL )
    {
        // Determine file size
        off_t fileSize = AAsset_getLength( pFile );
        
        // Read data
        char* pData = (char*)malloc( fileSize );
        AAsset_read( pFile, pData, fileSize );
    
        // Allocate space for the file content
        *ppContent = (char*)malloc( fileSize );
    
        // Copy the content
        memcpy( *ppContent, pData, fileSize );
        *pSize = fileSize;

        free( pData );

        AAsset_close( pFile );
    }
}
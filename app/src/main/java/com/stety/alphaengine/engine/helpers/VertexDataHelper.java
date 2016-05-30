package com.stety.alphaengine.engine.helpers;

import android.opengl.GLES20;

import com.stety.alphaengine.engine.constants.Constants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class VertexDataHelper
{
    /**
     * Load vertex data to GL buffer. And return number of vertices.
     *
     * @param vertexData Field in float with vertex data.
     * @param buffers    GL buffer.
     * @param stride     Stride (= Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE etc.)
     * @return Number of vertices.
     */
    public static int load(float[] vertexData, int buffers[], int stride)
    {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        int vertices = vertexData.length / (stride);

        // Transfer data to native memory.
        FloatBuffer vertexArray = ByteBuffer.allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertexData);
        vertexArray.position(0);

        // Transfer data from native memory to the GPU buffer.
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexArray.capacity() * Constants.BYTES_PER_FLOAT, vertexArray, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        return vertices;
    }

    /**
     * Load vertex data to GL buffer as DYNAMIC_DRAW. And return number of vertices.
     *
     * @param vertexData Field in float with vertex data.
     * @param buffers    GL buffer.
     * @param stride     Stride (= Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE etc.)
     * @return Number of vertices.
     */
    public static int loadDD(float[] vertexData, int buffers[], int stride)
    {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        int vertices = vertexData.length / (stride);

        // Transfer data to native memory.
        FloatBuffer vertexArray = ByteBuffer.allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertexData);
        vertexArray.position(0);

        // Transfer data from native memory to the GPU buffer.
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexArray.capacity() * Constants.BYTES_PER_FLOAT, vertexArray, GLES20.GL_DYNAMIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        return vertices;
    }

    /**
     * Load vertices to native memory, no VBO.
     *
     * @param vertexData
     * @return
     */
    public static FloatBuffer loadNoVBO(float[] vertexData)
    {
        // Transfer data to native memory.
        FloatBuffer vertexArray = ByteBuffer.allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertexData);
        vertexArray.position(0);

        return vertexArray;
    }

    /**
     * Number of vertices.
     *
     * @param vertexData
     * @param stride
     * @return count of vertices
     */
    public static int vertices(float[] vertexData, int stride)
    {
        return vertexData.length / (stride);
    }

    /**
     * Load vertex data to GL buffer.
     *
     * @param vertexData Field in float with vertex data.
     * @param buffers    GL buffer.
     */
    public static void load(float[] vertexData, int buffers[])
    {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);

        // Transfer data to native memory.
        FloatBuffer vertexArray = ByteBuffer.allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertexData);
        vertexArray.position(0);

        // Transfer data from native memory to the GPU buffer.
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexArray.capacity() * Constants.BYTES_PER_FLOAT, vertexArray, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Load vertex data to specific place in GL buffer. And return number of vertices.
     * Use for example in Draw3D.
     *
     * @param vertexData Field in float with vertex data.
     * @param buffers    GL buffer[place]! Important specific place in GL buffer.
     * @param stride     Stride (= Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE etc.)
     * @return Number of vertices.
     */
    public static int load(float[] vertexData, int buffers, int stride)
    {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers);
        int vertices = vertexData.length / (stride);

        // Transfer data to native memory.
        FloatBuffer vertexArray = ByteBuffer.allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertexData);
        vertexArray.position(0);

        // Transfer data from native memory to the GPU buffer.
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexArray.capacity() * Constants.BYTES_PER_FLOAT, vertexArray, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        return vertices;
    }
}

package com.stety.alphaengine.game.programs;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.stety.alphaengine.engine.constants.Constants;
import com.stety.alphaengine.engine.helpers.MatrixHelper;
import com.stety.alphaengine.engine.helpers.ShaderHelper;
import com.stety.alphaengine.engine.helpers.TextReaderHelper;
import com.stety.alphaengine.engine.objects.Draw3D;

/**
 * Coming Soon.
 * PTN Program
 */
public class ptnProgram
{
    private static final int stride = (Constants.POSITION_3D_DATA_SIZE + Constants.NORMAL_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE) * Constants.BYTES_PER_FLOAT;
    private static int vertexShaderHandle;
    private static int fragmentShaderHandle;
    private static int program;
    private static int mMVPMatrixHandle;
    private static int mPositionHandle;
    private static int mTextureCoordinateHandle;
    private static int mNormalHandle;

    public static void use()
    {
        GLES20.glUseProgram(program);
        Draw3D.usingProgram = Programs.programs.ptProgram;
    }

    public static void draw(int[] bufferId, int order, int vertices)
    {
        // Pass in the position information
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferId[order]);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, Constants.POSITION_3D_DATA_SIZE, GLES20.GL_FLOAT, false, stride, 0);

                /* // Pass in the texture information
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubeBufferIdx);
                GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
                GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE, GLES20.GL_FLOAT, false,
                stride, (POSITION_DATA_SIZE + NORMAL_DATA_SIZE) * BYTES_PER_FLOAT);*/

     /*   // Pass in the normal information
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferId[order]);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle, constants.COLOR_RGBA_DATA_SIZE, GLES20.GL_FLOAT, false, stride, constants.POSITION_3D_DATA_SIZE *  constants.BYTES_PER_FLOAT);*/


        // Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(MatrixHelper.MVPMatrix, 0, MatrixHelper.ViewMatrix, 0, MatrixHelper.ModelMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(MatrixHelper.MVPMatrix, 0, MatrixHelper.projectionMatrix, 0, MatrixHelper.MVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixHelper.MVPMatrix, 0);

        // Draw
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices/*mActualCubeFactor * mActualCubeFactor * mActualCubeFactor * 36*/);
    }

    public static void compile()
    {
        vertexShaderHandle = ShaderHelper.compileVertexShader(TextReaderHelper.readShader("v_ptn"));
        fragmentShaderHandle = ShaderHelper.compileFragmentShader(TextReaderHelper.readShader("c_ptn"));
        program = ShaderHelper.linkProgram(vertexShaderHandle, fragmentShaderHandle);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(program, "a_Position");
        // mColorHandle = GLES20.glGetAttribLocation(program, "a_Color");
    }

    public static void delete()
    {
        ShaderHelper.deleteProgramAndShaders(program, vertexShaderHandle, fragmentShaderHandle);
    }
}

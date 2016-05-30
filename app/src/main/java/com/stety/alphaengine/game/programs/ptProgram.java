package com.stety.alphaengine.game.programs;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.stety.alphaengine.engine.constants.Constants;
import com.stety.alphaengine.engine.helpers.MatrixHelper;
import com.stety.alphaengine.engine.helpers.ShaderHelper;
import com.stety.alphaengine.engine.helpers.TextReaderHelper;
import com.stety.alphaengine.engine.objects.Draw3D;

/**
 * PT program. (Position and texture)
 */
public class ptProgram
{
    private static final int stride = (Constants.POSITION_3D_DATA_SIZE + Constants.TEXTURE_2D_COORDINATE_DATA_SIZE) * Constants.BYTES_PER_FLOAT;
    private static int vertexShaderHandle;
    private static int fragmentShaderHandle;
    private static int program;
    private static int mMVPMatrixHandle;
    private static int mPositionHandle;
    private static int mTextureUniformHandle;
    private static int mTextureCoordinateHandle;

    public static void use()
    {
        GLES20.glUseProgram(program);
        Draw3D.usingProgram = Programs.programs.ptProgram;
    }

    public static void draw(int[] bufferId, int order, int vertices, int texture)
    {
        // Pass in the position information
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferId[order]);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, Constants.POSITION_3D_DATA_SIZE, GLES20.GL_FLOAT, false, stride, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);

        GLES20.glUniform1i(mTextureUniformHandle, 0);

        // Pass in the texture information
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferId[order]);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, Constants.TEXTURE_2D_COORDINATE_DATA_SIZE, GLES20.GL_FLOAT, false, stride, Constants.POSITION_3D_DATA_SIZE * Constants.BYTES_PER_FLOAT);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        // model * view
        Matrix.multiplyMM(MatrixHelper.MVPMatrix, 0, MatrixHelper.ViewMatrix, 0, MatrixHelper.ModelMatrix, 0);

        // modelView * projection.
        Matrix.multiplyMM(MatrixHelper.MVPMatrix, 0, MatrixHelper.projectionMatrix, 0, MatrixHelper.MVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixHelper.MVPMatrix, 0);

        // Draw
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices);
    }

    public static void compile()
    {
        vertexShaderHandle = ShaderHelper.compileVertexShader(TextReaderHelper.readShader("v_pt"));
        fragmentShaderHandle = ShaderHelper.compileFragmentShader(TextReaderHelper.readShader("f_pt"));
        program = ShaderHelper.linkProgram(vertexShaderHandle, fragmentShaderHandle);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(program, "a_Position");
        mTextureUniformHandle = GLES20.glGetUniformLocation(program, "u_Texture");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(program, "a_TexCoordinate");
    }

    public static void delete()
    {
        ShaderHelper.deleteProgramAndShaders(program, vertexShaderHandle, fragmentShaderHandle);
    }
}

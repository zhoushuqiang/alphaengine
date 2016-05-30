package com.stety.alphaengine.game.programs;

import android.opengl.GLES20;

import com.stety.alphaengine.engine.constants.Constants;
import com.stety.alphaengine.engine.helpers.MatrixHelper;
import com.stety.alphaengine.engine.helpers.ShaderHelper;
import com.stety.alphaengine.engine.helpers.TextReaderHelper;

import java.nio.ByteBuffer;

import static android.opengl.GLES20.GL_LEQUAL;
import static android.opengl.GLES20.GL_LESS;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDepthFunc;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.Matrix.multiplyMM;

/**
 * Skybox program.
 */
public class skyboxProgram {
    private static final int stride = 0;
    private static int vertexShaderHandle;
    private static int fragmentShaderHandle;
    private static int program;
    private static int mMVPMatrixHandle;
    private static int mPositionHandle;
    private static int uTextureUnit;

    public static void use() {
        GLES20.glUseProgram(program);
    }

    public static void draw(int[] bufferId, int order, int texture, ByteBuffer indexArray) {
        indexArray.position(0);

        multiplyMM(MatrixHelper.tempMatrix, 0, MatrixHelper.viewMatrixSkybox, 0, MatrixHelper.ModelMatrix, 0);
        multiplyMM(MatrixHelper.MVPMatrix, 0, MatrixHelper.projectionMatrix, 0, MatrixHelper.tempMatrix, 0);
        glDepthFunc(GL_LEQUAL);

        glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixHelper.MVPMatrix, 0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texture);
        glUniform1i(uTextureUnit, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferId[order]);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, Constants.POSITION_3D_DATA_SIZE, GLES20.GL_FLOAT, false, stride, 0);

        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_BYTE, indexArray);
        glDepthFunc(GL_LESS);
    }

    public static void compile() {
        vertexShaderHandle = ShaderHelper.compileVertexShader(TextReaderHelper.readShader("v_skybox"));
        fragmentShaderHandle = ShaderHelper.compileFragmentShader(TextReaderHelper.readShader("f_skybox"));
        program = ShaderHelper.linkProgram(vertexShaderHandle, fragmentShaderHandle);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(program, "a_Position");
        uTextureUnit = GLES20.glGetUniformLocation(program, "u_TextureUnit");
    }

    public static void delete() {
        ShaderHelper.deleteProgramAndShaders(program, vertexShaderHandle, fragmentShaderHandle);
    }
}

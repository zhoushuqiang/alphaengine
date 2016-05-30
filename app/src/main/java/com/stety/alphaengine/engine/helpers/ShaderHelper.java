package com.stety.alphaengine.engine.helpers;

import android.opengl.GLES20;
import android.util.Log;

import com.stety.alphaengine.engine.logger.Logs;

/**
 * EN: Helper for shaders.
 * CS: Pomocník pro shadery.
 */
public class ShaderHelper
{
    /**
     * EN: Compiles a vertex shader.
     * CS: Zkompiluje vertex shader.
     *
     * @param shaderCode shader code
     * @return OpenGL object ID
     */
    public static int compileVertexShader(String shaderCode)
    {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    /**
     * EN: Compiles a fragment shader.
     * CS: Zkompiluje fragment shader.
     *
     * @param shaderCode shader code
     * @return OpenGL object ID
     */
    public static int compileFragmentShader(String shaderCode)
    {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }

    /**
     * EN: Compiles a shader.
     * CS: Zkompiluje shader.
     *
     * @param type       type of shader vertex/fragment
     * @param shaderCode code
     * @return OpenGL object ID.
     */
    private static int compileShader(int type, String shaderCode)
    {
        final int shaderObjectId = GLES20.glCreateShader(type);

        if (Logs.Logs)
        {
            if (shaderObjectId == 0)
            {
                Log.w(Logs.tagShaderHelper, "Could not create new shader.");
                return 0;
            }
        }

        GLES20.glShaderSource(shaderObjectId, shaderCode);
        GLES20.glCompileShader(shaderObjectId);

        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        if (Logs.Logs)
        {
            Log.v(Logs.tagShaderHelper, "Results of compiling shader: " /*+ "\n" + shaderCode + "\n:*/ + GLES20.glGetShaderInfoLog(shaderObjectId));
        }

        if (compileStatus[0] == 0)
        {
            GLES20.glDeleteShader(shaderObjectId);

            if (Logs.Logs)
            {
                Log.w(Logs.tagShaderHelper, "Compilation of shader failed.");
            }

            return 0;
        }
        return shaderObjectId;
    }

    /**
     * EN: Links a vertex shader and a fragment shader to program.
     * CS: Propojí vertex a fragment shader do programu.
     *
     * @param vertexShaderId   OpenGL vertex shader ID
     * @param fragmentShaderId OpenGL fragment shader ID
     * @return OpenGL program ID.
     */
    public static int linkProgram(int vertexShaderId, int fragmentShaderId)
    {
        final int programObjectId = GLES20.glCreateProgram();

        if (programObjectId == 0)
        {
            if (Logs.Logs)
            {
                Log.w(Logs.tagShaderHelper, "Could not create new program");
            }

            return 0;
        }
        GLES20.glAttachShader(programObjectId, vertexShaderId);
        GLES20.glAttachShader(programObjectId, fragmentShaderId);

        GLES20.glLinkProgram(programObjectId);
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus, 0);

        if (Logs.Logs)
        {
            Log.v(Logs.tagShaderHelper, "Results of linking program: " + GLES20.glGetProgramInfoLog(programObjectId));
        }

        if (linkStatus[0] == 0)
        {
            GLES20.glDeleteProgram(programObjectId);
            if (Logs.Logs)
            {
                Log.w(Logs.tagShaderHelper, "Linking of program failed.");
            }
            return 0;
        }

        return programObjectId;
    }

    /**
     * ONLY FOR TESTING.
     * EN: Validates an OpenGL program.
     * CS: Ověří OpenGL program.
     *
     * @param programObjectId OpenGL program ID
     * @return validate status.
     */
    public static boolean validateProgram(int programObjectId)
    {
        GLES20.glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(Logs.tagShaderHelper, "Results of validating program: " + validateStatus[0] + "\nLog:" + GLES20.glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }

    /**
     * EN: Delete OpenGL program and shaders.
     * CS: Smaže OpenGL program a shaders.
     *
     * @param programObjectId        OpenGL program ID
     * @param vertexShaderObjectId   OpenGL vertex shader ID
     * @param fragmentShaderObjectId OpenGL fragment shader ID
     */
    public static void deleteProgramAndShaders(int programObjectId, int vertexShaderObjectId,
                                               int fragmentShaderObjectId)
    {
        GLES20.glDeleteProgram(programObjectId);
        GLES20.glDeleteShader(vertexShaderObjectId);
        GLES20.glDeleteShader(fragmentShaderObjectId);
    }
}
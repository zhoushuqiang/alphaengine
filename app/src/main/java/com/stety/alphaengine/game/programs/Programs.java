package com.stety.alphaengine.game.programs;

/**
 * EN: List of programs.
 * CS: Seznam programů.
 */
public class Programs {
    /**
     * EN: List of programs for compile.
     * CS: Seznam programů pro kompilaci.
     */
    public static void compile() {
        ptProgram.compile();
        ptAlphaEtc1Program.compile();
        skyboxProgram.compile();
        texture2DProgram.compile();
        texture2DAlphaEtc1Program.compile();
        texture2DProgramNoVBO.compile();
        texture2DProgramNoVBOAlpha.compile();
    }

    /**
     * EN: List of programs for delete.
     * CS: Seznam programů pro smazání.
     */
    public static void delete() {
        ptProgram.delete();
        ptAlphaEtc1Program.delete();
        skyboxProgram.delete();
        texture2DProgram.delete();
        texture2DAlphaEtc1Program.delete();
        texture2DProgramNoVBO.delete();
        texture2DProgramNoVBOAlpha.delete();
    }

    /**
     * EN: List of programs.
     * CS: Seznam programů.
     */
    public enum programs {
        ptProgram,
        ptProgramAlphaEtc1,
        skyboxProgram,
        texture2DProgram,
        texture2DAlphaEtc1Program,
        texture2DProgramNoVBO,
        texture2DProgramNoVBOAlpha
    }
}

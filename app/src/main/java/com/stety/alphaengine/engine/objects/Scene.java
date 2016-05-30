package com.stety.alphaengine.engine.objects;

/**
 * EN: Only abstract class as parent for create scenes.
 * CS: Pouze abstraktní třída jako rodič pro vytvoření scén.
 */
public abstract class Scene
{
    /**
     * EN: Here is created OpenGL ES surface. This method is first called.
     * CS: Zde je vytvořen OpenGL ES surface. Tato metoda je zavolána jako první.
     */
    public abstract void created();

    /**
     * EN: Here is changed OpenGL ES surface - rotation. This method is second called.
     * CS: Zde je změněn OpenGL ES surface - rotace obrazovky. Tato metoda je zavolána jako druhá.
     */
    public abstract void changed(/*int width, int height*/);

    /**
     * EN: Here draw every frame. Only Draw!!!
     * CS: Zde se vykreslí každý snímek. Pouze vykreslí!!!
     */
    public abstract void draw2D(/*float interpolation*/);

    /**
     * EN: Here draw every frame. Only Draw!!!
     * CS: Zde se vykreslí každý snímek. Pouze vykreslí!!!
     */
    public abstract void draw3D(/*float interpolation*/);

    /**
     * EN: Here update game.
     * CS: Zde se aktualizuje hra.
     */
    public abstract void update();

    /**
     * EN: Call when the is created OpenGL ES surface and when is device unlock.
     * CS: Zavolá se když je vytvořen OpenGL ES surface a když je zařízení odemčeno.
     */
    public abstract void unlock();

    /**
     * EN: Here is clean and delete buffers, etc...
     * CS: Zde se uklidí a smažou buffery, atd...
     */
    @SuppressWarnings("unused")
    public abstract void cleanUp();

    /**
     * EN: Here is reset some things for new load (when OnResume), e.g. Draw3D.pause();
     * CS: Zde se resetují některé věci pro nové načtení (při OnResume), např. Draw3D.pause();
     */
    public abstract void pause();

    public abstract void input();
}

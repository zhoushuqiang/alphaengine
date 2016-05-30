package com.stety.alphaengine.game.Menu;

import android.opengl.GLES20;

import com.stety.alphaengine.engine.android.Renderer;
import com.stety.alphaengine.engine.cameras.OrthographicCamera;
import com.stety.alphaengine.engine.helpers.MatrixHelper;
import com.stety.alphaengine.engine.objects.Draw3D;
import com.stety.alphaengine.engine.objects.Scene;
import com.stety.alphaengine.engine.textures.Texture2D;
import com.stety.alphaengine.game.Level1.Level1;

/**
 * Menu.
 */
public class Menu extends Scene {
    final int drawObjectsAll = 0;
    final int drawObjectsTextures = 0;
    final Draw3D Draw3D = new Draw3D(drawObjectsAll, drawObjectsTextures);
    //final PerspectiveCamera cam = new PerspectiveCamera(0.0f, 0.0f, -1.5f, 0.0f, 0.0f, 0.0f);
    final OrthographicCamera cam = new OrthographicCamera(0.0f, 0.0f, -1.5f, 0.0f, 0.0f, 0.0f);
    final Texture2D texture2DMultiRes = new Texture2D();
    private int count = 0;

    public static void load() {
        Renderer.scene = new Menu();
        Renderer.scene.created();
        Renderer.scene.unlock();
        System.gc(); //For clean unnecessary memory.
        Renderer.scene.changed();
    }

    @Override
    public void created() {
        Draw3D.initialization();

        texture2DMultiRes.initialization();
    }

    @Override
    public void unlock() {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }

    @Override
    public void changed() {
        cam.setProjection(-MatrixHelper.ratio, MatrixHelper.ratio, -1, 1, -1, 1);
        texture2DMultiRes.load("czech", GLES20.GL_LINEAR, GLES20.GL_LINEAR, new float[]{0, 0, 0, 0, 0, 0, 0, 0}, new float[]{20000, 20000, 20000, 20000, 20000, 20000, 20000, 20000});
    }

    @Override
    public void draw3D() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        //1. cam update
        //2. translate 3.scale 4. rotate!!!
        //5. draw objects
        cam.update();

        if (count >= 2) {
            cleanUp();
            Level1.load();
        }
    }

    @Override
    public void draw2D() {

        texture2DMultiRes.draw();
    }

    @Override
    public void update() {
        count += 1;
        Draw3D.rotate += 1;
    }

    @Override
    public void cleanUp() {
        Draw3D.delete();
        MatrixHelper.resetMatrix();
        texture2DMultiRes.delete();
    }

    @Override
    public void pause() {
        Draw3D.pause();
    }

    @Override
    public void input() {
    }
}


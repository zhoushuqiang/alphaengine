package com.stety.alphaengine.game.objects;

import com.stety.alphaengine.engine.geometry.Axis;
import com.stety.alphaengine.engine.geometry.Point3D;
import com.stety.alphaengine.game.programs.ptProgram;

/**
 * EN: Dynamic game object with position and texture, is object for translate, rotate, etc...
 * As is car, boot, people, etc...
 * CS: Dynamický herní objekt s pozicí a texturou, je objekt pro překlady, rotaci, atd...
 * Jako je auto, boot, člověk, atd...
 */
public class GameObjectPT extends com.stety.alphaengine.engine.objects.GameObjectPT
{
    public GameObjectPT(float scale, float rotation, Axis.axis rotationAxis, Point3D position)
    {
        this.scale = scale;
        this.rotation = rotation;
        this.rotationAxis = rotationAxis;
        this.position = position;
    }

    @Override
    public void draw()
    {
        location();
        ptProgram.use();
        ptProgram.draw(bufferId, 0, vertices, texture);
    }
}

package com.stety.alphaengine.game.objects;

import com.stety.alphaengine.engine.geometry.Axis;
import com.stety.alphaengine.engine.geometry.Point3D;
import com.stety.alphaengine.engine.objects.GameObjectPT;
import com.stety.alphaengine.game.programs.ptProgram;

/**
 * Cube.
 */
public class Cube extends GameObjectPT
{
    public Cube(float scale, float rotation, Axis.axis rotationAxis, Point3D position)
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

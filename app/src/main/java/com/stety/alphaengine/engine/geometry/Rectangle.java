package com.stety.alphaengine.engine.geometry;

/**
 * Rectangle.
 */
public class Rectangle
{
    public short[] upperLeftCorner = new short[2];
    public short[] lowerRightCorner = new short[2];

    public short width;
    public short height;

    public Rectangle(short[] upperLeftCorner, short width, short height)
    {
        this.upperLeftCorner = upperLeftCorner;
        this.width = width;
        this.height = height;
        this.lowerRightCorner[1] = (short) (upperLeftCorner[1] + height);
        this.lowerRightCorner[0] = (short) (upperLeftCorner[0] + width);
    }

    public Rectangle(short[] upperLeftCorner, short[] lowerRightCorner)
    {
        this.upperLeftCorner = upperLeftCorner;
        this.lowerRightCorner = lowerRightCorner;
        this.width = (short) (lowerRightCorner[0] - upperLeftCorner[0]);
        this.height = (short) (lowerRightCorner[1] - upperLeftCorner[1]);
    }
}

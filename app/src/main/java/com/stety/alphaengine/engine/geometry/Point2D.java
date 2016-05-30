package com.stety.alphaengine.engine.geometry;

/**
 * EN: 2D point.
 * CS: 2D bod.
 */
public class Point2D
{
    public float x;
    public float y;

    public Point2D(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * EN: Translate point about axis X. Warning: Create new point!!!
     * If you want translate actual point use: point.x = distance;
     * CS: Přeloží bod podél osy X. Pozor: Vytvoří nový bod!!!
     * Pokud chceš přeložit pozici aktuálního bodu použij: point.x = distance;
     *
     * @param distance - EN: Distance by which you want to increase point on axis X.
     *                 CS: Vzdálenost o kterou chceš navýšit bod na ose X.
     * @return - EN: New point.
     * CS: Nový bod.
     */
    public Point2D translateNewX(float distance)
    {
        return new Point2D(x + distance, y);
    }

    /**
     * EN: Translate point about axis Y. Warning: Create new point!!!
     * If you want translate actual point use: point.y = distance;
     * CS: Přeloží bod podél osy Y. Pozor: Vytvoří nový bod!!!
     * Pokud chceš přeložit pozici aktuálního bodu použij: point.y = distance;
     *
     * @param distance - EN: Distance by which you want to increase point on axis Y.
     *                 CS: Vzdálenost o kterou chceš navýšit bod na ose Y.
     * @return - EN: New point.
     * CS: Nový bod.
     */
    public Point2D translateNewY(float distance)
    {
        return new Point2D(x, y + distance);
    }

    /**
     * EN: Translate point about vector. Warning: Create new point!!!
     * If you want translate actual point use: translate(vector);
     * CS: Přeloží bod podle vektoru. Pozor: Vytvoří nový bod!!!
     * Pokud chceš přeložit pozici aktuálního bodu použij: translate(vector);
     *
     * @param vector - EN: Distance by which you want translate point.
     *               CS: Vzdálenost o kterou chceš přeložit bod.
     * @return - EN: New point.
     * CS: Nový bod.
     */
    public Point2D translateNew(Vector vector)
    {
        return new Point2D(x + vector.x, y + vector.y);
    }

    /**
     * EN: Translate point about vector. Warning: Create new point!!!
     * If you want translate actual point use: translate(vector);
     * CS: Přeloží bod podle vektoru. Pozor: Vytvoří nový bod!!!
     * Pokud chceš přeložit pozici aktuálního bodu použij: translate(vector);
     *
     * @param x EN: Distance in axis X by which you want translate point.
     *          CS: Vzdálenost v ose X o kterou chceš přeložit bod.
     * @param y EN: Distance in axis Y by which you want translate point.
     *          CS: Vzdálenost v ose Y o kterou chceš přeložit bod.
     * @return - EN: New point.
     * CS: Nový bod.
     */
    public Point2D translateNew(float x, float y)
    {
        return new Point2D(this.x + x, this.y + y);
    }

    /**
     * EN: Translate actual point about vector.
     * CS: Přeloží aktuální bod podle vektoru.
     *
     * @param vector - EN: Distance by which you want translate point.
     *               CS: Vzdálenost o kterou chceš přeložit bod.
     */
    public void translate(Vector vector)
    {
        x += vector.x;
        y += vector.y;
    }

    /**
     * EN: Translate actual point about vector.
     * CS: Přeloží aktuální bod podle vektoru.
     *
     * @param x EN: Distance in axis X by which you want translate point.
     *          CS: Vzdálenost v ose X o kterou chceš přeložit bod.
     * @param y EN: Distance in axis Y by which you want translate point.
     *          CS: Vzdálenost v ose Y o kterou chceš přeložit bod.
     */
    public void translate(float x, float y)
    {
        this.x += x;
        this.y += y;
    }
}

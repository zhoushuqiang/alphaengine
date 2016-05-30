package com.stety.alphaengine.engine.input;

import android.util.Log;

import com.stety.alphaengine.engine.helpers.MatrixHelper;
import com.stety.alphaengine.engine.logger.Logs;
import com.stety.alphaengine.engine.textures.Texture2DAlpha;
import com.stety.alphaengine.engine.textures.Texture2DJoystickAlpha;

/**
 * Joystick
 */
public class Joystick {
    //Directions
    public static final byte STICK_NONE = 0;
    public static final byte STICK_UP = 1;
    public static final byte STICK_DOWN = 2;
    public static final byte STICK_LEFT = 3;
    public static final byte STICK_RIGHT = 4;
    public static final byte STICK_UPLEFT = 5;
    public static final byte STICK_UPRIGHT = 6;
    public static final byte STICK_DOWNLEFT = 7;
    public static final byte STICK_DOWNRIGHT = 8;

    //Textures
    public Texture2DJoystickAlpha stickTexture;
    public Texture2DAlpha backgroundTexture;

    /**
     * Is button active?
     */
    public boolean active = false;
    /**
     * Left-upper corner joystick coordinate
     */
    public short[] triggerUpperLeftCorner = null;
    /**
     * Right-lower corner joystick coordinate
     */
    public short[] triggerLowerRightCorner = null;
    /**
     * Distance in DPI
     */
    public short distanceDPI = 0;
    /**
     * Distance in px
     */
    public short distance = 0;
    /**
     * Pointer, which press button
     */
    public short pressPointer = -1;
    /**
     * Can was button pressed only, other buttons... no?
     */
    private boolean only = false;
    private short x = 0;
    private short y = 0;
    /**
     * Angle in degrees*
     */
    private short angle = 0;
    private short OFFSET = 0;
    /**
     * Minimum distance for activate (x,y) joystick
     */
    private short MIN_DISTANCE = 0;
    /**
     * Width of joystick
     */
    private short width = 0;
    /**
     * Width of stick
     */
    private short widthStick = 0;
    /**
     * Height of stick
     */
    private short heightStick = 0;

    /**
     * Joystick
     *
     * @param only If is joystick pressed can be press ex. touchpad...?
     */
    public Joystick(boolean only) {
        this.only = only;
        stickTexture = new Texture2DJoystickAlpha();
        backgroundTexture = new Texture2DAlpha();
    }


    /**
     * Set Width and height of stick.
     *
     * @param width  Width in DPI.
     * @param height Height in DPI!
     */
    public void setWidthAndHeightStick(short[] width, short[] height) {
        switch (MatrixHelper.ratioRound) {
            case 177:
            default: //16:9
                this.widthStick = width[3];
                this.heightStick = height[3];
                break;
            case 150: //3:2
                this.widthStick = width[1];
                this.heightStick = height[1];
                break;
            case 133: //4:3
                this.widthStick = width[0];
                this.heightStick = height[0];
                break;
            case 166: //5:3
                this.widthStick = width[2];
                this.heightStick = height[2];
                break;
        }
    }

    /**
     * Set trigger for joystick.
     *
     * @param triggerUpperLeftCorner  X and Y positions upper left corner position the button.
     * @param triggerLowerRightCorner X and Y positions lower right corner position the button.
     */
    public void setTrigger(short[] triggerUpperLeftCorner, short[] triggerLowerRightCorner) {
        switch (MatrixHelper.ratioRound) {
            case 177:
            default: //16:9
                this.triggerUpperLeftCorner = new short[]{(short) (triggerUpperLeftCorner[6] * MatrixHelper.factorWidth), (short) (triggerUpperLeftCorner[7] * MatrixHelper.factorHeight)};
                this.triggerLowerRightCorner = new short[]{(short) (triggerLowerRightCorner[6] * MatrixHelper.factorWidth), (short) (triggerLowerRightCorner[7] * MatrixHelper.factorHeight)};
                break;
            case 150: //3:2
                this.triggerUpperLeftCorner = new short[]{(short) (triggerUpperLeftCorner[2] * MatrixHelper.factorWidth), (short) (triggerUpperLeftCorner[3] * MatrixHelper.factorHeight)};
                this.triggerLowerRightCorner = new short[]{(short) (triggerLowerRightCorner[2] * MatrixHelper.factorWidth), (short) (triggerLowerRightCorner[3] * MatrixHelper.factorHeight)};
                break;
            case 133: //4:3
                this.triggerUpperLeftCorner = new short[]{(short) (triggerUpperLeftCorner[0] * MatrixHelper.factorWidth), (short) (triggerUpperLeftCorner[1] * MatrixHelper.factorHeight)};
                this.triggerLowerRightCorner = new short[]{(short) (triggerLowerRightCorner[0] * MatrixHelper.factorWidth), (short) (triggerLowerRightCorner[1] * MatrixHelper.factorHeight)};
                break;
            case 166: //5:3
                this.triggerUpperLeftCorner = new short[]{(short) (triggerUpperLeftCorner[4] * MatrixHelper.factorWidth), (short) (triggerUpperLeftCorner[5] * MatrixHelper.factorHeight)};
                this.triggerLowerRightCorner = new short[]{(short) (triggerLowerRightCorner[4] * MatrixHelper.factorWidth), (short) (triggerLowerRightCorner[5] * MatrixHelper.factorHeight)};
                break;
        }

        width = (short) ((triggerLowerRightCorner[0] - triggerUpperLeftCorner[0]) * MatrixHelper.factorWidth);
    }

    /**
     * Function for call in action_down.
     *
     * @param x           Y position touch.
     * @param y           X position touch.
     * @param pointerDown PointerDownID.
     * @return If was pressed true.
     */
    public boolean action_down(short x, short y, short pointerDown) {
        boolean onlyNow = false;
        if (!active && x >= triggerUpperLeftCorner[0] && x <= triggerLowerRightCorner[0] && y >= triggerUpperLeftCorner[1] && y <= triggerLowerRightCorner[1]) {
            this.x = (short) ((x - triggerUpperLeftCorner[0]) - (width / 2));
            this.y = (short) ((y - triggerUpperLeftCorner[1]) - (width / 2));
            distance = (short) Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
            angle = calculateAngle(this.x, this.y);

            if (distance <= (width / 2) - OFFSET) {
                if (Logs.Logs)
                    Log.d("joystick", "joystick action down");
                active = true;
                if (only)
                    onlyNow = true;
                pressPointer = pointerDown;
                setStickPosition(x, y);
            }
        }

        return onlyNow;
    }

    /**
     * Function for call in pointer up.
     *
     * @param pointerUp PointerDownID.
     */
    public void action_pointer_up(short pointerUp) {
        if (pressPointer == pointerUp) {
            if (active)
                active = false;

            if (Logs.Logs)
                Log.d("joystick", "joystick action pointer up");
            pressPointer = -1;
        }
    }

    /**
     * Function for call in action up.
     */
    public void action_up() {
        if (active) {
            active = false;
        }
        if (Logs.Logs)
            Log.d("joystick", "joystick action up");
        pressPointer = -1;
    }

    /**
     * Function for call in action move.
     *
     * @param x X position.
     * @param y Y position.
     */
    public void action_move(short x, short y) {
        this.x = (short) ((x - triggerUpperLeftCorner[0]) - (width / 2));
        this.y = (short) ((y - triggerUpperLeftCorner[1]) - (width / 2));
        distance = (short) Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
        angle = calculateAngle(this.x, this.y);

        if (distance <= (width / 2) - OFFSET) {
            setStickPosition(x, y);
        } else if (distance > (width / 2) - OFFSET) {
            short x1 = (short) (Math.cos(Math.toRadians(calculateAngle(this.x, this.y))) * ((width / 2) - OFFSET));
            short y1 = (short) (Math.sin(Math.toRadians(calculateAngle(this.x, this.y))) * ((width / 2) - OFFSET));
            x1 += triggerUpperLeftCorner[0] + (width / 2);
            y1 += triggerUpperLeftCorner[1] + (width / 2);
            setStickPosition(x1, y1);
        } else {
            if (Logs.Logs)
                Log.d("joystick", "joystick action move error");
        }
    }

    /**
     * Calculate angle in degrees.
     *
     * @param x X position.
     * @param y Y position.
     * @return Angle in degrees
     */
    private short calculateAngle(float x, float y) {
        if (x >= 0 && y >= 0)
            return (short) Math.toDegrees(Math.atan(y / x));
        else if (x < 0 && y >= 0 || x < 0 && y < 0)
            return (short) (Math.toDegrees(Math.atan(y / x)) + 180);
        else if (x >= 0 && y < 0)
            return (short) (Math.toDegrees(Math.atan(y / x)) + 360);
        return 0;
    }

    /**
     * Draw joystick.
     */
    public void draw() {
        backgroundTexture.draw();
        if (active) {
            stickTexture.draw();
        }
    }

    private void setStickPosition(short x, short y) {
        short x1 = (short) ((20000 * x) / MatrixHelper.width);
        short y1 = (short) ((20000 * y) / MatrixHelper.height);

        short[] upperLeftCorner = {(short) (x1 - (widthStick / 2)), (short) (y1 - (heightStick / 2))};
        short[] lowerRightCorner = {(short) (x1 + (widthStick / 2)), (short) (y1 + (heightStick / 2))};
        stickTexture.setPosition(new float[]{upperLeftCorner[0], upperLeftCorner[1]}, new float[]{lowerRightCorner[0], lowerRightCorner[1]});
    }

    /**
     * Get Direction in 8 direction.
     *
     * @return Direction
     */
    public byte getDirection8() {
        if (distance > MIN_DISTANCE && active) {
            if (angle >= 247.5 && angle < 292.5)
                return STICK_UP;
            else if (angle >= 292.5 && angle < 337.5)
                return STICK_UPRIGHT;
            else if (angle >= 337.5 || angle < 22.5)
                return STICK_RIGHT;
            else if (angle >= 22.5 && angle < 67.5)
                return STICK_DOWNRIGHT;
            else if (angle >= 67.5 && angle < 112.5)
                return STICK_DOWN;
            else if (angle >= 112.5 && angle < 157.5)
                return STICK_DOWNLEFT;
            else if (angle >= 157.5 && angle < 202.5)
                return STICK_LEFT;
            else if (angle >= 202.5 && angle < 247.5)
                return STICK_UPLEFT;
        }
        return 0;
    }


    /**
     * Get Direction in 4 direction.
     *
     * @return Direction
     */
    public byte getDirection4() {
        if (distance > MIN_DISTANCE && active) {
            if (angle >= 225 && angle < 315)
                return STICK_UP;
            else if (angle >= 315 || angle < 45)
                return STICK_RIGHT;
            else if (angle >= 45 && angle < 135)
                return STICK_DOWN;
            else if (angle >= 135 && angle < 225)
                return STICK_LEFT;
        }
        return 0;
    }


    /**
     * Distance in DPI.
     *
     * @return Distance
     */
    public short getDistanceDPI() {
        if (distance > MIN_DISTANCE && active)
            distanceDPI = (short) Math.sqrt(Math.pow((this.x * 20000) / MatrixHelper.width, 2) + Math.pow((this.y * 20000) / (MatrixHelper.height * MatrixHelper.ratio), 2));
        else
            distanceDPI = 0;
        return distanceDPI;
    }

    /**
     * Set OFFSET for move stick in round.
     *
     * @param OFFSET OFFSET in DPI.
     */
    public void setOFFSET(short OFFSET) {
        this.OFFSET = (short) (OFFSET * MatrixHelper.factorWidth);
    }

    /**
     * Set MIN_DISTANCE for move stick.
     *
     * @param MIN_DISTANCE OFFSET in DPI.
     */
    public void setMIN_DISTANCE(short MIN_DISTANCE) {
        this.MIN_DISTANCE = (short) (MIN_DISTANCE * MatrixHelper.factorWidth);
    }
}

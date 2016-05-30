package com.stety.alphaengine.engine.input;

import android.util.Log;

import com.stety.alphaengine.engine.helpers.MatrixHelper;
import com.stety.alphaengine.engine.logger.Logs;

/**
 * Touchpad.
 */
public class Touchpad {
    /**
     * Pointer, which press button
     */
    public short pressPointer = -1;
    /**
     * Is button active?
     */
    public boolean active = false;

    /**
     * Left-upper corner button coordinate
     */
    public short[] triggerUpperLeftCorner = new short[2];
    /**
     * Right-lower corner button coordinate
     */
    public short[] triggerLowerRightCorner = new short[2];
    public boolean change = false;
    private short deltaX;
    private short deltaY;
    /**
     * Previous X position
     */
    private short previousX;
    /**
     * Previous Y position
     */
    private short previousY;

    /**
     * Set trigger for touchpad.
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
    }

    /**
     * Function for call in action_down.
     *
     * @param x           Y position touch.
     * @param y           X position touch.
     * @param pointerDown PointerDownID.
     */
    public void action_down(short x, short y, short pointerDown) {
        if (!active) {
            if (x >= triggerUpperLeftCorner[0] && x <= triggerLowerRightCorner[0] && y >= triggerUpperLeftCorner[1] && y <= triggerLowerRightCorner[1]) {
                previousX = x;
                previousY = y;
                pressPointer = pointerDown;
                active = true;

                if (Logs.Logs)
                    Log.d(Logs.tagInput, "Touchpad was pressed.");
            }
        }
    }

    /**
     * Function for call in pointer up.
     *
     * @param pointerUp PointerDownID.
     */
    public void action_pointer_up(short pointerUp) {
        if (pressPointer == pointerUp) {
            if (active) {
                active = false;
                deltaX = 0;
                deltaY = 0;
            }
            if (Logs.Logs)
                Log.d(Logs.tagInput, "Touchpad was unpressed.");
            pressPointer = -1;
        }
    }

    /**
     * Function for call in action up.
     */
    public void action_up() {
        if (active) {
            active = false;
            deltaX = 0;
            deltaY = 0;
        }
        if (Logs.Logs)
            Log.d(Logs.tagInput, "Touchpad was unpressed.");
        pressPointer = -1;
    }

    /**
     * Function for call in action move.
     *
     * @param x X position.
     * @param y Y position.
     */
    public void action_move(short x, short y) {
        deltaX = (short) (x - previousX);
        deltaY = (short) (y - previousY);

        previousX = x;
        previousY = y;

        change = true;
    }

    /**
     * Get DeltaX.
     *
     * @return delta X in short
     */
    public short getDeltaX() {
        return (short) ((20000 * deltaX) / MatrixHelper.width);
    }

    /**
     * Get DeltaY.
     *
     * @return delta Y in short
     */
    public short getDeltaY() {
        return (short) ((20000 * deltaY) / MatrixHelper.height);
    }
}

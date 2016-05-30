package com.stety.alphaengine.engine.input;

import android.util.Log;

import com.stety.alphaengine.engine.helpers.MatrixHelper;
import com.stety.alphaengine.engine.logger.Logs;
import com.stety.alphaengine.engine.textures.Texture2DButtonAlpha;

/**
 * Button, which responds on action down, but also out trigger.
 * Responds on press:
 * 1) On pressed: if(pressed) and after you must (in if): pressed = false;
 * 2) On pressedSwitch - work as switch ON/OFF: if(pressedSwitch)
 * Responds on focus: if(focus)
 */
public class ButtonDownOut {
    /**
     * Pointer, which press button
     */
    public short pressPointer = -1;
    /**
     * Is button active?
     */
    public boolean active = false;
    /**
     * Is button pressed?
     */
    public boolean pressed = false;
    /**
     * Is button focus?
     */
    public boolean focus = false;
    /**
     * ON/OFF button, or normal?
     */
    public boolean pressedSwitch;
    /**
     * Left-upper corner button coordinate
     */
    public short[] triggerUpperLeftCorner = new short[2];
    /**
     * Right-lower corner button coordinate
     */
    public short[] triggerLowerRightCorner = new short[2];
    /**
     * Texture
     */
    public Texture2DButtonAlpha texture;
    /**
     * Can was button pressed only, other buttons... no?
     */
    private boolean only = false;

    /**
     * Button, which responds on action down and, but also out trigger.
     * Responds on press:
     * 1) On pressed: if(pressed) and after you must (in if): pressed = false;
     * 2) On pressedSwitch - work as switch on/off: if(pressedSwitch)
     * Responds on focus: if(focus)
     *
     * @param pressedSwitch Initial state for pressedSwitch (ON/OFF). (if you don't use pressedSwitch is it freely.)
     * @param only          If is button pressed can be press ex. touchpad...?
     */
    public ButtonDownOut(boolean pressedSwitch, boolean only) {
        this.pressedSwitch = pressedSwitch;
        this.only = only;
        texture = new Texture2DButtonAlpha();
    }

    /**
     * Button, which responds on action down and, but also out trigger.
     * Responds on press:
     * 1) On pressed: if(pressed) and after you must (in if): pressed = false;
     * 2) On pressedSwitch - work as switch on/off: if(pressedSwitch)
     * Responds on focus: if(focus)
     *
     * @param pressedSwitch Initial state for pressedSwitch (ON/OFF). (if you don't use pressedSwitch is it freely.)
     */
    public ButtonDownOut(boolean pressedSwitch) {
        this.pressedSwitch = pressedSwitch;
        texture = new Texture2DButtonAlpha();
    }

    /**
     * Set trigger for button.
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
     * @param x           Y position touch.
     * @param y           X position touch.
     * @param pointerDown PointerDownID
     * @return If was pressed true.
     */
    public boolean action_down(short x, short y, short pointerDown) {
        if (!active && !focus && x >= triggerUpperLeftCorner[0] && x <= triggerLowerRightCorner[0] && y >= triggerUpperLeftCorner[1] && y <= triggerLowerRightCorner[1]) {
            active = true;
            pressed = true;
            focus = true;
            pressPointer = pointerDown;

            pressedSwitch = !pressedSwitch;

            if (Logs.Logs)
                Log.d(Logs.tagInput, "Button is pressed.");

            return only;
        } else
            return false;
    }

    /**
     * Function for call in pointer up.
     *
     * @param pointerUp PointerDownID.
     */
    public void action_pointer_up(short pointerUp) {
        if (pressPointer == pointerUp) {
            if (focus) {
                pressed = false;
                focus = false;

                if (Logs.Logs)
                    Log.d(Logs.tagInput, "Button is unpressed.");
            }
            pressPointer = -1;
            active = false;
        }
    }

    /**
     * Function for call in action up.
     */
    public void action_up() {
        if (focus) {
            pressed = false;
            focus = false;

            if (Logs.Logs)
                Log.d(Logs.tagInput, "Button is unpressed.");
        }
        pressPointer = -1;
        active = false;
    }

    /**
     * Draw button.
     */
    public void draw() {
        if (focus) {
            texture.drawPress();
        } else {
            texture.draw();
        }
    }
}

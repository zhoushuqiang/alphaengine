package com.stety.alphaengine.engine.input;

import android.util.Log;

import com.stety.alphaengine.engine.helpers.MatrixHelper;
import com.stety.alphaengine.engine.logger.Logs;
import com.stety.alphaengine.engine.textures.Texture2DButtonAlpha;

/**
 * Button, which is special for Menu. Only singletouch, no multitouch.
 * Responds on press:
 * 1) On pressed: if(pressed) and after you must (in if): pressed = false;
 * 2) On pressedSwitch - work as switch ON/OFF: if(pressedSwitch)
 * Responds on focus: if(focus)
 */
public class ButtonMenu {
    /**
     * Is button active?
     */
    public boolean active = false;
    /**
     * Is button pressed?
     */
    public boolean pressed = false;
    /**
     * ON/OFF button, or normal?
     */
    public boolean pressedSwitch;
    /**
     * Is button focus?
     */
    public boolean focus = false;

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
     * Button, which is special for Menu. Only singletouch, no multitouch.
     * Responds on press:
     * 1) On pressed: if(pressed) and after you must (in if): pressed = false;
     * 2) On pressedSwitch - work as switch on/off: if(pressedSwitch)
     * Responds on focus: if(focus)
     *
     * @param pressedSwitch Initial state for pressedSwitch (ON/OFF). (if you don't use pressedSwitch is it freely.)
     */
    public ButtonMenu(boolean pressedSwitch) {
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
     * Only for ACTION_DOWN, no ACTION_POINTER_DOWN.
     */
    public void action_down(short x, short y) {
        if (!active && x >= triggerUpperLeftCorner[0] && x <= triggerLowerRightCorner[0] && y >= triggerUpperLeftCorner[1] && y <= triggerLowerRightCorner[1]) {
            focus = true;
            active = true;
            if (Logs.Logs)
                Log.d(Logs.tagInput, "ButtonMenu is focus.");
        }
    }

    /**
     * Function for call in action move.
     *
     * @param x X position.
     * @param y Y position.
     */
    public void action_move(short x, short y) {
        if (x >= triggerUpperLeftCorner[0] && x <= triggerLowerRightCorner[0] && y >= triggerUpperLeftCorner[1] && y <= triggerLowerRightCorner[1]) {
            focus = true;
            if (Logs.Logs)
                Log.d(Logs.tagInput, "ButtonMenu is focus.");
        } else {
            focus = false;
            if (Logs.Logs)
                Log.d(Logs.tagInput, "ButtonMenu is unfocus.");
        }
    }

    /**
     * Function for call in action up.
     */
    public void action_up() {
        if (focus) {
            pressed = true;
            focus = false;

            if (Logs.Logs)
                Log.d(Logs.tagInput, "ButtonMenu is pressed.");

            pressedSwitch = !pressedSwitch;
        }
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

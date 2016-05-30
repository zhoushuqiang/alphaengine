package com.stety.alphaengine.engine.input;

import android.util.Log;

import com.stety.alphaengine.engine.logger.Logs;

/**
 * Button, which responds on action down and only in trigger.
 * Responds on press:
 * 1) On pressed: if(pressed) and after you must (in if): pressed = false;
 * 2) On pressedSwitch - work as switch ON/OFF: if(pressedSwitch)
 * Responds on focus: if(focus)
 */
public class ButtonDownIn extends ButtonDownOut {
    /**
     * Button, which responds on action down and only in trigger.
     * Responds on press:
     * 1) On pressed: if(pressed) and after you must (in if): pressed = false;
     * 2) On pressedSwitch - work as switch on/off: if(pressedSwitch)
     * Responds on focus: if(focus)
     *
     * @param pressedSwitch Initial state for pressedSwitch (ON/OFF). (if you don't use pressedSwitch is it freely.)
     * @param only          If is button pressed can be press ex. touchpad...?
     */
    public ButtonDownIn(boolean pressedSwitch, boolean only) {
        super(pressedSwitch, only);
    }

    /**
     * Button, which responds on action down and only in trigger.
     * Responds on press:
     * 1) On pressed: if(pressed) and after you must (in if): pressed = false;
     * 2) On pressedSwitch - work as switch on/off: if(pressedSwitch)
     * Responds on focus: if(focus)
     *
     * @param pressedSwitch Initial state for pressedSwitch (ON/OFF). (if you don't use pressedSwitch is it freely.)
     */
    public ButtonDownIn(boolean pressedSwitch) {
        super(pressedSwitch);
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
            pressed = true;
            if (Logs.Logs)
                Log.d(Logs.tagInput, "ButtonDownIn is focus.");
        } else {
            focus = false;
            pressed = false;
            if (Logs.Logs)
                Log.d(Logs.tagInput, "ButtonDownIn is unfocus.");
        }
    }
}
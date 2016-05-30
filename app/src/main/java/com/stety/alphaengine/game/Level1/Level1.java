package com.stety.alphaengine.game.Level1;

import android.opengl.GLES20;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import com.stety.alphaengine.engine.android.Renderer;
import com.stety.alphaengine.engine.cameras.PerspectiveCamera;
import com.stety.alphaengine.engine.geometry.Axis;
import com.stety.alphaengine.engine.geometry.Point3D;
import com.stety.alphaengine.engine.geometry.Transform;
import com.stety.alphaengine.engine.helpers.AlphaHelper;
import com.stety.alphaengine.engine.helpers.MatrixHelper;
import com.stety.alphaengine.engine.input.ButtonDownIn;
import com.stety.alphaengine.engine.input.ButtonUpIn;
import com.stety.alphaengine.engine.input.JoystickFloat;
import com.stety.alphaengine.engine.input.Touchpad;
import com.stety.alphaengine.engine.logger.Logs;
import com.stety.alphaengine.engine.objects.Draw3D;
import com.stety.alphaengine.engine.objects.Scene;
import com.stety.alphaengine.engine.objects.Skybox;
import com.stety.alphaengine.engine.textures.Texture2Din3DAlpha;
import com.stety.alphaengine.game.programs.ptProgram;

/**
 * Level 1 (scene).
 */
public class Level1 extends Scene {
    VelocityTracker velocity = VelocityTracker.obtain();
    final int drawObjectsAll = 13;
    final int drawObjectsTextures = 13;
    final Draw3D Draw3D = new Draw3D(drawObjectsAll, drawObjectsTextures);

    final Skybox skybox = new Skybox();

    final PerspectiveCamera camFPS = new PerspectiveCamera(0.0f, 0.0f, -9.0f, 0.0f, 0.0f, 0.0f);

    final ButtonDownIn btnD = new ButtonDownIn(false);
    final ButtonUpIn btnU = new ButtonUpIn(true, true);
    final Touchpad touchpad = new Touchpad();

    final JoystickFloat joystick = new JoystickFloat(true);

    /**
     * For 2 touches in one time.
     */
    boolean wasActionDown = false;

    float yRotation = 0;
    float xRotation = 0;

    Texture2Din3DAlpha textureLinux = new Texture2Din3DAlpha(1, 0, Axis.axis.z, new Point3D(2, 0, -3));

    public static void load() {
        Renderer.scene = new Level1();
        Renderer.scene.created();
        Renderer.scene.unlock();
        System.gc(); //For clean unnecessary memory.
        Renderer.scene.changed();
    }

    @Override
    public void created() {
        Draw3D.initialization();
        skybox.initialization();

        btnD.texture.initialization();
        btnU.texture.initialization();

        textureLinux.initialization();
    }

    @Override
    public void unlock() {
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        Draw3D.load3GLmodelPT("LPSWRD.3gl", "LPSWRD", "lpswrd", GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_LINEAR);
        Draw3D.load3GLmodelPT("house.3gl", "house", "house", GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_LINEAR);
        Draw3D.load3GLmodelPT("grassPlane.3gl", "grass", "grass", GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_LINEAR);
        Draw3D.load3GLmodelPT("barel.3gl", "barel", "barel", GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_LINEAR);
        Draw3D.load3GLmodelPT("bench1.3gl", "bench1", "bench1", GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_LINEAR);
        Draw3D.load3GLmodelPT("bench2.3gl", "bench2", "bench2", GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_LINEAR);
        Draw3D.load3GLmodelPT("bench3.3gl", "bench3", "bench3", GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_LINEAR);
        Draw3D.load3GLmodelPT("bench4.3gl", "bench4", "bench4", GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_LINEAR);
        Draw3D.load3GLmodelPT("beretta92.3gl", "beretta92", "beretta92", GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_LINEAR);
        Draw3D.load3GLmodelPT("RPG.3gl", "RPG", "rpg", GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_LINEAR);
        Draw3D.load3GLmodelPT("column.3gl", "column", "column", GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_LINEAR);
        Draw3D.load3GLmodelPT("zuleyka.3gl", "zuleyka", "zuleyka", GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_LINEAR);
        Draw3D.load3GLmodelPT("CreatedBy.3gl", "createdBy", "createdbyandauthors", GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_LINEAR);

        skybox.load(new String[]{"skyboxleft", "skyboxright", "skyboxbottom", "skyboxtop", "skyboxfront", "skyboxback"});

        textureLinux.load("linux", GLES20.GL_LINEAR_MIPMAP_LINEAR, GLES20.GL_LINEAR);

    }

    @Override
    public void changed() {
        camFPS.setProjection(60, MatrixHelper.ratio, 1, 1000);

        btnD.texture.loadNoMR("jump", "jump_press", GLES20.GL_LINEAR, GLES20.GL_LINEAR, new float[]{17000, 16000, 17000, 16000, 17000, 16000, 17000, 16000}, new float[]{20000, 20000, 20000, 20000, 20000, 20000, 20000, 20000});
        btnU.texture.loadNoMR("down", "down_press", GLES20.GL_LINEAR, GLES20.GL_LINEAR, new float[]{0, 16000, 0, 16000, 0, 16000, 0, 16000}, new float[]{3000, 20000, 3000, 20000, 3000, 20000, 3000, 20000});

        btnD.setTrigger(new short[]{17000, 16000, 17000, 16000, 17000, 16000, 17000, 16000}, new short[]{20000, 20000, 20000, 20000, 20000, 20000, 20000, 20000});
        btnU.setTrigger(new short[]{0, 16000, 0, 16000, 0, 16000, 0, 16000}, new short[]{3000, 20000, 3000, 20000, 3000, 20000, 3000, 20000});

        joystick.setTrigger(new short[]{0, 0, 0, 0, 0, 0, 0, 0}, new short[]{8000, 20000, 8000, 20000, 8000, 20000, 8000, 20000});

        joystick.backgroundTexture.loadNoMR("joystick-back", GLES20.GL_LINEAR, GLES20.GL_LINEAR, new float[]{0, 0, 0, 0, 0, 0, 0, 0}, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        joystick.stickTexture.loadNoMR("joystick-stick", GLES20.GL_LINEAR, GLES20.GL_LINEAR, new float[]{0, 0, 0, 0, 0, 0, 0, 0}, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        joystick.setWidthAndHeightStick(new short[]{1250, 1250, 1250, 1250}, new short[]{2222, 2222, 2222, 2222});
        joystick.setWidthAndHeightBack(new short[]{3500, 3500, 3500, 3500}, new short[]{6222, 6222, 6222, 6222});
        joystick.setOFFSET((short) 250);
        joystick.setMIN_DISTANCE((short) 200);

        touchpad.setTrigger(new short[]{8000, 0, 8000, 0, 8000, 0, 8000, 0}, new short[]{20000, 20000, 20000, 20000, 20000, 20000, 20000, 20000});
    }

    @Override
    public void draw3D() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        camFPS.update();

        skybox.draw();

        ptProgram.use();

        if (btnU.pressedSwitch) {
            Transform.identity();
            Transform.translate(-4.0f, -3.5f, -3.0f);
            Transform.scale(4f, 2f, 4f);
            Draw3D.draw("grass");
        }

        Transform.identity();
        Transform.translate(0.0f, -1.0f, -5.0f);
        Transform.scale(0.6f, 0.4f, 0.6f);
        Draw3D.draw("LPSWRD");

        if (btnD.pressed) {
            Transform.identity();
            Transform.translate(0f, -1.0f, 5.0f);
            Transform.scale(0.2f, 0.2f, 0.2f);
            Draw3D.draw("house");
        }

        Transform.identity();
        Transform.translate(0f, -1.5f, 13.0f);
        Transform.scale(0.7f, 0.7f, 0.7f);
        Draw3D.draw("barel");

        Transform.identity();
        Transform.translate(-4f, -1.2f, 6.0f);
        Transform.rotate(90f, Axis.axis.y);
        Draw3D.draw("bench1");

        Transform.identity();
        Transform.translate(4f, -1.2f, 6.0f);
        Transform.rotate(270f, Axis.axis.y);
        Draw3D.draw("bench2");

        Transform.identity();
        Transform.translate(3f, -1.2f, 14.0f);
        Draw3D.draw("bench3");

        Transform.identity();
        Transform.translate(12f, -1.2f, 14.0f);
        Draw3D.draw("bench4");

        Transform.identity();
        Transform.translate(12f, -0.5f, 8.0f);
        Transform.scale(0.7f, 0.7f, 0.7f);
        Draw3D.draw("beretta92");

        Transform.identity();
        Transform.translate(12f, -0.5f, 4.0f);
        Transform.scale(0.7f, 0.7f, 0.7f);
        Draw3D.draw("RPG");

        Transform.identity();
        Transform.translate(8f, -1.2f, -5.0f);
        Transform.scale(0.4f, 0.4f, 0.4f);
        Draw3D.draw("zuleyka");

        Transform.identity();
        Transform.translate(21.5f, 3f, -9.0f);
        Transform.rotate(90, Axis.axis.x);
        Transform.rotate(90, Axis.axis.z);
        Draw3D.draw("createdBy");

        Transform.identity();
        Transform.translate(12f, -1.2f, -5.0f);
        Transform.scale(0.7f, 0.7f, 0.7f);
        Draw3D.draw("column");

        AlphaHelper.EnableAlphaIn3D();
        {
            textureLinux.draw();
        }
        AlphaHelper.DisableAlphaIn3D();
    }


    @Override
    public void draw2D() {
        AlphaHelper.EnableAlphaIn2D();
        {
            joystick.draw();

            btnD.draw();
            btnU.draw();
        }
        AlphaHelper.DisableAlphaIn2D();
    }

    @Override
    public void update() {

        if (touchpad.active && touchpad.change) {
            //Only testing, propably delete
            /*velocity.computeCurrentVelocity(1000);
            float x_velocity = velocity.getXVelocity();
            float y_velocity = velocity.getYVelocity();
            Log.d("velocity", "velocity x: " + x_velocity + " y: " + y_velocity);

           /* if(x_velocity < 0)
                x_velocity = - x_velocity;
            if(y_velocity < 0)
                y_velocity = - y_velocity;*/

            //This code to characterController.
            /*xRotation += touchpad.getDeltaX() / 125;//200f + (x_velocity * 0.001); //125
            yRotation += touchpad.getDeltaY() / 220;//400f + (y_velocity * 0.001); //220

            if (yRotation < -90) {
                yRotation = -90;
            } else if (yRotation > 90) {
                yRotation = 90;
            }

            camFPS.yRotation = xRotation;
            camFPS.xRotation = -yRotation;*/

            touchpad.change = false;
        }

        if (joystick.active) {
            short joystickDistance = joystick.getDistanceDPI();
            if (joystickDistance > 2000)
                joystickDistance = 2000;

            //This code to characterController.
            switch (joystick.getDirection4()) {
                case 0:
                default:
                    break;
                case 1:
                    camFPS.zPosition += (float) joystickDistance / 20000f;
                    break;
                case 2:
                    camFPS.zPosition -= (float) joystickDistance / 20000f;
                    break;
                case 3:
                    camFPS.xPosition -= (float) joystickDistance / 20000f;
                    break;
                case 4:
                    camFPS.xPosition += (float) joystickDistance / 20000f;
                    break;
            }
        }

        Draw3D.rotate += 1;
        textureLinux.rotation += 1;
        if (textureLinux.rotation >= 360)
            textureLinux.rotation = 0;
        textureLinux.scale += 0.001;
        if (textureLinux.scale >= 3)
            textureLinux.scale = 1;
    }

    @Override
    public void cleanUp() {
        Draw3D.delete();
        MatrixHelper.resetMatrix();
        skybox.delete();
        btnD.texture.delete();
        btnU.texture.delete();
        textureLinux.delete();
        joystick.backgroundTexture.delete();
        joystick.stickTexture.delete();
    }

    @Override
    public void pause() {
        Draw3D.pause();
    }

    @Override
    public void input() {
        MotionEvent event = Renderer.event;
        final short action = (short) event.getAction();

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                short pointerDownID = (short) event.getPointerId(event.getActionIndex());
                short x = (short) event.getX();
                short y = (short) event.getY();

                if (Logs.Logs)
                    Log.d(Logs.tagInput, "ACTION_DOWN, ID: " + pointerDownID + " X: " + x + " Y: " + y);

                if (!btnD.action_down(x, y, pointerDownID) && !btnU.action_down(x, y, pointerDownID) && !joystick.action_down(x, y, pointerDownID)) {
                    touchpad.action_down(x, y, pointerDownID);
                }
                wasActionDown = true;
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                short pointerDownID = (short) event.getPointerId(event.getActionIndex());
                short pointerCount = (short) event.getPointerCount();
                short index = (short) event.getActionIndex();

                if (index >= 0 && index <= (pointerCount - 1)) {
                    short x = (short) event.getX(event.getActionIndex());
                    short y = (short) event.getY(event.getActionIndex());

                    if (!wasActionDown) //For 2 touches in one time.
                    {
                        short firstPointerDownID = (short) event.getPointerId(0); //first finger
                        short xDownFirst = (short) event.getX();
                        short yDownFirst = (short) event.getY();

                        if (Logs.Logs)
                            Log.d(Logs.tagInput, "ACTION_DOWN in ACTION_POINTER_DOWN (2 touches in one time)" + ", X: " + xDownFirst + " Y: " + yDownFirst);

                        if (!btnD.action_down(xDownFirst, yDownFirst, firstPointerDownID) && !btnU.action_down(xDownFirst, yDownFirst, firstPointerDownID) && !joystick.action_down(xDownFirst, yDownFirst, firstPointerDownID)) {
                            touchpad.action_down(xDownFirst, yDownFirst, firstPointerDownID);
                        }
                        wasActionDown = true;
                    }

                    if (Logs.Logs)
                        Log.d(Logs.tagInput, "ACTION_POINTER_DOWN, ID: " + pointerDownID + ", All count: " + pointerCount + ", X: " + x + " Y: " + y);

                    if (!btnD.action_down(x, y, pointerDownID) && !btnU.action_down(x, y, pointerDownID) && !joystick.action_down(x, y, pointerDownID)) {
                        touchpad.action_down(x, y, pointerDownID);
                    }
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                short pointerUpID = (short) event.getPointerId(event.getActionIndex());

                if (Logs.Logs)
                    Log.d(Logs.tagInput, "ACTION_UP, ID: " + pointerUpID);

                btnD.action_up();
                btnU.action_up();
                touchpad.action_up();
                joystick.action_up();

                wasActionDown = false;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                short pointerUpID = (short) event.getPointerId(event.getActionIndex());

                if (Logs.Logs) {
                    int pointerCount = event.getPointerCount() - 1;
                    Log.d("multitouch", "ACTION_POINTER_UP, ID: " + pointerUpID + ", All count: " + pointerCount);
                }

                btnD.action_pointer_up(pointerUpID);
                btnU.action_pointer_up(pointerUpID);
                touchpad.action_pointer_up(pointerUpID);
                joystick.action_pointer_up(pointerUpID);

                break;
            }

            case MotionEvent.ACTION_MOVE: {
                short pointerCount = (short) event.getPointerCount();

                velocity.addMovement(event);

                if (btnU.active) {
                    short index = (short) event.findPointerIndex(btnU.pressPointer);
                    if (index >= 0 && index <= (pointerCount - 1)) {
                        btnU.action_move((short) event.getX(index), (short) event.getY(index));
                    }
                }

                if (btnD.active) {
                    short index = (short) event.findPointerIndex(btnD.pressPointer);
                    if (index >= 0 && index <= (pointerCount - 1)) {
                        btnD.action_move((short) event.getX(index), (short) event.getY(index));
                    }
                }

                if (touchpad.active) {
                    short index = (short) event.findPointerIndex(touchpad.pressPointer);
                    if (index >= 0 && index <= (pointerCount - 1)) {
                        touchpad.action_move((short) event.getX(index), (short) event.getY(index));

                        velocity.computeCurrentVelocity(1000);
                        float x_velocity = velocity.getXVelocity();
                        float y_velocity = velocity.getYVelocity();
                        Log.d("velocity", "velocity x: " + x_velocity + " y: " + y_velocity);

                        /* if(x_velocity < 0)
                            x_velocity = - x_velocity;
                        if(y_velocity < 0)
                            y_velocity = - y_velocity;*/

                        //This code to characterController.
                        xRotation += touchpad.getDeltaX() / 20;//200f + (x_velocity * 0.001); //125
                        yRotation += touchpad.getDeltaY() / 20;//400f + (y_velocity * 0.001); //220

                        if (yRotation < -90) {
                            yRotation = -90;
                        } else if (yRotation > 90) {
                            yRotation = 90;
                        }

                        camFPS.yRotation = xRotation;
                        camFPS.xRotation = -yRotation;
                    }
                }

                if (joystick.active) {
                    short index = (short) event.findPointerIndex(joystick.pressPointer);
                    if (index >= 0 && index <= (pointerCount - 1)) {
                        joystick.action_move((short) event.getX(index), (short) event.getY(index));
                    }
                }

                break;
            }
        }
    }
}

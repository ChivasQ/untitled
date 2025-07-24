package com.ferralith.engine.inputs;

import com.ferralith.engine.Window;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, xOld, yOld;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean Dragging;

    private MouseListener() {
        this.scrollX = 0.0f;
        this.scrollY = 0.0f;
        this.xPos = 0.0f;
        this.yPos = 0.0f;
        this.xOld = 0.0f;
        this.yOld = 0.0f;
    }

    public static MouseListener get() {
        if (instance == null) {
            instance = new MouseListener();
        }
        return instance;
    }

    public static void mousePosCallback(long window, double x, double y) {
        get().xOld = get().xPos;
        get().yOld = get().yPos;

        get().xPos = x;
        get().yPos = y;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button < get().mouseButtonPressed.length) {
            if (action == GLFW_PRESS) {
                get().mouseButtonPressed[button] = true;
            } else if (action == GLFW_RELEASE) {
                get().mouseButtonPressed[button] = false;
                get().Dragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long windows, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;

        get().xOld = get().xPos;
        get().yOld = get().yPos;
    }

    public static float getX() {
        return (float)get().xPos;
    }

    public static float getY() {
        return (float)get().yPos;
    }

    public static float getOrthoX() {
        float currentX = getX();
        currentX = (currentX / (float) Window.getWidth()) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);

        tmp.mul(Window.getScene().getCamera().getInverseProjection())
                .mul(Window.getScene().getCamera().getInverseView());
        currentX = tmp.x;

        return currentX;
    }

    public static float getOrthoY() {
        float currentY = getY();
        currentY = (currentY / (float) Window.getHeight()) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(0, currentY, 0, 1);

        tmp.mul(Window.getScene().getCamera().getInverseProjection())
                .mul(Window.getScene().getCamera().getInverseView());
        currentY = tmp.y;

        return currentY;
    }

    public static float getDx() {
        return (float)(get().xOld - get().xPos);
    }

    public static float getDy() {
        return (float)(get().yOld - get().yPos);
    }

    public static float getScrollX() {
        return (float) get().scrollX;
    }

    public static float getScrollY() {
        return (float) get().scrollY;
    }

    public static boolean isDragging() {
        return get().Dragging;
    }

    public static boolean mouseButtonDown(int button) {
        return get().mouseButtonPressed[button];
    }
}

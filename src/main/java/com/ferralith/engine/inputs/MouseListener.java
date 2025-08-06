package com.ferralith.engine.inputs;

import com.ferralith.engine.Camera;
import com.ferralith.engine.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, xOld, yOld;
    private double worldX, worldY, oldWorldX, oldWorldY;
    private boolean mouseButtonPressed[] = new boolean[9];
    private boolean Dragging;

    private int mouseButtonDown = 0;

    private Vector2f gameViewportPos = new Vector2f();
    private Vector2f gameViewportSize = new Vector2f();

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
        if (get().mouseButtonDown > 0) {
            get().Dragging = true;
        }

        get().xOld = get().xPos;
        get().yOld = get().yPos;

        get().oldWorldX = get().worldX;
        get().oldWorldY = get().worldY;

        get().xPos = x;
        get().yPos = y;

        calcWorldCoords();


    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button < get().mouseButtonPressed.length) {
            if (action == GLFW_PRESS) {
                get().mouseButtonDown++;
                get().mouseButtonPressed[button] = true;
            } else if (action == GLFW_RELEASE) {
                get().mouseButtonDown--;
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

        get().oldWorldX = get().worldX;
        get().oldWorldY = get().worldY;
    }

    public static float getX() {
        return (float)get().xPos;
    }

    public static float getY() {
        return (float)get().yPos;
    }



    public static float getDx() {
        return (float)(get().xOld - get().xPos);
    }

    public static float getDy() {
        return (float)(get().yOld - get().yPos);
    }

    public static float getWorldDx() {
        return (float)(get().oldWorldX - get().worldX);
    }

    public static float getWorldDy() {
        return (float)(get().oldWorldY - get().worldY);
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

    public static float getOrthoX() {
        return (float) get().worldX;
    }

    private static void calcWorldCoords() {
        float currentX = (float)get().xPos - get().gameViewportPos.x;
        float currentY = (float)get().yPos - get().gameViewportPos.y;

        float ndcX = (currentX / get().gameViewportSize.x) * 2.0f - 1.0f;
        float ndcY = -((currentY / get().gameViewportSize.y) * 2.0f - 1.0f);

        Vector4f tmp = new Vector4f(ndcX, ndcY, 0, 1);

        Camera camera = Window.getScene().getCamera();
        Matrix4f viewProjection = new Matrix4f();

        camera.getInverseView().mul(camera.getInverseProjection(), viewProjection);
        tmp.mul(viewProjection);

        get().worldX = tmp.x;
        get().worldY = tmp.y;
    }

    public static float getOrthoY() {
        return (float) get().worldY;
    }


    public static float getScreenX() {
        float currentX = getX() - get().gameViewportPos.x;
        currentX = (currentX / get().gameViewportSize.x) * Window.getWidth();
        return currentX;
    }
    public static float getScreenY() {
        float currentY = getY() - get().gameViewportPos.y;
        currentY = Window.getHeight() - ((currentY / get().gameViewportSize.y) * Window.getHeight());
        return currentY;
    }

    public void setGameViewportPos(Vector2f gameViewportPos) {
        this.gameViewportPos.set(gameViewportPos);
    }

    public void setGameViewportSize(Vector2f gameViewportSize) {
        this.gameViewportSize.set(gameViewportSize);
    }

    public static double getxOld() {
        return get().xOld;
    }

    public static double getyOld() {
        return get().yOld;
    }

    public static double getOldWorldX() {
        return get().oldWorldX;
    }

    public static double getOldWorldY() {
        return get().oldWorldY;
    }
}

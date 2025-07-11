package com.ferralith.engine;

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
}

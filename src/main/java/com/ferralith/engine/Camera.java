package com.ferralith.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix, inverseProjection, inverseView;
    public Vector2f position;
    private Vector2f projectionSize = new Vector2f(Window.getWidth(), Window.getHeight());

    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjective();
    }

    public void adjustProjective() {
        adjustProjectionSize();
        projectionMatrix.identity();
        projectionMatrix.ortho( 0.0f,
                                projectionSize.x,
                                0.0f,
                                projectionSize.y,
                                0.0f,
                                100.0f);
        projectionMatrix.invert(inverseProjection);
        //projectionMatrix.ortho2D(0, Window.getWidth(), 0,  Window.getHeight());
    }

    private void adjustProjectionSize() {
        projectionSize = new Vector2f(Window.getWidth(), Window.getHeight());
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        this.viewMatrix.lookAt( new Vector3f(position.x, position.y, 20.0f),
                                cameraFront.add(position.x, position.y, 0.0f),
                                cameraUp);
        this.viewMatrix.invert(inverseView);

        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public Matrix4f getInverseProjection() {
        return inverseProjection;
    }

    public Matrix4f getInverseView() {
        return inverseView;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getProjectionSize() {
        return projectionSize;
    }
}

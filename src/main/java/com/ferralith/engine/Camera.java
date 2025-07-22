package com.ferralith.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix;
    public Vector2f position;

    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjective();
    }

    public void adjustProjective() {
        projectionMatrix.identity();
        projectionMatrix.ortho( 0.0f,
                                Window.getWidth(),
                                0.0f,
                                Window.getHeight(),
                                0.0f,
                                100.0f);
        //projectionMatrix.ortho2D(0, Window.getWidth(), 0,  Window.getHeight());
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        this.viewMatrix.lookAt( new Vector3f(position.x, position.y, 20.0f),
                                cameraFront.add(position.x, position.y, 0.0f),
                                cameraUp);
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }
}

package com.ferralith.engine.scenes.components;

import com.ferralith.engine.Camera;
import com.ferralith.engine.Component;
import com.ferralith.engine.inputs.MouseListener;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

public class EditorCameraMovement extends Component {
    private final Camera levelEditorCamera;
    private Vector2f clickOrigin;
    private static final float SENSITIVITY = 30.0f;

    private float dragDebounce = 0.01f;

    public EditorCameraMovement(Camera camera) {
        this.levelEditorCamera = camera;
        this.clickOrigin = new Vector2f();
    }


    @Override
    public void update(float dt) {
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE) && dragDebounce > 0) {
            this.clickOrigin = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            dragDebounce -= dt;
            return;
        } else if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
            Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            Vector2f delta = new Vector2f(mousePos).sub(this.clickOrigin);

            levelEditorCamera.position.sub(delta.mul(dt).mul(SENSITIVITY));

            this.clickOrigin.lerp(new Vector2f(mousePos), dt);
        }

        if (dragDebounce <= 0.0f && !MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
            dragDebounce = 0.01f;
        }
    }
}

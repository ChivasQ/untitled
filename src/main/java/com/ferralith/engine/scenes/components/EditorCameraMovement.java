package com.ferralith.engine.scenes.components;

import com.ferralith.engine.Camera;
import com.ferralith.engine.Component;
import com.ferralith.engine.inputs.KeyListener;
import com.ferralith.engine.inputs.MouseListener;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class EditorCameraMovement extends Component {
    private static final float SCROLL_SENSETIVITY = 0.1f;
    private final Camera levelEditorCamera;
    private Vector2f clickOrigin;
    private static final float SENSITIVITY = 30.0f;
    private boolean reset = false;
    private float lerpTime = 0.0f;

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

        if (MouseListener.getScrollY() != 0.0) {
            float addValue = (float)Math.pow(Math.abs(MouseListener.getScrollY() * SCROLL_SENSETIVITY),
                    1 / levelEditorCamera.getZoom());
            addValue *= -Math.signum(MouseListener.getScrollY());


            Vector2f oldCenter = levelEditorCamera.getCameraCenter();

            levelEditorCamera.addZoom(addValue);

            Vector2f newCenter = levelEditorCamera.getCameraCenter();

            levelEditorCamera.position.add(oldCenter.sub(newCenter));
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_KP_DECIMAL)) {
            reset = true;
        }

        if (reset) {
            levelEditorCamera.position.lerp(new Vector2f(), lerpTime);
            levelEditorCamera.setZoom(this.levelEditorCamera.getZoom() +
                    ((1.0f - levelEditorCamera.getZoom()) * lerpTime));
            this.lerpTime += 0.1f * dt;

            if (Math.abs(levelEditorCamera.position.x) <= 3.0f &&
                    Math.abs(levelEditorCamera.position.y) <= 3.0f) {
                this.lerpTime = 0.0f;
                levelEditorCamera.position.set(0,0);
                levelEditorCamera.setZoom(1);
                reset = false;
            }
        }
    }
}

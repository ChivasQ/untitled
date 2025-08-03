package com.ferralith.engine.scenes.components;

import com.ferralith.engine.Component;
import com.ferralith.engine.GameObject;
import com.ferralith.engine.Window;
import com.ferralith.engine.inputs.MouseListener;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {
    GameObject holdingObject = null;
    private static final int GRID_WIDTH = 32;
    private static final int GRID_HEIGHT = 32;

    public void pickupObject(GameObject go) {
        this.holdingObject = go;
        Window.getScene().addGameObject(go);
    }

    public void place() {
        this.holdingObject = null;
    }



    @Override
    public void update(float dt) {
        if (this.holdingObject != null)
        {
            holdingObject.transform.position.x = MouseListener.getOrthoX();
            holdingObject.transform.position.y = MouseListener.getOrthoY();

            if (holdingObject.transform.position.x >= 0.0f) {
                holdingObject.transform.position.x = (int) (holdingObject.transform.position.x / GRID_WIDTH) * GRID_WIDTH;
            } else {
                holdingObject.transform.position.x = (int) (holdingObject.transform.position.x / GRID_WIDTH) * GRID_WIDTH - GRID_WIDTH;
            }
            if (holdingObject.transform.position.y >= 0.0f) {
                holdingObject.transform.position.y = (int) (holdingObject.transform.position.y / GRID_HEIGHT) * GRID_HEIGHT;
            } else {
                holdingObject.transform.position.y = (int) (holdingObject.transform.position.y / GRID_HEIGHT) * GRID_HEIGHT - GRID_HEIGHT;
            }

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }
}

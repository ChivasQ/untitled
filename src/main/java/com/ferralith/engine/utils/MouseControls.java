package com.ferralith.engine.utils;

import com.ferralith.engine.Component;
import com.ferralith.engine.GameObject;
import com.ferralith.engine.Window;
import com.ferralith.engine.inputs.MouseListener;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {
    GameObject holdingObject = null;

    public void pickupObject(GameObject go) {
        this.holdingObject = go;
        Window.getScene().addGameObject(go);
    }

    public void place() {
        this.holdingObject = null;
    }



    @Override
    public void update(float dt) {
        if (holdingObject != null) {
            int gridsize = 100;

            float orthoX = MouseListener.getOrthoX();
            float orthoY = MouseListener.getOrthoY();
            holdingObject.transform.position.x = (orthoX - (orthoX % gridsize)) - 16;
            holdingObject.transform.position.y = (orthoY - (orthoY % gridsize)) - 16;

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }
}

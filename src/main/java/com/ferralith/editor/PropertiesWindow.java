package com.ferralith.editor;

import com.ferralith.engine.GameObject;
import com.ferralith.engine.Scene;
import com.ferralith.engine.inputs.MouseListener;
import com.ferralith.engine.renderer.PickingTexture;
import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {
    protected GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    public PropertiesWindow(PickingTexture texture) {
        this.pickingTexture = texture;
    }

    public void update(float dt, Scene currentCcene) {
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            int x = (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();
            activeGameObject = currentCcene.getGameObject(pickingTexture.readPixel(x, y));
        }

    }


    public void imgui() {
        ImGui.begin("Inspector");
        if (activeGameObject != null) {
            activeGameObject.imgui();
        }
        ImGui.end();
    }
}

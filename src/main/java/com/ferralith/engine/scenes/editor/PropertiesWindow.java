package com.ferralith.engine.scenes.editor;

import com.ferralith.engine.GameObject;
import com.ferralith.engine.Scene;
import com.ferralith.engine.inputs.MouseListener;
import com.ferralith.engine.renderer.PickingTexture;
import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {
    protected GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    private float debounce = 0.2f;

    public PropertiesWindow(PickingTexture texture) {
        this.pickingTexture = texture;
    }

    public void update(float dt, Scene currentCcene) {
        debounce -= dt;

        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {
            int x = (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();
            activeGameObject = currentCcene.getGameObject(pickingTexture.readPixel(x, y));
            this.debounce = 0.2f;
        }

    }


    public void imgui() {
        ImGui.begin("Inspector");
        if (activeGameObject != null) {
            activeGameObject.imgui();
        }
        ImGui.end();
    }

    public GameObject getActiveGameObject() {
        return activeGameObject;
    }
}

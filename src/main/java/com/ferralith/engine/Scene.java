package com.ferralith.engine;

import com.ferralith.engine.renderer.Renderer;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;

    public Scene() {
        
    }

    public void init() {

    }

    public void start() {
        if (!isRunning) {
            for (GameObject go : gameObjects) {
                go.start();
                this.renderer.add(go);
            }
            isRunning = true;
        }
    }

    public void addGameObject(GameObject go) {
        if (isRunning) {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        } else {
            gameObjects.add(go);
        }
    }
    public abstract void update(float dt);


    public Camera getCamera() {
        return this.camera;
    }

    public void sceneImgui() {
        if (activeGameObject != null) {
            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }

        imgui();
    }

    public void imgui() {

    }
}

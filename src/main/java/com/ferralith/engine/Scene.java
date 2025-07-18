package com.ferralith.engine;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene() {
        
    }

    public void init() {

    }

    public void start() {
        if (!isRunning) {
            for (GameObject go : gameObjects) {
                go.start();
            }
            isRunning = true;
        }
    }

    public void addGameObject(GameObject go) {
        if (isRunning) {
            gameObjects.add(go);
            go.start();
        } else {
            gameObjects.add(go);
        }
    }
    public abstract void update(float dt);
}

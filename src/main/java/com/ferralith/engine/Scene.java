package com.ferralith.engine;

import com.ferralith.engine.gson.ComponentDeserializer;
import com.ferralith.engine.gson.ComponentSerializer;
import com.ferralith.engine.gson.GameObjectDeserializer;
import com.ferralith.engine.renderer.Renderer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    private List<Component> sceneComponents = new ArrayList<>();
    public boolean loadedLevel = false;

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
    public void update(float dt) {
        updateComponents(dt);
    }

    public void startSceneComponents() {
        for (Component c :
                sceneComponents) {
            c.start();
        }
    }

    public void updateComponents(float dt) {
        for (Component c :
                sceneComponents) {
            c.update(dt);
        }
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : sceneComponents) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    assert false : "Error: Casting component.";
                }
            }
        }
        return null;
    }

    public abstract void render();

    public Camera getCamera() {
        return this.camera;
    }

    public void imgui() {

    }

    public void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(Component.class, new ComponentSerializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try {
            FileWriter fileWriter = new FileWriter("level.txt");
            List<GameObject> toSerialize = gameObjects.stream().filter(gameObject -> !gameObject.hasTag(Tags.doNotSerialize)).toList();
            fileWriter.write(gson.toJson(toSerialize));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(Component.class, new ComponentSerializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String inFile = "";

        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            int maxGameObjectId = -1;
            int maxComponentId = -1;

            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);

            for (int i = 0; i < objs.length; i++) {
                addGameObject(objs[i]);
                System.out.println(objs[i].name);

                for (Component c : objs[i].getAllComponents()) {
                    if (c.getUid() > maxComponentId) {
                        maxComponentId = c.getUid();
                    }
                }

                if (objs[i].getUid() > maxGameObjectId) {
                    maxGameObjectId = objs[i].getUid();
                }
            }

            maxComponentId++;
            maxGameObjectId++;

            GameObject.init(maxGameObjectId);
            Component.init(maxComponentId);
            this.loadedLevel = true;
        }
    }

    public GameObject getGameObject(int i) {
        Optional<GameObject> gameObject = gameObjects.stream().filter(go -> go.getUid() == i).findFirst();
        return gameObject.orElse(null);
    }

    public void addSceneComponent(Component component) {
        sceneComponents.add(component);
    }

    public void componentImgui() {
        for (Component c : sceneComponents) {
            c.imgui();
        }
    }
}

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

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;
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

    public void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(Component.class, new ComponentSerializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try {
            FileWriter fileWriter = new FileWriter("level.txt");
            fileWriter.write(gson.toJson(this.gameObjects));
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
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);

            for (int i = 0; i < objs.length; i++) {
                addGameObject(objs[i]);
                System.out.println(objs[i].name);
            }
            this.loadedLevel = true;
        }
    }
}

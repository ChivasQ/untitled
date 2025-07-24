package com.ferralith.engine.scenes;

import com.ferralith.engine.*;
import com.ferralith.engine.components.RigidBody;
import com.ferralith.engine.components.SpriteRenderer;
import com.ferralith.engine.components.SpriteSheet;
import com.ferralith.engine.gson.ComponentDeserializer;
import com.ferralith.engine.gson.ComponentSerializer;
import com.ferralith.engine.gson.GameObjectDeserializer;
import com.ferralith.engine.inputs.KeyListener;
import com.ferralith.engine.utils.AssetPool;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.awt.event.KeyEvent;

public class TestScene extends Scene {

    private GameObject object1;
    private SpriteSheet spriteSheet;

    public TestScene() {
        System.out.println("TEST SCENE");
    }

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(-100,100));

        System.out.println(this.loadedLevel);
        if (loadedLevel) {
            this.activeGameObject = gameObjects.get(0);
            return;
        }

        spriteSheet = AssetPool.getSpritesheet("spritesheets/cat1.png");

        object1 = new GameObject("obj51", new Transform(new Vector2f(0, 500), new Vector2f(256, 256)));
        object1.addComponent(new SpriteRenderer(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f)));
        object1.addComponent(new RigidBody());
        addGameObject(object1);
        this.activeGameObject = object1;

        GameObject obj1 = new GameObject("obj1", new Transform(new Vector2f(Window.getWidth() - 300, Window.getHeight()-100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(AssetPool.getTexture("fumo2.png")));
        addGameObject(obj1);

        GameObject obj2 = new GameObject("obj2", new Transform(new Vector2f(100, 100), new Vector2f(511, 511)));
        obj2.addComponent(new SpriteRenderer(AssetPool.getTexture("testImage.png")));
        addGameObject(obj2);

        GameObject obj3 = new GameObject("obj3", new Transform(new Vector2f(600, 100), new Vector2f(256, 256)));
        obj3.addComponent(new SpriteRenderer(AssetPool.getTexture("google.png")));
        addGameObject(obj3);

        GameObject obj4 = new GameObject("obj4", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj4.addComponent(new SpriteRenderer(spriteSheet.getSprite(0)));
        addGameObject(obj4);


    }

    private void loadResources() {
        AssetPool.getShader("default");
        AssetPool.addSpriteSheet("spritesheets/cat1.png",
                new SpriteSheet(AssetPool.getTexture("spritesheets/cat1.png"),
                        131, 240, 50, 0));
    }

    @Override
    public void update(float dt) {
        if (KeyListener.isKeyPressed(KeyEvent.VK_1)) {
            Window.changeScene(0);
        }

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }

    @Override
    public void imgui() {
        ImGui.begin("Test window");
        ImGui.text("HEAVEN PIERCE HER");
        ImGui.end();

        if (ImGui.button("Serialize!")) {
            //System.out.println(gson.toJson(object1));
        }
    }
}

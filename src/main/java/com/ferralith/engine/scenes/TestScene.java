package com.ferralith.engine.scenes;

import com.ferralith.engine.*;
import com.ferralith.engine.components.SpriteRenderer;
import com.ferralith.engine.inputs.KeyListener;
import com.ferralith.engine.utils.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.awt.event.KeyEvent;

public class TestScene extends Scene {
    private boolean initialized = false;

    public TestScene() {
        System.out.println("TEST SCENE");
    }

    @Override
    public void init() {
        if (initialized) return;
        initialized = true;
        this.camera = new Camera(new Vector2f(-250,0));

        GameObject obj1 = new GameObject("obj1", new Transform(new Vector2f(-300, 100), new Vector2f(512, 512)));
        obj1.addComponent(new SpriteRenderer(AssetPool.getTexture("fumo2.png")));
        addGameObject(obj1);

        GameObject obj2 = new GameObject("obj2", new Transform(new Vector2f(100, 100), new Vector2f(512, 512)));
        obj2.addComponent(new SpriteRenderer(AssetPool.getTexture("testImage.png")));
        addGameObject(obj2);

        GameObject obj3 = new GameObject("obj3", new Transform(new Vector2f(600, 100), new Vector2f(512, 512)));
        obj3.addComponent(new SpriteRenderer(AssetPool.getTexture("test_image.png")));
        addGameObject(obj3);

        loadResources();
    }

    private void loadResources() {
        AssetPool.getShader("default");
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
}

package com.ferralith.engine.scenes;

import com.ferralith.engine.*;
import com.ferralith.engine.components.SpriteRenderer;
import com.ferralith.engine.components.SpriteSheet;
import com.ferralith.engine.inputs.KeyListener;
import com.ferralith.engine.utils.AssetPool;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.awt.event.KeyEvent;

public class TestScene extends Scene {
    private boolean initialized = false;
    private GameObject object1;
    private SpriteSheet spriteSheet;
    public TestScene() {
        System.out.println("TEST SCENE");
    }

    @Override
    public void init() {
        if (initialized) return;
        initialized = true;

        loadResources();


        spriteSheet = AssetPool.getSpritesheet("spritesheets/cat1.png");

        this.camera = new Camera(new Vector2f(-100,100));

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

        object1 = new GameObject("obj5", new Transform(new Vector2f(0, 500), new Vector2f(256, 256)));
        object1.addComponent(new SpriteRenderer(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f)));
        addGameObject(object1);
        this.activeGameObject = object1;
    }

    private void loadResources() {
        AssetPool.getShader("default");
        AssetPool.addSpriteSheet("spritesheets/cat1.png",
                new SpriteSheet(AssetPool.getTexture("spritesheets/cat1.png"),
                        131, 240, 50, 0));
    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlitTimeLeft = 0.0f;

    @Override
    public void update(float dt) {
        if (KeyListener.isKeyPressed(KeyEvent.VK_1)) {
            Window.changeScene(0);
        }
//        spriteFlitTimeLeft -= dt;
//        if (spriteFlitTimeLeft <= 0) {
//            spriteFlitTimeLeft = spriteFlipTime;
//            spriteIndex++;
//            if (spriteIndex > 4) {
//                spriteIndex = 0;
//            }
//            //object1.getComponent(SpriteRenderer.class).setSprite(spriteSheet.getSprite(spriteIndex));
//        }
//
//        object1.transform.position.x += dt * 30;



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
    }
}

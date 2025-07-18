package com.ferralith.engine.scenes;

import com.ferralith.engine.*;
import com.ferralith.engine.components.SpriteRenderer;
import com.ferralith.engine.inputs.KeyListener;
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
        this.camera = new Camera(new Vector2f(-500,-59));

        int xOffset = 10;
        int yOffset = 10;
        float totalWidth = (float)(600 - xOffset * 2);
        float totalHeight = (float)(600 - yOffset * 2);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;

        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);
                GameObject go = new GameObject("Obj" + x + " " + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                go.addComponent(new SpriteRenderer(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1)));
                this.addGameObject(go);
            }
        }
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

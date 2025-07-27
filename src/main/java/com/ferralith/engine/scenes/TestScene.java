package com.ferralith.engine.scenes;

import com.ferralith.engine.*;
import com.ferralith.engine.components.RigidBody;
import com.ferralith.engine.components.Sprite;
import com.ferralith.engine.components.SpriteRenderer;
import com.ferralith.engine.components.SpriteSheet;
import com.ferralith.engine.gson.ComponentDeserializer;
import com.ferralith.engine.gson.ComponentSerializer;
import com.ferralith.engine.gson.GameObjectDeserializer;
import com.ferralith.engine.inputs.KeyListener;
import com.ferralith.engine.inputs.MouseListener;
import com.ferralith.engine.renderer.DebugDraw;
import com.ferralith.engine.utils.AssetPool;
import com.ferralith.engine.utils.GenObject;
import com.ferralith.engine.utils.MouseControls;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.event.KeyEvent;

public class TestScene extends Scene {

    private GameObject object1;
    private SpriteSheet spriteSheet;

    MouseControls mouseControls = new MouseControls();


    public TestScene() {
        System.out.println("TEST SCENE");
    }

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(-100,100));
        spriteSheet = AssetPool.getSpritesheet("spritesheets/cat1.png");
        DebugDraw.addLine2D(new Vector2f(0,0), new Vector2f(1000, 1000), new Vector3f(1,0,0));
        System.out.println(this.loadedLevel);
        if (loadedLevel) {
            this.activeGameObject = gameObjects.get(0);
            return;
        }



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

        // TODO: FIX THIS SHIT
        AssetPool.addSpriteSheet("spritesheets/cat1.png",
                new SpriteSheet(AssetPool.getTexture("spritesheets/cat1.png"),
                        131, 240, 50, 0));
    }

    @Override
    public void update(float dt) {
        if (KeyListener.isKeyPressed(KeyEvent.VK_1)) {
            Window.changeScene(0);
        }

        mouseControls.update(dt);

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }

    @Override
    public void imgui() {
        ImGui.begin("Test window");

        ImVec2 windowPos = ImGui.getWindowPos();
        ImVec2 windowSize = ImGui.getWindowSize();
        ImVec2 itemSpacing = ImGui.getStyle().getItemSpacing();

        float windowsX2 = windowPos.x + windowSize.x;

        for (int i = 0; i < spriteSheet.size(); i++) {
            Sprite sprite = spriteSheet.getSprite(i);
            float sWidth = sprite.getWidth();
            float sHeight = sprite.getHeight();
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTextureCoords();


            ImGui.pushID(i);
            if (ImGui.imageButton("hello" + id, id, sWidth, sHeight, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)) {
                System.out.println("click " + i);
                GameObject object = GenObject.generateSpriteObject(sprite, sWidth, sHeight);
                mouseControls.pickupObject(object);
            }
            ImGui.popID();

            ImVec2 lasButtonPos =  ImGui.getItemRectMax();

            float lastButtonX2 = lasButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + sWidth;

            if (i + 1 < spriteSheet.size() && nextButtonX2 < windowsX2) {
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
}

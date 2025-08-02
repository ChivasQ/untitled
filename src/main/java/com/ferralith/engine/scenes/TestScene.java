package com.ferralith.engine.scenes;

import com.ferralith.engine.*;
import com.ferralith.engine.components.*;
import com.ferralith.engine.inputs.KeyListener;
import com.ferralith.engine.renderer.DebugDraw;
import com.ferralith.engine.utils.AssetPool;
import com.ferralith.engine.utils.GenObject;
import com.ferralith.engine.components.MouseControls;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.event.KeyEvent;

public class TestScene extends Scene {
    private SpriteSheet spriteSheet;

    GameObject levelEditorMagicNumbers = new GameObject("levelEditorMagicNumbers");



    public TestScene() {
        System.out.println("TEST SCENE");
    }

    @Override
    public void init() {
        levelEditorMagicNumbers.addComponent(new MouseControls());
        levelEditorMagicNumbers.addComponent(new GridLines());

        loadResources();
        this.camera = new Camera(new Vector2f(-100,100));
        spriteSheet = AssetPool.getSpritesheet("spritesheets/cat1.png");
        DebugDraw.addLine2D(new Vector2f(0,0), new Vector2f(1000, 1000), new Vector3f(1,0,0));
        System.out.println(this.loadedLevel);

        if (loadedLevel) {
            return;
        }

    }

    private void loadResources() {
        AssetPool.getShader("default");

        // TODO: FIX THIS SHIT
        AssetPool.addSpriteSheet("spritesheets/cat1.png",
                new SpriteSheet(AssetPool.getTexture("spritesheets/cat1.png"),
                        131, 240, 50, 0));

        for (GameObject g : gameObjects) {
            if (g.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spriteRenderer = g.getComponent(SpriteRenderer.class);
                if (spriteRenderer.getTexture() != null) {
                    spriteRenderer.setTexture(AssetPool.getTexture(spriteRenderer.getTexture().getPath()));
                }
            }
        }
    }

    float counter = 1;
    @Override
    public void update(float dt) {
        if (KeyListener.isKeyPressed(KeyEvent.VK_1)) {
            Window.changeScene(0);
        }
        counter += dt * 200;
        levelEditorMagicNumbers.update(dt);

        DebugDraw.addBox2D(new Vector2f(544, 800), new Vector2f(128, 64), counter, new Vector3f(1,0,0), 1);
        DebugDraw.addLine2D(new Vector2f(544, 800), new Vector2f(544, 801), new Vector3f(1,1,1));

        //DebugDraw.addPolygon(new Vector2f(544, 800), 128, new Vector3f(1,0,0), 1, (int) counter);

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
    }

    @Override
    public void render() {
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
            if (ImGui.imageButton("hello" + id, id, sWidth, sHeight, texCoords[3].x, texCoords[3].y, texCoords[1].x, texCoords[1].y)) {
                System.out.println("click " + i);
                GameObject object = GenObject.generateSpriteObject(sprite, sWidth, sHeight);
                levelEditorMagicNumbers.getComponent(MouseControls.class).pickupObject(object);
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

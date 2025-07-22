package com.ferralith.engine.scenes;

import com.ferralith.engine.Camera;
import com.ferralith.engine.Scene;
import com.ferralith.engine.Window;
import com.ferralith.engine.inputs.KeyListener;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.IntBuffer;
import java.security.Key;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS;
import static org.lwjgl.opengl.GL20.GL_MAX_TEXTURE_IMAGE_UNITS;
import static org.lwjgl.opengl.GL42.GL_MAX_IMAGE_UNITS;

public class LevelScene extends Scene {
    public LevelScene() {
        System.out.println("LEVEL SCENE");
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(-0,-0));
    }

    @Override
    public void update(float dt) {
        IntBuffer a = BufferUtils.createIntBuffer(1);
        glGetIntegerv(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS, a);
        System.out.println(a.get(0));


        if (KeyListener.isKeyPressed(KeyEvent.VK_2)) {
            Window.changeScene(1);
        }
    }
}

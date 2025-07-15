package com.ferralith.engine.scenes;

import com.ferralith.engine.Scene;
import com.ferralith.engine.Window;
import com.ferralith.engine.inputs.KeyListener;

import java.awt.event.KeyEvent;

public class TestScene extends Scene {
    public TestScene() {
        System.out.println("TEST SCENE");
    }

    @Override
    public void update(float dt) {
        System.out.println((1.0 / dt) + "FPS");

        if (KeyListener.isKeyPressed(KeyEvent.VK_SPACE))
        {
            Window.get().r = 1.0f;
            Window.get().g = 0.0f;
            Window.get().b = 0.0f;
        } else {
            Window.get().r = 0.0f;
            Window.get().g = 0.0f;
            Window.get().b = 0.0f;
        }

        if (KeyListener.isKeyPressed(KeyEvent.VK_1)) {
            Window.changeScene(0);
        }
    }
}

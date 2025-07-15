package com.ferralith.engine.scenes;

import com.ferralith.engine.Scene;
import com.ferralith.engine.Window;
import com.ferralith.engine.inputs.KeyListener;

import java.awt.event.KeyEvent;
import java.security.Key;

public class LevelScene extends Scene {
    public LevelScene() {
        System.out.println("LEVEL SCENE");
    }

    @Override
    public void update(float dt) {
        if (KeyListener.isKeyPressed(KeyEvent.VK_SPACE))
        {
            Window.get().r = 1.0f;
            Window.get().g = 1.0f;
            Window.get().b = 0.0f;
        } else {
            Window.get().r = 0.0f;
            Window.get().g = 0.0f;
            Window.get().b = 0.0f;
        }

        if (KeyListener.isKeyPressed(KeyEvent.VK_2)) {
            Window.changeScene(1);
        }
    }
}

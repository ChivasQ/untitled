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




        if (KeyListener.isKeyPressed(KeyEvent.VK_2)) {
            Window.changeScene(1);
        }
    }
}

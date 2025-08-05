package com.ferralith.engine.scenes.components;

import com.ferralith.engine.Window;
import com.ferralith.engine.components.Sprite;
import com.ferralith.engine.inputs.MouseListener;
import com.ferralith.engine.scenes.editor.PropertiesWindow;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class TranslateGizmo extends Gizmo{
    public TranslateGizmo(Sprite arrowSprite, Sprite squareSprite, PropertiesWindow propertiesWindow) {
        super(arrowSprite, squareSprite, propertiesWindow);
    }

    @Override
    public void update(float dt) {
        if (activeGameObject != null) {
            if (xAxisActive) {
                activeGameObject.transform.position.x -= MouseListener.getWorldDx();
            } else if (yAxisActive) {
                activeGameObject.transform.position.y -= MouseListener.getWorldDy();
            } else if (xyAxisActive) {
                activeGameObject.transform.position.x -= MouseListener.getWorldDx();
                activeGameObject.transform.position.y -= MouseListener.getWorldDy();
            }
        }

        super.update(dt);
    }
}

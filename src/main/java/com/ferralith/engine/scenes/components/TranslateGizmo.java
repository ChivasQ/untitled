package com.ferralith.engine.scenes.components;

import com.ferralith.engine.Window;
import com.ferralith.engine.components.Sprite;
import com.ferralith.engine.inputs.MouseListener;
import com.ferralith.engine.scenes.editor.PropertiesWindow;
import imgui.ImGui;
import imgui.flag.ImGuiMouseCursor;
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
                ImGui.setMouseCursor(ImGuiMouseCursor.ResizeEW);
                activeGameObject.transform.position.x -= MouseListener.getWorldDx();
            } else if (yAxisActive) {
                ImGui.setMouseCursor(ImGuiMouseCursor.ResizeNS);
                activeGameObject.transform.position.y -= MouseListener.getWorldDy();
            } else if (xyAxisActive) {
                ImGui.setMouseCursor(ImGuiMouseCursor.ResizeAll);
                activeGameObject.transform.position.x -= MouseListener.getWorldDx();
                activeGameObject.transform.position.y -= MouseListener.getWorldDy();
            }
        }

        super.update(dt);
    }
}

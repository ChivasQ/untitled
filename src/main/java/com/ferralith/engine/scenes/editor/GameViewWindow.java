package com.ferralith.engine.scenes.editor;

import com.ferralith.engine.Window;
import com.ferralith.engine.inputs.MouseListener;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;

public class GameViewWindow {

    private boolean isHovering;

    public void imgui() {
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        isHovering = ImGui.isWindowHovered();

        ImVec2 viewportSize = getLargestSizeForViewport();

        ImVec2 availableRegion = ImGui.getContentRegionAvail();
        float offsetX = (availableRegion.x - viewportSize.x) * 0.5f;
        float offsetY = (availableRegion.y - viewportSize.y) * 0.5f;

        ImGui.setCursorPosX(ImGui.getCursorPosX() + offsetX);
        ImGui.setCursorPosY(ImGui.getCursorPosY() + offsetY);

        // fix for offset in mouse position
        ImVec2 viewportTopLeft = new ImVec2();
        ImGui.getCursorScreenPos(viewportTopLeft);

        MouseListener.get().setGameViewportPos(new Vector2f(viewportTopLeft.x, viewportTopLeft.y));
        MouseListener.get().setGameViewportSize(new Vector2f(viewportSize.x, viewportSize.y));


//        if (viewportSize.x > 0 && viewportSize.y > 0 &&
//                (Window.getFramebuffer().width != (int)viewportSize.x ||
//                        Window.getFramebuffer().height != (int)viewportSize.y)) {
//            Window.resizeFrameBuffer((int)viewportSize.x, (int)viewportSize.y);
//        }

        int textureID = Window.getFramebuffer().getTextureID();
        ImGui.image(textureID, viewportSize.x, viewportSize.y, 0, 1, 1, 0);

        ImGui.end();
    }

    private ImVec2 getLargestSizeForViewport() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.getTargetAspectRatio();
        if (aspectHeight > windowSize.y) {
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }


    public boolean getWantCaptureMouse() {
        return isHovering;
    }

    public boolean isHoveringViewport() {
        return isHovering;
    }
}
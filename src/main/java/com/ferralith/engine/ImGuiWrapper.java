package com.ferralith.engine;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiKey;
import imgui.flag.ImGuiMouseCursor;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

public class ImGuiWrapper {

    private long glfwWindow;

    // Mouse cursors provided by GLFW
    private final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];

    // LWJGL3 renderer (SHOULD be initialized)
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final ImGuiImplGlfw imGuiImplGlfw = new ImGuiImplGlfw();
    private String glslVersion = "#version 330 core";

    public ImGuiWrapper(long glfwWindow) {
        this.glfwWindow = glfwWindow;
    }


    // Initialize Dear ImGui.
    public void initImGui() {

        ImGui.createContext();
        imGuiImplGlfw.init(glfwWindow, true);
        imGuiGl3.init(glslVersion);
    }

    public void update(float dt) {
        startFrame();
        ImGui.showDemoWindow();
        endFrame();
    }

    protected void startFrame() {
        imGuiGl3.newFrame();
        imGuiImplGlfw.newFrame();
        ImGui.newFrame();
    }

    protected void endFrame() {
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        // Update and Render additional Platform Windows
        // (Platform functions may change the current OpenGL context, so we save/restore it to make it easier to paste this code elsewhere.
        //  For this specific demo app we could also call glfwMakeContextCurrent(window) directly)
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupCurrentContext = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupCurrentContext);
        }

        //renderBuffer();
    }

    protected void disposeImGui() {
        ImGui.destroyContext();
    }
}

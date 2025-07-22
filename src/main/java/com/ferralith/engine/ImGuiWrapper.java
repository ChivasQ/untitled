package com.ferralith.engine;

import com.ferralith.engine.utils.AssetPool;
import imgui.*;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiMouseCursor;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.GLFW;

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

        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename("imgui.ini");



        io.getFonts().setFreeTypeRenderer(true);

        final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder(); // Glyphs ranges provide
        rangesBuilder.addRanges(io.getFonts().getGlyphRangesDefault());
        rangesBuilder.addRanges(io.getFonts().getGlyphRangesCyrillic());
        rangesBuilder.addRanges(io.getFonts().getGlyphRangesJapanese());

        final ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setMergeMode(false);  // Enable merge mode to merge cyrillic, japanese and icons with default font
        fontConfig.setPixelSnapH(true);

        final short[] glyphRanges = rangesBuilder.buildRanges();
        io.getFonts().addFontFromMemoryTTF(AssetPool.loadFontFromResources("assets/fonts/VCR_OSD_MONO_1.001.ttf"), 18, fontConfig, glyphRanges); // cyrillic glyphs

        fontConfig.destroy();
        imGuiImplGlfw.init(glfwWindow, true);
        imGuiGl3.init(glslVersion);
    }

    public void update(float dt, Scene currentScene) {
        startFrame();
        currentScene.sceneImgui();
//        ImGui.showDemoWindow();
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

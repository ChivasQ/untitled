package com.ferralith.engine;

import com.ferralith.engine.inputs.KeyListener;
import com.ferralith.engine.inputs.MouseListener;
import com.ferralith.engine.renderer.*;
import com.ferralith.engine.scenes.LevelScene;
import com.ferralith.engine.scenes.TestScene;
import com.ferralith.engine.utils.AssetPool;
import com.ferralith.engine.utils.Time;
import imgui.ImGui;
import imgui.ImGuiViewport;
import imgui.internal.ImGuiWindow;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import static java.lang.Character.getType;
import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Window {
    private int width;
    private int height;
    private String title;
    private long glfwWindow;
    public float r, b, g;
    private ImGuiWrapper imGuiWrapper;
    private boolean isResized = false;
    private Framebuffer framebuffer;
    private PickingTexture pickingTexture;

    private static Window window = null;

    private static Scene currentScene = null;
    private Shader pickingShader;
    private Shader defaultShader;

    public Window(){
        this.height =  100;
        this.width =  100;
        this.title =  "title";
        this.r = 1f;
        this.g = 0.2f;
        this.b = 1f;
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new TestScene();
                currentScene.load();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false : ("Unknown scene: " + newScene);
                break;
        }
    }

    public static Window getInstance() {
        if (Window.window == null)
            Window.window = new Window();

        return Window.window;
    }

    public static Scene getScene() {
        return getInstance().currentScene;
    }

    public static Framebuffer getFramebuffer() {
        return getInstance().framebuffer;
    }

    public static float getTargetAspectRatio() {
        return 16.0f / 9.0f;
    }


    public Window setSize(int width, int height) {
        this.height = height;
        this.width = width;
        return this;
    }

    public Window setTitle(String title) {
        this.title = title;
        return this;
    }

    public void run() {
        System.out.println(this.title + " is running");

        init();
        loop();

        close();
    }

    private void close() {
//        ImGuiIO io = ImGui.getIO();
//        io.setWantSaveIniSettings(true);


        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if ( glfwWindow == NULL ) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);

        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(glfwWindow, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    glfwWindow,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwSetWindowSizeCallback(glfwWindow, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(final long window, final int width, final int height) {
                Window.getInstance().setSize(width, height);
                isResized = true;
                //glViewport(0, 0, width, height);
                //update(0);
            }
        });

//        glfwSetFramebufferSizeCallback(glfwWindow, (window, width, height) -> {
//            glViewport(0, 0, width, height);
//            isResized = true;
//        });



        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        this.imGuiWrapper = new ImGuiWrapper(glfwWindow);
        this.imGuiWrapper.initImGui();

        this.framebuffer = new Framebuffer(getWidth(),getHeight());

        this.pickingTexture = new PickingTexture(getWidth(),getHeight());

        Window.changeScene(1);
    }

    private void loop() {
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;

        // TODO: HELL NO;
        defaultShader = AssetPool.getShader("default");
        pickingShader = AssetPool.getShader("picking_shader");

        while(!glfwWindowShouldClose(glfwWindow)) {
            update(dt);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }

        // TODO: SAME
        currentScene.save();
    }

    private void update(float dt) {
        glfwPollEvents();

        pickingTexture();

        DebugDraw.beginFrame();

        this.framebuffer.bind();

        glClearColor(r, g, b, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);



        if (dt >= 0) {
            Renderer.bindShader(defaultShader);
            currentScene.update(dt);
            currentScene.render();
            DebugDraw.draw();
        }

        this.framebuffer.unbind();

        this.imGuiWrapper.update(dt, currentScene);

        glfwSwapBuffers(glfwWindow);

        if (KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(glfwWindow, true);
        }

        if (isResized) {
            if (width == 0 && height == 0) {
                System.out.println("Window is minimized.");
            } else {
                System.out.println("Window is resized(" + width + ", " + height + "): adjusting perspective...");
                currentScene.camera.adjustProjective();
            }

            isResized = false;
        }
    }

    private void pickingTexture() {
        // Render pass for picking texture;
        glDisable(GL_BLEND);
        pickingTexture.enableWriting();
        glViewport(0,0, getWidth(), getHeight());
        glClearColor(0,0,0,0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Renderer.bindShader(pickingShader);
        currentScene.render();

        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            int x = (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();
            System.out.println(pickingTexture.readPixel(x, y));
        }

        pickingTexture.disable();
        glEnable(GL_BLEND);
    }


    public static int getHeight() {
        return getInstance().height;
    }

    public static int getWidth() {
        return getInstance().width;
    }
}

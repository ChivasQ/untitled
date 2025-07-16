package com.ferralith.engine.scenes;

import com.ferralith.engine.Camera;
import com.ferralith.engine.Scene;
import com.ferralith.engine.Window;
import com.ferralith.engine.inputs.KeyListener;
import com.ferralith.engine.renderer.Shader;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class TestScene extends Scene {

    private String vertexShaderSrc = "#version 330 core\n" +
            "\n" +
            "layout (location = 0) in vec3 aPos;\n" +
            "layout (location = 1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main() {\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";

    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main() {\n" +
            "     color = fColor;\n" +
            "}";

    private int vertexID, fragmentID, shaderProgram;

    //        x     y     z       r     g     b     a
    private float[] vertexArray = {
            0.5f * 100, -0.5f * 100, 0.0f * 100,   1.0f, 0.0f, 0.0f, 1.0f,  // 0: Bottom right
            -0.5f * 100,  0.5f * 100, 0.0f * 100,   0.0f, 1.0f, 0.0f, 1.0f,  // 1: Top left
            0.5f * 100,  0.5f * 100, 0.0f * 100,   0.0f, 0.0f, 1.0f, 1.0f,  // 2: Top right
            -0.5f * 100, -0.5f * 100, 0.0f * 100,   1.0f, 1.0f, 0.0f, 1.0f   // 3: Bottom left
    };


    private int[] elementArray = {
            2, 1, 0,  // Top right triangle
            0, 1, 3   // Bottom left triangle
    };

    private int vaoID, vboID, eboID;

    private Shader testShader;

    public TestScene() {
        System.out.println("TEST SCENE");
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        testShader = new Shader("assets/shaders/default.fsh", "assets/shaders/default.vsh");
        testShader.compile();

//        for (int i = 0; i < vertexArray.length; i++) {
//            vertexArray[i] *= 200;
//        }

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);


    }

    @Override
    public void update(float dt) {
        if (KeyListener.isKeyPressed(KeyEvent.VK_1)) {
            Window.changeScene(0);
        }
        camera.position.x -= dt * 25.0f;
        testShader.use();
        testShader.uploadmat4f("uProjection", camera.getProjectionMatrix());
        testShader.uploadmat4f("uView", camera.getViewMatrix());

        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        testShader.detach();
    }
}

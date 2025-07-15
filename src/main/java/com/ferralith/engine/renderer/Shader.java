package com.ferralith.engine.renderer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private int shaderProgramID;

    private String vertexSource;
    private String fragmentSource;

    public Shader(String fsh_path, String vsh_path) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(fsh_path);
            String fragmentSource = new String(is.readAllBytes(), StandardCharsets.UTF_8);;
            this.fragmentSource = fragmentSource;
        } catch (IOException e){
            e.printStackTrace();
            assert false : "Error: could not open file for fragment shader: " + fsh_path;
        }

        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(vsh_path);
            String vertexSource = new String(is.readAllBytes(), StandardCharsets.UTF_8);;
            this.vertexSource = vertexSource;
        } catch (IOException e){
            e.printStackTrace();
            assert false : "Error: could not open file for vertex shader: " + vsh_path;
        }

        //System.out.println(fragmentSource);
        //System.out.println(vertexSource);
    }

    public void compile() {
        int fragmentID, vertexID;

        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: VERTEX");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: FRAGMENT");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: LINKING");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    public void use() {
        glUseProgram(shaderProgramID);
    }

    public void detach() {
        glUseProgram(0);
    }
}

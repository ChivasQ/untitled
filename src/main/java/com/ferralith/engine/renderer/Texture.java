package com.ferralith.engine.renderer;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Texture {
    private String path;
    private transient int texID;
    private int width;
    private int height;

    public Texture() {
    }

    public Texture(String path) {
        this.path = path;

        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        try {
            stbi_set_flip_vertically_on_load(true);

            InputStream is = getClass().getClassLoader().getResourceAsStream(this.path);
            if (is == null) {
                throw new RuntimeException("Resource not found in JAR: " + this.path);
            }
            byte[] bytes = is.readAllBytes();
            ByteBuffer imageBuffer = memAlloc(bytes.length);
            imageBuffer.put(bytes).flip();
            ByteBuffer image = null;
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer x = stack.mallocInt(1);
                IntBuffer y = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);

                image = stbi_load_from_memory(imageBuffer, x, y, channels, 0);
                if (image == null) {
                    throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
                }

                this.width = x.get(0);
                this.height = y.get(0);
                System.out.println("Loaded texture: " + path + " " + width + "x" + height);
                if (channels.get(0) == 3) {
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, this.width, this.height, 0, GL_RGB, GL_UNSIGNED_BYTE, image);
                } else if (channels.get(0) == 4) {
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
                } else {
                    assert false : "Error: (Texture) unknown number of channels: '" + channels.get(0) + "'";
                }



            } finally {
                if (image != null) stbi_image_free(image);
                memFree(imageBuffer);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource: " + path, e);
        }
    }



    public Texture(int width, int height) {
        this.path = "Generated";

        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getId() {
        return texID;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Texture)) return false;

        Texture tex = (Texture) obj;

        return  tex.getWidth() == this.getWidth() &&
                tex.getHeight() == this.getHeight() &&
                tex.getId() == this.getId() &&
                tex.getPath().equals(this.getPath());
    }
}

package com.ferralith.engine.components;

import com.ferralith.engine.renderer.Texture;
import org.joml.Vector2f;

public class Sprite {
    private float width, height;

    private Texture texture = null;
    private Vector2f[] textureCoords = {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1),
    };

    public Sprite(Texture texture) {
        this.texture = texture;
        setHeight(texture.getHeight());
        setWidth(texture.getWidth());
    }

    public Sprite(Texture texture, Vector2f[] textureCoords) {
        this.texture = texture;
        this.textureCoords = textureCoords;
    }

    public Sprite() {
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTextureCoords() {
        return textureCoords;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getTexId() {
        return texture == null ? -1 : texture.getId();
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}

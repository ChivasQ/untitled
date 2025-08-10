package com.ferralith.game.models;

import org.joml.Vector4f;

public class Pixel {
    private int color;
    public PixelType type;
    public boolean updated;

    public Pixel(int color, PixelType type) {
        this.type = type;
        this.color = color;
    }

    public Pixel setColor(Vector4f color) {
        this.color = ((int)(color.w*255) << 24) | ((int)(color.x*255) << 16) | ((int)(color.y*255) << 8) | (int)(color.z*255);
        return this;
    }

    public int getColor() {
        return this.color;
    }

    public PixelType getType() {
        return type;
    }
}

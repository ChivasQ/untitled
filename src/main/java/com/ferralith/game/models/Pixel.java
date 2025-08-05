package com.ferralith.game.models;

import org.joml.Vector4f;

public class Pixel {
    private int color;
    private float weight;
    private boolean movable = true;


    public Pixel(int color, float weight) {
        this.color = color;
        this.weight = weight;
    }

    public void setColor(Vector4f color) {
        this.color = ((int)(color.w*255) << 24) | ((int)(color.x*255) << 16) | ((int)(color.y*255) << 8) | (int)(color.z*255);
    }
}

package com.ferralith.engine.utils;

import com.ferralith.engine.GameObject;
import com.ferralith.engine.Transform;
import com.ferralith.engine.components.Sprite;
import com.ferralith.engine.components.SpriteRenderer;
import org.joml.Vector2f;

public class GenObject {

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        GameObject object = new GameObject("Sprite_object_gen",
                new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        SpriteRenderer spriteRenderer = new SpriteRenderer(sprite);
        object.addComponent(spriteRenderer);
        return object;
    }

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY, int Zindex) {
        GameObject object = new GameObject("Sprite_object_gen",
                new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), Zindex);
        SpriteRenderer spriteRenderer = new SpriteRenderer(sprite);
        object.addComponent(spriteRenderer);
        return object;
    }
}

package com.ferralith.engine.components;

import com.ferralith.engine.Component;
import com.ferralith.engine.Transform;
import com.ferralith.engine.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class SpriteRenderer extends Component {
    Vector4f color;
    private Sprite sprite;
    private Transform lastTransform;
    private boolean isDirty = true;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = new Sprite(null);
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.color = new Vector4f(1);
    }

    public SpriteRenderer(Texture texture) {
        this.sprite = new Sprite(texture);
        this.color = new Vector4f(1);
    }
    @Override
    public void start() {
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt) {
        if (!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(this.lastTransform);
            setDirty();
        }
    }

    public void setDirty() {
        this.isDirty = true;
    }

    public void setDirty(boolean f) {
        this.isDirty = f;
    }

    public Vector4f getColor() {
        return this.color;
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTextureCoords();
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        setDirty();
    }

    public void setColor(Vector4f color) {
        if (this.color.equals(color)) return;

        this.color.set(color);
        setDirty();
    }

    public boolean isDirty() {
        return this.isDirty;
    }
}

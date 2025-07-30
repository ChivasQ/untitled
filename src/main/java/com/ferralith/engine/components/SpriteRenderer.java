package com.ferralith.engine.components;

import com.ferralith.engine.Component;
import com.ferralith.engine.Transform;
import com.ferralith.engine.renderer.Texture;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class SpriteRenderer extends Component {
    private Vector4f color = new Vector4f(1);
    private Sprite sprite = new Sprite();
    private transient Transform lastTransform;
    private transient boolean isDirty = false;

    public SpriteRenderer() {
    }

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = new Sprite(null);
        this.setDirty();
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.color = new Vector4f(1);
        this.setDirty();
    }

    public SpriteRenderer(Texture texture) {
        this.sprite = new Sprite(texture);
        this.color = new Vector4f(1);
        this.setDirty();
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

    public Sprite getSprite() {
        return sprite;
    }

    public void setColor(Vector4f color) {
        if (this.color.equals(color)) return;

        this.color.set(color);
        setDirty();
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    @Override
    public void imgui() {
        float[] imColor = {color.x, color.y, color.z, color.w};
        if (ImGui.colorPicker4("Color Picker: ", imColor)) {
            //System.out.println("COLOR CHANGED");
            this.color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            this.setDirty();
        }
    }

    public void setTexture(Texture texture) {
        this.sprite.setTexture(texture);
    }
}

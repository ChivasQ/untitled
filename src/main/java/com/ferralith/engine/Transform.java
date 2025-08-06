package com.ferralith.engine;

import com.ferralith.engine.utils.MyImGui;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

public class Transform {
    public Vector2f position;
    public Vector2f scale;
    public float rotation = 0.0f;
    public int zIndex = 0;

    public Transform() {
        init(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position) {
        init(position, new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale) {
        init(position, scale);
    }

    public void init(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
        this.zIndex = 0;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

    public Transform copy() {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    public void copy(Transform transform) {
        transform.position.set(this.position);
        transform.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transform transform = (Transform) o;
        return transform.position.equals(this.position) &&
                transform.scale.equals(this.scale) &&
                zIndex == transform.getZIndex() &&
                transform.rotation == this.rotation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, scale);
    }

    public int getZIndex() {
        return this.zIndex;
    }

    public void imgui() {
        // todo: move to diff class
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());

                if (isPrivate) field.setAccessible(true);
                if (isTransient) continue;

                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if (type == int.class) {
                    int val = (int)value;
                    MyImGui.dragInt(name, val);
                } else if (type == float.class) {
                    float val = (float) value;
                    MyImGui.dragFloat(name, val);
                } else if (type == Vector2f.class) {
                    Vector2f val = (Vector2f)value;
                    MyImGui.drawVec2Control(name, val);
                } else if (type == Vector3f.class) {
                    Vector3f val = (Vector3f)value;
                    MyImGui.drawVec3Control(name, val);
                } else if (type == Vector4f.class) {
                    Vector4f val = (Vector4f)value;
                    float[] imVec4f = {val.x, val.y, val.z, val.w};
                    if (ImGui.dragFloat4(name + ": ", imVec4f)) {
                        val.set(imVec4f[0], imVec4f[1], imVec4f[2], imVec4f[3]);
                    }
                }

                if (isPrivate) field.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
        }
    }
}

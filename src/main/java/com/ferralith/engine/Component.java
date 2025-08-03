package com.ferralith.engine;

import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {
    private static int ID_COUNTER = 0;
    private int uid = -1;

    public transient GameObject gameObject = null;

    public void start() {

    }

    public abstract void update(float dt);

    public void imgui() {
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
                    int[] imInt = {val};
                    if (ImGui.dragInt(name + ": ", imInt)) {
                        field.set(this, imInt[0]);
                    }
                } else if (type == float.class) {
                    float val = (float) value;
                    float[] imFloat = {val};
                    if (ImGui.dragFloat(name + ": ", imFloat)) {
                        field.set(this, imFloat[0]);
                    }
                } else if (type == Vector2f.class) {
                    Vector2f val = (Vector2f)value;
                    float[] imVec2f = {val.x, val.y};
                    if (ImGui.dragFloat2(name + ": ", imVec2f)) {
                        val.set(imVec2f[0], imVec2f[1]);
                    }
                } else if (type == Vector3f.class) {
                    Vector3f val = (Vector3f)value;
                    float[] imVec3f = {val.x, val.y, val.z};
                    if (ImGui.dragFloat3(name + ": ", imVec3f)) {
                        val.set(imVec3f[0], imVec3f[1], imVec3f[2]);
                    }
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

    public void generateId() {
        if (this.uid == -1) {
            this.uid = ID_COUNTER++;
        }
    }

    public int getUid() {
        return this.uid;
    }

    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }

}

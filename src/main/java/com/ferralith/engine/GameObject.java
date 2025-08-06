package com.ferralith.engine;

import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private static int ID_COUNTER;
    private int uid;


    public String name;
    private List<Component> components;
    private List<Tags> tags;
    public Transform transform;

    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.transform = new Transform();

        this.uid = ID_COUNTER++;
    }

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.components = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.transform = transform;
        this.transform.zIndex = zIndex;
        this.uid = ID_COUNTER++;
    }

    public GameObject(String name, Transform transform) {
        this.name = name;
        this.components = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.transform = transform;

        this.uid = ID_COUNTER++;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    assert false : "Error: Casting component.";
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> component) {
        for (int i = 0; i < components.size(); i++) {
            if (component.isAssignableFrom(components.get(i).getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component component) {
        component.generateId();
        this.components.add(component);
        component.gameObject = this;
    }

    public void update(float dt) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
    }

    public void start() {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    public int getzIndex() {
        return this.transform.zIndex;
    }

    public void setzIndex(int zIndex) {
        this.transform.zIndex = zIndex;
    }

    public void imgui() {
        if (ImGui.collapsingHeader(transform.getClass().getSimpleName()))
            transform.imgui();

        for (Component c : components) {
            if (ImGui.collapsingHeader(c.getClass().getSimpleName()))
                c.imgui();
        }
    }

    public boolean hasTag(Tags tag) {
        for (Tags i: tags) {
            if (tag == i) return true;
        }
        return false;
    }

    public void addTag(Tags tag) {
        this.tags.add(tag);
    }

    public int getUid() {
        return this.uid;
    }

    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }

    public List<Component> getAllComponents() {
        return this.components;
    }
}

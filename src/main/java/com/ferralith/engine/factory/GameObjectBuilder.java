package com.ferralith.engine.factory;

import com.ferralith.engine.Component;
import com.ferralith.engine.GameObject;
import com.ferralith.engine.Tags;
import com.ferralith.engine.Transform;

public class GameObjectBuilder {
    private GameObject gameObject;

    public GameObjectBuilder(String name) {
        gameObject = new GameObject(name);
    }

    public GameObjectBuilder withTransform(Transform transform) {
        gameObject.transform = transform;
        return this;
    }

    public GameObjectBuilder withComponent(Component component) {
        gameObject.addComponent(component);
        return this;
    }

    public GameObjectBuilder withTag(Tags tag) {
        gameObject.addTag(tag);
        return this;
    }

    public GameObject build() {
        return gameObject;
    }
}

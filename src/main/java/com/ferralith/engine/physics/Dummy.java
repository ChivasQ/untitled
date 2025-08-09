package com.ferralith.engine.physics;

import com.ferralith.engine.GameObject;
import com.ferralith.engine.Transform;
import org.joml.Vector2f;

public class Dummy extends GameObject {
    private AABB box;
    private RigidBody rigidBody;

    public Dummy(String name, Transform transform, int zIndex) {
        super(name, transform, zIndex);
        this.box = new AABB(transform.position, transform.scale);
        this.rigidBody = new RigidBody();

        addComponent(rigidBody);
        addComponent(box);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        box.setCenter(transform.position);
        box.setSize(new Vector2f(100, 100));
    }
}

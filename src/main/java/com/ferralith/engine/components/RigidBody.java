package com.ferralith.engine.components;

import com.ferralith.engine.Component;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class RigidBody extends Component {
    private int colliderType = 0;
    private float friction = 0.1f;
    public Vector3f velocity = new Vector3f(0, 0.6f,0);
    public transient Vector4f tmp = new Vector4f(0);

    @Override
    public void update(float dt) {

    }
}

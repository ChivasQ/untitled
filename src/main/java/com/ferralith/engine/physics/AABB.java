package com.ferralith.engine.physics;

import com.ferralith.engine.renderer.DebugDraw;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class AABB { //Component?
    private Vector2f minPos;
    private Vector2f maxPos;

    public AABB(Vector2f center, Vector2f size) {
        Vector2f halfSize = new Vector2f(size).mul(0.5f);

        this.minPos = new Vector2f(center).sub(halfSize);
        this.maxPos = new Vector2f(center).add(halfSize);

        DebugDraw.addBox2D(center, size, 0, new Vector3f(1,0,0), 1);
    }

    public boolean isInside(Vector2f pos) {
        return pos.x > minPos.x &&
                pos.x < maxPos.x &&
                pos.y > minPos.y &&
                pos.y < maxPos.y;
    }
}

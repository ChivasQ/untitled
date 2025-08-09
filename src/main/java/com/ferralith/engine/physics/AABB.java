package com.ferralith.engine.physics;

import com.ferralith.engine.Component;
import com.ferralith.engine.renderer.DebugDraw;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class AABB extends Component {
    private Vector2f center;
    private Vector2f size;
    private float r, g, b;

    public AABB(Vector2f center, Vector2f size) {
        this.center = center;
        this.size = size;
        this.r = 1;
        this.g = 1;
        this.b = 1;

    }

    public boolean isInside(Vector2f pos) {
        return pos.x > getMinPos().x &&
                pos.x < getMaxPos().x &&
                pos.y > getMinPos().y &&
                pos.y < getMaxPos().y;
    }

    @Override
    public void update(float dt) {
        DebugDraw.addBox2D(center, size, 0, new Vector3f(r,g,b), 1);
    }

    public Vector2f getCenter() {
        return center;
    }

    public void setCenter(Vector2f center) {
        this.center = center;
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        this.size = size;
    }

    public AABB inflate(float x, float y) {
        this.size.add(x, y);
        return this;
    }

    public boolean intersects(AABB other) {
        return !(other.getMaxPos().x < this.getMinPos().x ||
                other.getMinPos().x > this.getMaxPos().x ||
                other.getMaxPos().y < this.getMinPos().y ||
                other.getMinPos().y > this.getMaxPos().y);
    }


    private boolean isInBetween(float min, float max, float other) {
        return  other > min &&
                other < max;
    }

    private boolean isInBox(Vector2f min, Vector2f max, Vector2f other) {
        return  isInBetween(min.x, max.x, other.x) &&
                isInBetween(min.y, max.y, other.y);
    }

    public Vector2f getMinPos() {
        return new Vector2f(center).sub(size.x / 2, size.y / 2);
    }

    public Vector2f getMaxPos() {
        return new Vector2f(center).add(size.x / 2, size.y / 2);
    }

    public void setColor(float r, float g, float b) {
        this.r = r;
        this.b = b;
        this.g = g;
    }
}

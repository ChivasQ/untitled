package com.ferralith.engine.physics;

import com.ferralith.engine.Component;
import com.ferralith.engine.renderer.DebugDraw;
import org.joml.Vector2f;

public class RigidBody extends Component {
    private static final float GRAVITY_CONST = 9.8F;
    private int colliderType = 0;
    private float friction = 0.1f;
    private float mass = 10.0f;
    public Vector2f velocity = new Vector2f(0, 0);

    @Override
    public void update(float dt) {
        gameObject.transform.position.add(velocity);
        velocity.sub(0,GRAVITY_CONST * dt);

        velocity.sub(Math.signum(velocity.x) * dt,0);

        DebugDraw.addLine2D(new Vector2f(gameObject.transform.position), new Vector2f(gameObject.transform.position).add(new Vector2f(velocity).mul(5)));
    }

    public void applyForce(Vector2f force) {
       // Vector2f acceleration = new Vector2f(force).div(mass);
        this.velocity.add(force);
    }
}

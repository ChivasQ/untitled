package com.ferralith.engine.physics;

import com.ferralith.engine.Component;
import com.ferralith.engine.renderer.DebugDraw;
import org.joml.Vector2f;

import java.util.List;

public class PolygonCollider extends Component {
    private List<Vector2f> polygon;
    private float scale = 1;

    public PolygonCollider(List<Vector2f> polygon, float scale) {
        this.polygon = polygon;
        this.scale = scale;
    }

    @Override
    public void update(float dt) {
        if (!polygon.isEmpty()) {
            DebugDraw.addPolygonn(polygon, scale, gameObject.transform.position);
        }
    }

    public void setPolygon(List<Vector2f> polygon) {
        this.polygon = polygon;
    }
}

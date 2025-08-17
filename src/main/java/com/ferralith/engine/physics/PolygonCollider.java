package com.ferralith.engine.physics;

import com.ferralith.engine.Component;
import com.ferralith.engine.renderer.DebugDraw;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class PolygonCollider extends Component {
    private List<Vector2f> polygon;
    private float scale;

    public PolygonCollider(List<Vector2f> polygon, float scale) {
        this.polygon = polygon;
        this.scale = scale;
    }

    @Override
    public void update(float dt) {
        if (!polygon.isEmpty()) {
            DebugDraw.addPolygonn(getWorldVertices(), 1, new Vector2f(), 0, new Vector2f());
        }
    }

    public void setPolygon(List<Vector2f> polygon) {
        this.polygon = polygon;
    }

    public List<Vector2f> getPolygon() {
        return polygon;
    }

    public List<Vector2f> getWorldVertices() {
        List<Vector2f> world = new ArrayList<>(polygon.size());

        // центр вращения (например, позиция объекта)
        Vector2f pos = gameObject.transform.position;
        // TODO: redo MANY stuff to fix center point in go;
//        pivot = new Vector2f(pivot).sub((float) 29 /2, (float) 63 /2);
//        float cos = (float) Math.cos(gameObject.transform.rotation);
//        float sin = (float) Math.sin(gameObject.transform.rotation);

        for (Vector2f v : polygon) {
            Vector2f tmp = new Vector2f(v);

            // масштаб
            tmp.mul(scale);

            // перенос к pivot
            //tmp.sub(pivot);

//            // вращение
//            float x = tmp.x * cos - tmp.y * sin;
//            float y = tmp.x * sin + tmp.y * cos;
            //tmp.set(x, y);

            // возвращаем обратно и в мировые координаты
            tmp.add(pos);

            world.add(tmp);
        }

        return world;
    }

}

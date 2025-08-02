package com.ferralith.engine.scenes.components;

import com.ferralith.engine.Camera;
import com.ferralith.engine.Component;
import com.ferralith.engine.Window;
import com.ferralith.engine.renderer.DebugDraw;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GridLines extends Component {
    private static final int GRID_WIDTH = 32;
    private static final int GRID_HEIGHT = 32;
    private Vector3f color = new Vector3f(0,0,0);

    @Override
    public void update(float dt) {
        Camera camera = Window.getScene().getCamera();
        // TODO: some lines still alive, fix
//        System.out.println(camera.getZoom());
//        if (camera.getZoom() > 5) return;
        Vector2f cameraPos = camera.getPosition();
        Vector2f projectionSize = camera.getProjectionSize();

        int firstX = ((int)((cameraPos.x / GRID_WIDTH) - 1)  * GRID_WIDTH);
        int firstY = ((int)((cameraPos.y / GRID_HEIGHT) - 1) * GRID_HEIGHT);

        int numVtLines = (int)(projectionSize.x * camera.getZoom() / GRID_WIDTH) + 2;
        int numHzLines = (int)(projectionSize.y * camera.getZoom()  / GRID_HEIGHT) + 2;

        int height = (int) (projectionSize.y  * camera.getZoom()) + GRID_HEIGHT * 2;
        int width = (int) (projectionSize.x  * camera.getZoom()) + GRID_WIDTH * 2;

        int maxLines = Math.max(numHzLines, numVtLines);

        for (int i = 0; i < maxLines; i++) {
            int x = firstX + (GRID_WIDTH * i);
            int y = firstY + (GRID_HEIGHT * i);

            if (i < numVtLines) {
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color, 1, 1);
            }

            if (i < numHzLines) {
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width , y), color);
            }
        }
    }


}

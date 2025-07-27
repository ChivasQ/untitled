package com.ferralith.engine.components;

import com.ferralith.engine.Component;
import com.ferralith.engine.Window;
import com.ferralith.engine.renderer.DebugDraw;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GridLines extends Component {
    private static final int GRID_WIDTH = 32;
    private static final int GRID_HEIGHT = 32;
    private Vector3f color = new Vector3f(1,0,0);

    @Override
    public void update(float dt) {
        Vector2f cameraPos = Window.getScene().getCamera().getPosition();
        Vector2f projectionSize = Window.getScene().getCamera().getProjectionSize();

        int firstX = ((int)(cameraPos.x / GRID_WIDTH) * GRID_WIDTH);
        int firstY = ((int)(cameraPos.x / GRID_HEIGHT) * GRID_HEIGHT);

        int numVtLines = (int)(projectionSize.x / GRID_WIDTH);
        int numHzLines = (int)(projectionSize.y / GRID_HEIGHT);

        int height = (int) projectionSize.y;
        int width = (int) projectionSize.x;

        int maxLines = Math.max(numHzLines, numVtLines);

        for (int i = 0; i < maxLines; i++) {
            int x = firstX + (GRID_WIDTH * i);
            int y = firstY + (GRID_HEIGHT * i);

            if (i < numVtLines) {
                DebugDraw.addLine2D(new Vector2f(x, y), new Vector2f(x, y + height), color);
            }

            if (i < numHzLines) {
                DebugDraw.addLine2D(new Vector2f(x, y), new Vector2f(x + width , y), color);
            }
        }
    }


}

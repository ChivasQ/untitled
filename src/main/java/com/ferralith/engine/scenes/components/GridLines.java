package com.ferralith.engine.scenes.components;

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
        Vector2f cameraPos = Window.getScene().getCamera().getPosition();
        Vector2f projectionSize = new Vector2f(Window.getWidth(), Window.getHeight());

        int firstX = ((int)((cameraPos.x / GRID_WIDTH) - 1)  * GRID_WIDTH);
        int firstY = ((int)((cameraPos.y / GRID_HEIGHT) - 1) * GRID_HEIGHT);

        int numVtLines = (int)(projectionSize.x / GRID_WIDTH) + 2;
        int numHzLines = (int)(projectionSize.y / GRID_HEIGHT) + 2;

        int height = (int) projectionSize.y + GRID_HEIGHT * 2;
        int width = (int) projectionSize.x + GRID_WIDTH * 2;

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

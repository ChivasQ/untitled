package com.ferralith.engine.renderer;

import com.ferralith.engine.Window;
import com.ferralith.engine.utils.AssetPool;
import com.ferralith.engine.utils.Mth;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DebugDraw {
    private static int MAX_LINES = 500;

    private static List<Line2D> lines = new ArrayList<>();

    private static float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static Shader shader = AssetPool.getShader("line2d");

    private static int vaoID;
    private static int vboID;

    private static boolean started = false;

    private static float lineWidth = 1.0f;

    public static void start() {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // TODO: SET LINE WIDTH
        glLineWidth(lineWidth);
    }

    public static void beginFrame() {
        if (!started) {
            start();
            started = true;
        }

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).beginFrame() <= 0) {
                lines.remove(i);
                i--;
            }

        }
    }

    public static void draw() {
        if (lines.isEmpty()) return;

        int index = 0;
        for (Line2D line : lines) {
            for (int i = 0; i < 2; i++) {
                Vector2f pos = i == 0 ? line.getFrom() : line.getTo();
                Vector3f color = line.getColor();

                vertexArray[index] = pos.x;
                vertexArray[index + 1] = pos.y;
                vertexArray[index + 2] = 10;

                vertexArray[index + 3] = color.x;
                vertexArray[index + 4] = color.y;
                vertexArray[index + 5] = color.z;

                index += 6;

            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2));

        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawArrays(GL_LINES, 0, lines.size() * 6 * 2);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    public static void setLineWidth(float lineWidth) {
        DebugDraw.lineWidth = lineWidth;
    }

    public static void addLine2D(Vector2f from, Vector2f to) {
        addLine2D(from, to, new Vector3f(1, 0, 0),1);
    }

    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color) {
        addLine2D(from, to, color, 1);
    }


    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
        //System.out.println(lines.size());
        if (lines.size() >= MAX_LINES) return;

        DebugDraw.lines.add(new Line2D(from, to, color, lifetime));
    }

    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color, int lifetime, int width) {
        setLineWidth(width);
        addLine2D(from, to, color, lifetime);
    }

    public static void addBox2D(Vector2f center, Vector2f size, float angle, Vector3f color, int lifetime) {
        Vector2f halfSize = new Vector2f(size).mul(0.5f);

        Vector2f min = new Vector2f(center).sub(halfSize);
        Vector2f max = new Vector2f(center).add(halfSize);

        Vector2f[] vectices = {
            new Vector2f(min.x, min.y),
            new Vector2f(min.x, max.y),
            new Vector2f(max.x, max.y),
            new Vector2f(max.x, min.y)
        };

        if (angle != 0.0) {
            for (int i = 0; i < vectices.length; i++) {
                Mth.rotate(vectices[i], center, angle);
            }
        }

        addLine2D(vectices[0], vectices[1], color, lifetime);
        addLine2D(vectices[1], vectices[2], color, lifetime);
        addLine2D(vectices[2], vectices[3], color, lifetime);
        addLine2D(vectices[3], vectices[0], color, lifetime);

    }

    public static void addPolygon(Vector2f center, float radius, Vector3f color, int lifetime, int vectices) {
        Vector2f[] points = new Vector2f[vectices];

        int increment = 360 / vectices;
        int currentAngle = 0;

        for (int i = 0; i < vectices; i++) {
            Vector2f tmp = new Vector2f(radius, 0);
            Mth.rotate(tmp, new Vector2f(), currentAngle);
            points[i] = new Vector2f(tmp).add(center);
            if (i > 0) {
                //System.out.println(points[i - 1].toString());
                addLine2D(points[i - 1], points[i], color, lifetime);
            }
            currentAngle += increment;
        }
        addLine2D(points[0], points[vectices - 1]);
    }

    public static void addCircle(Vector2f center, float radius, Vector3f color, int lifetime) {
        addPolygon(center,radius, color, lifetime, 16);
    }
}

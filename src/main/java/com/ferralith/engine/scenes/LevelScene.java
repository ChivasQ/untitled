package com.ferralith.engine.scenes;

import com.ferralith.engine.*;
import com.ferralith.engine.components.Sprite;
import com.ferralith.engine.components.SpriteRenderer;
import com.ferralith.engine.components.SpriteSheet;
import com.ferralith.engine.inputs.KeyListener;
import com.ferralith.engine.inputs.MouseListener;
import com.ferralith.engine.physics.AABB;
import com.ferralith.engine.physics.Dummy;
import com.ferralith.engine.physics.RigidBody;
import com.ferralith.engine.renderer.DebugDraw;
import com.ferralith.engine.renderer.Texture;
import com.ferralith.engine.scenes.components.EditorCameraMovement;
import com.ferralith.engine.scenes.components.GridLines;
import com.ferralith.engine.scenes.components.MouseControls;
import com.ferralith.engine.scenes.components.TranslateGizmo;
import com.ferralith.engine.utils.AssetPool;
import com.ferralith.engine.utils.Mth;
import com.ferralith.game.models.Pixel;
import com.ferralith.game.models.PixelType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.opengl.GL11.glLineWidth;

public class LevelScene extends Scene {
    private final int width = 512;
    private final int height = 512;
    private final Pixel[][] pixels = new Pixel[width][height];
    private final List<Pixel> dirty_pixels = new ArrayList<>();
    private final ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
    private final Texture texture = new Texture(width, height, 0);
    private Random rand;
    private GameObject go;

    public LevelScene() {
        System.out.println("LEVEL SCENE");
        this.camera = new Camera(new Vector2f(0, 0));
    }

    public LevelScene(Camera camera) {
        this.camera = camera;
    }

    @NotNull
    private static Pixel newAirPixel() {
        return new Pixel(0x00000000, PixelType.Air);
    }

    @Override
    public void init() {
        loadResources();
        addSceneComponent(new MouseControls());
        addSceneComponent(new GridLines());
        addSceneComponent(new EditorCameraMovement(camera));
        SpriteSheet gizmos = AssetPool.getSpritesheet("editor/gizmos.png");
        addSceneComponent(new TranslateGizmo(gizmos.getSprite(1), gizmos.getSprite(0), Window.getImGuiWrapper().getPropertiesWindow()));

        startSceneComponents();

        go = new GameObject("gen");
        go.addComponent(new SpriteRenderer(new Sprite(texture)));
        go.addTag(Tags.doNotSerialize);
        go.transform.scale = new Vector2f(1024, 1024);
        addGameObject(go);

        Dummy dummy = new Dummy("test", new Transform(new Vector2f(400, 400)), 100);
        dummy.removeComponent(RigidBody.class);
        addGameObject(dummy);


        Dummy dummy2 = new Dummy("test", new Transform(new Vector2f(300, 100)), 100);
        dummy2.removeComponent(RigidBody.class);
        addGameObject(dummy2);


        Dummy dummy1 = new Dummy("test1", new Transform(new Vector2f(50, 200)), 100);
        dummy1.getComponent(RigidBody.class).applyForce(new Vector2f(10,10));
        addGameObject(dummy1);

        if (loadedLevel) {
            return;
        }
        rand = new Random();
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                if (false) {
                    pixels[i][j] = newSandPixel();
                } else {
                    pixels[i][j] = newAirPixel();
                }
            }
        }
    }

    private Pixel newSandPixel() {
        return new Pixel(0xFFFFFFFF, PixelType.Stone).setColor(
                new Vector4f(0.96f, 0.84f, 0.69f, 1)
                        .mul(new Vector4f((float) Math.max(rand.nextFloat(0.6f, 1), 0.3))));
    }

    @Override
    public void update(float dt) {
        updateComponents(dt);
        camera.adjustProjective();
        boolean[][] tmp = new boolean[width][height];
        texture.bind();

        buffer.clear();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = pixels[x][y].getColor();
                tmp[y][x] = pixels[x][y].getType() != PixelType.Air;
                byte r = (byte) ((color >> 16) & 0xFF);
                byte g = (byte) ((color >> 8) & 0xFF);
                byte b = (byte) (color & 0xFF);
                byte a = (byte) ((color >> 24) & 0xFF);

                buffer.put(r).put(g).put(b).put(a);
            }
        }
        buffer.flip();

        texture.update(buffer);
        texture.unbind();



        List<Vector2f> polygons = Mth.DouglasPeucker( Mth.marchingSquares(tmp), 10);

        //System.out.println(polygons);
        if (!polygons.isEmpty()) {
            DebugDraw.addPolygonn(polygons, 2f);
        }






        Vector2f pos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        Transform go_t = go.transform;
        Vector2f pixelPerfectPos = new Vector2f((int)(pos.x - (pos.x % 2)), (int)(pos.y - (pos.y % 2)));
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            Dummy dummy1 = new Dummy("test", new Transform(pixelPerfectPos), 100);
            //dummy1.getComponent(RigidBody.class).applyForce(new Vector2f(10,10));
            addGameObject(dummy1);
            if (pos.x > go_t.position.x && pos.x < go_t.position.x + width * 2 &&
                    pos.y > go_t.position.y && pos.y < go_t.position.y + height * 2) {
                DebugDraw.addBox2D(pixelPerfectPos, new Vector2f(100, 100), 0, new Vector3f(1), 1);
                for (int i = 0; i < 50; i++) {
                    for (int j = 0; j < 50; j++) {
                        int x = (int) (pos.x / 2 + i - 25);
                        int y = (int) (pos.y / 2 + j - 25);
                        if (inBounds(x, y)) {
                            pixels[x][y] = newSandPixel();
                        }
                    }
                }
            }

        } else if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT)) {
            if (pos.x > go_t.position.x && pos.x < go_t.position.x + width * 2 &&
                    pos.y > go_t.position.y && pos.y < go_t.position.y + height * 2) {
                DebugDraw.addBox2D(pixelPerfectPos, new Vector2f(100, 100), 0, new Vector3f(1), 1);
                for (int i = 0; i < 50; i++) {
                    for (int j = 0; j < 50; j++) {
                        int x = (int) (pos.x / 2 + i - 25);
                        int y = (int) (pos.y / 2 + j - 25);
                        if (inBounds(x, y)) {
                            pixels[x][y] = newAirPixel();
                        }
                    }
                }
            }
        }


        updateCells(dt);

        glLineWidth(2);
        DebugDraw.addBox2D(new Vector2f(512, 512), new Vector2f(1024, 1024), 0, new Vector3f(1, 0, 0), 1);

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        updatePhysics(dt);

        if (KeyListener.isKeyPressed(KeyEvent.VK_R)) {
            Window.changeScene(0, camera);
            DebugDraw.clear();
        }
    }

    private void updatePhysics(float dt) {
        List<GameObject> list_objects = gameObjects.stream()
                .filter(go -> go.getComponent(AABB.class) != null)
                .toList();

        sweepAndPrune(list_objects);
    }

    private void sweepAndPrune(List<GameObject> objects) {
        List<GameObject> mutableObjects = new ArrayList<>(objects);

        mutableObjects.sort(Comparator.comparingDouble(
                o -> o.getComponent(AABB.class).getMinPos().x
        ));

        for (int i = 0; i < objects.size(); i++) {
            GameObject object1 = objects.get(i);
            AABB aabb1 = object1.getComponent(AABB.class);
            float minX1 = aabb1.getMinPos().x;

            for (int j = i + 1; j < objects.size(); j++) {
                GameObject object2 = objects.get(j);
                AABB aabb2 = object2.getComponent(AABB.class);
                float maxX2 = aabb2.getMaxPos().x;

                if (minX1 > maxX2) {
                    aabb2.setColor(1,1,1);
                    aabb1.setColor(1,1,1);
                    break;
                }

                if (aabb1.intersects(aabb2)) {
                    //do something
                    aabb1.setColor(1,0,0);
                    aabb2.setColor(1,0,0);
                    resolveCollision(object1, object2);
                }
            }
        }
    }

    private void resolveCollision(GameObject a, GameObject b) {
        AABB boxA = a.getComponent(AABB.class);
        AABB boxB = b.getComponent(AABB.class);
        RigidBody bodyA = a.getComponent(RigidBody.class);
        RigidBody bodyB = b.getComponent(RigidBody.class);

        Vector2f aMin = boxA.getMinPos();
        Vector2f aMax = boxA.getMaxPos();
        Vector2f bMin = boxB.getMinPos();
        Vector2f bMax = boxB.getMaxPos();

        // overlap X & Y
        float overlapX = Math.min(aMax.x, bMax.x) - Math.max(aMin.x, bMin.x); // calculating overlapping parts on both axis
        float overlapY = Math.min(aMax.y, bMax.y) - Math.max(aMin.y, bMin.y);

        // by what axis do move obj (which one that has less overlapped parts)
        if (overlapX < overlapY) {
            // move by x
            // todo: add check if there is rigidbody, if not, only affect object with rb
            if (a.transform.position.x < b.transform.position.x) {
                a.transform.position.x -= overlapX / 2; // divide by 2 because there is 2 object to collide
                b.transform.position.x += overlapX / 2;
            } else {
                a.transform.position.x += overlapX / 2;
                b.transform.position.x -= overlapX / 2;
            }
            // reduce speed x-axis
            if (bodyA != null) bodyA.velocity.x = 0;
            if (bodyB != null) bodyB.velocity.x = 0;
        } else {
            // move by y
            if (a.transform.position.y < b.transform.position.y) {
                a.transform.position.y -= overlapY / 2;
                b.transform.position.y += overlapY / 2;
            } else {
                a.transform.position.y += overlapY / 2;
                b.transform.position.y -= overlapY / 2;
            }
            // reduce speed y-axis
            if (bodyA != null) bodyA.velocity.y = 0;
            if (bodyB != null) bodyB.velocity.y = 0;
        }
    }


    private Pixel newWaterPixel() {
        return new Pixel(0xFFFFFFFF, PixelType.Water).setColor(
                new Vector4f(0.1f, 0.1f, 1f, 0.1f)
                        .mul(new Vector4f((float) Math.max(rand.nextFloat(0.6f, 1), 0.3))));
    }

    public void updateCells(float dt) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Pixel cell = pixels[x][y];
                if (cell == null || cell.updated) continue;

                switch (cell.type) {
                    case Sand:
                        tryFall(x, y, 0, - 1);
                        break;
                    case Water:
                        tryFlow(x, y, 0, -1);
                        break;
                }

                cell.updated = true;
            }
        }

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                pixels[x][y].updated = false;
    }

    private void tryFlow(int x, int y, int dx, int dy) {
        tryFall(x, y, 0, -1);

        if (Math.random() > 0.5) {
            if (inBounds(x - 1, y) && pixels[x - 1][y].type == PixelType.Air) swap(x, y, x - 1, y);
            else if (inBounds(x + 1, y) && pixels[x + 1][y].type == PixelType.Air) swap(x, y, x + 1, y);
        } else {
            if (inBounds(x + 1, y) && pixels[x + 1][y].type == PixelType.Air) swap(x, y, x + 1, y);
            else if (inBounds(x - 1, y) && pixels[x - 1][y].type == PixelType.Air) swap(x, y, x - 1, y);
        }
    }

    private void tryFall(int x, int y, int dx, int dy) {
        if (inBounds(x + dx, y + dy) && pixels[x + dx][y + dy].type == PixelType.Air) {
            swap(x, y, x + dx, y + dy);
        } else if (inBounds(x - 1, y + dy) && pixels[x - 1][y + dy].type == PixelType.Air) {
            swap(x, y, x - 1, y + dy);
        } else if (inBounds(x + 1, y + dy) && pixels[x + 1][y + dy].type == PixelType.Air) {
            swap(x, y, x + 1, y + dy);
        }
    }

    private void swap(int x0, int y0, int x1, int y1) {
        if (! inBounds(x0, y0) || ! inBounds(x1, y1)) return;
        Pixel tmp = pixels[x0][y0];
        pixels[x0][y0] = pixels[x1][y1];
        pixels[x1][y1] = tmp;
    }


    private boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private void loadResources() {
        AssetPool.getShader("default");

        // TODO: FIX THIS SHIT
        AssetPool.addSpriteSheet("spritesheets/cat1.png",
                new SpriteSheet(AssetPool.getTexture("spritesheets/cat1.png"),
                        131, 240, 50, 0));
        AssetPool.addSpriteSheet("editor/gizmos.png",
                new SpriteSheet(AssetPool.getTexture("editor/gizmos.png"),
                        24, 48, 2, 0));


        for (GameObject g : gameObjects) {
            if (g.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spriteRenderer = g.getComponent(SpriteRenderer.class);
                if (spriteRenderer.getTexture() != null) {
                    spriteRenderer.setTexture(AssetPool.getTexture(spriteRenderer.getTexture().getPath()));
                }
            }
        }
    }

    @Override
    public void render() {
        this.renderer.render();
    }
}

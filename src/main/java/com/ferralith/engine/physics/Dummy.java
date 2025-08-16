package com.ferralith.engine.physics;

import com.ferralith.engine.GameObject;
import com.ferralith.engine.Transform;
import com.ferralith.engine.components.SpriteRenderer;
import com.ferralith.engine.renderer.Texture;
import com.ferralith.engine.utils.Mth;
import org.joml.Vector2f;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Dummy extends GameObject {
    private AABB box;
    private RigidBody rigidBody;
    private Texture texture;
    private PolygonCollider collider;

    public Dummy(String name, Transform transform, int zIndex) {
        super(name, transform, zIndex);
        this.box = new AABB(new Vector2f(transform.position).add(new Vector2f(transform.scale).div(2f)), transform.scale);
        this.rigidBody = new RigidBody();

        addComponent(rigidBody);
        addComponent(box);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        box.setCenter(new Vector2f(transform.position).add(new Vector2f(transform.scale).div(2f)));
    }

    public void setBoxSize(Vector2f size) {
        this.box.setSize(size);
    }

    public void loadTexture(String path) {
        try {
            stbi_set_flip_vertically_on_load(true);

            InputStream is = getClass().getClassLoader().getResourceAsStream(path);
            if (is == null) {
                throw new RuntimeException("Resource not found in JAR: " + path);
            }
            byte[] bytes = is.readAllBytes();
            ByteBuffer imageBuffer = memAlloc(bytes.length);
            imageBuffer.put(bytes).flip();
            ByteBuffer image = null;
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer x = stack.mallocInt(1);
                IntBuffer y = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);

                image = stbi_load_from_memory(imageBuffer, x, y, channels, 0);
                if (image == null) {
                    throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
                }

                int width = x.get(0);
                int height = y.get(0);
                System.out.println("Loaded texture: " + path + " " + width + "x" + height);
                this.transform.scale = new Vector2f(width * 8, height * 8);
                this.texture = new Texture(width, height, 0);
                this.setBoxSize(new Vector2f(width * 8, height * 8));

                boolean[][] tmp = new boolean[height][width];
                int comp = channels.get(0);
                for (int yf = 0; yf < height; yf++) {
                    for (int xf = 0; xf < width; xf++) {
                        int index = (yf * width + xf) * comp;

                        byte r = image.get(index);
                        byte g = image.get(index + 1);
                        byte b = image.get(index + 2);
                        byte a = (comp == 4) ? image.get(index + 3) : (byte) 0xFF;

                        tmp[yf][xf] = (a & 0xFF) != 0;
                    }
                }

                this.collider = new PolygonCollider(Mth.DouglasPeucker( Mth.marchingSquares(tmp), 1), transform.scale.x / width);
                texture.bind();
                texture.update(image);
                addComponent(this.collider);
                addComponent(new SpriteRenderer(texture));
                texture.unbind();


            } finally {
                if (image != null) stbi_image_free(image);
                memFree(imageBuffer);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource: " + path, e);
        }
    }
}

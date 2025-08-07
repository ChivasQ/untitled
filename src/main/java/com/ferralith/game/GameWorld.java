package com.ferralith.game;

import com.ferralith.game.models.Pixel;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    private Pixel[][] pixels = new Pixel[100][100];
    private List<Pixel> dirty_pixels = new ArrayList<>();

    private void main_temp() {
        ByteBuffer buffer = BufferUtils.createByteBuffer(100 * 100 * 4);

        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
                int color = -0;//get color from dirty? pixel in pixels[] array

                byte r = (byte) ((color >> 16) & 0xFF);
                byte g = (byte) ((color >> 8) & 0xFF);
                byte b = (byte) (color & 0xFF);
                byte a = (byte) ((color >> 24) & 0xFF);

                buffer.put(r).put(g).put(b).put(a);
            }
        }
        buffer.flip();
        //upload buffer
    }
}

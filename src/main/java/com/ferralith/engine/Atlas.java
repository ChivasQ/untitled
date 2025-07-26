package com.ferralith.engine;

import com.ferralith.engine.renderer.Texture;

import java.util.List;

public class Atlas {
    private Texture altas = null;
    private int width, height;
    private List<Texture> textures;


    public void addTexture(Texture texture) {
        this.textures.add(texture);
    }

    public void buildAtlas() {
        int atlasWidth = 0;
        int atlasHeight = 0;

        for (int i = 0; i < textures.size(); i++) {
            Texture texture = textures.get(i);
            
        }
    }
}

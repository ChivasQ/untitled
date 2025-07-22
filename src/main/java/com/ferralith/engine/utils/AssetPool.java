package com.ferralith.engine.utils;

import com.ferralith.Main;
import com.ferralith.engine.components.SpriteSheet;
import com.ferralith.engine.renderer.Shader;
import com.ferralith.engine.renderer.Texture;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    public static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, SpriteSheet> spritesheets = new HashMap<>();

    public static Shader getShader(String resourceName) {
        File file = new File(Paths.SHADER_PATH + resourceName);
        if (AssetPool.shaders.containsKey(file.getAbsolutePath())) {
            return AssetPool.shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(
                    Paths.SHADER_PATH + resourceName + ".fsh",
                    Paths.SHADER_PATH + resourceName + ".vsh");
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName) {
        File file = new File(Paths.TEXTURE_PATH + resourceName);
        if (AssetPool.textures.containsKey(file.getAbsolutePath())) {
            return AssetPool.textures.get(file.getAbsolutePath());
        } else {
            Texture texture = new Texture(Paths.TEXTURE_PATH + resourceName);
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static byte[] loadFontFromResources(String resourceName) {
        try {
            InputStream is = Main.class.getClassLoader().getResourceAsStream(resourceName);
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Texture loadTextureFromResources(String resourceName) {
        return getTexture(resourceName);
    }

    public static Shader loadShaderFromResources(String resourceName) {
        return getShader(resourceName);
    }



    public static void addSpriteSheet(String resourceName, SpriteSheet spriteSheet) {
        File file = new File(Paths.TEXTURE_PATH + resourceName);
        if (!AssetPool.spritesheets.containsKey(file.getAbsolutePath())) {
            AssetPool.spritesheets.put(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static SpriteSheet getSpritesheet(String resourceName) {
        File file = new File(Paths.TEXTURE_PATH + resourceName);
        if (!AssetPool.spritesheets.containsKey(file.getAbsolutePath())) {
            assert false : "Error: tried to access spritesheet '" + file.getAbsolutePath() + "' and it has not been added to asset pool";
        }
        return AssetPool.spritesheets.getOrDefault(file.getAbsolutePath(), null);
    }

}

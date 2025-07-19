package com.ferralith.engine.utils;

import com.ferralith.engine.renderer.Shader;
import com.ferralith.engine.renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    public static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();

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

}

package com.ferralith.engine.gson;

import com.ferralith.engine.Component;
import com.ferralith.engine.GameObject;
import com.ferralith.engine.Transform;
import com.ferralith.engine.components.Sprite;
import com.ferralith.engine.components.SpriteRenderer;
import com.ferralith.engine.renderer.Texture;
import com.ferralith.engine.utils.AssetPool;
import com.google.gson.*;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject> {
    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.getAsJsonArray("components");
        Transform transform = context.deserialize(jsonObject.get("transform"), Transform.class);
        int zIndex = context.deserialize(jsonObject.get("zIndex"), int.class);

        GameObject go = new GameObject(name, transform, zIndex);
        for (JsonElement e : components) {
            Component c = context.deserialize(e, Component.class);

            if (c instanceof SpriteRenderer) {
                SpriteRenderer spriteRenderer = (SpriteRenderer) c;
                spriteRenderer.setDirty();
                if (spriteRenderer.getSprite() != null) {
                    // TODO: FIX BUG WITH TEXTURE LOADING
//                    try {
//                        String texpath = e.getAsJsonObject()
//                                .getAsJsonObject("properties").getAsJsonObject("sprite").getAsJsonObject("texture").get("path").getAsString();
//                        System.out.println(texpath);
//                        //spriteRenderer.setSprite(new Sprite(AssetPool.getTexture(texpath)));
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
                }


            }
            go.addComponent(c);
        }
        return go;
    }
}

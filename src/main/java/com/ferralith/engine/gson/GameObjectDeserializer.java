package com.ferralith.engine.gson;

import com.ferralith.engine.Component;
import com.ferralith.engine.GameObject;
import com.ferralith.engine.Transform;
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

        for (JsonElement element : components) {
            Component c = context.deserialize(element, Component.class);
            //c.gameObject = go;
            go.addComponent(c);
        }

        return go;
    }
}

package com.ferralith.engine.gson;

import com.ferralith.engine.Component;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ComponentDeserializer implements JsonDeserializer<Component> {
    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        try {
            return context.deserialize(element, Class.forName(type));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown element type: " + type, e);
        }
    }
}

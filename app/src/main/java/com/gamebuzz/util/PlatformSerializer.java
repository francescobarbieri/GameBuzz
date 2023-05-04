package com.gamebuzz.util;

import static com.gamebuzz.util.Constants.SERIALIZE_PLATFORM_NAME;

import com.gamebuzz.model.GamePlatform;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class PlatformSerializer implements JsonSerializer<GamePlatform> {

    @Override
    public JsonElement serialize(GamePlatform gamePlatform, Type typeOfSrc, JsonSerializationContext  context) {
        JsonObject json = new JsonObject();
        json.addProperty(SERIALIZE_PLATFORM_NAME, gamePlatform.getName());
        return json;
    }
}

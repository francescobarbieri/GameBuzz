package com.gamebuzz.util;

import static com.gamebuzz.util.Constants.SERIALIZE_PLATFORM_NAME;

import com.gamebuzz.model.GamePlatform;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

public class PlatformDeserializer implements JsonDeserializer<GamePlatform> {

    @Override
    public GamePlatform deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) {
        GamePlatform gamePlatform = new GamePlatform();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        gamePlatform.setName(jsonObject.get(SERIALIZE_PLATFORM_NAME).getAsString());

        return gamePlatform;
    }
}

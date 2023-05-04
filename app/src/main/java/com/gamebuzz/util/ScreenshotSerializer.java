package com.gamebuzz.util;

import static com.gamebuzz.util.Constants.SERIALIZE_SCREENSHOT_URL;

import com.gamebuzz.model.GameScreenshot;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ScreenshotSerializer implements JsonSerializer<GameScreenshot> {

    @Override
    public JsonElement serialize(GameScreenshot gameScreenshot, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty(SERIALIZE_SCREENSHOT_URL, gameScreenshot.getUrl());
        return json;
    }
}

package com.gamebuzz.util;

import static com.gamebuzz.util.Constants.SERIALIZE_SCREENSHOT_URL;

import com.gamebuzz.model.GameScreenshot;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

public class ScreenshotDeserializer implements JsonDeserializer<GameScreenshot> {
    @Override
    public GameScreenshot deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) {
        GameScreenshot gameScreenshot = new GameScreenshot();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        gameScreenshot.setUrl(jsonObject.get(SERIALIZE_SCREENSHOT_URL).getAsString());

        return gameScreenshot;
    }
}

package com.gamebuzz.util;

import static com.gamebuzz.util.Constants.SERIALIZE_THEME_NAME;

import com.gamebuzz.model.GameTheme;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

public class ThemeDeserializer implements JsonDeserializer<GameTheme> {

    @Override
    public GameTheme deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) {
        GameTheme gameTheme = new GameTheme();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        gameTheme.setName(jsonObject.get(SERIALIZE_THEME_NAME).getAsString());

        return gameTheme;
    }
}

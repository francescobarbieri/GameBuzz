package com.gamebuzz.util;

import static com.gamebuzz.util.Constants.SERIALIZE_THEME_NAME;

import com.gamebuzz.model.GameTheme;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ThemeSerializer implements JsonSerializer<GameTheme> {

    @Override
    public JsonElement serialize(GameTheme gameTheme, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty(SERIALIZE_THEME_NAME, gameTheme.getName());
        return json;
    }
}

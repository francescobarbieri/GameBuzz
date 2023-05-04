package com.gamebuzz.util;

import static com.gamebuzz.util.Constants.SERIALIZE_GENRE_NAME;

import com.gamebuzz.model.GameGenre;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class GenreSerializer implements JsonSerializer<GameGenre> {

    @Override
    public JsonElement serialize(GameGenre gameGenre, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty(SERIALIZE_GENRE_NAME, gameGenre.getName());
        return json;
    }
}

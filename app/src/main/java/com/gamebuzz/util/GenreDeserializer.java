package com.gamebuzz.util;

import static com.gamebuzz.util.Constants.SERIALIZE_GENRE_NAME;

import com.gamebuzz.model.GameGenre;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

public class GenreDeserializer implements JsonDeserializer<GameGenre> {

    @Override
    public GameGenre deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) {
        GameGenre gameGenre = new GameGenre();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        gameGenre.setName(jsonObject.get(SERIALIZE_GENRE_NAME).getAsString());

        return gameGenre;
    }
}

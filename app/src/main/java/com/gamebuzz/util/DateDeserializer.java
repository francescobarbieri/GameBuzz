package com.gamebuzz.util;

import static com.gamebuzz.util.Constants.SERIALIZE_DATE_NAME;

import android.util.Log;

import com.gamebuzz.model.GameDate;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

public class DateDeserializer implements JsonDeserializer<GameDate> {

    @Override
    public GameDate deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) {

        GameDate gameDate = new GameDate();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        gameDate.setDate(jsonObject.get(SERIALIZE_DATE_NAME).getAsString());

        return gameDate;
    }
}

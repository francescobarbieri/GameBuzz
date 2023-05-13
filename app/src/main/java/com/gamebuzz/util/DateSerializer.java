package com.gamebuzz.util;

import static com.gamebuzz.util.Constants.SERIALIZE_DATE_NAME;

import android.util.Log;

import com.gamebuzz.model.GameDate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class DateSerializer implements JsonSerializer<GameDate> {

    @Override
    public JsonElement serialize(GameDate gameDate, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty(SERIALIZE_DATE_NAME, gameDate.getDate());

        return json;
    }
}

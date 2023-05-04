package com.gamebuzz.util;

import androidx.room.TypeConverter;

import com.gamebuzz.model.Game;
import com.gamebuzz.model.GameGenre;
import com.gamebuzz.model.GamePlatform;
import com.gamebuzz.model.GameScreenshot;
import com.gamebuzz.model.GameTheme;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Converters {

    // Genre converters

    @TypeConverter
    public static String fromGameGenre(List<GameGenre> gameGenres) {
        Gson gson = new GsonBuilder().registerTypeAdapter(GameGenre.class, new GenreSerializer()).create();
        return gson.toJson(gameGenres);
    }

    @TypeConverter
    public static List<GameGenre> toGameGenre(String json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(GameGenre.class, new GenreDeserializer()).create();
        return gson.fromJson(json, new TypeToken<List<GameGenre>>(){}.getType());
    }

    // Platform converters

    @TypeConverter
    public static String fromGamePlatform(List<GamePlatform> gamePlatformList) {
        Gson gson = new GsonBuilder().registerTypeAdapter(GamePlatform.class, new PlatformSerializer()).create();
        return gson.toJson(gamePlatformList);
    }

    @TypeConverter
    public static List<GamePlatform> toGamePlatform(String json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(GamePlatform.class, new PlatformDeserializer()).create();
        return gson.fromJson(json, new TypeToken<List<GamePlatform>>(){}.getType());
    }

    // Screenshot converters

    @TypeConverter
    public static String fromGameScreenshot(List<GameScreenshot> gameScreenshotList) {
        Gson gson = new GsonBuilder().registerTypeAdapter(GameScreenshot.class, new ScreenshotSerializer()).create();
        return gson.toJson(gameScreenshotList);
    }

    @TypeConverter
    public static List<GameScreenshot> toGameScreenshot(String json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(GameScreenshot.class, new ScreenshotDeserializer()).create();
        return gson.fromJson(json, new TypeToken<List<GameScreenshot>>(){}.getType());
    }

    // Theme converters

    @TypeConverter
    public static String fromGameTheme(List<GameTheme> gameThemeList) {
        Gson gson = new GsonBuilder().registerTypeAdapter(GameTheme.class, new ThemeSerializer()).create();
        return gson.toJson(gameThemeList);
    }

    @TypeConverter
    public static List<GameTheme> toGameTheme(String json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(GameTheme.class, new ThemeDeserializer()).create();
        return gson.fromJson(json, new TypeToken<List<GameTheme>>(){}.getType());
    }

}

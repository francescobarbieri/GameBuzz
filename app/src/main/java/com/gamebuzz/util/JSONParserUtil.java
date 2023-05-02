package com.gamebuzz.util;

import android.app.Application;
import android.util.Log;

import com.gamebuzz.model.Game;
import com.gamebuzz.model.GameApiResponse;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JSONParserUtil {

    private static final String TAG = JSONParserUtil.class.getSimpleName();

    private final Application application;

    public JSONParserUtil(Application application) {
        this.application = application;
    }

    // TODO:  deve ritornare un GameApiResponse
    public void parseJSONWithJSONObjectArray(String filename) throws IOException, JSONException {
        InputStream inputStream = application.getAssets().open(filename);

        String  content  = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        JSONArray rootJSONOArray = new JSONArray(content);
        int itemCount = rootJSONOArray.length();

        List<Game> gameList = null;
        if(itemCount > 0) {
            gameList = new ArrayList<>();
            Game game;
            for(int i = 0;  i < itemCount; i++) {
                JSONObject gameJSONObject = rootJSONOArray.getJSONObject(i);

                Log.d(TAG, "id: " + gameJSONObject.getInt("id"));
                Log.d(TAG, "name: " + gameJSONObject.getString("name"));
            }
        }

    }


    public GameApiResponse parseJSONFileWithGSON(String filename) throws IOException {
        InputStream inputStream = application.getAssets().open(filename);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        return new Gson().fromJson(bufferedReader, GameApiResponse.class);
    }
}

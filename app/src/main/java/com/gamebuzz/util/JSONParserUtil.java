package com.gamebuzz.util;

import android.app.Application;
import android.util.Log;

import com.gamebuzz.model.Game;
import com.gamebuzz.model.GameApiResponse;
import com.gamebuzz.model.GameCompany;
import com.gamebuzz.model.GameCover;
import com.gamebuzz.model.GameDate;
import com.gamebuzz.model.GameGenre;
import com.gamebuzz.model.GamePlatform;
import com.gamebuzz.model.GameScreenshot;
import com.gamebuzz.model.GameTheme;
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

    public enum JsonParserType {
        JSON_OBJECT_ARRAY,
        JSON_ERROR
    };

    public JSONParserUtil(Application application) {
        this.application = application;
    }

    // TODO:  deve ritornare un GameApiResponse
    public GameApiResponse parseJSONWithJSONObjectArray(String filename) throws IOException, JSONException {
        InputStream inputStream = null;

        String  content  = null;

        GameApiResponse gameApiResponse = new GameApiResponse();

        List<Game> gameList = null;

        try {
            inputStream = application.getResources().getAssets().open(filename);
            content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            JSONArray rootJSONOArray = null;
            rootJSONOArray = new JSONArray(content);


            int itemCount = rootJSONOArray.length();

            gameList = new ArrayList<>();

            if(itemCount > 0) {
                Game game;
                for(int i = 0;  i < itemCount; i++) {
                    JSONObject gameJSONObject = null;

                    gameJSONObject = rootJSONOArray.getJSONObject(i);

                    game = new Game();

                    /*
                        id*
                        cover[]*
                        genres[]*
                        involved_companies[]*
                        name*
                        platforms[]*
                        release_dates[]
                        screenshots[]
                        summary*
                        themes[]*
                     */
                    // id
                    // name
                    // summary
                    game.setGameId(gameJSONObject.getInt("id"));
                    game.setTitle(gameJSONObject.getString("name"));
                    game.setSummary(gameJSONObject.getString("summary"));

                    // cover
                    int coverId = gameJSONObject.getJSONObject("cover").getInt("id");
                    String coverUrl = gameJSONObject.getJSONObject("cover").getString("url");
                    game.setCover(new GameCover(coverId, coverUrl));

                    // genres
                    int genresCount = gameJSONObject.getJSONArray("genres").length();
                    List<GameGenre> gameGenresList = new ArrayList<>();
                    for(int j = 0; j < genresCount; j++) {
                        String gameGenreName = gameJSONObject.getJSONArray("genres").getJSONObject(j).getString("name");
                        GameGenre gameGenre = new GameGenre(gameGenreName);
                        gameGenresList.add(gameGenre);
                    }
                    game.setGameGenre(gameGenresList);

                    // involved_company
                    String companyName = gameJSONObject.getJSONArray("involved_companies").getJSONObject(0).getJSONObject("company").getString("name");
                    game.setGameCompany(new GameCompany(companyName));

                    // paltforms
                    int platformCount = gameJSONObject.getJSONArray("platforms").length();
                    List<GamePlatform> gamePlatformList = new ArrayList<>();
                    for(int j = 0; j < platformCount; j++) {
                        String gameCompanyName = gameJSONObject.getJSONArray("platforms").getJSONObject(j).getString("name");
                        GamePlatform gamePlatform = new GamePlatform(gameCompanyName);
                        gamePlatformList.add(gamePlatform);
                    }
                    game.setGamePlatform(gamePlatformList);

                    // release_dates
                    if(gameJSONObject.has("release_dates"))
                    {
                        String date = gameJSONObject.getJSONArray("release_dates").getJSONObject(0).getString("human");
                        game.setReleaseDate(new GameDate(date));
                    } else {
                        game.setReleaseDate(new GameDate());
                    }

                    // screenshots
                    if(gameJSONObject.has("screenshots")) {
                        int screenCount = gameJSONObject.getJSONArray("screenshots").length();
                        List<GameScreenshot> gameScreenshotList = new ArrayList<>();
                        for(int j = 0; j < screenCount; j++) {
                            String screenUrl = gameJSONObject.getJSONArray("screenshots").getJSONObject(j).getString("url");
                            GameScreenshot gameScreen = new GameScreenshot(screenUrl);
                            gameScreenshotList.add(gameScreen);
                        }
                        game.setScreenshotList(gameScreenshotList);
                    }

                    // themes
                    int themesCount = gameJSONObject.getJSONArray("themes").length();
                    List<GameTheme> gameThemeList = new ArrayList<>();
                    for(int j = 0; j < themesCount; j++) {
                        String themeName = gameJSONObject.getJSONArray("themes").getJSONObject(j).getString("name");
                        GameTheme gameTheme = new GameTheme(themeName);
                        gameThemeList.add(gameTheme);
                    }
                    game.setGameThemeList(gameThemeList);


                    gameList.add(game);
                }
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        gameApiResponse.setGamesList(gameList);

        Log.d(TAG, "msg: " + gameApiResponse);

        return gameApiResponse;

    }


    public GameApiResponse parseJSONFileWithGSON(String filename) throws IOException {
        InputStream inputStream = application.getAssets().open(filename);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        return new Gson().fromJson(bufferedReader, GameApiResponse.class);
    }
}

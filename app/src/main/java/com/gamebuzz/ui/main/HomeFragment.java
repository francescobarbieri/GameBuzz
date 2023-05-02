package com.gamebuzz.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gamebuzz.R;
import com.gamebuzz.adapter.GameRecyclerViewAdapter;
import com.gamebuzz.model.Game;
import com.gamebuzz.model.GameApiResponse;
import com.gamebuzz.model.GameCompany;
import com.gamebuzz.model.GameCover;
import com.gamebuzz.model.GameDate;
import com.gamebuzz.model.GameGenre;
import com.gamebuzz.model.GamePlatform;
import com.gamebuzz.model.GameScreenshot;
import com.gamebuzz.model.GameTheme;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private GameRecyclerViewAdapter gameRecyclerViewAdapter;

    private List<Game> gameList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
        for(int i = 0; i < 40; i++) {
            gameList.add(new Game("Title: " + i));
        }
        */

        // List<Game> gameList = parseJson();

        List<Game> gameList = parseJsonTest();

        RecyclerView recyclerViewGame = view.findViewById(R.id.recyclerview_game);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false);

        gameRecyclerViewAdapter = new GameRecyclerViewAdapter(gameList, new GameRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onGameItemClick(Game game) {
                Snackbar.make(view, game.getTitle(), Snackbar.LENGTH_SHORT).show();
            }
        });

        recyclerViewGame.setLayoutManager(layoutManager);
        recyclerViewGame.setAdapter(gameRecyclerViewAdapter);
    }

    private List<Game> parseJson() {
        InputStream inputStream = null;
        try {
            inputStream = requireActivity().getAssets().open("test.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        //  GameApiResponse[] newsApiResponse =  new Gson().fromJson(bufferedReader, GameApiResponse[].class);

        GameApiResponse newsApiResponse =  new Gson().fromJson(bufferedReader, GameApiResponse.class);

        Log.e(TAG, newsApiResponse.toString());


        return newsApiResponse.getGamesList();
    }

    public List<Game> parseJsonTest() {
        InputStream inputStream = null;

        String  content  = null;

        GameApiResponse gameApiResponse = new GameApiResponse();

        List<Game> gameList = null;

        try {
            inputStream = requireActivity().getResources().getAssets().open("game-list.json");
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
                    game.setId(gameJSONObject.getInt("id"));
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

        return gameApiResponse.getGamesList();
    }

}
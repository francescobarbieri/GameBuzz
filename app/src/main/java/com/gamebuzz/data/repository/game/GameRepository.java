package com.gamebuzz.data.repository.game;

import android.app.Application;

import androidx.annotation.NonNull;

import com.gamebuzz.data.database.GameDao;
import com.gamebuzz.data.database.GameRoomDatabase;
import com.gamebuzz.model.Game;
import com.gamebuzz.service.GameApiService;
import com.gamebuzz.util.ServiceLocator;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameRepository implements IGameRepository {

    private static final String TAG = GameRepository.class.getSimpleName();

    private final Application application;

    private final GameApiService gameApiService;
    private final GameDao gameDao;
    private final GameResponseCallback gameResponseCallback;

    public GameRepository (Application application, GameResponseCallback gameResponseCallback) {
        this.application = application;
        this.gameApiService = ServiceLocator.getInstance().getGameApiService();
        GameRoomDatabase gameRoomDatabase = ServiceLocator.getInstance().getGameDao(application);
        this.gameDao = gameRoomDatabase.gameDao();
        this.gameResponseCallback = gameResponseCallback;
    }

    @Override
    public void fetchGames() {

        String text = "fields name, summary, genres.name, cover.url, release_dates.human, screenshots.url, involved_companies.company.name, themes.name, platforms.name; where summary != null & genres.name != null & cover.url != null & involved_companies != null & themes != null & platforms != null;  limit 20;";
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), text);

        Call<List<Game>> gameResponseCall = gameApiService.getGames(body);

        gameResponseCall.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(@NonNull Call<List<Game>> call,
                                   @NonNull Response<List<Game>> response) {
                if(response.body() != null && response.isSuccessful()) {
                    saveDataInDatabase(response.body());
                } else {
                    gameResponseCallback.onFailure("Error retrieving the games");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Game>> call, @NonNull Throwable t) {
                gameResponseCallback.onFailure(t.getMessage());
            }
        });
    }

    public void saveDataInDatabase(List<Game> gameList) {
        GameRoomDatabase.databaseWriteExecutor.execute( () -> {
            List<Game> allGames = gameDao.getAll();

            for(Game game : allGames) {
                if(gameList.contains(game)) {
                    gameList.set(gameList.indexOf(game), game);
                }
            }

            List<Long> insertedGameIds = gameDao.insertGameList(gameList);
            for(int i = 0; i < gameList.size(); i++) {
                gameList.get(i).setId(insertedGameIds.get(i));
            }

            gameResponseCallback.onSuccess(gameList);
        });
    }

    @Override
    public void getFavoriteGames() {

    }

    @Override
    public void deleteFavoriteGames() {

    }

}
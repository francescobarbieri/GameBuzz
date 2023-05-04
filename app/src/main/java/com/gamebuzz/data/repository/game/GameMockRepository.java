package com.gamebuzz.data.repository.game;

import android.app.Application;
import android.app.GameManager;

import com.gamebuzz.data.database.GameDao;
import com.gamebuzz.data.database.GameRoomDatabase;
import com.gamebuzz.model.Game;
import com.gamebuzz.model.GameApiResponse;
import com.gamebuzz.util.JSONParserUtil;
import com.gamebuzz.util.ServiceLocator;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class GameMockRepository implements IGameRepository {

    private final Application application;
    private final GameResponseCallback gameResponseCallback;
    private final GameDao gameDao;
    private final JSONParserUtil.JsonParserType jsonParserType;


    public GameMockRepository(Application application, GameResponseCallback gameResponseCallback, JSONParserUtil.JsonParserType jsonParserType) {
        this.application = application;
        this.gameResponseCallback = gameResponseCallback;
        GameRoomDatabase gameRoomDatabase = ServiceLocator.getInstance().getGameDao(application);
        this.gameDao = gameRoomDatabase.gameDao();

        this.jsonParserType = jsonParserType;
    }

    @Override
    public void fetchGames() {
        GameApiResponse gameApiResponse = null;
        JSONParserUtil jsonParserUtil = new JSONParserUtil(application);

        switch (jsonParserType) {
            case JSON_OBJECT_ARRAY:
                try {
                    gameApiResponse = jsonParserUtil.parseJSONWithJSONObjectArray("game-list.json");
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                break;
            case JSON_ERROR:
                gameResponseCallback.onFailure("Error in retrieving the games");
                break;
        }

        if(gameApiResponse != null) {
            saveDataInDatabase(gameApiResponse.getGamesList());
        } else {
            gameResponseCallback.onFailure("Error in retrieving the games");
        }
    }

    @Override
    public void getFavoriteGames() {
        //TODO: add room db
    }

    @Override
    public void deleteFavoriteGames() {
        // TODO: add room db
    }

    private void saveDataInDatabase(List<Game> gameList) {
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
}

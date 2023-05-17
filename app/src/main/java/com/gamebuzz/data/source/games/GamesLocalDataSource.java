package com.gamebuzz.data.source.games;

import com.gamebuzz.data.database.GameDao;
import com.gamebuzz.data.database.GameRoomDatabase;
import com.gamebuzz.model.Game;
import com.gamebuzz.model.GameApiResponse;
import com.gamebuzz.service.GameApiService;
import com.google.android.gms.common.util.SharedPreferencesUtils;

import java.util.List;

public class GamesLocalDataSource extends BaseGamesLocalDataSource {

    private final GameDao gameDao;
    //TODO: private final SharedPreferencesUtils sharedPreferencesUtils;
    //TODO: private final DataEncryptionUtil dataEncryptionUtil;

    public GamesLocalDataSource(GameRoomDatabase gameRoomDatabase) {
        this.gameDao = gameRoomDatabase.gameDao();
        // this.sharedPreferenceUtils = sharedPreferenceUtils;
        // this.dataEncryptionUtil = dataEncryptionUtil;
    }

    @Override
    public void getGames() {
        GameRoomDatabase.databaseWriteExecutor.execute( () -> {
            GameApiResponse gameApiResponse = new GameApiResponse();
            gameApiResponse.setGamesList(gameDao.getAll());
            gamesCallback.onSuccessFromLocal(gameApiResponse);
        });
    }

    @Override
    public void getFavoriteGames() {
        GameRoomDatabase.databaseWriteExecutor.execute( () -> {
            List<Game> favoriteGames = gameDao.getFavoriteGame();
            gamesCallback.onGameFavoriteStatusChanged(favoriteGames);
        });
    }

    @Override
    public void updateGames(Game game) {
        // TODO: do this
    }

    @Override
    public void insertGames(GameApiResponse gameApiResponse) {
        GameRoomDatabase.databaseWriteExecutor.execute( () -> {
            List<Game> allGames = gameDao.getAll();
            List<Game> gameList = gameApiResponse.getGamesList();

            if(gameList != null) {
                for(Game game : allGames) {
                    if(gameList.contains(game)) {
                        gameList.set(gameList.indexOf(game), game);
                    }
                }

                List<Long> insertedGamesIds = gameDao.insertGameList(gameList);
                for(int i = 0; i < gameList.size(); i++) {
                    gameList.get(i).setId((insertedGamesIds.get(i)));
                }

                // TODO: sharedPreferencesUtil.writeStringData(SHARED_PREFERENCES_FILE_NAME, LAST_UPDATE, String.valueOf(System.currentTimeMillis()));

                gamesCallback.onSuccessFromLocal(gameApiResponse);
            }
        });
    }

    @Override
    public void insertGames(List<Game> gameList) {
        GameRoomDatabase.databaseWriteExecutor.execute( () -> {
            if(gameList != null) {
                List<Game> allGames = gameDao.getAll();

                for(Game game : allGames) {
                    if(gameList.contains(game)) {
                        game.setSynchronized(true);
                        gameList.set(gameList.indexOf(game), game);
                    }
                }

                List<Long> insertedGamesIds = gameDao.insertGameList(gameList);
                for(int i = 0; i < gameList.size(); i++) {
                    gameList.get(i).setId((insertedGamesIds.get(i)));
                }

                GameApiResponse gameApiResponse = new GameApiResponse(gameList);
                gamesCallback.onSuccessSynchronization();
            }
        });
    }

    public void deleteAll() {
        GameRoomDatabase.databaseWriteExecutor.execute( () -> {
            int gameCounter = gameDao.getAll().size();
            int gameDeletedGames = gameDao.deleteAll();

            if(gameDeletedGames == gameCounter) {
                // TODO: sharedPreferencesUtil.deleteAll(SHARED_PREFERENCES_FILE_NAME);
                // TODO: dataEncryptionUtil.deleteAll(ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, ENCRYPTED_DATA_FILE_NAME);
                gamesCallback.onSuccessDeletion();
            }
        });
    }
}

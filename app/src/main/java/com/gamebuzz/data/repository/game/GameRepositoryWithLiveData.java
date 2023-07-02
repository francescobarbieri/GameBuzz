package com.gamebuzz.data.repository.game;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.gamebuzz.data.source.games.BaseFavoriteGamesDataSource;
import com.gamebuzz.data.source.games.BaseGamesLocalDataSource;
import com.gamebuzz.data.source.games.BaseGamesRemoteDataSource;
import com.gamebuzz.data.source.games.GamesCallback;
import com.gamebuzz.model.Game;
import com.gamebuzz.model.GameApiResponse;
import com.gamebuzz.model.Result;

import java.security.cert.Extension;
import java.util.ArrayList;
import java.util.List;

public class GameRepositoryWithLiveData implements IGamesRepositoryWithLiveData, GamesCallback {

    private static final String TAG = GameRepositoryWithLiveData.class.getSimpleName();

    private final MutableLiveData<Result> allGamesMutableLiveData;

    private final MutableLiveData<Result> favoriteGamesMutableLiveData;

    private final BaseGamesRemoteDataSource gamesRemoteDataSource;

    private final BaseGamesLocalDataSource gamesLocalDataSource;

    private final BaseFavoriteGamesDataSource backupDataSource;


    public GameRepositoryWithLiveData(BaseGamesRemoteDataSource gameRemoteDataSource, BaseGamesLocalDataSource gamesLocalDataSource, BaseFavoriteGamesDataSource favoriteGamesDataSource) {
        allGamesMutableLiveData = new MutableLiveData<>();
        favoriteGamesMutableLiveData = new MutableLiveData<>();

        this.gamesRemoteDataSource = gameRemoteDataSource;
        this.gamesLocalDataSource = gamesLocalDataSource;
        this.backupDataSource = favoriteGamesDataSource;
        this.gamesRemoteDataSource.setGamesCallback(this);
        this.gamesLocalDataSource.setGamesCallback(this);
        this.backupDataSource.setGamesCallback(this);
    }


    @Override
    public MutableLiveData<Result> fetchGames() {
        gamesRemoteDataSource.getGames();
        return allGamesMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> searchGames(String query) {
        gamesRemoteDataSource.searchGames(query);
        return allGamesMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getFavoriteGames(boolean isFirstLoading) {
        if(isFirstLoading) {
            backupDataSource.getFavoriteGames();
        } else {
            gamesLocalDataSource.getFavoriteGames();
        }
        return favoriteGamesMutableLiveData;
    }

    @Override
    public void updateGames(Game game) {
        gamesLocalDataSource.updateGames(game);

        if(game.getFavorite()) {
            backupDataSource.addFavoriteGames(game);
        } else {
            backupDataSource.deleteFavoriteGames(game);
        }
    }

    @Override
    public void onSuccessFromRemote(GameApiResponse gameApiResponse) {
        gamesLocalDataSource.insertGames(gameApiResponse);
    }

    @Override
    public void onFailureFromRemote(Exception exception) {
        Result.Error result = new Result.Error(exception.getMessage());
        allGamesMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromLocal(GameApiResponse gameApiResponse) {
        if(allGamesMutableLiveData.getValue() != null && allGamesMutableLiveData.getValue().isSuccess()) {
            List<Game> gameList = ((Result.GameResponseSuccess)allGamesMutableLiveData.getValue()).getData().getGamesList();
            gameList.addAll(gameApiResponse.getGamesList());
            gameApiResponse.setGamesList(gameList);
            Result.GameResponseSuccess result = new Result.GameResponseSuccess(gameApiResponse);
            allGamesMutableLiveData.postValue(result);
        } else {
            Result.GameResponseSuccess result = new Result.GameResponseSuccess(gameApiResponse);
            allGamesMutableLiveData.postValue(result);
        }
    }

    @Override
    public void onFailureFromLocal(Exception e) {
        Result.Error resultError = new Result.Error(e.getMessage());
        allGamesMutableLiveData.postValue(resultError);
        favoriteGamesMutableLiveData.postValue(resultError);
    }

    @Override
    public void onGameFavoriteStatusChanged(Game game, List<Game> favoriteGames) {
        Result allGamesResult = allGamesMutableLiveData.getValue();


        if(allGamesResult != null & allGamesResult.isSuccess()) {
            List<Game> oldAllGames = ((Result.GameResponseSuccess)allGamesResult).getData().getGamesList();
            if(oldAllGames.contains(game)) {
                oldAllGames.set(oldAllGames.indexOf(game), game);
                allGamesMutableLiveData.postValue(allGamesResult);
            }
        }
        favoriteGamesMutableLiveData.postValue(new Result.GameResponseSuccess(new GameApiResponse(favoriteGames)));
    }

    @Override
    public void onGameFavoriteStatusChanged(List<Game> gameList) {
        List<Game> notSynchronizedGameList = new ArrayList<>();

        for(Game game : gameList) {
            if(!game.isSynchronized()) {
                notSynchronizedGameList.add(game);
            }
        }

        if(!notSynchronizedGameList.isEmpty()) {
            backupDataSource.synchronizeFavoriteGames(notSynchronizedGameList);
        }

        favoriteGamesMutableLiveData.postValue(new Result.GameResponseSuccess(new GameApiResponse(gameList)));
    }

    @Override
    public void onDeleteFavoriteGameSuccess(List<Game> favoriteGames) {
        Result allGamesResult = allGamesMutableLiveData.getValue();

        if(allGamesResult != null && allGamesResult.isSuccess()) {
            List<Game> oldAllGames = ((Result.GameResponseSuccess)allGamesResult).getData().getGamesList();
            for(Game game : favoriteGames) {
                if(oldAllGames.contains(game)) {
                    oldAllGames.set(oldAllGames.indexOf(game), game);
                }
            }
            allGamesMutableLiveData.postValue(allGamesResult);
        }

        if(favoriteGamesMutableLiveData.getValue() != null &&
            favoriteGamesMutableLiveData.getValue().isSuccess()) {
            favoriteGames.clear();
            Result.GameResponseSuccess result = new Result.GameResponseSuccess(new GameApiResponse(favoriteGames));
            favoriteGamesMutableLiveData.postValue(result);
        }

        // TODO: IMPORTANT backupDataSource.deleteAllFavoriteGames();
    }

    @Override
    public void onSuccessFromCloudReading(List<Game> gameList) {
        if(gameList != null) {
            for(Game game : gameList) {
                game.setSynchronized(true);
            }
            gamesLocalDataSource.insertGames(gameList);
            favoriteGamesMutableLiveData.postValue(new Result.GameResponseSuccess(new GameApiResponse(gameList)));
        }
    }

    @Override
    public void onSuccessFromCloudWriting(Game game) {
        if(game != null && !game.getFavorite()) {
            game.setSynchronized(false);
        }
        gamesLocalDataSource.updateGames(game);
        backupDataSource.getFavoriteGames();
    }

    @Override
    public void onSuccessSynchronization() { Log.d(TAG, "Games synchronized from remote"); }

    @Override
    public void onFailureFromCloud(Exception e) {}

    @Override
    public void onSuccessDeletion() {}

}

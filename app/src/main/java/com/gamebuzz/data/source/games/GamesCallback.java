package com.gamebuzz.data.source.games;

import com.gamebuzz.model.Game;
import com.gamebuzz.model.GameApiResponse;

import java.util.List;

public interface GamesCallback {

    void onSuccessFromRemote(GameApiResponse gameApiResponse);

    void onFailureFromRemote(Exception exception);

    void onSuccessFromLocal(GameApiResponse gameApiResponse);

    void onFailureFromLocal(Exception exception);

    void onGameFavoriteStatusChanged(Game game, List<Game> favoriteGames);

    void onGameFavoriteStatusChanged(List<Game> games);

    void onDeleteFavoriteGameSuccess(List<Game> favoriteGames);

    void onSuccessFromCloudReading(List<Game> gameList);

    void onSuccessFromCloudWriting(Game game);

    void onFailureFromCloud(Exception exception);

    void onSuccessSynchronization();

    void onSuccessDeletion();

}

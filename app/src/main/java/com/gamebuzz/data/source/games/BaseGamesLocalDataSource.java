package com.gamebuzz.data.source.games;

import com.gamebuzz.model.Game;
import com.gamebuzz.model.GameApiResponse;

import java.util.List;

public abstract class BaseGamesLocalDataSource {

    protected GamesCallback gamesCallback;

    public void setGamesCallback(GamesCallback gamesCallback) { this.gamesCallback = gamesCallback; }

    public abstract void getGames();

    public abstract void getFavoriteGames();

    public abstract void updateGames(Game game);

    public abstract void insertGames(GameApiResponse gameApiResponse);

    public abstract void insertGames(List<Game> gameList);

    public abstract void deleteAll();
}

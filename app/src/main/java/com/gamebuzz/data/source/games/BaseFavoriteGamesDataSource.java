package com.gamebuzz.data.source.games;

import com.gamebuzz.model.Game;

import java.util.List;

public abstract class BaseFavoriteGamesDataSource {

    protected GamesCallback gamesCallback;


    public void setGamesCallback(GamesCallback gamesCallback) { this.gamesCallback = gamesCallback; }

    public abstract void getFavoriteGames();

    public abstract void addFavoriteGames(Game game);

    public abstract void synchronizeFavoriteGames(List<Game> notSynchronizedGamesList);

    public abstract void deleteFavoriteGames(Game game);
}

package com.gamebuzz.data.source.games;

public abstract class BaseGamesRemoteDataSource {

    protected GamesCallback gamesCallback;

    public void setGamesCallback(GamesCallback gamesCallback) { this.gamesCallback = gamesCallback; }

    public abstract void getGames();

    public abstract void searchGames(String query);
}

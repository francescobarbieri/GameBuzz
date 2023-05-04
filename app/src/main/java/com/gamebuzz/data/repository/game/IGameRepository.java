package com.gamebuzz.data.repository.game;

public interface IGameRepository {
    void fetchGames();

    // TODO: serve questo metodo?
    // void updateGames();

    void getFavoriteGames();
    void deleteFavoriteGames();
}

package com.gamebuzz.data.repository.game;

public interface IGameRepository {
    void fetchGames();

    public enum JsonParserType {
        JSON_OBJECT_ARRAY,
        JSON_ERROR
    };

    // TODO: serve questo metodo?
    // void updateGames();

    void getFavoriteGames();
    void deleteFavoriteGames();
}

package com.gamebuzz.data.repository.game;

import androidx.lifecycle.MutableLiveData;

import com.gamebuzz.model.Game;
import com.gamebuzz.model.Result;

public interface IGamesRepositoryWithLiveData {
    MutableLiveData<Result> fetchGames();

    MutableLiveData<Result> searchGames(String query);

    MutableLiveData<Result> getFavoriteGames(boolean firsLoading);

    void updateGames(Game game);

}

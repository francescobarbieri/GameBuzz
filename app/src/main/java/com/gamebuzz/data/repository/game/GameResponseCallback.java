package com.gamebuzz.data.repository.game;

import androidx.lifecycle.ViewModelProvider;

import com.gamebuzz.model.Game;
import com.gamebuzz.model.GameApiResponse;

import java.util.List;

public interface GameResponseCallback {
    void onSuccess(List<Game> gameList);
    void onFailure(String errorMessage);

    // TODO: metodi per la gestione dei preferiti
    // void onGameFavoriteStatusChanged(Game game);
}

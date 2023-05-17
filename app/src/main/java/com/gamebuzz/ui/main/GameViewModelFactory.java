package com.gamebuzz.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gamebuzz.data.repository.game.IGamesRepositoryWithLiveData;

public class GameViewModelFactory implements ViewModelProvider.Factory {

    private final IGamesRepositoryWithLiveData iGamesRepositoryWithLiveData;

    public GameViewModelFactory(IGamesRepositoryWithLiveData iGamesRepositoryWithLiveData) {
        this.iGamesRepositoryWithLiveData = iGamesRepositoryWithLiveData;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new GameViewModel(iGamesRepositoryWithLiveData);
    }
}

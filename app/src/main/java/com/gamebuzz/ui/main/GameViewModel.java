package com.gamebuzz.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gamebuzz.data.repository.game.IGamesRepositoryWithLiveData;
import com.gamebuzz.model.Game;
import com.gamebuzz.model.Result;

public class GameViewModel extends ViewModel {

    private static final String TAG = GameViewModel.class.getSimpleName();

    private final IGamesRepositoryWithLiveData gameRepositoryWithLiveData;
    private boolean isLoading;
    private boolean firstLoading;

    private MutableLiveData<Result> gameListLiveData;
    private MutableLiveData<Result> favoriteGameListLiveData;

    public GameViewModel(IGamesRepositoryWithLiveData iGameRepositoryWithLiveData) {
        this.gameRepositoryWithLiveData = iGameRepositoryWithLiveData;
        this.firstLoading = true;
    }

    public MutableLiveData<Result> getGames() {
        if(gameListLiveData == null) {
            fetchGames();
        }
        return gameListLiveData;
    }

    public MutableLiveData<Result> getGames(String query) {
        if(gameListLiveData == null) {
            searchGames(query);
        }
        return gameListLiveData;
    }

    public MutableLiveData<Result> getFavoriteGamesLiveData(boolean firstLoading) {
        if(favoriteGameListLiveData == null) {
            getFavoriteGames(firstLoading);
        }
        return favoriteGameListLiveData;
    }

    public void updateGames(Game game) { gameRepositoryWithLiveData.updateGames(game); }

    private void fetchGames() {
        gameListLiveData = gameRepositoryWithLiveData.fetchGames();
    }

    private void searchGames(String query) { gameListLiveData = gameRepositoryWithLiveData.searchGames(query); }

    public void getFavoriteGames(boolean firstLoading) {
        favoriteGameListLiveData = gameRepositoryWithLiveData.getFavoriteGames(firstLoading);
    }

    public void removeFromFavorite(Game game) { gameRepositoryWithLiveData.updateGames(game); }

    public boolean isLoading() { return isLoading; }

    public void setLoading(boolean loading) { isLoading = loading; }

    public boolean isFirstLoading() { return firstLoading; }

    public void setFirstLoading(boolean firstLoading) { this.firstLoading = firstLoading; }

    public MutableLiveData<Result> getGameResponseLiveData() { return gameListLiveData; }
}

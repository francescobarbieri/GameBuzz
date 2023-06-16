package com.gamebuzz.data.repository.user;

import androidx.lifecycle.MutableLiveData;

import com.gamebuzz.data.source.games.BaseGamesLocalDataSource;
import com.gamebuzz.data.source.games.GamesCallback;
import com.gamebuzz.data.source.user.BaseUserDataRemoteDataSource;
import com.gamebuzz.data.source.user.user.BaseUserAuthenticationRemoteDataSource;
import com.gamebuzz.model.Game;
import com.gamebuzz.model.GameApiResponse;
import com.gamebuzz.model.Result;
import com.gamebuzz.model.User;
import com.gamebuzz.util.DataEncryptionUtil;
import com.google.android.gms.common.internal.ResourceUtils;

import java.util.List;

public class UserRepository implements IUserRepository, UserResponseCallback, GamesCallback {

    private static final String TAG = UserRepository.class.getSimpleName();

    private final BaseUserAuthenticationRemoteDataSource userRemoteDataSource;
    private final BaseUserDataRemoteDataSource userDataRemoteDataSource;
    private final MutableLiveData<Result> userMutableLiveData;
    private final MutableLiveData<Result> userFavoriteGamesMutableLiveData;
    private final BaseGamesLocalDataSource gamesLocalDataSource;


    public UserRepository (BaseUserAuthenticationRemoteDataSource userRemoteDataSource, BaseGamesLocalDataSource baseGamesLocalDataSource, BaseUserDataRemoteDataSource userDataRemoteDataSource) {
        this.userRemoteDataSource = userRemoteDataSource;
        this.userDataRemoteDataSource = userDataRemoteDataSource;
        this.gamesLocalDataSource = baseGamesLocalDataSource;

        this.userMutableLiveData = new MutableLiveData<>();
        this.userFavoriteGamesMutableLiveData = new MutableLiveData<>();

        this.userRemoteDataSource.setUserResponseCallback(this);
        this.gamesLocalDataSource.setGamesCallback(this);
        this.userDataRemoteDataSource.setUserResponseCallback(this);
    }

    @Override
    public MutableLiveData<Result> getUser (String email, String password, boolean isUserRegistered) {
        if(isUserRegistered) {
            login(email, password);
        } else {
            signUp(email, password);
        }

        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getGoogleUser(String idToken) {
        signInWithGoogle(idToken);
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> logout() {
        userRemoteDataSource.logout();
        return userMutableLiveData;
    }

    @Override
    public User getLoggedUser() {
        return userRemoteDataSource.getLoggedUser();
    }

    @Override
    public void signUp(String email, String password) {
        userRemoteDataSource.signUp(email, password);
    }

    @Override
    public void login(String email, String password) {
        userRemoteDataSource.login(email, password);
    }

    @Override
    public MutableLiveData<Result> getUserFavoriteGames(String idToken) {
        userDataRemoteDataSource.getUserFavoriteGames(idToken);
        return userFavoriteGamesMutableLiveData;
    }

    @Override
    public void signInWithGoogle(String token) {
        userRemoteDataSource.signInWithGoogle(token);
    }

    @Override
    public void onSuccessFromAuthentication (User user) {
        if (user != null) {
            userDataRemoteDataSource.saveUserData(user);
        }
    }

    @Override
    public void onFailureFromAuthentication(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessLogout() {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(null);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(User user) {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(user);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(List<Game> gameList) {
        gamesLocalDataSource.insertGames(gameList);
    }

    @Override
    public void onFailureFromRemoteDatabase(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemote(GameApiResponse gameApiResponse) {

    }

    @Override
    public void onFailureFromRemote(Exception exception) {

    }

    @Override
    public void onSuccessFromLocal(GameApiResponse gameApiResponse) {
        Result.GameResponseSuccess result = new Result.GameResponseSuccess(gameApiResponse);
        userFavoriteGamesMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromLocal(Exception exception) {

    }

    @Override
    public void onGameFavoriteStatusChanged(Game game, List<Game> favoriteGames) {

    }

    @Override
    public void onGameFavoriteStatusChanged(List<Game> games) {

    }

    @Override
    public void onDeleteFavoriteGameSuccess(List<Game> favoriteGames) {

    }

    @Override
    public void onSuccessFromCloudReading(List<Game> gameList) {

    }

    @Override
    public void onSuccessFromCloudWriting(Game game) {

    }

    @Override
    public void onFailureFromCloud(Exception exception) {

    }

    @Override
    public void onSuccessSynchronization() {
        userFavoriteGamesMutableLiveData.postValue(new Result.GameResponseSuccess(null));
    }

    @Override
    public void onSuccessDeletion() {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(null);
        userMutableLiveData.postValue(result);
    }
}

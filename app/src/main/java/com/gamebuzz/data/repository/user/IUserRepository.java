package com.gamebuzz.data.repository.user;

import androidx.lifecycle.MutableLiveData;

import com.gamebuzz.model.Result;
import com.gamebuzz.model.User;

public interface IUserRepository {
    MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered);
    MutableLiveData<Result> getGoogleUser(String idToken);
    MutableLiveData<Result> logout();
    MutableLiveData<Result> getUserFavoriteGames(String idToken);
    User getLoggedUser();
    void signUp(String email, String password);
    void login(String email, String password);
    void signInWithGoogle(String token);
}
